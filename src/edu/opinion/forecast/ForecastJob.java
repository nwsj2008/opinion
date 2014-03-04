package edu.opinion.forecast;

import edu.bjtu.incs.opinion.jobbase.JobActions;
import edu.opinion.forecast.convert.database.DatabaseClusterObject;

/**
 * Ԥ���������
 * @author ch
 *
 */
public class ForecastJob implements JobActions {
	/**
	 * ������������
	 */
	private static String RUNNING = "\u8FD0\u884C";	//running
	/**
	 * �������
	 */
	private static String SUSPEND = "\u6302\u8D77";	//suspend
	/**
	 * �������
	 */
	private static String FINISHED = "\u5B8C\u6210";	//finished
	/**
	 * ����ֹͣ
	 */
	private static String STOPPED = "\u505C\u6B62";	//stopped
	
	/**
	 * ������ͣ
	 */
	private static String PAUSED = "\u6682\u505C";	//paused
	/**
	 * ����ȴ�
	 */
	//private static String WAITTING = "\u7B49\u5F85";	//waitting
	
	/**
	 * ����״̬
	 */
	private String status = SUSPEND;
	/**
	 * ��������
	 */
	private String jobname = "Ԥ��";
	/**
	 * ��
	 */
	private String profile = null;
	/**
	 * ����������
	 */
	private int counter = 0;
	/**
	 * �������������Ϊ12
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
	 * ���ɾ���Ĺ���״̬<br>
	 * ��������Ԥ����࣬�Լ�Ԥ��ĵ�ǰ״̬
	 * @return
	 */
	public String generateStatus(){
		String clusterName = ((DatabaseClusterObject)WatchThread.getInstance().getForecastState().getCluster()).getName();
		String forecastStatus = "";
		int forecastState = WatchThread.getInstance().getForecastState().getWorkState();
		switch (forecastState) {
		case ForecastState.READAYTOWORK:
			forecastStatus = "���ݾۼ�";
			break;
		case ForecastState.FINISHDATACONVERT:
			forecastStatus = "Ԥ��";
			break;
		case ForecastState.FINISHFORECAST:
			forecastStatus = "Ԥ��ͼ����";
			break;
		case ForecastState.FINISHDISPLAYCONVERT:
			forecastStatus = "���";
			break;
		default:
			forecastStatus = "δ֪״̬";
			break;
		}
		String sta = "��ǰ���ڴ�����ࣺ" + clusterName + ";��ǰ����״̬��" + forecastStatus;
		return sta;
	}

}
