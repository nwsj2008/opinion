/**
 * 
 */
package edu.opinion.forecast.exception.model;

/**
 * Ԥ����̳���������쳣
 * 
 * @author ch
 * 
 */
public class ForecastErrorException extends Exception {
	static final long serialVersionUID = 804221619;

	/**
	 * ���캯��
	 */
	public ForecastErrorException() {
		super("Ԥ������г��ִ���.");
	}

	/**
	 * @param message ������Ϣ
	 * @param cause �������쳣
	 */
	public ForecastErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message ������Ϣ
	 */
	public ForecastErrorException(String message) {
		super(message);
	}

	/**
	 * @param cause �������쳣
	 */
	public ForecastErrorException(Throwable cause) {
		super(cause);
	}
	
}
