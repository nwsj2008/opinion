package edu.opinion.forecast.convert;

import edu.opinion.forecast.Observer;

/**
 * 将源数据转换成特定用于预测的数据格式
 * 
 * @author ch
 * 
 */
public interface DataConverter {

	/**
	 * 设置数据源的路径和数据源的格式
	 * 
	 * @param source
	 *            数据源，可以使路径，数据库连接或者是数据
	 * @param sourceFormat
	 *            数据源的数据组织格式
	 */
	public void setDatasource(Object source, Object sourceFormat);
	
	/**
	 * 设置用来做拟合的观察数据的周期个数
	 * @param leftPeriod
	 */
	public void setLeftPeriod(int leftPeriod);

	/**
	 * 取出得到的原始数据
	 * 
	 * @return 经过转换得到的原始数据
	 */
	public Object getTargetData();

	/**
	 * 进行数据转换的工作
	 */
	public void doWork();

	/**
	 * 数据转换器是否是活动的，正在工作的
	 * 
	 * @return 当前的数据转换器的工作状态
	 */
	public boolean isActive();

	/**
	 * 增加一个观察者
	 * 
	 * @param observer
	 *            要增加的观察者对象
	 */
	public void addObersever(Observer observer);

	/**
	 * 删除一个观察者
	 * 
	 * @param observer
	 *            要删除的观察者对象
	 */
	public void removeObserver(Observer observer);

	/**
	 * 通知观察者事件更新
	 */
	public void notifyObserver();
}
