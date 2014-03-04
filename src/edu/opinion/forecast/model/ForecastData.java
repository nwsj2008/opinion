package edu.opinion.forecast.model;

import java.util.Date;
import java.util.List;

/**
 * 预测数据对象
 * 
 * @author ch
 * 
 */
public class ForecastData {

	/**
	 * 时间序列的名称
	 */
	private String dataName;
	/**
	 * 时间序列的数据
	 */
	private List<Double> data;
	/**
	 * 时间序列的开始时间
	 */
	private Date starDate;
	/**
	 * 时间序列的结束时间
	 */
	private Date endDate;
	/**
	 * 时间序列的时间间隔
	 */
	private long timeInterval;
	/**
	 * 用来进行数据拟合的时间周期
	 */
	private int leftPeriod;

	/**
	 * @return the data
	 */
	public List<Double> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Double> data) {
		this.data = data;
	}

	/**
	 * @return the starDate
	 */
	public Date getStarDate() {
		return starDate;
	}

	/**
	 * @param starDate
	 *            the starDate to set
	 */
	public void setStarDate(Date starDate) {
		this.starDate = starDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the timeInterval
	 */
	public long getTimeInterval() {
		return timeInterval;
	}

	/**
	 * @param timeInterval
	 *            the timeInterval to set
	 */
	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * @return the dataName
	 */
	public String getDataName() {
		return dataName;
	}

	/**
	 * @param dataName
	 *            the dataName to set
	 */
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	/**
	 * @return the leftPeriod
	 */
	public int getLeftPeriod() {
		return leftPeriod;
	}

	/**
	 * @param leftPeriod
	 *            the leftPeriod to set
	 */
	public void setLeftPeriod(int leftPeriod) {
		this.leftPeriod = leftPeriod;
	}
}
