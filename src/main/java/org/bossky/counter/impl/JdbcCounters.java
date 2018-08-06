package org.bossky.counter.impl;

import javax.sql.DataSource;

import org.bossky.counter.Counter;
import org.bossky.counter.Counters;

/**
 * 基于jdbc的计数器集合
 * 
 * @author bo
 *
 */
public class JdbcCounters implements Counters {

	protected DataSource dataSource;

	public JdbcCounters(DataSource source) {
		dataSource = source;
	}

	@Override
	public Counter openCounter(String name) {
		return new JdbcCounter(dataSource, name);
	}

}
