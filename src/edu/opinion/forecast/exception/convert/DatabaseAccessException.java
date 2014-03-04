/**
 * 
 */
package edu.opinion.forecast.exception.convert;

/**
 * 转化过程中的数据库访问出错
 * @author ch
 *
 */
public class DatabaseAccessException extends Exception {
	static final long serialVersionUID = 804221649;
	
	/**
	 * 构造函数
	 */
	public DatabaseAccessException() {
		super("数据库访问出现错误");
	}

	/**
	 * @param message 错误信息
	 * @param cause 引发的异常
	 */
	public DatabaseAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message 错误信息
	 */
	public DatabaseAccessException(String message) {
		super(message);
	}

	/**
	 * @param cause 引发的异常
	 */
	public DatabaseAccessException(Throwable cause) {
		super(cause);
	}


}
