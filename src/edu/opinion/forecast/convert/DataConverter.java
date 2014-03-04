package edu.opinion.forecast.convert;

import edu.opinion.forecast.Observer;

/**
 * ��Դ����ת�����ض�����Ԥ������ݸ�ʽ
 * 
 * @author ch
 * 
 */
public interface DataConverter {

	/**
	 * ��������Դ��·��������Դ�ĸ�ʽ
	 * 
	 * @param source
	 *            ����Դ������ʹ·�������ݿ����ӻ���������
	 * @param sourceFormat
	 *            ����Դ��������֯��ʽ
	 */
	public void setDatasource(Object source, Object sourceFormat);
	
	/**
	 * ������������ϵĹ۲����ݵ����ڸ���
	 * @param leftPeriod
	 */
	public void setLeftPeriod(int leftPeriod);

	/**
	 * ȡ���õ���ԭʼ����
	 * 
	 * @return ����ת���õ���ԭʼ����
	 */
	public Object getTargetData();

	/**
	 * ��������ת���Ĺ���
	 */
	public void doWork();

	/**
	 * ����ת�����Ƿ��ǻ�ģ����ڹ�����
	 * 
	 * @return ��ǰ������ת�����Ĺ���״̬
	 */
	public boolean isActive();

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
