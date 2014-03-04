/**
 * 
 */
package edu.opinion.forecast.convert;

/**
 * 数据转换的线程
 * @author ch
 *
 */
public class DataConvertThread implements Runnable {
	
	/**
	 * 进行数据转换的转换器
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
