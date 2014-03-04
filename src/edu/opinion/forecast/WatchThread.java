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
	 * 单例
	 */
	private static WatchThread instance = null;
	/**
	 * 分类管理器
	 */
	private ClusterManager clusterManager;
	/**
	 * 是否停止线程
	 */
	private boolean isCancle = false;
	/**
	 * 线程是否正在运行
	 */
	private boolean isRunning = false;
	/**
	 * 生成预测图的路径
	 */
	private String filePath = System.getProperty("user.dir") + "\\";
	/**
	 * 预测的运行状态
	 */
	private ForecastState forecastState = null;

	/**
	 * 构造函数
	 */
	private WatchThread() {
		initialize();
	}

	/**
	 * 获得单例
	 * 
	 * @return WatchThread对象
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
	 * 初始化
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
		
		// 载入所有的热点话题
		clusterManager.loadHotTopicCluster(); 
		// 检查每一个话题
		ClusterObject cluster = clusterManager.begin();
		while (cluster != null) {
			if(isCancle){ //停止任务
				break;
			}
			forecastState = new ForecastState();
			forecastState.setWorkState(ForecastState.READAYTOWORK);
			forecastState.setCluster(cluster);
			forecast(cluster, forecastState);
			//每次只运行一个预测的任务
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
	 * 根据分类的名称做预测
	 * 
	 * @param clusterName
	 *            分类的名称
	 */
	public void forecast(String clusterName) {
		System.out.println("start forecasting...");
		ClusterObject clusterObject = clusterManager
				.getClusterObjectByName(clusterName);
		ForecastState forecastState = new ForecastState();
		forecast(clusterObject, forecastState);
	}

	/**
	 * 根据分类做预测
	 * 
	 * @param cluster
	 *            分类的对象
	 */
	public void forecast(ClusterObject cluster, ForecastState forecastState) {
		if (cluster == null) {
			return;
		}
		// 数据转换器
		DataConverter converter = DataConverterFactory
				.createDatabaseConverter();
		if (converter != null) {
			converter.setDatasource(cluster, null);
			converter.setLeftPeriod(ForecastConfig.observPeriod);
			// 数据转换线程
			Thread convertThread = new Thread(new DataConvertThread(converter));
			// 预测线程
			ForecastThread forecastThread = new ForecastThread();
			// 显示数据转换线程
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
	 * 停止监视线程
	 */
	public void cancleThread() {
		isCancle = true;
	} 

	/**
	 * 被聚类对象唤醒进行的动作 ◎◎
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
	 * 主函数<br>
	 * 开始预测过程
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