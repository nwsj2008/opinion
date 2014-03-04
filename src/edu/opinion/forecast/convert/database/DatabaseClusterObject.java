package edu.opinion.forecast.convert.database;

import java.util.Date;
import java.util.List;

import edu.opinion.common.db.TbCluster;
import edu.opinion.forecast.convert.ClusterObject;

/**
 * 用于数据库的聚类对象
 * 
 * @author ch
 * 
 */
public class DatabaseClusterObject implements ClusterObject {

	/**
	 * 分类记录在数据库中的主键
	 */
	private String id;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 上次预测时间
	 */
	private Date forecastDate;
	/**
	 * 包含的数据
	 */
	private List<DatabaseDataObject> dataList;

	/**
	 * 构造函数
	 */
	public DatabaseClusterObject() {

	}

	/**
	 * 构造函数
	 * 
	 * @param id
	 *            主键
	 * @param name
	 *            名称
	 * @param forecastDate
	 *            上次预测时间
	 */
	public DatabaseClusterObject(String id, String name, Date forecastDate) {
		this.id = id;
		this.name = name;
//		this.forecastDate = forecastDate;
		this.forecastDate = new Date(1L);
	}

	/**
	 * 构造函数
	 * 
	 * @param cluster
	 *            hibernate中关于分类的pojo对象
	 */
	public DatabaseClusterObject(TbCluster cluster) {
		this.id = cluster.getId();
		this.name = cluster.getName();
//		if (cluster.getForecastTime() != null) {
//			this.forecastDate = cluster.getForecastTime();
//		} else {
//			this.forecastDate = new Date(0);
//		}
		this.forecastDate = new Date(1L);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterObject#checkChanged()
	 */
	public int changedNum() {
		ConvertDAO dao = new ConvertDAO();
		int num = dao.getChangedNum(this);
		return num;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterObject#updateTime(java.util.Date)
	 */
	public void updateTime(Date forecastDate) {
		ConvertDAO dao = new ConvertDAO();
		dao.updataForecastTime(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		DatabaseClusterObject clusterObject = (DatabaseClusterObject) obj;
		if (clusterObject.getName().equals(this.name)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the forecastDate
	 */
	public Date getForecastDate() {
		return forecastDate;
	}

	/**
	 * @param forecastDate
	 *            the forecastDate to set
	 */
	public void setForecastDate(Date forecastDate) {
		this.forecastDate = forecastDate;
	}

	/**
	 * @return the dataList
	 */
	public List<DatabaseDataObject> getDataList() {
		return dataList;
	}

	/**
	 * @param dataList
	 *            the dataList to set
	 */
	public void setDataList(List<DatabaseDataObject> dataList) {
		this.dataList = dataList;
	}
}
