package edu.opinion.forecast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.opinion.forecast.convert.ClusterManager;
import edu.opinion.forecast.convert.ClusterObject;
import edu.opinion.forecast.convert.DataConvertThread;
import edu.opinion.forecast.convert.DataConverter;
import edu.opinion.forecast.convert.DataConverterFactory;
import edu.opinion.forecast.display.DisplayConverterThread;
import edu.opinion.forecast.model.ForecastThread;

/**
 * 
 * @author ch
 * 
 */
public class WatchThread implements Runnable, Observer {
	/**
	 * ����
	 */
	private static WatchThread instance = null;
	/**
	 * ���������
	 */
	private ClusterManager clusterManager;
	/**
	 * �Ƿ�ֹͣ�߳�
	 */
	private boolean isCancle = false;
	/**
	 * �߳��Ƿ���������
	 */
	private boolean isRunning = false;
	/**
	 * ����Ԥ��ͼ��·��
	 */
	private String filePath = System.getProperty("user.dir") + "\\";
	/**
	 * Ԥ�������״̬
	 */
	private ForecastState forecastState = null;

	/**
	 * ���캯��
	 */
	private WatchThread() {
		initialize();
	}

	/**
	 * ��õ���
	 * 
	 * @return WatchThread����
	 */
	public static WatchThread getInstance() {
		if (instance == null) {
			instance = new WatchThread();
		}
		if (!ForecastConfig.isInit) {
			ForecastConfig.getForecastConfig();
		}
		return instance;
	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {
		clusterManager = ClusterManager.createDatabaseClusterManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		isRunning = true;
		isCancle = false;
		
		// �������е��ȵ㻰��
		clusterManager.loadHotTopicCluster(); 
		// ���ÿһ������
		ClusterObject cluster = clusterManager.begin();
		while (cluster != null) {
			if(isCancle){ //ֹͣ����
				break;
			}
			forecastState = new ForecastState();
			forecastState.setWorkState(ForecastState.READAYTOWORK);
			forecastState.setCluster(cluster);
			forecast(cluster, forecastState);
			//ÿ��ֻ����һ��Ԥ�������
			while (forecastState.getWorkState() != ForecastState.FINISHDISPLAYCONVERT) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			cluster = clusterManager.next();
		}
		
		isRunning = false;	
	}

	/**
	 * ���ݷ����������Ԥ��
	 * 
	 * @param clusterName
	 *            ���������
	 */
	public void forecast(String clusterName) {
		System.out.println("start forecasting...");
		ClusterObject clusterObject = clusterManager
				.getClusterObjectByName(clusterName);
		ForecastState forecastState = new ForecastState();
		forecast(clusterObject, forecastState);
	}

	/**
	 * ���ݷ�����Ԥ��
	 * 
	 * @param cluster
	 *            ����Ķ���
	 */
	public void forecast(ClusterObject cluster, ForecastState forecastState) {
		if (cluster == null) {
			return;
		}
		// ����ת����
		DataConverter converter = DataConverterFactory
				.createDatabaseConverter();
		if (converter != null) {
			converter.setDatasource(cluster, null);
			converter.setLeftPeriod(ForecastConfig.observPeriod);
			// ����ת���߳�
			Thread convertThread = new Thread(new DataConvertThread(converter));
			// Ԥ���߳�
			ForecastThread forecastThread = new ForecastThread();
			// ��ʾ����ת���߳�
			DisplayConverterThread displayThread = new DisplayConverterThread(
					forecastState);
			displayThread.setFilePath(filePath + "opinionImg\\");
			converter.addObersever(forecastState);
			converter.addObersever(forecastThread);
			forecastThread.addObserver(forecastState);
			forecastThread.addObserver(displayThread);
			convertThread.start();
		}

	}

	/**
	 * ֹͣ�����߳�
	 */
	public void cancleThread() {
		isCancle = true;
	} 

	/**
	 * ����������ѽ��еĶ��� ���
	 * 
	 * @param o
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (arg == null) {
			return;
		}
		List<String> clusterIdList = (List<String>) arg;
		for (String id : clusterIdList) {
			ClusterObject clusterObject = clusterManager
					.getClusterObjectByID(id);
			ForecastState forecastState = new ForecastState();
			forecast(clusterObject, forecastState);
		}
	}

	/**
	 * ������<br>
	 * ��ʼԤ�����
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(WatchThread.getInstance()).start();
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
		//this.filePath = filePath;
	}

	/**
	 * @return the forecastState
	 */
	public ForecastState getForecastState() {
		return forecastState;
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

}