package edu.opinion.forecast.convert;

import java.util.Date;


/**
 * �������
 * 
 * @author ch
 * 
 */
public interface ClusterObject {
	/**
	 * �������Ƿ�ı�
	 * 
	 * @return �������������������
	 */
	public int changedNum();
	
	/**
	 * �����ϴ�Ԥ��ʱ��
	 * @param forecastDate
	 */
	public void updateTime(Date forecastDate);
}
