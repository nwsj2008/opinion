package edu.opinion.forecast;

import edu.bjtu.incs.opinion.jobbase.JobActions;
import edu.opinion.forecast.convert.database.DatabaseClusterObject;

/**
 * 预测组的任务
 * @author ch
 *
 */
public class ForecastJob implements JobActions {
	/**
	 * 任务正在运行
	 */
	private static String RUNNING = "\u8FD0\u884C";	//running
	/**
	 * 任务挂起
	 */
	private static String SUSPEND = "\u6302\u8D77";	//suspend
	/**
	 * 任务完成
	 */
	private static String FINISHED = "\u5B8C\u6210";	//finished
	/**
	 * 任务停止
	 */
	private static String STOPPED = "\u505C\u6B62";	//stopped
	
	/**
	 * 任务暂停
	 */
	private static String PAUSED = "\u6682\u505C";	//paused
	/**
	 * 任务等待
	 */
	//private static String WAITTING = "\u7B49\u5F85";	//waitting
	
	/**
	 * 任务状态
	 */
	private String status = SUSPEND;
	/**
	 * 任务名称
	 */
	private String jobname = "预测";
	/**
	 * ？
	 */
	private String profile = null;
	/**
	 * 子任务数量
	 */
	private int counter = 0;
	/**
	 * 最大子任务数量为12
	 */
	private static int MAXCOUNTER = 12;	
	

	public String getJobname() {
		return jobname;
	}
	
	public String getProfile(){
		return profile;
	}

	public int getNumOfFinishedTask() {
		return counter;
	}

	public int getNumOfTask() {
		return MAXCOUNTER;
	}

	public String getState() {
		WatchThread thread = WatchThread.getInstance();
		synchronized (thread) {
			if (thread.isRunning() && status.equals(RUNNING)) {
				this.status = RUNNING;
			} else {
				this.status = FINISHED;
			}
		}
		return this.status;
	}

	synchronized public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	synchronized public void setProfile(String profile) {
		this.profile = profile;
	}

	synchronized public void setState(String state) {
		this.status = state;
	}

	public void run() {
		System.out.println("forecast job begins running...");
		this.status = RUNNING;
		if(this.jobname != null)
			Thread.currentThread().setName(this.jobname);
		Thread thread = new Thread(WatchThread.getInstance());
		thread.start();
		while(status.equals(RUNNING)){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(ForecastConfig.isInit){
				status = getState();
			}
		}
		System.out.println("forecast job finishes successfully...");
	}

	public void start() {
		this.run();
	}

	public void stop() {
		System.err.println("now try to stop forecast job...");
		synchronized (status) {
			this.status = STOPPED;
		}
		WatchThread thread = WatchThread.getInstance();
		synchronized (thread) {
			thread.cancleThread();
		}
		System.err.println("stop forecast job successfully...");
	}

	public void pause() {
		System.err.println("now try to pause forecast job...");
		this.status = PAUSED;
	}
	
	public static void main(String[] args) {
		ForecastJob job = new ForecastJob();
		Thread thread = new Thread(job);
		thread.start();
		System.err.println("start forecast job...");
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		job.stop();
		System.err.println("stop forecast job...");
	}
	
	/**
	 * 生成具体的工作状态<br>
	 * 包含正在预测的类，以及预测的当前状态
	 * @return
	 */
	public String generateStatus(){
		String clusterName = ((DatabaseClusterObject)WatchThread.getInstance().getForecastState().getCluster()).getName();
		String forecastStatus = "";
		int forecastState = WatchThread.getInstance().getForecastState().getWorkState();
		switch (forecastState) {
		case ForecastState.READAYTOWORK:
			forecastStatus = "数据聚集";
			break;
		case ForecastState.FINISHDATACONVERT:
			forecastStatus = "预测";
			break;
		case ForecastState.FINISHFORECAST:
			forecastStatus = "预测图生成";
			break;
		case ForecastState.FINISHDISPLAYCONVERT:
			forecastStatus = "完成";
			break;
		default:
			forecastStatus = "未知状态";
			break;
		}
		String sta = "当前正在处理的类：" + clusterName + ";当前工作状态：" + forecastStatus;
		return sta;
	}

}
