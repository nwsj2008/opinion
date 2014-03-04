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
 * Ԥ����ص�����
 * 
 * @author ch
 * 
 */
public class ForecastConfig {
	/**
	 * һ��Сʱ��ʱ�䳤�ȣ�ת�ɺ��룩
	 */
	public static final long HOUR = 1000 * 60 * 60;
	/**
	 * ����Сʱ��ʱ�䳤�ȣ�ת�ɺ��룩
	 */
	public static final long SIXHOUR = HOUR * 6;
	/**
	 * ʮ����Сʱ��ʱ�䳤�ȣ�ת�ɺ��룩
	 */
	public static final long HALFDAY = HOUR * 12;
	/**
	 * һ���ʱ�䳤�ȣ�ת�ɺ��룩
	 */
	public static final long DAY = HOUR * 24;
	/**
	 * һ�ܵ�ʱ�䳤�ȣ�ת�ɺ��룩
	 */
	public static final long WEEK = DAY * 7;

	/**
	 * ������ݸ��µļ����3,600,000��һ��Сʱ
	 */
	public static int checkChangedInteval = 3600000;
	/**
	 * ��������ת���������ļ�¼����
	 */
	public static int criticalRecordNum = 20;
	/**
	 * ���ٵ�ʱ������
	 */
	public static int minTimePeriod = 40;
	/**
	 * Ԥ���ʱ������
	 */
	public static int forecastPeriod = 10;
	/**
	 * ���µĹ۲����ڣ������Ƚ�Ԥ����
	 */
	public static int observPeriod = 0;
	/**
	 * Ԥ����̳�ʱ����ֵ,��Ϊ1����(600,00)
	 */
	public static long overTime = 600000;
	/**
	 * Ԥ�����ݱ����·��
	 */
	public static String forecastPath = "/opinionImg/";
	public static String forecastPicPath = "opinionImg/";
	// File.separator + "forecast" + File.separator;
	/**
	 * �޷�Ԥ���������ʾ��ͼƬ
	 */
	public static String noPicPathString = "noforecast.png";
	
	public static boolean isInit = false;

	/**
	 * ��ȡ�����ļ����õĲ���
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
	 * ����Ԥ��Ĳ���
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
