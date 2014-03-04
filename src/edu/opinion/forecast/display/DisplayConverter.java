package edu.opinion.forecast.display;

import edu.opinion.forecast.Observer;

/**
 * 显示数据转换器的接口<br>
 * 用于将预测数据转换为显示用的数据并生成预测图像
 * 
 * @author ch
 * 
 */
public interface DisplayConverter {

	/**
	 * 将预测数据转换为显示数据的操作
	 * 
	 * @param filePath
	 *            转换文件的路径
	 * @param dtdPath
	 *            转换文件所遵循的DTD的路径
	 * @param dataObject
	 *            要进行转换的数据对象
	 */
	public void convert(String filePath, String dtdPath, Object dataObject);

	/**
	 * 关闭或打开转换完成之后的图像显示
	 * 
	 * @param isTurnOff
	 *            true 表示关闭图像显示<br>
	 *            false 表示打开图像显示
	 */
	public void turnOffDisplayCart(boolean isTurnOff);

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
