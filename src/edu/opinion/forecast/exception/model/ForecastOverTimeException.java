/**
 * 
 */
package edu.opinion.forecast.exception.model;

/**
 * 预测过程超时引起的异常
 * 
 * @author ch
 * 
 */
public class ForecastOverTimeException extends Exception {
	static final long serialVersionUID = 804221612;

	/**
	 * 构造函数
	 */
	public ForecastOverTimeException() {
		super("预测过程超时. 可能是在预测的过程中出现错误");
	}

	/**
	 * @param message 错误信息
	 * @param cause 引发的异常
	 */
	public ForecastOverTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message 错误信息
	 */
	public ForecastOverTimeException(String message) {
		super(message);
	}

	/**
	 * @param cause 引发的异常
	 */
	public ForecastOverTimeException(Throwable cause) {
		super(cause);
	}

}
