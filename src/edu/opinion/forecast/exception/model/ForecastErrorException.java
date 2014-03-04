/**
 * 
 */
package edu.opinion.forecast.exception.model;

/**
 * 预测过程出错引起的异常
 * 
 * @author ch
 * 
 */
public class ForecastErrorException extends Exception {
	static final long serialVersionUID = 804221619;

	/**
	 * 构造函数
	 */
	public ForecastErrorException() {
		super("预测过程中出现错误.");
	}

	/**
	 * @param message 错误信息
	 * @param cause 引发的异常
	 */
	public ForecastErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message 错误信息
	 */
	public ForecastErrorException(String message) {
		super(message);
	}

	/**
	 * @param cause 引发的异常
	 */
	public ForecastErrorException(Throwable cause) {
		super(cause);
	}
	
}
