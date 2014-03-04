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
 * �����ݿ��еĶ���ת�����ض�������Ԥ������ݸ�ʽ
 * 
 * @author ch
 * 
 */
public class DatabaseConverter implements DataConverter {

	/**
	 * �۲��߶���
	 */
	private List<Observer> observerList;
	/**
	 * ����Դ���ȵ㻰��ķ������
	 */
	private DatabaseClusterObject clusterObject;
	/**
	 * �õ��Ľ��������Ԥ�������
	 */
	private ForecastData forecastData;
	/**
	 * ��ǰת�����Ƿ����ڹ���
	 */
	private boolean isActive = false;
	/**
	 * ת�����ݵ�ʱ����
	 */
	private long timespan = ForecastConfig.DAY;
	/**
	 * ���ת�����ݵı�
	 */
	private Map<Long, Double> countMap;

	/**
	 * ���캯��
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
			// TODO ������
		}
		// ����ԭʼ����
		List<DatabaseDataObject> dataList = clusterObject.getDataList();
		if (dataList == null || dataList.size() == 0) {
			// try {
			// dataList= dao.getForecastedDataFromCluster(clusterObject);
			// clusterObject.setDataList(dataList);
			// } catch (DatabaseAccessException e) {
			// //����������Ϊû����ʷ����
			// }
			dataList = new ArrayList<DatabaseDataObject>();
		}

		dataList.addAll(newList);
		int index = getStartIndex(dataList, lateDate);
		for (int i = index - 1; i >= 0; i--) {
			dataList.remove(i);
		}
		if (dataList.size() < 1) {
			// TODO ������
			forecastData = null;
			notifyObserver();
			return;
		}
		earlyDate = dataList.get(0).getTime();

		// �������ݿ������Ԥ���ʱ��
		// TODO ע��ȥ�����ɸ������ݿ�
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
	 * ����ʱ���� ���ʱ������Ϊ1�죬�����ռ������ﵽ30��,����Ϊ1�� ���ʱ������Ϊ6Сʱ�������ռ������ﵽ30��,����Ϊ6Сʱ
	 * ���ʱ������Ϊ1Сʱ�������ռ������ﵽ30��,����Ϊ1Сʱ ���ʱ������Ϊ1Сʱ�������ռ������Բ���30������Ϊ1Сʱ
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
	 * ��hash map�����������е�ʱ���
	 * 
	 * @param earlyDate
	 *            ��ʼʱ��
	 * @param lateDate
	 *            ����ʱ��
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
	 * ��ʱ��ת��Ϊ��׼�Ŀ�ʼʱ��
	 * 
	 * @param date
	 *            Ҫת����ʱ��
	 * @return ת���õ���ʱ��
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
	 * ��ʱ��ת��Ϊ��׼�Ľ���ʱ��
	 * 
	 * @param date
	 *            Ҫת����ʱ��
	 * @return ת���õ���ʱ��
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
	 * ȡ�ÿ�ʼ������ֵ
	 * 
	 * @param list
	 *            �����ʷ���ݵ��б�
	 * @param lateDate
	 *            ����ʱ��
	 * @return lateDate �����ƹ涨��ʱ��εļ�¼��list��λ��
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
