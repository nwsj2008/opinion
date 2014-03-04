/**
 * 
 */
package edu.opinion.forecast.display;

import edu.opinion.forecast.Observer;
import edu.opinion.forecast.model.ForecastThread;
import edu.opinion.forecast.model.ARIMA.ARIMAModel;

/**
 * 显示数据转换的线程
 * 
 * @author ch
 * 
 */
public class DisplayConverterThread implements Observer, Runnable {
	/**
	 * 将要被转换的数据对象<br>
	 * 是预测过程产生的数据对象
	 */
	private Object dataObject;
	/**
	 * 用于显示数据转换的转换器
	 */
	private DisplayConverter converter;
	private String filePath = "";

	/**
	 * 构造函数<br>
	 * 构造显示数据转换器
	 */
	public DisplayConverterThread() {
		converter = DisplayConverterFactory.createDisplayXMLConverter();
	}

	/**
	 * 带参数的构造函数<br>
	 * 构造显示数据转换器，并传入数据转换的对象
	 * 
	 * @param observer
	 *            用于数据转换的对象
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
