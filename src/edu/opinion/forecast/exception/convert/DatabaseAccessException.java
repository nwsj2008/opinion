/**
 * 
 */
package edu.opinion.forecast.exception.convert;

/**
 * ת�������е����ݿ���ʳ���
 * @author ch
 *
 */
public class DatabaseAccessException extends Exception {
	static final long serialVersionUID = 804221649;
	
	/**
	 * ���캯��
	 */
	public DatabaseAccessException() {
		super("���ݿ���ʳ��ִ���");
	}

	/**
	 * @param message ������Ϣ
	 * @param cause �������쳣
	 */
	public DatabaseAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message ������Ϣ
	 */
	public DatabaseAccessException(String message) {
		super(message);
	}

	/**
	 * @param cause �������쳣
	 */
	public DatabaseAccessException(Throwable cause) {
		super(cause);
	}


}
