package edu.opinion.forecast.display;

import edu.opinion.forecast.Observer;

/**
 * ��ʾ����ת�����Ľӿ�<br>
 * ���ڽ�Ԥ������ת��Ϊ��ʾ�õ����ݲ�����Ԥ��ͼ��
 * 
 * @author ch
 * 
 */
public interface DisplayConverter {

	/**
	 * ��Ԥ������ת��Ϊ��ʾ���ݵĲ���
	 * 
	 * @param filePath
	 *            ת���ļ���·��
	 * @param dtdPath
	 *            ת���ļ�����ѭ��DTD��·��
	 * @param dataObject
	 *            Ҫ����ת�������ݶ���
	 */
	public void convert(String filePath, String dtdPath, Object dataObject);

	/**
	 * �رջ��ת�����֮���ͼ����ʾ
	 * 
	 * @param isTurnOff
	 *            true ��ʾ�ر�ͼ����ʾ<br>
	 *            false ��ʾ��ͼ����ʾ
	 */
	public void turnOffDisplayCart(boolean isTurnOff);

	/**
	 * ����һ���۲���
	 * 
	 * @param observer
	 *            Ҫ���ӵĹ۲��߶���
	 */
	public void addObersever(Observer observer);

	/**
	 * ɾ��һ���۲���
	 * 
	 * @param observer
	 *            Ҫɾ���Ĺ۲��߶���
	 */
	public void removeObserver(Observer observer);

	/**
	 * ֪ͨ�۲����¼�����
	 */
	public void notifyObserver();
}
