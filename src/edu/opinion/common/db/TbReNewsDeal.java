package edu.opinion.common.db;

public class TbReNewsDeal {

	/**
	 * 新闻评论表中加入记录
	 * 
	 * @param TbReBbs
	 *            插入的对象
	 */
	public static void addTbReNews(TbReNews obj) throws Exception {
		BasicDAO.save(obj);
	}

	/**
	 * 新闻评论表中删除记录
	 * 
	 * @param TbReNews
	 *            删除的的对象
	 */
	public static void delTbReNews(TbReNews obj) throws Exception {
		BasicDAO.delete(obj);
	}

	/**
	 * 新闻评论表中更新记录
	 * 
	 * @param TbReNews
	 *            更新的对象
	 */
	public static void updateTbReNews(TbReNews obj) throws Exception {
		BasicDAO.update(obj);
	}

	/**
	 * 通过ID从新闻评论表中获取记录
	 * 
	 * @param id
	 *            查找对象ID
	 * 
	 * 
	 * @return TbReNews对象
	 */
	public static TbReNews queryTbReNewsById(String id) throws Exception {

		return (TbReNews) BasicDAO.queryById(TbReNews.class, id);

	}

	/**
	 * 用于测试添加方法
	 */
	public static boolean testAddTbReNews() throws Exception {
		boolean flag = false;

		TbReNews obj = new TbReNews();
		obj.setReTitle("Re Oh My God!");
		obj.setReAuthor("zyc");
		obj.setReContent("zyc zyc");
		// obj.setReleaseTime(releaseTime);
		TbParserNews tpb = TbParserNewsDeal
				.queryTbParserNewsById("4af01da4190e2a9e01190e2aa1280001");
		obj.setTbParserNews(tpb);

		// 测试方法
		try {
			TbReNewsDeal.addTbReNews(obj);
			System.out.println("Add TbReNewsObj Successfully!");
		} catch (Exception e) {
			System.out.println("addTbReNews error!");
			e.printStackTrace();
		}

		return flag;
	}

	public static void main(String[] args) {

		// 测试添加方法
		try {
			TbReNewsDeal.testAddTbReNews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
