package edu.opinion.forecast.convert.database;

import java.util.Date;

/**
 * 获得的数据格式
 * 
 * @author ch
 * 
 */
public class DatabaseDataObject {
	/**
	 * 数据库中的主键
	 */
	private String id;
	/**
	 * 帖子发表的时间
	 */
	private Date time;
	/**
	 * 帖子所属的分类
	 */
	private String cluster;
	/**
	 * 帖子所在的数据库表
	 */
	@SuppressWarnings("unchecked")
	private Class dataClass;
	
	/**
	 * 构造函数
	 */
	public DatabaseDataObject(){
		
	}

	/**
	 * 构造函数
	 * @param id 主键
	 * @param time 时间
	 * @param cluster 分类类别
	 * @param dataClass pojo对象
	 */
	@SuppressWarnings("unchecked")
	public DatabaseDataObject(String id, Date time, String cluster, Class dataClass) {
		super();
		this.id = id;
		this.time = time;
		this.cluster = cluster;
		this.dataClass = dataClass;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the cluster
	 */
	public String getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the dataClass
	 */
	@SuppressWarnings("unchecked")
	public Class getDataClass() {
		return dataClass;
	}

	/**
	 * @param dataClass the dataClass to set
	 */
	@SuppressWarnings("unchecked")
	public void setDataClass(Class dataClass) {
		this.dataClass = dataClass;
	}
}
