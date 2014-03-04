/**
 * 
 */
package edu.opinion.forecast.model.ARIMA;

import java.util.Date;

import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.ts.TSDateRange;

import edu.opinion.forecast.ForecastConfig;

/**
 * ARIMAģ��<br>
 * ������ARIMAģ�͵ĸ��ֲ���������
 * 
 * @author ch
 * 
 */
public class ARIMAModel {
	/**
	 * ADF�����Ƿ����
	 */
	private boolean isADFDone = false;
	/**
	 * ģ��ѡ���Ƿ����
	 */
	private boolean isSpecDone = false;
	/**
	 * ģ�͹����Ƿ����
	 */
	private boolean isEstDone = false;
	/**
	 * �в����м����Ƿ����
	 */
	private boolean isDiagDone = false;
	/**
	 * Ԥ���Ƿ����
	 */
	private boolean isForecastDone = false;
	/**
	 * �Իع����
	 */
	private int p;
	/**
	 * �ƶ�ƽ������
	 */
	private int q;
	/**
	 * ��ֽ���
	 */
	private int d;
	/**
	 * 
	 */
	private int h;
	/**
	 * p��q�����ֵ
	 */
	private int pqmax;
	/**
	 * ��ֵ���У�����Ԥ���ԭʼ���ݣ�
	 */
	private JSCNArray y; // END_DATA
	/**
	 * �������У�����������ϱȽϵ�ԭʼ���ݣ�
	 */
	private JSCNArray yLeft; //
	/**
	 * 
	 */
	private JSCNArray det; // DET_DATA_ALL
	/**
	 * ʱ��(��Ӧ����֬���е�ʱ��)
	 */
	private TSDateRange range; // DRANGE
	/**
	 * ��ʼʱ��
	 */
	private Date startDate;
	/**
	 * ����ʱ��
	 */
	private Date endDate;
	/**
	 * ʱ����
	 */
	private long timeinterval;
	/**
	 * ���е�����
	 */
	private String yName; // END_NAME
	/**
	 * 
	 */
	private JSCSArray detNames; // DET_NAMES_ALL
	/**
	 * 3��1�����飬�ֱ��ǳ������������ơ��������ƣ�1�����У�0��ʾû��
	 */
	private JSCNArray selection; // DET_SEL
	/**
	 * 
	 */  
	private JSCNArray b0;
	/**
	 * 
	 */
	private JSCNArray b;// EST_PARAMS
	/**
	 * log likelihood
	 */
	private JSCNArray ll;// LOG_LIK
	/**
	 * �в�����
	 */
	private JSCNArray e;// EST_RESIDS
	/**
	 * 
	 */
	private JSCNArray covb;// COV_EST_PARAMS
	/**
	 * 
	 */
	private JSCNArray infoCrits;// INFO_CRITS
	/**
	 * Ԥ�����У�ÿ�������У������������ޡ�Ԥ�����ݡ������������ޣ�
	 */
	private JSCNArray forecastDet; // d_forecast
	/**
	 * Ԥ�����е�ʱ��
	 */
	private TSDateRange forecastRange;
	/**
	 * 
	 */
	private JSCNArray stdError; // standard error
	/**
	 * ��������
	 */
	private double ciLevel = 0.95;
	/**
	 * Ԥ�����
	 */
	private int horizon = ForecastConfig.forecastPeriod;
	/**
	 * ADF ���
	 */
	private JSCNArray adf_result;
	/**
	 * ADF �в�����
	 */
	private JSCNArray adf_resids;
	/**
	 * 
	 */
	private int lagLength;
	/**
	 * 1��ʲô����������û�У�<br>
	 * 2�����ϳ�����<br>
	 * 3�����ϳ������������� <br>
	 * 4�����ϳ������������ƺͼ������ƣ�<br>
	 * 5: ���ϳ����ͼ�������
	 */
	private int testState;
	/**
	 * �Ƿ���м��ڼ���
	 */
	private boolean isSeasonCheck;
	/**
	 * �в����м���ʹ�õ�
	 */
	private int diagLags;
	/**
	 * �в����м���Ľ��
	 */
	private JSCNArray portRes;

	private int leftPeriod;

	/**
	 * ADF ����Ľ���ֵ
	 */
	private static final double[][] criticalValue = new double[][] {
			{ 0.0, 0.0, 0.0 }, { -2.56, -1.94, -1.62 },
			{ -3.43, -2.86, -2.57 }, { -3.96, -3.41, -3.13 },
			{ -3.96, -3.41, -3.13 }, { -3.43, -2.86, -2.57 } };

	/**
	 * 
	 */
	public ARIMAModel() {

	}

	/**
	 * 
	 * @param model
	 */
	public ARIMAModel(ARIMAModel model) {
		this.adf_resids = model.adf_resids == null ? null : model.adf_resids
				.copy();
		this.adf_result = model.adf_result == null ? null : model.adf_result
				.copy();
		this.b = model.b == null ? null : model.b.copy();
		this.b0 = model.b0 == null ? null : model.b0.copy();
		this.ciLevel = model.ciLevel;
		this.covb = model.covb == null ? null : model.covb.copy();
		this.d = model.d;
		this.det = model.det == null ? null : model.det.copy();
		this.detNames = model.detNames == null ? null : model.detNames.copy();
		this.e = model.e == null ? null : model.e.copy();
		this.forecastDet = model.forecastDet == null ? null : model.forecastDet
				.copy();
		this.h = model.h;
		this.horizon = model.horizon;
		this.infoCrits = model.infoCrits == null ? null : model.infoCrits
				.copy();
		this.isSeasonCheck = model.isSeasonCheck;
		this.lagLength = model.lagLength;
		this.ll = model.ll == null ? null : model.ll.copy();
		this.p = model.p;
		this.q = model.q;
		this.pqmax = model.pqmax;
		this.range = model.range;
		this.selection = model.selection == null ? null : model.selection
				.copy();
		this.stdError = model.stdError == null ? null : model.stdError.copy();
		this.testState = model.testState;
		this.y = model.y == null ? null : model.y.copy();
		this.yLeft = model.yLeft == null ? null : model.yLeft.copy();
		this.yName = model.yName;
		this.diagLags = model.diagLags;
		this.portRes = model.portRes == null ? null : model.portRes.copy();
		this.isADFDone = model.isADFDone;
		this.isDiagDone = model.isDiagDone;
		this.isEstDone = model.isEstDone;
		this.isForecastDone = model.isForecastDone;
		this.isSpecDone = model.isSpecDone;
		this.leftPeriod = model.leftPeriod;
		this.startDate = model.startDate;
		this.endDate = model.endDate;
		this.timeinterval = model.timeinterval;
		this.forecastRange = model.forecastRange;
	}

	// ���������ж�ADF����Ľ��

	/**
	 * ȡ�� ADF ����Ľ���ֵ
	 * 
	 * @param option
	 *            0��1����1��5����2��10��
	 * @return ����ֵ��ֵ
	 */
	public double getCritical(int option) {
		return criticalValue[testState][option];
	}

	/**
	 * ���� ADF �����ֵ
	 * 
	 * @param option
	 *            0��1����1��5����2��10��
	 * @return 1��6�����飬�ֱ��ǲ�ֽ��������������ԡ����ڡ���ֽ����Ĳ�ͼ��ڵĲ�
	 */
	public double[] getADFResult(int option) {
		double[] res = new double[] { 0, 0, 0, 0, 0, 0 };
		double critical = getCritical(option);

		if(adf_result == null){
			return res;
		}
		double[][] result = adf_result.doubleArray();

		res[0] = -1;
		for (int i = 0; i <= lagLength; i++) { // ��ֽ���
			if (result[i][1] < critical) {
				res[0] = i;
				res[4] = critical - result[i][1];
				break;
			}
		}

		if (testState > 1) { // ����
			res[1] = 1;
		}

		int startIndex = lagLength + 1;
		if (testState == 3 || testState == 4) { // ��������
			if (result[startIndex][1] < critical) {
				res[2] = 1;
			}
			startIndex++;
		}

		if (testState > 3) { // ��������
			for (int a = 1; a < range.lowerBound().subPeriodicity(); a++) {
				if (result[startIndex + a][1] < critical) {
					res[3] = 1;
					res[5] = critical - result[startIndex + a][1];
					break;
				}
			}
		}

		return res;
	}

	// ADF ���� ����

	// ģ�ͼ���

	// ģ�ͼ��� ����

	// �в����м���

	// �в����м��� ����

	/**
	 * @return the p
	 */
	public int getP() {
		return p;
	}

	/**
	 * @return the ciLevel
	 */
	public double getCiLevel() {
		return ciLevel;
	}

	/**
	 * @param ciLevel
	 *            the ciLevel to set
	 */
	public void setCiLevel(double ciLevel) {
		this.ciLevel = ciLevel;
	}

	/**
	 * @return the horizon
	 */
	public int getHorizon() {
		return horizon;
	}

	/**
	 * @param horizon
	 *            the horizon to set
	 */
	public void setHorizon(int horizon) {
		this.horizon = horizon;
	}

	/**
	 * @param p
	 *            the p to set
	 */
	public void setP(int p) {
		this.p = p;
	}

	/**
	 * @return the q
	 */
	public int getQ() {
		return q;
	}

	/**
	 * @param q
	 *            the q to set
	 */
	public void setQ(int q) {
		this.q = q;
	}

	/**
	 * @return the d
	 */
	public int getD() {
		return d;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(int d) {
		this.d = d;
	}

	/**
	 * @return the h
	 */
	public int getH() {
		return h;
	}

	/**
	 * @param h
	 *            the h to set
	 */
	public void setH(int h) {
		this.h = h;
	}

	/**
	 * @return the pqmax
	 */
	public int getPqmax() {
		return pqmax;
	}

	/**
	 * @param pqmax
	 *            the pqmax to set
	 */
	public void setPqmax(int pqmax) {
		this.pqmax = pqmax;
	}

	/**
	 * @return the y
	 */
	public JSCNArray getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(JSCNArray y) {
		this.y = y;
	}

	/**
	 * @return the det
	 */
	public JSCNArray getDet() {
		return det;
	}

	/**
	 * @param det
	 *            the det to set
	 */
	public void setDet(JSCNArray det) {
		this.det = det;
	}

	/**
	 * @return the range
	 */
	public TSDateRange getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(TSDateRange range) {
		this.range = range;
	}

	/**
	 * @return the yName
	 */
	public String getYName() {
		return yName;
	}

	/**
	 * @param name
	 *            the yName to set
	 */
	public void setYName(String name) {
		yName = name;
	}

	/**
	 * @return the detNames
	 */
	public JSCSArray getDetNames() {
		return detNames;
	}

	/**
	 * @param detNames
	 *            the detNames to set
	 */
	public void setDetNames(JSCSArray detNames) {
		this.detNames = detNames;
	}

	/**
	 * @return the forecastDet
	 */
	public JSCNArray getForecastDet() {
		return forecastDet;
	}

	/**
	 * @param forecastDet
	 *            the forecastDet to set
	 */
	public void setForecastDet(JSCNArray forecastDet) {
		this.forecastDet = forecastDet;
	}

	/**
	 * @return the selection
	 */
	public JSCNArray getSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            the selection to set
	 */
	public void setSelection(JSCNArray selection) {
		this.selection = selection;
	}

	/**
	 * @return the b0
	 */
	public JSCNArray getB0() {
		return b0;
	}

	/**
	 * @param b0
	 *            the b0 to set
	 */
	public void setB0(JSCNArray b0) {
		this.b0 = b0;
	}

	/**
	 * @return the b
	 */
	public JSCNArray getB() {
		return b;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(JSCNArray b) {
		this.b = b;
	}

	/**
	 * @return the ll
	 */
	public JSCNArray getLl() {
		return ll;
	}

	/**
	 * @param ll
	 *            the ll to set
	 */
	public void setLl(JSCNArray ll) {
		this.ll = ll;
	}

	/**
	 * @return the e
	 */
	public JSCNArray getE() {
		return e;
	}

	/**
	 * @param e
	 *            the e to set
	 */
	public void setE(JSCNArray e) {
		this.e = e;
	}

	/**
	 * @return the covb
	 */
	public JSCNArray getCovb() {
		return covb;
	}

	/**
	 * @param covb
	 *            the covb to set
	 */
	public void setCovb(JSCNArray covb) {
		this.covb = covb;
	}

	/**
	 * @return the infoCrits
	 */
	public JSCNArray getInfoCrits() {
		return infoCrits;
	}

	/**
	 * @param infoCrits
	 *            the infoCrits to set
	 */
	public void setInfoCrits(JSCNArray infoCrits) {
		this.infoCrits = infoCrits;
	}

	/**
	 * @return the stdError
	 */
	public JSCNArray getStdError() {
		return stdError;
	}

	/**
	 * @param stdError
	 *            the stdError to set
	 */
	public void setStdError(JSCNArray stdError) {
		this.stdError = stdError;
	}

	/**
	 * @return the adf_result
	 */
	public JSCNArray getAdf_result() {
		return adf_result;
	}

	/**
	 * @param adf_result
	 *            the adf_result to set
	 */
	public void setAdf_result(JSCNArray adf_result) {
		this.adf_result = adf_result;
	}

	/**
	 * @return the adf_resids
	 */
	public JSCNArray getAdf_resids() {
		return adf_resids;
	}

	/**
	 * @param adf_resids
	 *            the adf_resids to set
	 */
	public void setAdf_resids(JSCNArray adf_resids) {
		this.adf_resids = adf_resids;
	}

	/**
	 * @return the lagLength
	 */
	public int getLagLength() {
		return lagLength;
	}

	/**
	 * @param lagLength
	 *            the lagLength to set
	 */
	public void setLagLength(int lagLength) {
		this.lagLength = lagLength;
	}

	/**
	 * @return the testState
	 */
	public int getTestState() {
		return testState;
	}

	/**
	 * @param testState
	 *            the testState to set
	 */
	public void setTestState(int testState) {
		this.testState = testState;
	}

	/**
	 * @return the isSeasonCheck
	 */
	public boolean isSeasonCheck() {
		return isSeasonCheck;
	}

	/**
	 * @param isSeasonCheck
	 *            the isSeasonCheck to set
	 */
	public void setSeasonCheck(boolean isSeasonCheck) {
		this.isSeasonCheck = isSeasonCheck;
	}

	/**
	 * @return the diagLags
	 */
	public int getDiagLags() {
		return diagLags;
	}

	/**
	 * @param diagLags
	 *            the diagLags to set
	 */
	public void setDiagLags(int diagLags) {
		this.diagLags = diagLags;
	}

	/**
	 * @return the portRes
	 */
	public JSCNArray getPortRes() {
		return portRes;
	}

	/**
	 * @param portRes
	 *            the portRes to set
	 */
	public void setPortRes(JSCNArray portRes) {
		this.portRes = portRes;
	}

	/**
	 * @return the isADFDone
	 */
	public boolean isADFDone() {
		return isADFDone;
	}

	/**
	 * @param isADFDone
	 *            the isADFDone to set
	 */
	public void setADFDone(boolean isADFDone) {
		this.isADFDone = isADFDone;
	}

	/**
	 * @return the isSpecDone
	 */
	public boolean isSpecDone() {
		return isSpecDone;
	}

	/**
	 * @param isSpecDone
	 *            the isSpecDone to set
	 */
	public void setSpecDone(boolean isSpecDone) {
		this.isSpecDone = isSpecDone;
	}

	/**
	 * @return the isEstDone
	 */
	public boolean isEstDone() {
		return isEstDone;
	}

	/**
	 * @param isEstDone
	 *            the isEstDone to set
	 */
	public void setEstDone(boolean isEstDone) {
		this.isEstDone = isEstDone;
	}

	/**
	 * @return the isDiagDone
	 */
	public boolean isDiagDone() {
		return isDiagDone;
	}

	/**
	 * @param isDiagDone
	 *            the isDiagDone to set
	 */
	public void setDiagDone(boolean isDiagDone) {
		this.isDiagDone = isDiagDone;
	}

	/**
	 * @return the isForecastDone
	 */
	public boolean isForecastDone() {
		return isForecastDone;
	}

	/**
	 * @param isForecastDone
	 *            the isForecastDone to set
	 */
	public void setForecastDone(boolean isForecastDone) {
		this.isForecastDone = isForecastDone;
	}

	/**
	 * @return the forecastRange
	 */
	public TSDateRange getForecastRange() {
		return forecastRange;
	}

	/**
	 * @param forecastRange
	 *            the forecastRange to set
	 */
	public void setForecastRange(TSDateRange forecastRange) {
		this.forecastRange = forecastRange;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the timeinterval
	 */
	public long getTimeinterval() {
		return timeinterval;
	}

	/**
	 * @param timeinterval
	 *            the timeinterval to set
	 */
	public void setTimeinterval(long timeinterval) {
		this.timeinterval = timeinterval;
	}

	/**
	 * @return the yLeft
	 */
	public JSCNArray getYLeft() {
		return yLeft;
	}

	/**
	 * @param left
	 *            the yLeft to set
	 */
	public void setYLeft(JSCNArray left) {
		yLeft = left;
	}

	/**
	 * @return the leftPeriod
	 */
	public int getLeftPeriod() {
		return leftPeriod;
	}

	/**
	 * @param leftPeriod
	 *            the leftPeriod to set
	 */
	public void setLeftPeriod(int leftPeriod) {
		this.leftPeriod = leftPeriod;
	}

}
