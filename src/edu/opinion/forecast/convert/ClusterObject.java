package edu.opinion.forecast.convert;

import java.util.Date;


/**
 * 聚类对象
 * 
 * @author ch
 * 
 */
public interface ClusterObject {
	/**
	 * 检查聚类是否改变
	 * 
	 * @return 返回类别中新增的条数
	 */
	public int changedNum();
	
	/**
	 * 更新上次预测时间
	 * @param forecastDate
	 */
	public void updateTime(Date forecastDate);
}
