package edu.opinion.common.db;

import java.util.List;

public class TbParserNewsDeal {
	/**
	 * 新闻主表中加入记录
	 * @param TbParserNews 插入的对象
	 * */
	public static void addTbParserNews(TbParserNews obj){
		try {
			BasicDAO.save(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 新闻主表中删除记录
	 * @param TbParserNews 删除的的对象
	 * */
	public static void delTbParserNews(TbParserNews obj){
		try {
			BasicDAO.delete(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 新闻主表中更新记录
	 * @param TbParserNews 更新的对象
	 * */
	public static void updateTbParserNews(TbParserNews obj){
		try {
			BasicDAO.update(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过ID从新闻主表中获取记录
	 * @param id 查找对象ID
	 * 
	 * @return TbParserNews对象
	 * @throws Exception 
	 * */
	public static TbParserNews queryTbParserNewsById(String id) throws Exception{
		
		return (TbParserNews)BasicDAO.queryById(TbParserNews.class, id);
		
	}
	
	/**
	 * 根据hql语句查找对象
	 * 
	 * @param hql
	 * @return
	 * @throws Exception
	 * */
	public static List queryTbParserNewsByHql(String hql) throws Exception{
		
		return BasicDAO.queryByHql(hql);
		
	}
	
	/**
	 * 用于测试添加方法
	 * */
	public static boolean testAddTbParserNews() throws Exception {
		boolean flag = false;
		
		TbParserNews obj = new TbParserNews();
		obj.setTitle("Oh My God!");
		obj.setAuthor("李舒晨");
		obj.setContent("测试测试"); 
		//tpb.setReleaseTime(releaseTime);
		obj.setReNum(2);
		
		//测试方法
		try{
			TbParserNewsDeal.addTbParserNews(obj);
			System.out.println("Add TbParserNewsObj Successfully!");
			flag = true;
		}catch(Exception e){
			System.out.println("addTbParserNews error!");
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * 用于测试查找方法
	 * 
	 * */
	public static boolean testqueryTbParserNewsById(String id) throws Exception {
		boolean flag = false;
		
		//测试方法
		try{
			TbParserNews tpb = TbParserNewsDeal.queryTbParserNewsById(id);
			System.out.println(tpb.getAuthor());
			System.out.println("Query TbParserNewsObj Successfully!");
			flag = true;
		}catch(Exception e){
			System.out.println("queryTbParserNewsById error!");
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public static void main(String[] args){
		
		//测试添加方法
//		try {
//			TbParserNewsDeal.testAddTbParserNews();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//测试查找方法
		try {
			TbParserNewsDeal.testqueryTbParserNewsById("4af01def195c5faa01195c5fc47d0001");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
