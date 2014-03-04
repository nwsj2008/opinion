package edu.opinion.forecast;

/**
 * 观察者
 * 
 * @author ch
 * 
 */
public interface Observer {

	/**
	 * 观察事件更新
	 * 
	 * @param o
	 *            更新的对象
	 * @param param
	 *            更新的参数
	 */
	public void update(Object o, Object param);
}
