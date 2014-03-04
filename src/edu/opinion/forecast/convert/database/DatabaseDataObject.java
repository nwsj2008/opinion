package edu.opinion.forecast.convert.database;

import java.util.Date;

/**
 * ��õ����ݸ�ʽ
 * 
 * @author ch
 * 
 */
public class DatabaseDataObject {
	/**
	 * ���ݿ��е�����
	 */
	private String id;
	/**
	 * ���ӷ����ʱ��
	 */
	private Date time;
	/**
	 * ���������ķ���
	 */
	private String cluster;
	/**
	 * �������ڵ����ݿ��
	 */
	@SuppressWarnings("unchecked")
	private Class dataClass;
	
	/**
	 * ���캯��
	 */
	public DatabaseDataObject(){
		
	}

	/**
	 * ���캯��
	 * @param id ����
	 * @param time ʱ��
	 * @param cluster �������
	 * @param dataClass pojo����
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
