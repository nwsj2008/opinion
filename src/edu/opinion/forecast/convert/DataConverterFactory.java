package edu.opinion.forecast.convert;

import edu.opinion.forecast.convert.database.DatabaseConverter;

/**
 * ����ת�����Ĺ���
 * 
 * @author ch
 * 
 */
public class DataConverterFactory{
	/**
	 * ȡ��һ�����ݿ�ת����
	 * 
	 * @return �õ������ݿ�ת��������������޷�ȡ�ã��򷵻�null
	 */
	public static DataConverter createDatabaseConverter() {
		return new DatabaseConverter();
	}
}
