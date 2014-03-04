package edu.opinion.common.db;

import java.util.List;

public class TbParserNewsDeal {
	/**
	 * ���������м����¼
	 * @param TbParserNews ����Ķ���
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
	 * ����������ɾ����¼
	 * @param TbParserNews ɾ���ĵĶ���
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
	 * ���������и��¼�¼
	 * @param TbParserNews ���µĶ���
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
	 * ͨ��ID�����������л�ȡ��¼
	 * @param id ���Ҷ���ID
	 * 
	 * @return TbParserNews����
	 * @throws Exception 
	 * */
	public static TbParserNews queryTbParserNewsById(String id) throws Exception{
		
		return (TbParserNews)BasicDAO.queryById(TbParserNews.class, id);
		
	}
	
	/**
	 * ����hql�����Ҷ���
	 * 
	 * @param hql
	 * @return
	 * @throws Exception
	 * */
	public static List queryTbParserNewsByHql(String hql) throws Exception{
		
		return BasicDAO.queryByHql(hql);
		
	}
	
	/**
	 * ���ڲ�����ӷ���
	 * */
	public static boolean testAddTbParserNews() throws Exception {
		boolean flag = false;
		
		TbParserNews obj = new TbParserNews();
		obj.setTitle("Oh My God!");
		obj.setAuthor("���泿");
		obj.setContent("���Բ���"); 
		//tpb.setReleaseTime(releaseTime);
		obj.setReNum(2);
		
		//���Է���
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
	 * ���ڲ��Բ��ҷ���
	 * 
	 * */
	public static boolean testqueryTbParserNewsById(String id) throws Exception {
		boolean flag = false;
		
		//���Է���
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
		
		//������ӷ���
//		try {
//			TbParserNewsDeal.testAddTbParserNews();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//���Բ��ҷ���
		try {
			TbParserNewsDeal.testqueryTbParserNewsById("4af01def195c5faa01195c5fc47d0001");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
