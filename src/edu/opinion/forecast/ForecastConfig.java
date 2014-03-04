/**
 * 
 */
package edu.opinion.forecast;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 预测相关的配置
 * 
 * @author ch
 * 
 */
public class ForecastConfig {
	/**
	 * 一个小时的时间长度（转成毫秒）
	 */
	public static final long HOUR = 1000 * 60 * 60;
	/**
	 * 六个小时的时间长度（转成毫秒）
	 */
	public static final long SIXHOUR = HOUR * 6;
	/**
	 * 十二个小时的时间长度（转成毫秒）
	 */
	public static final long HALFDAY = HOUR * 12;
	/**
	 * 一天的时间长度（转成毫秒）
	 */
	public static final long DAY = HOUR * 24;
	/**
	 * 一周的时间长度（转成毫秒）
	 */
	public static final long WEEK = DAY * 7;

	/**
	 * 检查数据更新的间隔，3,600,000是一个小时
	 */
	public static int checkChangedInteval = 3600000;
	/**
	 * 触发数据转换的新增的记录条数
	 */
	public static int criticalRecordNum = 20;
	/**
	 * 最少的时间周期
	 */
	public static int minTimePeriod = 40;
	/**
	 * 预测的时间周期
	 */
	public static int forecastPeriod = 10;
	/**
	 * 留下的观察周期，用来比较预测结果
	 */
	public static int observPeriod = 0;
	/**
	 * 预测过程超时的阈值,设为1分钟(600,00)
	 */
	public static long overTime = 600000;
	/**
	 * 预测数据保存的路径
	 */
	public static String forecastPath = "/opinionImg/";
	public static String forecastPicPath = "opinionImg/";
	// File.separator + "forecast" + File.separator;
	/**
	 * 无法预测的序列显示的图片
	 */
	public static String noPicPathString = "noforecast.png";
	
	public static boolean isInit = false;

	/**
	 * 获取配置文件配置的参数
	 * 
	 * @param configFilePath
	 */
	@SuppressWarnings("unchecked")
	public static void getForecastConfig() {
		try {			
			String configFilePath ="conf/forecast.xml";
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(configFilePath);
			System.out.println("forecast config:" + document.getPath());
			List<Element> paras = document.selectNodes("//opinion:para");
			for (Element element : paras) {
				List<Element> names = element.selectNodes("opinion:para_name");
				List<Element> values = element.selectNodes("opinion:para_value");
				String name = names.get(0).getText();
				if (name.equals("minTimePeriod")) {
					minTimePeriod = Integer.parseInt(values.get(0).getText());
				} else if (name.equals("forecastPeriod")) {
					forecastPeriod = Integer.parseInt(values.get(0).getText());
				} else if (name.equals("observPeriod")) {
					observPeriod = Integer.parseInt(values.get(0).getText());
				} else if (name.equals("overTime")) {
					overTime = Long.parseLong(values.get(0).getText());
				} else if (name.equals("forecastPath")) {
					forecastPath = values.get(0).getText();
				} else if (name.equals("forecastPicPath")) {
					forecastPicPath = values.get(0).getText();
				} else if (name.equals("noPicPathString")) {
					noPicPathString = values.get(0).getText();
				}
			}
			isInit = true;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存预测的参数
	 */
	public static void saveForecastConfig(){
		//String configFilePath = "conf/forecast.xml";
	}
	
	public static void main(String[] args)
	{
		WatchThread.getInstance();
		System.out.println(ForecastConfig.forecastPeriod);
	}
}
