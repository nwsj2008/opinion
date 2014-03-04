package edu.opinion.forecast.convert;

import edu.opinion.forecast.convert.database.DatabaseConverter;

/**
 * 数据转换器的工厂
 * 
 * @author ch
 * 
 */
public class DataConverterFactory{
	/**
	 * 取得一个数据库转换器
	 * 
	 * @return 得到的数据库转换器，如果现在无法取得，则返回null
	 */
	public static DataConverter createDatabaseConverter() {
		return new DatabaseConverter();
	}
}
