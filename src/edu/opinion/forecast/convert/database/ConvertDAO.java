/**
 * 
 */
package edu.opinion.forecast.convert.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.opinion.common.db.BaseHibernateDAO;
import edu.opinion.common.db.TbCluster;
import edu.opinion.common.db.TbClusterDAO;
import edu.opinion.common.db.TbParserBbs;
import edu.opinion.common.db.TbParserNews;
import edu.opinion.common.db.TbReBbs;
import edu.opinion.common.db.TbReNews;
import edu.opinion.forecast.exception.convert.DatabaseAccessException;

/**
 * ���и�����ת���йص����ݿ����
 * 
 * @author ch
 * 
 */
public class ConvertDAO extends BaseHibernateDAO {
	private Date earlyDate = null;
	private Date lateDate = null;

	/**
	 * ȡ�������ȵ㻰���Ӧ�ķ���
	 * 
	 * @return �����ȵ㻰���Ӧ�ķ���
	 */
	@SuppressWarnings("unchecked")
	public List<TbCluster> getHotTopicList() throws DatabaseAccessException {
		try {
			Session session = getSession();
			String hql = "select cluster "
					+ " from TbHotTopic as topic, TbCluster as cluster"
					+ " where topic.tbCluster.id = cluster.id";
			Query query = session.createQuery(hql);
			List<TbCluster> list = query.list();
			return list;
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DatabaseAccessException("���������ȵ㻰������г������ݿ�����쳣", e);
		}
	}

	/**
	 * ����ָ����������Ԥ��ʱ��
	 * 
	 * @param clusterObject
	 */
	public void updataForecastTime(DatabaseClusterObject clusterObject) {
		try {
			TbClusterDAO dao = new TbClusterDAO();
			TbCluster cluster = dao.findById(clusterObject.getId());
			cluster.setForecastTime(clusterObject.getForecastDate());
			dao.save(cluster);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("�ڸ������Ԥ��ʱ��Ĺ����г������ݿ�����쳣", e);
		}
	}

	/**
	 * ȡ�����е�ָ�����������ļ�¼����
	 * 
	 * @param cluster
	 *            ָ���ķ���
	 * @return ��¼������
	 */
	public int getChangedNum(DatabaseClusterObject cluster) {
		// TODO ���Ҽ�¼����
		return 100;
	}

	/**
	 * ���ݸ��������ȡ��������������(ȡ�õ����������ϴ�Ԥ��֮�������)
	 * 
	 * @param cluster
	 *            ָ�������
	 * @return ���ӵ��б�
	 */
	public List<DatabaseDataObject> getDataFromCluster(
			DatabaseClusterObject cluster) throws DatabaseAccessException{
		List<DatabaseDataObject> dataList = new ArrayList<DatabaseDataObject>();
		List<DatabaseDataObject> tmpList = getBBSData(cluster, ">");
		if (tmpList != null) {
			dataList.addAll(tmpList);
		}

		tmpList = getNewsData(cluster, ">");
		if (tmpList != null) {
			dataList.addAll(tmpList);
		}

		return dataList;
	}

	/**
	 * ȡ����ǰ�Ѿ�Ԥ���������
	 * 
	 * @param cluster
	 *            ָ�������
	 * @return ���ӵ��б�
	 */
	public List<DatabaseDataObject> getForecastedDataFromCluster(
			DatabaseClusterObject cluster) throws DatabaseAccessException{
		List<DatabaseDataObject> dataList = new ArrayList<DatabaseDataObject>();
		List<DatabaseDataObject> tmpList = getBBSData(cluster, "<=");
		if (tmpList != null) {
			dataList.addAll(tmpList);
		}

		tmpList = getNewsData(cluster, "<=");
		if (tmpList != null) {
			dataList.addAll(tmpList);
		}

		return dataList;
	}

	/**
	 * ȡ��ָ�����cluster��Ӧ������bss������Ϣ(ȡ�õ����������ϴ�Ԥ��֮�������)
	 * 
	 * @param cluster
	 *            ָ�������
	 * @return ��Ӧ��bbs�������б�
	 */
	@SuppressWarnings("unchecked")
	private List<DatabaseDataObject> getBBSData(DatabaseClusterObject cluster,
			String compareString) throws DatabaseAccessException{
		Session session = getSession();
		StringBuilder sb = new StringBuilder();
		List<DatabaseDataObject> dataList = new ArrayList<DatabaseDataObject>();

		// ����bbs�������б�
		sb.append("select bbs")
			.append(" from TbCluster as cluster ")
			.append(" , TbClusterTopic as topic , TbParserBbs as bbs ")
			.append(" where cluster.id = topic.tbCluster.id ")
			.append(" and topic.tbParserBbs is not null ")
			.append(" and topic.tbParserBbs.id = bbs.id ")
			.append(" and cluster.id=:clusterid ")
			.append(" and bbs.releaseTime ").append(compareString).append(":lasttime ")
			.append(" order by bbs.releaseTime");
		String hql = sb.toString();
		Query query;
		List<TbParserBbs> bbsList = null;
		try {
			query = session.createQuery(hql);
			query.setParameter("lasttime", cluster.getForecastDate());
			query.setParameter("clusterid", cluster.getId());
//			query.setFirstResult(3500);
//			query.setMaxResults(6000);
			bbsList = query.list();

			for (TbParserBbs parserBbs : bbsList) {
				DatabaseDataObject object = new DatabaseDataObject();
				object.setId(parserBbs.getId());
				Date date = parserBbs.getReleaseTime();
				if (lateDate == null || lateDate.before(date)) {
					lateDate = date;
				}
				if (earlyDate == null || earlyDate.after(date)) {
					earlyDate = date;
				}
				object.setTime(date);
				object.setCluster(cluster.getName());
				object.setDataClass(TbParserBbs.class);
				dataList.add(object);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DatabaseAccessException("��ȡ��bbs������¼�Ĺ����г������ݿ�����쳣", e);
		}

		return dataList;
	}

	/**
	 * ȡ��ָ�����cluster��Ӧ������News������Ϣ(ȡ�õ����������ϴ�Ԥ��֮�������)
	 * 
	 * @param cluster
	 *            ָ�������
	 * @return ��Ӧ��News�����б�
	 */
	@SuppressWarnings("unchecked")
	private List<DatabaseDataObject> getNewsData(DatabaseClusterObject cluster,
			String compareString) throws DatabaseAccessException{
		Session session = getSession();
		StringBuilder sb = new StringBuilder();
		List<DatabaseDataObject> dataList = new ArrayList<DatabaseDataObject>();

		// ����news�������б�
		sb.append("select news")
			.append(" from TbCluster as cluster ")
			.append(" , TbClusterTopic as topic , TbParserNews as news ")
			.append(" where cluster.id = topic.tbCluster.id ")
			.append(" and topic.tbParserNews is not null ")
			.append(" and topic.tbParserNews.id = news.id ")
			.append(" and cluster.id=:clusterid")
			.append(" and news.releaseTime").append(compareString).append(":lasttime ")
			.append(" order by news.releaseTime");
		String hql = sb.toString();
		Query query;
		List<TbParserNews> newsList = null;
		try {
			query = session.createQuery(hql);
			query.setParameter("lasttime", cluster.getForecastDate());
			query.setParameter("clusterid", cluster.getId());
			newsList = query.list();

			for (TbParserNews news : newsList) {

				DatabaseDataObject object = new DatabaseDataObject();
				object.setId(news.getId());
				Date date = news.getReleaseTime();
				if (lateDate == null || lateDate.before(date)) {
					lateDate = date;
				}
				if (earlyDate == null || earlyDate.after(date)) {
					earlyDate = date;
				}
				object.setTime(date);
				object.setCluster(cluster.getName());
				object.setDataClass(TbParserNews.class);
				dataList.add(object);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DatabaseAccessException("��ȡ�����ż�¼�Ĺ����г������ݿ�����쳣", e);
		}

		try {
			for (TbParserNews news : newsList) {
				StringBuffer sBuffer = new StringBuffer();
				sBuffer.append("from TbReNews as reNews ")
					.append(" where reNews.tbParserNews.id=:newsid ")
					.append(" and reNews.reTime").append(compareString).append(":lasttime ")
					.append(" order by reNews.reTime");
				query = session.createQuery(sBuffer.toString());
				query.setParameter("newsid", news.getId());
				query.setParameter("lasttime", cluster.getForecastDate());
				List<TbReNews> rebbsList = query.list();
				for (TbReNews reNews : rebbsList) {
					DatabaseDataObject object = new DatabaseDataObject();
					object.setId(reNews.getId());
					Date date = reNews.getReTime();
					if (lateDate == null || lateDate.before(date)) {
						lateDate = date;
					}
					if (earlyDate == null || earlyDate.after(date)) {
						earlyDate = date;
					}
					object.setTime(date);
					object.setCluster(cluster.getName());
					object.setDataClass(TbReNews.class);
					dataList.add(object);
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DatabaseAccessException("��ȡ���������ۼ�¼�Ĺ����г������ݿ�����쳣", e);
		}

		return dataList;
	}

	/**
	 * @return the earlyDate
	 */
	public Date getEarlyDate() {
		return earlyDate;
	}

	/**
	 * @return the lateDate
	 */
	public Date getLateDate() {
		return lateDate;
	}
}
