package edu.opinion.forecast;

/**
 * �۲���
 * 
 * @author ch
 * 
 */
public interface Observer {

	/**
	 * �۲��¼�����
	 * 
	 * @param o
	 *            ���µĶ���
	 * @param param
	 *            ���µĲ���
	 */
	public void update(Object o, Object param);
}
