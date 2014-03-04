/**
 * 
 */
package edu.opinion.forecast.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.ts.TS;
import com.jstatcom.ts.TSDate;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.util.UMatrix;

import edu.opinion.forecast.ForecastConfig;
import edu.opinion.forecast.ForecastState;
import edu.opinion.forecast.Observer;
import edu.opinion.forecast.exception.model.ForecastErrorException;
import edu.opinion.forecast.exception.model.ForecastOverTimeException;
import edu.opinion.forecast.model.ARIMA.ARIMAModel;
import edu.opinion.forecast.model.ARIMA.ARIMAPCall;

/**
 * Ԥ���߳�
 * 
 * @author ch
 * 
 */
public class ForecastThread implements Observer, Runnable {

	/**
	 * ʹ�� ARIMA �㷨����Ԥ��
	 */
	public static final int ARIMAFORECAST = 1;
	/**
	 * ʹ��ָ��ƽ����Exponential Smoothing���㷨����Ԥ��
	 */
	public static final int EXPONENTIALFORECAST = 2;

	/**
	 * �۲����б�
	 */
	private List<Observer> observerList;

	/**
	 * Ԥ������
	 */
	private ForecastData forecastData;
	/**
	 * ��������
	 */
	private int workType = 0;
	/**
	 * ָ��ƽ��ģ��
	 */
	ARIMAModel arimaModel;

	/**
	 * ���캯��
	 */
	public ForecastThread() {
		observerList = new ArrayList<Observer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("forecast thread running...");
		switch (workType) {
		case 1:
			try {
				ARIMAForecast2();
			} catch (ForecastOverTimeException e) {
				e.printStackTrace();
				throw new RuntimeException(arimaModel.getYName() + "Ԥ����̳�ʱ.", e);
			} catch (ForecastErrorException e) {
				throw new RuntimeException(
						arimaModel.getYName() + "Ԥ������г��ִ���.", e);
			}
			break;
		case 2:
			break;
		}
		notifyObserver();
	}

	/**
	 * ���� ARIMA Ԥ��
	 * @throws ForecastOverTimeException
	 * @throws ForecastErrorException
	 */
	public void ARIMAForecast() throws ForecastOverTimeException,
			ForecastErrorException {
		ARIMAPCall call = new ARIMAPCall();
		arimaModel = call.getModel();
		long waitedTime = 0; // �ȴ���ʱ��

		// ת��forecastData
		List<Double> values = forecastData.getData();
		int size = values.size();
		int leftPeriod = forecastData.getLeftPeriod();
		double[] yData = new double[size - leftPeriod];
		if (yData.length < ForecastConfig.minTimePeriod) {
			return;
		}
		double[] yLeftData = new double[leftPeriod];
		double lastValue = -1;
		int positiveNum = 0;
		for (int i = 0; i < size; i++) {
			if (i < size - leftPeriod) {
				if (lastValue != values.get(i)) {
					positiveNum++;
				}
				yData[i] = values.get(i);
				lastValue = yData[i];
			} else {
				yLeftData[i - size + leftPeriod] = values.get(i);
			}
		}
		if (positiveNum < yData.length / 2) {
			String picPath = ForecastConfig.forecastPath
					+ Integer.toString((forecastData.getDataName().replace(' ',
							'_').hashCode())) + ".png";
			try {
				FileInputStream inStream = new FileInputStream(new File(
						ForecastConfig.forecastPath
								+ ForecastConfig.noPicPathString));
				File outFile = new File(picPath);
				outFile.createNewFile();
				FileOutputStream outStream = new FileOutputStream(outFile);
				byte[] data = new byte[inStream.available()];
				inStream.read(data);
				outStream.write(data);
				inStream.close();
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
			//throw new ForecastErrorException("Ԥ�����д���!");
		}
		// ���е����Ƽ�Ϊ���������
		arimaModel.setYName(forecastData.getDataName().replace(' ', '_'));
		JSCNArray y = new JSCNArray(arimaModel.getYName(), yData);
		arimaModel.setY(y);
		JSCNArray yLeft = new JSCNArray("left_data", yLeftData);
		arimaModel.setYLeft(yLeft);

		// ��ʼת��ʱ��
		// �����ʱ�����е�ʱ�䶼������Ȼ������������
		TSDateRange range = new TSDateRange(new TSDate(1, 1), new TSDate(size
				- leftPeriod, 1));
		arimaModel.setRange(range);
		arimaModel.setStartDate(forecastData.getStarDate()); // �����Ŀ�ʼʱ��
		arimaModel.setEndDate(forecastData.getEndDate()); // �����Ľ���ʱ��
		arimaModel.setTimeinterval(forecastData.getTimeInterval()); // ʱ����
		// ת��ʱ�����

		// ADF Test ����λ�����飩
		// TODO �Ը���������м��飬�����ǲ��Ӽ������Ƶļ���
		List<ARIMAModel> modelList = new ArrayList<ARIMAModel>();

		arimaModel.setLagLength(2);
		for (int i = 1; i < 4; i++) {
			ARIMAModel model = new ARIMAModel(arimaModel);
			model.setTestState(i);
			if (i < 4) {
				model.setSeasonCheck(false);
			} else {
				model.setSeasonCheck(true);
			}
			modelList.add(model);
			call.setModel(model);
			call.setWork(ARIMAPCall.ADFTEST);
			call.execute();
			while (!call.isDone()) {
				try {
					waitedTime += 100;
					if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
						// throw new ForecastOverTimeException("��λ������(ADF)��ʱ"
						// + "�۲����еĸ�ʽ�����ǲ���ȷ��");
						break;
					}
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(i + call.getOutString());
		}

		int modelIndex = 0;
		double maxResidual = 0;
		for (int i = 0; i < 2; i++) {
			double residual = modelList.get(i).getADFResult(1)[4];
			if (residual > maxResidual) {
				maxResidual = residual;
				modelIndex = i;
			}
		}
		System.out.println(modelIndex);
		System.out.println();
		System.out.println();

		// ADF ������
		arimaModel = new ARIMAModel(modelList.get(modelIndex));
		call.setModel(arimaModel);
		double[] ADFResult = arimaModel.getADFResult(1);
		if (ADFResult[0] == -1) {
			ADFResult[0] = 0;
			// throw new ForecastErrorException("��λ������(ADF)ʧ��" + "���в��ܱ�ƽ�Ȼ�");
		}
		// arimaModel.setD((int)ADFResult[0]);
		arimaModel.setD(1);
		arimaModel.setH(8);
		arimaModel.setPqmax(3);

		// specification ��������

		JSCNArray det = new JSCNArray("det");
		JSCSArray detNames = new JSCSArray("det_names");
		JSCNArray selection = new JSCNArray("DET_CEL", new int[] {
				(int) ADFResult[1], (int) ADFResult[3], (int) ADFResult[2] });
		if (ADFResult[1] == 1) { // const
			det.setVal(new JSCNArray("const", UMatrix
					.ones(size - leftPeriod, 1)));
			detNames.setVal("const");
		}
		if (ADFResult[3] == 1) { // season
			TS[] seasTS = range.createSeasDumTS(false, false);
			JSCNArray seasDum = new JSCNArray("seasDum");
			String[] seasNames = new String[seasTS.length];
			int index = 0;
			for (TS ts : seasTS) {
				seasDum.appendCols(new JSCNArray(ts.name(), ts.values()));
				seasNames[index++] = ts.name();
			}
			det.appendCols(seasDum);
			detNames.appendRows(new JSCSArray("seasNames", seasNames));
		}
		if (ADFResult[2] == 1) { // linner
			JSCNArray trend = new JSCNArray("const", UMatrix.seqa(1, 1, size
					- leftPeriod));
			det.appendCols(trend);
			detNames.appendRows(new JSCSArray("trendName", "TREND"));
		}
		arimaModel.setDet(det);
		arimaModel.setDetNames(detNames);
		arimaModel.setSelection(selection);

		// ģ�Ͳ�������
		call.setWork(ARIMAPCall.SPECIFICATION);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					throw new ForecastOverTimeException("��������(specification)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());

		// estimation �����õ���ģ��
		call.setWork(ARIMAPCall.ESTIMATION);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					// throw new
					// ForecastOverTimeException("ģ������(estimation)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());

		// ʹ��portmanteau ����õ���ģ��
		arimaModel.setDiagLags(10);
		call.setWork(ARIMAPCall.DIAGNOSTIC);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					throw new ForecastOverTimeException("ģ�ͼ���(diagnostic)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());

		if (arimaModel.getPortRes().doubleAt(0, 3) < 0.05) {
			// throw new ForecastErrorException("ģ�ͼ���ʧ�ܣ�ģ�Ͳ����п�Ԥ����");
		}

		// ����Ԥ��
		call.setWork(ARIMAPCall.FORECAST);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					throw new ForecastOverTimeException("Ԥ�����(forecast)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());
	}

	/**
	 * ARIMAԤ���
	 * @throws ForecastOverTimeException
	 * @throws ForecastErrorException
	 */
	public void ARIMAForecast2() throws ForecastOverTimeException,
			ForecastErrorException {
		ARIMAPCall call = new ARIMAPCall();
		arimaModel = call.getModel();
		long waitedTime = 0; // �ȴ���ʱ��

		// ת��forecastData
		List<Double> values = forecastData.getData();
		int size = values.size();
		int leftPeriod = forecastData.getLeftPeriod();
		double[] yData = new double[size - leftPeriod];
		double[] yLeftData = new double[leftPeriod];
		double lastValue = -1;
		int positiveNum = 0;
		for (int i = 0; i < size; i++) {
			if (i < size - leftPeriod) {
				if (lastValue != values.get(i)) {
					positiveNum++;
				}
				yData[i] = values.get(i);
				lastValue = yData[i];
			} else {
				yLeftData[i - size + leftPeriod] = values.get(i);
			}
		}
		if (positiveNum < yData.length / 3) {
			String picPath = ForecastConfig.forecastPath
					+ Integer.toString((forecastData.getDataName().replace(' ',
							'_').hashCode())) + ".png";
			try {
				String inFileString = ForecastConfig.forecastPath
						+ ForecastConfig.noPicPathString;
				FileInputStream inStream = new FileInputStream(new File(
						inFileString));
				File outFile = new File(picPath);
				outFile.createNewFile();
				FileOutputStream outStream = new FileOutputStream(outFile);
				byte[] data = new byte[inStream.available()];
				inStream.read(data);
				outStream.write(data);
				inStream.close();
				outStream.close();
				for (Observer observer : observerList) {
					if (observer instanceof ForecastState) {
						((ForecastState) observer)
								.setWorkState(ForecastState.FINISHDISPLAYCONVERT);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			throw new ForecastErrorException("Ԥ�����д���!");
		}

		// ���е����Ƽ�Ϊ���������
		arimaModel.setYName(forecastData.getDataName().replace(' ', '_'));
		JSCNArray y = new JSCNArray(arimaModel.getYName(), yData);
		arimaModel.setY(y);
		JSCNArray yLeft = new JSCNArray("left_data", yLeftData);
		arimaModel.setYLeft(yLeft);

		// ��ʼת��ʱ��
		// �����ʱ�����е�ʱ�䶼������Ȼ������������
		TSDateRange range = new TSDateRange(new TSDate(1, 1), new TSDate(size
				- leftPeriod, 1));
		arimaModel.setRange(range);
		arimaModel.setStartDate(forecastData.getStarDate()); // �����Ŀ�ʼʱ��
		arimaModel.setEndDate(forecastData.getEndDate()); // �����Ľ���ʱ��
		arimaModel.setTimeinterval(forecastData.getTimeInterval()); // ʱ����
		// ת��ʱ�����

		// ADF Test ����λ�����飩
		// TODO �Ը���������м��飬�����ǲ��Ӽ������Ƶļ���
		List<ARIMAModel> modelList = new ArrayList<ARIMAModel>();

		arimaModel.setLagLength(2);
		arimaModel.setH(8);
		arimaModel.setPqmax(3);
		arimaModel.setADFDone(true);

		int constValue = 1;
		int linnerValue = 0;
		int seasonValue = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 1; j < 3; j++) {
				ARIMAModel model = new ARIMAModel(arimaModel);
				model.setD(i);
				if (j == 0) {
					constValue = 0;
					linnerValue = 0;
					seasonValue = 0;
				} else if (j == 1) {
					constValue = 1;
					linnerValue = 0;
					seasonValue = 0;
				} else if (j == 2) {
					constValue = 1;
					linnerValue = 1;
					seasonValue = 0;
				} else if (j == 3) {
					constValue = 1;
					linnerValue = 0;
					seasonValue = 1;
				} else if (j == 4) {
					constValue = 1;
					linnerValue = 1;
					seasonValue = 1;
				}
				JSCNArray det = new JSCNArray("det");
				JSCSArray detNames = new JSCSArray("det_names");
				JSCNArray selection = new JSCNArray("DET_CEL", new int[] {
						constValue, seasonValue, linnerValue });
				if (constValue == 1) { // const
					det.setVal(new JSCNArray("const", UMatrix.ones(size
							- leftPeriod, 1)));
					detNames.setVal("const");
				}
				if (seasonValue == 1) { // season
					TS[] seasTS = range.createSeasDumTS(false, false);
					JSCNArray seasDum = new JSCNArray("seasDum");
					String[] seasNames = new String[seasTS.length];
					int index = 0;
					for (TS ts : seasTS) {
						seasDum
								.appendCols(new JSCNArray(ts.name(), ts
										.values()));
						seasNames[index++] = ts.name();
					}
					det.appendCols(seasDum);
					detNames.appendRows(new JSCSArray("seasNames", seasNames));
				}
				if (linnerValue == 1) { // linner
					JSCNArray trend = new JSCNArray("const", UMatrix.seqa(1, 1,
							size - leftPeriod));
					det.appendCols(trend);
					detNames.appendRows(new JSCSArray("trendName", "TREND"));
				}

				model.setDet(det);
				model.setDetNames(detNames);
				model.setSelection(selection);

				modelList.add(model);
				call.setModel(model);
				// specification ��������

				// ģ�Ͳ�������
				call.setWork(ARIMAPCall.SPECIFICATION);
				call.execute();
				waitedTime = 0;
				while (!call.isDone()) {
					try {
						waitedTime += 100;
						if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
							throw new ForecastOverTimeException(
									"��������(specification)��ʱ");
						}
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(call.getOutString());
			}
		}

		int minValue = 0;
		int maxValue = 6;
		for (ARIMAModel model : modelList) {
			if (model.getP() + model.getQ() < maxValue
					&& model.getP() + model.getQ() >= minValue) {
				maxValue = model.getP() + model.getQ();
				arimaModel = new ARIMAModel(model);
			}
		}
		if (maxValue == 6) {
			arimaModel = new ARIMAModel(modelList.get(0));
		}

		System.out.println(arimaModel.getP());
		System.out.println(arimaModel.getQ());
		System.out.println(arimaModel.getSelection().doubleArray()[0][0]);
		System.out.println(arimaModel.getSelection().doubleArray()[1][0]);
		System.out.println(arimaModel.getSelection().doubleArray()[2][0]);
		System.out.println();

		call.setModel(arimaModel);
		// estimation �����õ���ģ��
		call.setWork(ARIMAPCall.ESTIMATION);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					// throw new
					// ForecastOverTimeException("ģ������(estimation)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());

		// ʹ��portmanteau ����õ���ģ��
		arimaModel.setDiagLags(10);
		call.setWork(ARIMAPCall.DIAGNOSTIC);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					throw new ForecastOverTimeException("ģ�ͼ���(diagnostic)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());

		if (arimaModel.getPortRes().doubleAt(0, 3) < 0.05) {
			// throw new ForecastErrorException("ģ�ͼ���ʧ�ܣ�ģ�Ͳ����п�Ԥ����");
		}

		// ����Ԥ��
		call.setWork(ARIMAPCall.FORECAST);
		call.execute();
		waitedTime = 0;
		while (!call.isDone()) {
			try {
				waitedTime += 100;
				if (waitedTime > ForecastConfig.overTime) { // ���㳬ʱ
					throw new ForecastOverTimeException("Ԥ�����(forecast)��ʱ");
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(call.getOutString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.Observer#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void update(Object o, Object param) {
		this.forecastData = (ForecastData) param;
		if(this.forecastData == null){
			arimaModel = null;
			notifyObserver();
			return;
		}
		this.workType = 1;
		try {
			this.run();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * ����һ���۲���
	 * 
	 * @param observer
	 *            Ҫ���ӵĹ۲���
	 */
	public void addObserver(Observer observer) {
		synchronized (observerList) {
			if (!observerList.contains(observer)) {
				observerList.add(observer);
			}
		}
	}

	/**
	 * ���ѹ۲���
	 */
	private void notifyObserver() {
		synchronized (observerList) {
			for (Observer observer : observerList) {
				observer.update(this, arimaModel);
			}
		}
	}

}
