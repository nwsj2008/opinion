/**
 * 
 */
package edu.opinion.forecast.display;

/**
 * 显示数据转换器的工厂，用来产生显示数据转换器
 * 
 * @author ch
 * 
 */
public class DisplayConverterFactory {
	public static DisplayXMLConverter createDisplayXMLConverter() {
		return new DisplayXMLConverter();
	}
}
