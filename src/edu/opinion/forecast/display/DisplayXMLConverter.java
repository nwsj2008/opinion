/**
 * 
 */
package edu.opinion.forecast.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import edu.opinion.forecast.ForecastConfig;
import edu.opinion.forecast.Observer;
import edu.opinion.forecast.model.ARIMA.ARIMAModel;

/**
 * 将预测数据转换为xml格式的转换器<br>
 * 转换完成之后生成预测的图片
 * 
 * @author ch
 * 
 */
public class DisplayXMLConverter implements DisplayConverter {

	/**
	 * 观察者队列
	 */
	private List<Observer> observerList;
	/**
	 * 文件保存路径
	 */
	private String filePath;
	/**
	 * DTD保存路径
	 */
	private String dtdPath;
	/**
	 * 数据
	 */
	private Object data;
	/**
	 * 是否关掉显示
	 */
	private boolean isTurnOff = false;

	public DisplayXMLConverter() {
		observerList = new ArrayList<Observer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.display.DisplayConverter#convert()
	 */
	public void convert(String filePath, String dtdPath, Object dataObject) {
		//this.filePath = filePath;
		this.filePath = ForecastConfig.forecastPath;
		this.dtdPath = dtdPath;
		this.data = dataObject;
		//if (dataObject instanceof ARIMAModel) {
			convertARIMA();
		//}
	}

	/**
	 * 将 ARIMA 模型得到的预测数据转换为XML格式<br>
	 */
	private void convertARIMA() {
		if(data == null){
			notifyObserver();
			return;
		}
		ARIMAModel model = (ARIMAModel) data;
		// 以下是xml文件用到的数据
		// 名字
		String sourceName = model.getYName();
		// 观察值序列只有一列
		double[][] observationData = model.getY().doubleArray();
		double[][] observationLeft = model.getYLeft().doubleArray();
		// 预测值序列,有三列（置信区间下限、预测数据、置信区间上限）
		double[][] forecastData = model.getForecastDet().doubleArray();
		List<Date> obDateList = new ArrayList<Date>();
		List<Date> forecastDateList = new ArrayList<Date>();

		// 时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(model.getStartDate());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		int timeInterval = (int) (model.getTimeinterval() / ForecastConfig.HOUR);

		Document document = DocumentHelper.createDocument();
		// root element
		Element datasetElement = document.addElement("dataset");
		datasetElement.addAttribute("source", sourceName);
		datasetElement.addAttribute("time", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new Date()));

		// data items
		// observation items
		Element observationItems = datasetElement.addElement("dataitems");
		observationItems.addAttribute("itemname", "observation");
		String lastObTime = dateFormat.format(calendar.getTime());
		for (int i = 0; i < observationData.length; i++) {
			Element dataitem = observationItems.addElement("dataitem");
			lastObTime = dateFormat.format(calendar.getTime());
			obDateList.add(calendar.getTime());
			dataitem.addAttribute("timespan", lastObTime);
			dataitem.addAttribute("value", "" + observationData[i][0]);
			calendar.add(Calendar.HOUR, timeInterval);
		}
		Date lastDate = calendar.getTime();
		double lastObValue = observationData[observationData.length - 1][0];
		for (int i = 0; i < observationLeft.length; i++) {
			Element dataitem = observationItems.addElement("dataitem");
			dataitem.addAttribute("timespan", dateFormat.format(calendar
					.getTime()));
			dataitem.addAttribute("value", "" + observationLeft[i][0]);
			calendar.add(Calendar.HOUR, timeInterval);
		}

		calendar.setTime(lastDate);
		// forecast items
		Element forecastItems = datasetElement.addElement("dataitems");
		forecastItems.addAttribute("itemname", "forecast");
		// 最后一个观察值设为预测值，这样可以使产生的图形连续
		Element lastDataitem = forecastItems.addElement("dataitem");
		lastDataitem.addAttribute("timespan", lastObTime);
		lastDataitem.addAttribute("value", "" + lastObValue);
		lastDataitem.addAttribute("lowerci", "" + lastObValue);
		lastDataitem.addAttribute("upperci", "" + lastObValue);
		for (int i = 0; i < forecastData[0].length; i++) {
			Element dataitem = forecastItems.addElement("dataitem");
			forecastDateList.add(calendar.getTime());
			dataitem.addAttribute("timespan", dateFormat.format(calendar
					.getTime()));
			dataitem.addAttribute("value", "" + forecastData[0][i]);
			dataitem.addAttribute("lowerci", "" + forecastData[1][i]);
			dataitem.addAttribute("upperci", "" + forecastData[2][i]);
			calendar.add(Calendar.HOUR, timeInterval);
		}

		// 保存文件
		try {
			// 删除以前预测的数据
			File file = new File(filePath);
			if (file.isDirectory()) {
				String path = file.getAbsolutePath();
				String[] subFiles = file.list();
				for (String subFile : subFiles) {
					if (subFile.startsWith(model.getYName())) {
						File fileToDel = new File(path + File.separator
								+ subFile);
						fileToDel.delete();
					}
				}
			}

			// 写预测新数据
			String filename = filePath + model.getYName()
					+ new Date().getTime() + ".xml";
			XMLWriter writer = new XMLWriter(new FileOutputStream(filename));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String obName = "实际的文本数量";
		String forName = "预测的文本数量";
		String lowName = "置信区间下限";
		String upperName = "置信区间上限";

		TimeSeries obSeries = new TimeSeries(obName, Hour.class);
		TimeSeries forecastSeries = new TimeSeries(forName, Hour.class);
		TimeSeries lowerSeries = new TimeSeries(lowName, Hour.class);
		TimeSeries upperSeries = new TimeSeries(upperName, Hour.class);

		for (int i = 0; i < obDateList.size(); i++) {
			obSeries.add(new Hour(obDateList.get(i)), Math
					.round(observationData[i][0]));
		}

		forecastSeries.add(new Hour(obDateList.get(obDateList.size() - 1)),
				Math.round(observationData[obDateList.size() - 1][0]));
		lowerSeries.add(new Hour(obDateList.get(obDateList.size() - 1)), Math
				.round(observationData[obDateList.size() - 1][0]));
		upperSeries.add(new Hour(obDateList.get(obDateList.size() - 1)), Math
				.round(observationData[obDateList.size() - 1][0]));
		for (int i = 0; i < forecastDateList.size(); i++) {
//			if (observationLeft.length > i) {
//				obSeries.add(new Hour(forecastDateList.get(i)), Math
//						.round(observationLeft[i][0]));
//			}
			forecastSeries.add(new Hour(forecastDateList.get(i)), Math
					.round(forecastData[0][i]));
			lowerSeries.add(new Hour(forecastDateList.get(i)), Math
					.round(forecastData[1][i]));
			upperSeries.add(new Hour(forecastDateList.get(i)), Math
					.round(forecastData[2][i]));
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection(obSeries);
		dataset.addSeries(forecastSeries);
		dataset.addSeries(lowerSeries);
		dataset.addSeries(upperSeries);

		JFreeChart chart = ChartFactory.createTimeSeriesChart(model.getYName(),
				"时间", "帖子总数", dataset, true, true, false);
		XYPlot plot = chart.getXYPlot();

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(2.0F));
		renderer.setSeriesStroke(1, new BasicStroke(2.0F));
		renderer.setSeriesStroke(2, new BasicStroke(2.0F));
		renderer.setSeriesStroke(3, new BasicStroke(2.0F));

		renderer.setSeriesPaint(2, Color.yellow);
		renderer.setSeriesPaint(3, Color.yellow);
		renderer.setSeriesShape(0, new Rectangle(-2, -2, 4, 4));
		renderer.setSeriesShape(1, new Ellipse2D.Float(-2, -2, 4, 4));		
		renderer.setSeriesShape(2, new Ellipse2D.Float(-2, -2, 4, 4));		
		renderer.setSeriesShape(3, new Ellipse2D.Float(-2, -2, 4, 4));		
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShapesVisible(1, true);
		renderer.setSeriesShapesVisible(2, true);
		renderer.setSeriesShapesVisible(3, true);

		DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		dateAxis.setAutoTickUnitSelection(false);
		double interval = obDateList.get(1).getTime()
				- obDateList.get(0).getTime();
		if (interval > ForecastConfig.SIXHOUR) {
			dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY,
					(int) (interval / ForecastConfig.HOUR / 3)));
			dateFormat = new SimpleDateFormat("MM月dd日");
		} else {
			dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR,
					(int) (interval / ForecastConfig.HOUR * 8)));
			dateFormat = new SimpleDateFormat("MM月dd日HH时");
		}
		dateAxis.setDateFormatOverride(dateFormat);

		try {
			File file = new File(filePath
					+ Integer.toString(model.getYName().hashCode()) + ".png");
			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
		notifyObserver();
		if (!isTurnOff) { // 显示数据图
			ChartFrame frame = new ChartFrame("forecast result", chart);

			frame.pack();
			frame.setVisible(true);
		}
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the dtdPath
	 */
	public String getDtdPath() {
		return dtdPath;
	}

	/**
	 * @param dtdPath
	 *            the dtdPath to set
	 */
	public void setDtdPath(String dtdPath) {
		this.dtdPath = dtdPath;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.display.DisplayConverter#turnOffDisplayCart(boolean)
	 */
	public void turnOffDisplayCart(boolean isTurnOff) {
		this.isTurnOff = isTurnOff;
	}

	/**
	 * 增加一个观察者
	 * 
	 * @param observer
	 *            观察者
	 */
	public void addObersever(Observer observer) {
		synchronized (observerList) {
			if (!observerList.contains(observer)) {
				observerList.add(observer);
			}
		}
	}

	/**
	 * 唤醒观察者
	 */
	public void notifyObserver() {
		synchronized (observerList) {
			Iterator<Observer> iterator = observerList.iterator();
			for (; iterator.hasNext();) {
				Observer observer = iterator.next();
				observer.update(this, null);
			}
		}
	}

	/**
	 * 移除一个观察者
	 * 
	 * @param observer
	 *            观察者
	 */
	public void removeObserver(Observer observer) {
		synchronized (observerList) {
			if (observerList.contains(observer)) {
				Iterator<Observer> iterator = observerList.iterator();
				for (; iterator.hasNext();) {
					if (iterator.next().equals(observer)) {
						iterator.remove();
					}
				}
			}
		}
	}

}
