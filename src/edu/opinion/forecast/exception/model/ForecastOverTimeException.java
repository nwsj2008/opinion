/**
 * 
 */
package edu.opinion.forecast.exception.model;

/**
 * Ԥ����̳�ʱ������쳣
 * 
 * @author ch
 * 
 */
public class ForecastOverTimeException extends Exception {
	static final long serialVersionUID = 804221612;

	/**
	 * ���캯��
	 */
	public ForecastOverTimeException() {
		super("Ԥ����̳�ʱ. ��������Ԥ��Ĺ����г��ִ���");
	}

	/**
	 * @param message ������Ϣ
	 * @param cause �������쳣
	 */
	public ForecastOverTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message ������Ϣ
	 */
	public ForecastOverTimeException(String message) {
		super(message);
	}

	/**
	 * @param cause �������쳣
	 */
	public ForecastOverTimeException(Throwable cause) {
		super(cause);
	}

}
