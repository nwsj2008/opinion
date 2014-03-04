/**
 * 
 */
package edu.opinion.forecast.convert;

/**
 * ����ת�����߳�
 * @author ch
 *
 */
public class DataConvertThread implements Runnable {
	
	/**
	 * ��������ת����ת����
	 */
	private DataConverter converter;
	
	public DataConvertThread(DataConverter converter) {
		this.converter = converter;
	}	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("database converter thread running...");
		converter.doWork();
	}

}
