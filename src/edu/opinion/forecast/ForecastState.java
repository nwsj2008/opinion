/**
 * 
 */
package edu.opinion.forecast;

import edu.opinion.forecast.convert.ClusterObject;
import edu.opinion.forecast.convert.DataConverter;
import edu.opinion.forecast.display.DisplayConverter;
import edu.opinion.forecast.model.ForecastThread;

/**
 * 预测所出的状态
 * 
 * @author ch
 * 
 */
public class ForecastState implements Observer {

	/**
	 * 准备开始工作
	 */
	public static final int READAYTOWORK = 0;

	/**
	 * 完成数据转换工作
	 */
	public static final int FINISHDATACONVERT = 1;

	/**
	 * 完成了预测工作
	 */
	public static final int FINISHFORECAST = 2;

	/**
	 * 完成了显示数据转换工作，生成了预测图片
	 */
	public static final int FINISHDISPLAYCONVERT = 3;

	/**
	 * 当前的工作状态
	 */
	private int workState = READAYTOWORK;
	
	private ClusterObject cluster = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.Observer#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void update(Object o, Object param) {
		if (o instanceof DataConverter) {
//			if (this.workState < FINISHDISPLAYCONVERT)
//				this.workState++;
			this.workState = FINISHDATACONVERT;
			System.out.println("finish dataconvert");
			return;
		}

		if (o instanceof ForecastThread) {
//			if (this.workState < FINISHDISPLAYCONVERT)
//				this.workState++;
			this.workState = FINISHFORECAST;
			System.out.println("finish forecast");
			return;
		}

		if (o instanceof DisplayConverter) {
//			if (this.workState < FINISHDISPLAYCONVERT)
//				this.workState++;
			this.workState = FINISHDISPLAYCONVERT;
			System.out.println("finish display");
			return;
		}
	}

	/**
	 * @return the workState
	 */
	public int getWorkState() {
		return workState;
	}

	/**
	 * @param workState
	 *            the workState to set
	 */
	public void setWorkState(int workState) {
		this.workState = workState;
	}

	/**
	 * @return the cluster
	 */
	public ClusterObject getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(ClusterObject cluster) {
		this.cluster = cluster;
	}

}
