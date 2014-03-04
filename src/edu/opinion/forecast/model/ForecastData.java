package edu.opinion.forecast.model;

import java.util.Date;
import java.util.List;

/**
 * Ԥ�����ݶ���
 * 
 * @author ch
 * 
 */
public class ForecastData {

	/**
	 * ʱ�����е�����
	 */
	private String dataName;
	/**
	 * ʱ�����е�����
	 */
	private List<Double> data;
	/**
	 * ʱ�����еĿ�ʼʱ��
	 */
	private Date starDate;
	/**
	 * ʱ�����еĽ���ʱ��
	 */
	private Date endDate;
	/**
	 * ʱ�����е�ʱ����
	 */
	private long timeInterval;
	/**
	 * ��������������ϵ�ʱ������
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
