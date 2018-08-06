package org.bossky.counter.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.bossky.common.util.IoUtil;
import org.bossky.common.util.Misc;
import org.bossky.counter.Counter;
import org.bossky.exception.BusyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于jdbc的计数器
 * 
 * @author bo
 *
 */
public class JdbcCounter implements Counter {
	/** 日志 */
	private static final Logger _Logger = LoggerFactory.getLogger(JdbcCounter.class);
	/** 数据源 */
	protected DataSource dataSource;
	/** 计数器名称 */
	protected String name;
	/** 表名前缀 */
	private static final String TABLE_PREFIX = "ct_";
	/** 表主键 */
	private static final String KEY_NAME = "key";
	/** 表值 */
	private static final String VALUE_NAME = "value";
	/** 版本 */
	private static final String VERSION_NAME = "version";

	private static int TRY_COUNT = 3;

	public JdbcCounter(DataSource dataSource, String name) {
		this.dataSource = dataSource;
		this.name = name.toLowerCase();
		init();
	}

	/**
	 * 表名
	 * 
	 * @return
	 */
	private String tableName() {
		return TABLE_PREFIX + name;
	}

	private String keyName() {
		return KEY_NAME;
	}

	private String valueName() {
		return VALUE_NAME;
	}

	private String versionName() {
		return VERSION_NAME;
	}

	private void init() {
		String sql = "CREATE TABLE IF NOT EXISTS`" + tableName() + "` (`" + keyName() + "` VARCHAR(255) NOT NULL,`"
				+ valueName() + "` BIGINT ,`" + versionName() + "` BIGINT , PRIMARY KEY (`" + keyName() + "`));";
		execute(sql);
	}

	private int execute(String sql) {
		Connection cc = null;
		Statement st = null;
		try {
			cc = dataSource.getConnection();
			st = cc.createStatement();
			return st.executeUpdate(sql);
		} catch (SQLException e) {
			_Logger.error("执行" + sql + "异常", e);
			IoUtil.closeIo(cc);
			IoUtil.closeIo(st);
		}
		return 0;
	}

	private ValueAndVersion getInner(String key) {
		if (Misc.isEmpty(key)) {
			return null;
		}
		String sql = "SELECT `" + valueName() + "`,`" + versionName() + "` FROM `" + tableName() + "` where `"
				+ keyName() + "`='" + escape(key) + "'";
		Connection cc = null;
		Statement st = null;
		try {
			cc = dataSource.getConnection();
			st = cc.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				return new ValueAndVersion(rs.getLong(valueName()), rs.getLong(versionName()));
			}
		} catch (SQLException e) {
			_Logger.error("执行" + sql + "异常", e);
			IoUtil.closeIo(cc);
			IoUtil.closeIo(st);
		}
		return null;
	}

	private static String escape(String val) {
		if (null == val) {
			return "";
		} else {
			val = val.trim();
		}
		return val.replace("'", "''");
	}

	@Override
	public long get(String key) {
		ValueAndVersion p = getInner(key);
		return null == p ? 0 : p.value;
	}

	@Override
	public long set(String key, long value) {
		ValueAndVersion p = getInner(key);
		long version = 0;
		if (null == p) {
			String sql = "INSERT INTO `" + tableName() + "` (`" + keyName() + "`, `" + valueName() + "`,`"
					+ versionName() + "`) VALUES ('" + key + "', " + value + "," + version + ");";
			int result = execute(sql);
			if (result > 0) {
				return value;
			}
		}
		// Comparator And Swap
		for (int i = 0; i < TRY_COUNT; i++) {
			p = getInner(key);
			version = p.version + 1;
			String sql = "UPDATE `" + tableName() + "` SET `" + valueName() + "`=" + value + " and `" + versionName()
					+ "`=" + version + "WHERE `" + keyName() + "`='" + escape(key) + "' and `" + versionName() + "`='"
					+ p.version + "';";
			int result = execute(sql);
			if (result > 0) {
				return value;
			}
		}
		throw new BusyException();// 太忙咯
	}

	@Override
	public long inc(String key) {
		return inc(key, 1);
	}

	@Override
	public long inc(String key, long step) {
		ValueAndVersion p = getInner(key);
		long version = 0;
		if (null == p) {
			String sql = "INSERT INTO `" + tableName() + "` (`" + keyName() + "`, `" + valueName() + "`,`"
					+ versionName() + "`) VALUES ('" + key + "', " + step + "," + version + ");";
			int result = execute(sql);
			if (result > 0) {
				return step;
			}
		}
		// Comparator And Swap
		for (int i = 0; i < TRY_COUNT; i++) {
			p = getInner(key);
			long value = p.value + step;
			version = p.version + 1;
			String sql = "UPDATE `" + tableName() + "` SET `" + valueName() + "`=" + value + " , `" + versionName()
					+ "`=" + version + " WHERE `" + keyName() + "`='" + escape(key) + "' and `" + versionName() + "`='"
					+ p.version + "';";
			int result = execute(sql);
			if (result > 0) {
				return value;
			}
		}
		throw new BusyException();// 太忙咯
	}

	@Override
	public long dec(String key) {
		return dec(key, 1);
	}

	@Override
	public long dec(String key, long step) {
		return inc(key, -step);
	}

	private static class ValueAndVersion {
		private long value;
		private long version;

		public ValueAndVersion(long value, long version) {
			this.value = value;
			this.version = version;
		}
	}

}
