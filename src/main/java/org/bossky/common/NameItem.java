package org.bossky.common;

/**
 * 简单地提供一个name/value|id对照的项
 * 
 * 通常用于定义状态等值，通过findById/getNameById/findByValue/getNameByValue等取得对照
 * 
 * 
 * @author daibo
 */
public class NameItem {
	/** 项ID */
	public final int id;
	/** 项名 */
	public final String name;
	/** 项值 */
	public final Object value;

	/**
	 * 构造
	 * 
	 * @param name
	 *            对照名
	 * @param id
	 *            对照id
	 * @param value
	 *            对照值
	 */
	private NameItem(int id, String name, Object value) {
		this.name = name;
		this.value = value;
		this.id = id;
	}

	/** 项ID */
	public int getId() {
		return id;
	}

	/** 项名 */
	public String getName() {
		return name;
	}

	/** 项值 */
	public String getValue() {
		if (value instanceof String) {
			return (String) value;
		}
		return String.valueOf(value);
	}

	/**
	 * 更改ID
	 * 
	 * @param id
	 *            要变更的ID
	 * @return 变更后的对照项
	 */
	public NameItem changeId(int id) {
		return new NameItem(id, this.name, this.value);
	}

	/**
	 * 更改名称
	 * 
	 * @param name
	 *            要变更的名称
	 * @return 变更后的对照项
	 */
	public NameItem changeName(String name) {
		return new NameItem(this.id, name, this.value);
	}

	/**
	 * 更改值
	 * 
	 * @param value
	 *            要变更的值
	 * @return 变更后的对照项
	 */
	public NameItem changeValue(Object value) {
		return new NameItem(this.id, this.name, value);
	}

	/**
	 * 取得name/id的对照
	 * 
	 * @param name
	 *            对照名
	 * @param id
	 *            对照id
	 * @return 对照项
	 */
	static public NameItem valueOf(int id, String name) {
		return new NameItem(id, name, String.valueOf(id));
	}

	/**
	 * 取得name/id的对照
	 * 
	 * @param name
	 *            对照名
	 * @param id
	 *            对照id
	 * @return 对照项
	 */
	static public NameItem valueOf(int id, String name, Object value) {
		return new NameItem(id, name, value);
	}

	/**
	 * 取得name/value的对照
	 * 
	 * @param name
	 *            对照名
	 * @param value
	 *            对照值
	 * @return 对照项
	 */
	static public NameItem valueOf(String name, String value) {
		return new NameItem(0, name, value);
	}

	/**
	 * 由ID查询在items中的项
	 * 
	 */
	static public NameItem findById(int id, Iterable<NameItem> items) {
		for (NameItem item : items) {
			if (item.id == id)
				return item;
		}
		return null;
	}

	/**
	 * 由ID查询在items中的项
	 * 
	 */
	static public NameItem findById(int id, NameItem[] items) {
		for (NameItem item : items) {
			if (item.id == id)
				return item;
		}
		return null;
	}

	/**
	 * 由ID查询到相应项的名称，没找到则返回null
	 * 
	 */
	static public String getNameById(int id, NameItem[] items) {
		NameItem ni = findById(id, items);
		return (null == ni) ? null : ni.name;
	}

	/**
	 * 由ID查询到相应项的名称，没找到则返回null
	 * 
	 */
	static public String getNameById(int id, Iterable<NameItem> items) {
		NameItem ni = findById(id, items);
		return (null == ni) ? null : ni.name;
	}

	/**
	 * 由value查询在items中的项
	 * 
	 */
	static public NameItem findByValue(String value, Iterable<NameItem> items) {
		for (NameItem item : items) {
			if (item.value.equals(value))
				return item;
		}
		return null;
	}

	/**
	 * 由value查询在items中的项
	 * 
	 */
	static public NameItem findByValue(String value, NameItem[] items) {
		for (NameItem item : items) {
			if (item.value.equals(value))
				return item;
		}
		return null;
	}

	/**
	 * 由value查询到相应项的名称，没找到则返回null
	 * 
	 */
	static public String getNameByValue(String value, NameItem[] items) {
		NameItem ni = findByValue(value, items);
		return (null == ni) ? null : ni.name;
	}

	/**
	 * 由value查询到相应项的名称，没找到则返回null
	 * 
	 */
	static public String getNameByValue(String value, Iterable<NameItem> items) {
		NameItem ni = findByValue(value, items);
		return (null == ni) ? null : ni.name;
	}

	/**
	 * 由name查询在items中的项
	 * 
	 */
	static public NameItem findByName(String name, Iterable<NameItem> items) {
		for (NameItem item : items) {
			if (item.name.equalsIgnoreCase(name))
				return item;
		}
		return null;
	}

	/**
	 * 由name查询在items中的项
	 * 
	 */
	static public NameItem findByName(String name, NameItem[] items) {
		for (NameItem item : items) {
			if (item.name.equalsIgnoreCase(name))
				return item;
		}
		return null;
	}

	/**
	 * 由name查询到相应项的value，没找到则返回null
	 * 
	 */
	static public Object getValueByName(String name, NameItem[] items) {
		NameItem ni = findByName(name, items);
		return (null == ni) ? null : ni.value;
	}

	/**
	 * 由name查询到相应项的value，没找到则返回null
	 * 
	 */
	static public Object getValueByName(String name, Iterable<NameItem> items) {
		NameItem ni = findByName(name, items);
		return (null == ni) ? null : ni.value;
	}

	@Override
	public String toString() {
		if (null == value) {
			return name + "[" + id + "]";
		}
		return name + "[" + id + "]=" + value;
	}

}
