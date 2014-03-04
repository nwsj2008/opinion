/**
 * 
 */
package edu.opinion.forecast.model.ARIMA;

import java.io.File;
import java.io.IOException;

import com.jstatcom.engine.gauss.GaussLoadTypes;
import com.jstatcom.io.FileSupport;
import com.jstatcom.model.JSCData;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCNumber;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.model.JSCString;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.util.FArg;
import com.jstatcom.util.UString;

import edu.opinion.forecast.model.GaussPCall;

/**
 * ARIMA模型的调用和计算过程<br>
 * 定义了ARIMA模型计算过程中的各个方法和过程
 * 
 * @author ch
 * 
 */
public class ARIMAPCall extends GaussPCall {
	/**
	 * 对模型进行选取以及参数估计
	 */
	public static final int SPECIFICATION = 1;
	/**
	 * 评估估计得到的模型
	 */
	public static final int ESTIMATION = 2;
	/**
	 * 根据得到的模型对给定的时间序列进行预测
	 */
	public static final int FORECAST = 3;
	/**
	 * 进行ADF检验，判断差分次数以及季节性
	 */
	public static final int ADFTEST = 4;
	/**
	 * 模型的检验，判断模型是否合格
	 */
	public static final int DIAGNOSTIC = 5;

	/**
	 * 预测的模型
	 */
	public ARIMAModel model = null;
	/**
	 * 当前工作的类型
	 */
	private int workType = 0;
	/**
	 * 当前工作是否完成
	 */
	private boolean isDone = false;
	/**
	 * 输出的文本
	 */
	private String outString = "";

	// 参数估计(specification)用到
	/**
	 * 
	 */
	private JSCNArray allResults;
	/**
	 * 
	 */
	private JSCNArray optLags;

	// 模型检验（estimation）使用的
	/**
	 * 
	 */
	private File outFile = null;

	// ADF Test 使用的

	/**
	 * 构造函数
	 */
	public ARIMAPCall() {
		setName("ARIMA forecast");
		model = new ARIMAModel();
	}

	/**
	 * 取得预测模型
	 * 
	 * @return 预测模型
	 */
	public ARIMAModel getModel() {
		return model;
	}

	/**
	 * 设置预测模型
	 * 
	 * @param model
	 *            要预测的模型
	 */
	public void setModel(ARIMAModel model) {
		this.model = model;
	}

	/**
	 * 设置工作类型
	 * 
	 */
	public void setWork(int worktype) {
		outString = "";
		isDone = false;
		switch (worktype) {
		case ADFTEST:
			break;
		case SPECIFICATION:
			if (!model.isADFDone()) {
				return;
			}
			break;
		case ESTIMATION:
			if (!model.isSpecDone()) {
				return;
			}
			break;
		case FORECAST:
			if (!model.isEstDone()) {
				return;
			}
			break;
		case DIAGNOSTIC:
			if (!model.isEstDone()) {
				return;
			}
			break;
		}

		this.workType = worktype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jstatcom.engine.PCall#runCode()
	 */
	@Override
	protected void runCode() {
		switch (workType) {
		case SPECIFICATION: // 判断符合的模型
			doSpecificationWork();
			break;
		case ESTIMATION: // 评估得到的模型
			doEstimationWork();
			break;
		case FORECAST: // 预测
			doForecastWork();
			break;
		case ADFTEST:// 判断差分结束及季节周期
			doADFTestWork();
			break;
		case DIAGNOSTIC: // 判断得到的模型是否合理
			doDiagnosticWork();
			break;
		default:
			break;
		}
	}

	/**
	 * 进行模型的判断及参数估计
	 */
	private void doSpecificationWork() {
		double[][] resultData = new double[(model.getPqmax() + 1)
				* (model.getPqmax() + 1)][5];
		double[][] lagData = new double[3][2];
		allResults = new JSCNArray("allResults", resultData);
		optLags = new JSCNArray("optlags", lagData);

		engine().load("arima, uni", GaussLoadTypes.LIB);

		engine().call(
				"hannan_rissanen_uni",
				new JSCData[] { model.getY(), model.getDet(),
						new JSCInt("h", model.getH()),
						new JSCInt("pmax", model.getPqmax()),
						new JSCInt("d", model.getD()) },
				new JSCData[] { optLags, allResults, });
	}

	/**
	 * 对得到的模型进行评估
	 */
	private void doEstimationWork() {

		try {
			outFile = File.createTempFile("ARIMA", "OUT");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JSCString fileName = new JSCString("outFileName", UString
				.replaceAllSubStrings(outFile.getAbsolutePath(), "\\", "/"));

		JSCNArray b0 = new JSCNArray("b0", 0); //
		JSCNArray b = new JSCNArray("b"); // EST_PARAMS
		JSCNArray ll = new JSCNArray("ll"); // LOG_LIK
		JSCNArray e = new JSCNArray("e"); // EST_RESIDS
		JSCNArray covb = new JSCNArray("covb"); // COV_EST_PARAMS
		JSCNArray infoCrits = new JSCNArray("infoCrits"); // INFO_CRITS

		model.setB0(b0);
		model.setB(b);
		model.setLl(ll);
		model.setE(e);
		model.setCovb(covb);
		model.setInfoCrits(infoCrits);

		JSCData[] rtns = new JSCData[] { b, ll, e, covb, infoCrits };

		engine().load("arima, uni", GaussLoadTypes.LIB);

		JSCSArray allNames = new JSCSArray("allNames", model.getYName());
		allNames.appendRows(model.getDetNames());

		JSCData[] inArgs = new JSCData[] { b0, model.getY(),
				new JSCInt("p", model.getP()), new JSCInt("d", model.getD()),
				new JSCInt("q", model.getQ()), model.getDet(), fileName,
				allNames };

		engine().call("arima_uni", inArgs, rtns);
	}

	/**
	 * 进行预测
	 */
	private void doForecastWork() {
		double[][] forecastData = new double[20][6];
		JSCNArray forecastDet = new JSCNArray("forecast", forecastData);
		double[][] errorData = new double[10][1];
		JSCNArray stdErrors = new JSCNArray("forecast_error", errorData);

		model.setForecastDet(forecastDet);
		model.setStdError(stdErrors);

		JSCData[] rtns = new JSCData[] { forecastDet, stdErrors };

		engine().load("arima, uni", GaussLoadTypes.LIB);

		JSCData[] inArgs = new JSCData[] { model.getB(), model.getY(),
				new JSCInt("p", model.getP()), new JSCInt("d", model.getD()),
				new JSCInt("q", model.getQ()), model.getDet(), model.getE(),
				new JSCNumber("ciLevel", model.getCiLevel()),
				new JSCInt("horizon", model.getHorizon()) };

		engine().call("arimaForecast_uni", inArgs, rtns);
	}

	/**
	 * 进行ADF检验
	 */
	private void doADFTestWork() {
		JSCNArray seasDum = new JSCNArray("seasDum");
		if (model.isSeasonCheck())
			seasDum = new JSCNArray("seasDum", model.getRange().createSeasDum(
					false, false));

		JSCNArray adf_result = new JSCNArray("adf_result");
		JSCNArray adf_resids = new JSCNArray("adf_resids");
		model.setAdf_result(adf_result);
		model.setAdf_resids(adf_resids);

		engine().load("ur", GaussLoadTypes.LIB);
		engine().call(
				"adf_urtests",
				new JSCData[] { model.getY(),
						new JSCInt("lags", model.getLagLength()), seasDum,
						new JSCInt("teststate", model.getTestState()) },
				new JSCData[] { adf_result, adf_resids });
	}

	/**
	 * 进行残差序列的检验
	 */
	private void doDiagnosticWork() {
		JSCNArray res = model.getE();
		JSCNArray portRes = new JSCNArray("protRes");

		model.setPortRes(portRes);

		engine().load("fil", GaussLoadTypes.LIB);
		engine().load("diag", GaussLoadTypes.LIB);
		int correction = model.getP() + model.getQ()
				+ model.getDetNames().cols();

		engine().call(
				"portman_diagnos",
				new JSCData[] { res, new JSCInt("lags", model.getDiagLags()),
						new JSCInt("correction", correction) },
				new JSCData[] { portRes });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jstatcom.engine.PCall#finalCode()
	 */
	@Override
	protected void finalCode() {
		switch (workType) {
		case SPECIFICATION: // 判断符合的模型
			finalSpecificationWork();
			break;
		case ESTIMATION:// 评估得到的模型
			finalEstimationWork();
			break;
		case FORECAST:// 预测
			finalForecastWork();
			break;
		case ADFTEST: // ADF检验
			finalADFTestWork();
			break;
		case DIAGNOSTIC:
			finalDiagnosticWork();
			break;
		default:
			break;
		}
	}

	/**
	 * 完成模型的判断及参数估计
	 */
	private void finalSpecificationWork() {
		model.setP(optLags.intAt(1, 0));
		model.setQ(optLags.intAt(1, 1));
		TSDateRange tempRange = new TSDateRange(model.getRange().lowerBound(),
				model.getRange().upperBound());

		tempRange = (tempRange.addPeriodsToStart(model.getH()
				+ model.getPqmax() + model.getD()));
		// outString = "p=" + model.getP() + ",q=" + model.getQ();
		output.append("OPTIMAL LAGS FROM HANNAN-RISSANEN MODEL SELECTION\n");
		output.append("(Hannan & Rissanen, 1982, Biometrika 69)\n\n");

		output.append(FArg.sprintf("%-30s %s \n", "original variable:", model
				.getYName()));
		output.append(FArg.sprintf("%-30s %s \n", "order of differencing (d):",
				model.getD()));

		output.append(tempRange.format("adjusted sample range:", 31) + "\n\n");

		output
				.append("optimal lags p, q (searched all combinations where max(p,q) <= "
						+ model.getPqmax() + ")\n");
		output.append(FArg.sprintf("%-25s p=%i, q=%i\n",
				"Akaike Info Criterion:", optLags.intAt(0, 0), optLags.intAt(0,
						1)));
		output.append(FArg.sprintf("%-25s p=%i, q=%i\n",
				"Hannan-Quinn Criterion:", optLags.intAt(1, 0), optLags.intAt(
						1, 1)));
		output.append(FArg.sprintf("%-25s p=%i, q=%i\n", "Schwarz Criterion:",
				optLags.intAt(2, 0), optLags.intAt(2, 1)));
		outString = output.toString();

		model.setSpecDone(true);
		isDone = true;
	}

	/**
	 * 完成模型检测过程
	 */
	private void finalEstimationWork() {
		int toAdd = model.getRange().numOfObs() - model.getE().rows();
		TSDateRange r = model.getRange().addPeriodsToStart(toAdd);

		output.append(r.format("sample range:", 20) + "\n");

		output.append(FileSupport.getInstance().readTextFile(
				outFile.getAbsolutePath()));
		outString = output.toString();
		model.setEstDone(true);
		isDone = true;
	}

	/**
	 * 完成预测过程
	 */
	private void finalForecastWork() {
		TSDateRange r = new TSDateRange(model.getRange().upperBound()
				.addPeriods(1), model.getRange().upperBound().addPeriods(
				model.getHorizon()));
		model.setForecastRange(r);
		output.append("\nforecasted variable: " + model.getYName()
				+ " (in levels)\n");
		output.append(r.format("forecast range:", 21) + "\n\n");

		output.append(FArg.sprintf("%-14s %-20s %-20s %-20s %-20s\n", "time",
				"lower CI", "forecast", "upper CI", "std. err"));

		String[] times = r.timeAxisStringArray();
		for (int i = 0; i < model.getForecastDet().cols(); i++)
			output.append(FArg.sprintf(
					"%-14s %- 20.4f %- 20.4f %- 20.4f %- 20.4f\n", times[i],
					model.getForecastDet().doubleAt(1, i), model
							.getForecastDet().doubleAt(0, i), model
							.getForecastDet().doubleAt(2, i), model
							.getStdError().doubleAt(i, 0)));

		outString = output.toString();
		model.setForecastDone(true);
		isDone = true;
	}

	/**
	 * 完成单位根检验（ADF TEST）过程
	 */
	private void finalADFTestWork() {
		StringBuffer buffer = output;
		double result[][] = model.getAdf_result().doubleArray();

		buffer.append(FArg.sprintf("%-25s %s \n", new FArg(
				"ADF Test for series:").add(model.getYName())));
		buffer.append(model.getRange().addPeriodsToStart(
				model.getLagLength() + 1).format("sample range:", 26)
				+ "\n");
		buffer.append(FArg.sprintf("%-25s %i \n", new FArg(
				"lagged differences:").add(model.getLagLength())));

		// Critical Values.
		addCriticalValues(model.getTestState());

		buffer.append(FArg.sprintf("value of test statistic: %.4f\n", new FArg(
				result[0][1])));
		buffer.append("regression results:\n");
		buffer.append("---------------------------------------\n");
		buffer.append(FArg.sprintf("%-13s %-13s %-13s\n", new FArg("variable")
				.add("coefficient").add("t-statistic")));
		buffer.append("---------------------------------------\n");

		buffer.append(FArg.sprintf("%-13s %- 13.4f %- 13.4f\n", new FArg(
				" x(-1)").add(result[0][0]).add(result[0][1])));
		for (int a = 1; a < model.getLagLength() + 1; a++)
			buffer.append(FArg.sprintf("%-13s %- 13.4f %- 13.4f\n", new FArg(
					"dx(-" + a + ")").add(result[a][0]).add(result[a][1])));
		if (model.getTestState() > 1)
			buffer.append(FArg.sprintf("%-13s %- 13.4f %- 13.4f\n", new FArg(
					"constant").add(result[model.getLagLength() + 1][0]).add(
					result[model.getLagLength() + 1][1])));
		if (model.getTestState() == 3 || model.getTestState() == 4)
			buffer.append(FArg.sprintf("%-13s %- 13.4f %- 13.4f\n", new FArg(
					"trend").add(result[model.getLagLength() + 2][0]).add(
					result[model.getLagLength() + 2][1])));
		for (int a = 1; a < model.getRange().lowerBound().subPeriodicity(); a++) {
			if (model.getTestState() == 4)
				buffer.append(FArg.sprintf("%-13s %- 13.4f %- 13.4f\n",
						new FArg("sdummy(" + (a + 1) + ")").add(
								result[model.getLagLength() + 2 + a][0]).add(
								result[model.getLagLength() + 2 + a][1])));
			if (model.getTestState() == 5)
				buffer.append(FArg.sprintf("%-13s %- 13.4f %- 13.4f\n",
						new FArg("sdummy(" + (a + 1) + ")").add(
								result[model.getLagLength() + 1 + a][0]).add(
								result[model.getLagLength() + 1 + a][1])));
		}
		buffer.append(FArg.sprintf("%-13s %- 13.4f\n", new FArg("RSS")
				.add(result[result.length - 1][0])));
		buffer.append("\n");

		outString = output.toString();
		model.setADFDone(true);
		isDone = true;
	}

	/**
	 * 
	 * @param testState
	 */
	private void addCriticalValues(int testState) {
		StringBuffer buffer = output;
		String ref = "asymptotic critical values\nreference: Davidson, R. and MacKinnon, J. (1993),\n\"Estimation and Inference in Econometrics\" p 708, table 20.1,\nOxford University Press, London\n";
		if (testState == 1) {
			buffer.append("no intercept, no time trend\n");
			buffer.append(ref);
			buffer.append(FArg.sprintf(
					"%-10s %-10s %-10s \n%-10s %-10s %-10s\n", new FArg(" 1%")
							.add(" 5%").add(" 10%").add("-2.56").add("-1.94")
							.add("-1.62")));
		}
		if (testState == 2) {
			buffer.append("intercept, no time trend\n");
			buffer.append(ref);
			buffer.append(FArg.sprintf(
					"%-10s %-10s %-10s \n%-10s %-10s %-10s\n", new FArg(" 1%")
							.add(" 5%").add(" 10%").add("-3.43").add("-2.86")
							.add("-2.57")));
		}
		if (testState == 3) {
			buffer.append("intercept, time trend\n");
			buffer.append(ref);
			buffer.append(FArg.sprintf(
					"%-10s %-10s %-10s \n%-10s %-10s %-10s\n", new FArg(" 1%")
							.add(" 5%").add(" 10%").add("-3.96").add("-3.41")
							.add("-3.13")));
		}
		if (testState == 4) {
			buffer.append("intercept, time trend, seasonal dummies\n");
			buffer.append(ref);
			buffer.append(FArg.sprintf(
					"%-10s %-10s %-10s \n%-10s %-10s %-10s\n", new FArg(" 1%")
							.add(" 5%").add(" 10%").add("-3.96").add("-3.41")
							.add("-3.13")));
		}
		if (testState == 5) {
			buffer.append("intercept, seasonal dummies, no time trend\n");
			buffer.append(ref);
			buffer.append(FArg.sprintf(
					"%-10s %-10s %-10s \n%-10s %-10s %-10s\n", new FArg(" 1%")
							.add(" 5%").add(" 10%").add("-3.43").add("-2.86")
							.add("-2.57")));
		}
	}

	/**
	 * 残差序列检验结果
	 */
	private void finalDiagnosticWork() {
		StringBuffer buffer = output;

		buffer.append("PORTMANTEAU TEST with " + model.getDiagLags()
				+ " lags\n\n");
		buffer.append(FArg.sprintf("%-25s %- 13.4f\n", new FArg("Portmanteau:")
				.add(model.getPortRes().doubleAt(0, 0))));
		buffer.append(FArg.sprintf("%-25s %- 13.4f\n", new FArg(
				" p-Value (Chi^2):").add(model.getPortRes().doubleAt(0, 1))));
		buffer.append(FArg.sprintf("%-25s %- 13.4f\n", new FArg("Ljung & Box:")
				.add(model.getPortRes().doubleAt(0, 2))));
		buffer.append(FArg.sprintf("%-25s %- 13.4f\n", new FArg(
				" p-Value (Chi^2):").add(model.getPortRes().doubleAt(0, 3))));

		buffer.append("\n");

		outString = output.toString();
		isDone = true;
		model.setDiagDone(true);
	}

	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * @return the outString
	 */
	public String getOutString() {
		return outString;
	}
}
