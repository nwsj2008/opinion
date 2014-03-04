/**
 * 
 */
package edu.opinion.forecast.convert.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.opinion.forecast.ForecastConfig;
import edu.opinion.forecast.Observer;
import edu.opinion.forecast.convert.DataConverter;
import edu.opinion.forecast.exception.convert.DatabaseAccessException;
import edu.opinion.forecast.model.ForecastData;

/**
 * 将数据库中的对象转换成特定的用于预测的数据格式
 * 
 * @author ch
 * 
 */
public class DatabaseConverter implements DataConverter {

	/**
	 * 观察者队列
	 */
	private List<Observer> observerList;
	/**
	 * 数据源，热点话题的分类对象
	 */
	private DatabaseClusterObject clusterObject;
	/**
	 * 得到的结果，用于预测的数据
	 */
	private ForecastData forecastData;
	/**
	 * 当前转换器是否正在工作
	 */
	private boolean isActive = false;
	/**
	 * 转换数据的时间间隔
	 */
	private long timespan = ForecastConfig.DAY;
	/**
	 * 存放转换数据的表
	 */
	private Map<Long, Double> countMap;

	/**
	 * 构造函数
	 */
	public DatabaseConverter() {
		observerList = new ArrayList<Observer>();
		forecastData = new ForecastData();
		countMap = new TreeMap<Long, Double>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#getTargetData()
	 */
	public Object getTargetData() {
		return forecastData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.DataConverter#setLeftPeriod(int)
	 */
	public void setLeftPeriod(int leftPeriod) {
		forecastData.setLeftPeriod(leftPeriod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#setDatasource(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setDatasource(Object source, Object sourceFormat) {
		clusterObject = (DatabaseClusterObject) source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#doWork()
	 */
	public void doWork() {
		isActive = true;
		ConvertDAO dao = new ConvertDAO();
		List<DatabaseDataObject> newList = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR, -(ForecastConfig.forecastPeriod
				+ ForecastConfig.minTimePeriod + 1));
		clusterObject.setForecastDate(calendar.getTime());
		//dao.updataForecastTime(clusterObject);

		try {
			newList = dao.getDataFromCluster(clusterObject);
		} catch (DatabaseAccessException e) {
			e.printStackTrace();
			return;
		}
		Date earlyDate = dao.getEarlyDate();
		Date lateDate = dao.getLateDate();
		if (earlyDate == null || lateDate == null) {
			// TODO 错误处理
		}
		// 保存原始数据
		List<DatabaseDataObject> dataList = clusterObject.getDataList();
		if (dataList == null || dataList.size() == 0) {
			// try {
			// dataList= dao.getForecastedDataFromCluster(clusterObject);
			// clusterObject.setDataList(dataList);
			// } catch (DatabaseAccessException e) {
			// //不做处理，认为没有历史数据
			// }
			dataList = new ArrayList<DatabaseDataObject>();
		}

		dataList.addAll(newList);
		int index = getStartIndex(dataList, lateDate);
		for (int i = index - 1; i >= 0; i--) {
			dataList.remove(i);
		}
		if (dataList.size() < 1) {
			// TODO 错误处理
			forecastData = null;
			notifyObserver();
			return;
		}
		earlyDate = dataList.get(0).getTime();

		// 更新数据库中最后预测的时间
		// TODO 注释去掉即可更新数据库
		// clusterObject.setForecastDate(lateDate);
		// dao.updataForecastTime(clusterObject);

		setTimeSpan(earlyDate, lateDate);
		earlyDate = formatEarlyDate(earlyDate);
		lateDate = formatLateDate(lateDate);
		forecastData.setStarDate(earlyDate);
		forecastData.setEndDate(lateDate);
		genarateDateMap(earlyDate, lateDate);

		for (DatabaseDataObject object : dataList) {
			Date date = formatEarlyDate(object.getTime());
			if (date.before(earlyDate)) {
				continue;
			}
			countMap.put(date.getTime(), countMap.get(date.getTime()) + 1);
		}
		List<Double> values = new ArrayList<Double>();
		double totalValue = 0;
		for (Double value : countMap.values()) {
			totalValue += value;
			values.add(totalValue);
		}
		// values.addAll(countMap.values());
		forecastData.setData(values);
		forecastData.setTimeInterval(timespan);
		forecastData.setDataName(clusterObject.getName());

		notifyObserver();

		isActive = false;
	}

	/**
	 * 设置时间间隔 如果时间间隔设为1天，样本空间数量达到30个,设置为1天 如果时间间隔设为6小时，样本空间数量达到30个,设置为6小时
	 * 如果时间间隔设为1小时，样本空间数量达到30个,设置为1小时 如果时间间隔设为1小时，样本空间数量仍不足30个，设为1小时
	 * 
	 * @param earlyDate
	 * @param lateDate
	 */
	private void setTimeSpan(Date earlyDate, Date lateDate) {
		long span = lateDate.getTime() - earlyDate.getTime();
		int minTimePeriod = ForecastConfig.minTimePeriod
				+ forecastData.getLeftPeriod();
		if (span >= minTimePeriod * ForecastConfig.DAY) {
			timespan = ForecastConfig.DAY;
		} else if (span >= minTimePeriod * ForecastConfig.HALFDAY) {
			timespan = ForecastConfig.HALFDAY;
		} else if (span >= minTimePeriod * ForecastConfig.SIXHOUR) {
			timespan = ForecastConfig.SIXHOUR;
		} else {
			timespan = ForecastConfig.HOUR;
		}
		earlyDate.setTime(lateDate.getTime() - timespan * minTimePeriod);
	}

	/**
	 * 在hash map里面设置所有的时间段
	 * 
	 * @param earlyDate
	 *            开始时间
	 * @param lateDate
	 *            结束时间
	 */
	private void genarateDateMap(Date earlyDate, Date lateDate) {
		Calendar calendar = Calendar.getInstance();
		int addedHour = 0;
		if (timespan == ForecastConfig.DAY) {
			addedHour = 24;
		} else if (timespan == ForecastConfig.HALFDAY) {
			addedHour = 12;
		} else if (timespan == ForecastConfig.SIXHOUR) {
			addedHour = 6;
		} else {
			addedHour = 1;
		}

		calendar.setTime(earlyDate);
		while (calendar.getTime().before(lateDate)) {
			countMap.put(calendar.getTime().getTime(), 0.0);
			calendar.add(Calendar.HOUR_OF_DAY, addedHour);
		}
	}

	/**
	 * 将时间转换为标准的开始时间
	 * 
	 * @param date
	 *            要转换的时间
	 * @return 转换得到的时间
	 */
	private Date formatEarlyDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (timespan == ForecastConfig.DAY) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		} else if (timespan == ForecastConfig.HALFDAY) {
			int depart = calendar.get(Calendar.HOUR_OF_DAY) / 12;
			calendar.set(Calendar.HOUR_OF_DAY, 0 + 12 * depart);
		} else if (timespan == ForecastConfig.SIXHOUR) {
			int depart = calendar.get(Calendar.HOUR_OF_DAY) / 6;
			calendar.set(Calendar.HOUR_OF_DAY, 0 + 6 * depart);
		} else {
			int depart = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0 + depart);
		}
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 将时间转换为标准的结束时间
	 * 
	 * @param date
	 *            要转换的时间
	 * @return 转换得到的时间
	 */
	private Date formatLateDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (timespan == ForecastConfig.DAY) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		} else if (timespan == ForecastConfig.HALFDAY) {
			int depart = calendar.get(Calendar.HOUR_OF_DAY) / 12;
			calendar.set(Calendar.HOUR_OF_DAY, 11 + 12 * depart);
		} else if (timespan == ForecastConfig.SIXHOUR) {
			int depart = calendar.get(Calendar.HOUR_OF_DAY) / 6;
			calendar.set(Calendar.HOUR_OF_DAY, 5 + 6 * depart);
		} else {
			int depart = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0 + depart);
		}
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 取得开始的索引值
	 * 
	 * @param list
	 *            存放历史数据的列表
	 * @param lateDate
	 *            最后的时间
	 * @return lateDate 往后推规定个时间段的记录在list的位置
	 */
	private int getStartIndex(List<DatabaseDataObject> list, Date lateDate) {
		if (list == null) {
			return -1;
		}
		int size = list.size() - 1;
		if (size < 0) {
			return -1;
		}
		int index = size / 2;
		int smallIndex = 0;
		int largeIndex = size;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lateDate);
		calendar.add(Calendar.DAY_OF_YEAR,
				-(ForecastConfig.minTimePeriod + forecastData.getLeftPeriod()));
		Date targertDate = calendar.getTime();
		while (index > 0 && index < size) {
			Date dataDate = list.get(index).getTime();
			int res = dataDate.compareTo(targertDate);
			if (res == 0) {
				break;
			} else if (res < 0) {
				if (largeIndex - index == 1) {
					break;
				}
				smallIndex = index;
				index = (index + largeIndex) / 2;
			} else if (res > 0) {
				if (index - smallIndex == 1) {
					index = smallIndex;
					break;
				}
				largeIndex = index;
				index = (index + smallIndex) / 2;
			}
		}

		return index == 0 ? index : index - 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#isActive()
	 */
	public boolean isActive() {
		return isActive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#addObersever(edu.opinion.forecast.data.Observer)
	 */
	public void addObersever(Observer observer) {
		synchronized (observerList) {
			if (!observerList.contains(observer)) {
				observerList.add(observer);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#removeObserver(edu.opinion.forecast.data.Observer)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.data.DataConverter#notifyObserver()
	 */
	public void notifyObserver() {
		int count = 0;
		synchronized (observerList) {
			count = observerList.size();
		}
		for (int i = 0; i < count; i++) {
			Observer observer = null;
			synchronized (observerList) {
				observer = observerList.get(i);
			}
			try {
				observer.update(this, forecastData);
			} catch (RuntimeException e) {
				throw e;
			}
		}

	}
}
