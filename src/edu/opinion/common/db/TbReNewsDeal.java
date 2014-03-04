package edu.opinion.common.db;

public class TbReNewsDeal {

	/**
	 * �������۱��м����¼
	 * 
	 * @param TbReBbs
	 *            ����Ķ���
	 */
	public static void addTbReNews(TbReNews obj) throws Exception {
		BasicDAO.save(obj);
	}

	/**
	 * �������۱���ɾ����¼
	 * 
	 * @param TbReNews
	 *            ɾ���ĵĶ���
	 */
	public static void delTbReNews(TbReNews obj) throws Exception {
		BasicDAO.delete(obj);
	}

	/**
	 * �������۱��и��¼�¼
	 * 
	 * @param TbReNews
	 *            ���µĶ���
	 */
	public static void updateTbReNews(TbReNews obj) throws Exception {
		BasicDAO.update(obj);
	}

	/**
	 * ͨ��ID���������۱��л�ȡ��¼
	 * 
	 * @param id
	 *            ���Ҷ���ID
	 * 
	 * 
	 * @return TbReNews����
	 */
	public static TbReNews queryTbReNewsById(String id) throws Exception {

		return (TbReNews) BasicDAO.queryById(TbReNews.class, id);

	}

	/**
	 * ���ڲ�����ӷ���
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

		// ���Է���
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

		// ������ӷ���
		try {
			TbReNewsDeal.testAddTbReNews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
