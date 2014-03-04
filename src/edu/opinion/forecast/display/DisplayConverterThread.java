/**
 * 
 */
package edu.opinion.forecast.display;

import edu.opinion.forecast.Observer;
import edu.opinion.forecast.model.ForecastThread;
import edu.opinion.forecast.model.ARIMA.ARIMAModel;

/**
 * ��ʾ����ת�����߳�
 * 
 * @author ch
 * 
 */
public class DisplayConverterThread implements Observer, Runnable {
	/**
	 * ��Ҫ��ת�������ݶ���<br>
	 * ��Ԥ����̲��������ݶ���
	 */
	private Object dataObject;
	/**
	 * ������ʾ����ת����ת����
	 */
	private DisplayConverter converter;
	private String filePath = "";

	/**
	 * ���캯��<br>
	 * ������ʾ����ת����
	 */
	public DisplayConverterThread() {
		converter = DisplayConverterFactory.createDisplayXMLConverter();
	}

	/**
	 * �������Ĺ��캯��<br>
	 * ������ʾ����ת����������������ת���Ķ���
	 * 
	 * @param observer
	 *            ��������ת���Ķ���
	 */
	public DisplayConverterThread(Observer observer) {
		converter = DisplayConverterFactory.createDisplayXMLConverter();
		converter.addObersever(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("display converter thread running...");
		converter.turnOffDisplayCart(true);
		converter.convert(filePath, "", dataObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.Observer#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void update(Object o, Object param) {
		if (o instanceof ForecastThread) {
			if (param instanceof ARIMAModel) {
				dataObject = param;
				this.run();
			}else if(param == null){
				dataObject = param;
				this.run();
			}
		}

	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
