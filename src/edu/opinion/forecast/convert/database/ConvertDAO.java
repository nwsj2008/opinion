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
 * 进行跟数据转化有关的数据库访问
 * 
 * @author ch
 * 
 */
public class ConvertDAO extends BaseHibernateDAO {
	private Date earlyDate = null;
	private Date lateDate = null;

	/**
	 * 取得所有热点话题对应的分类
	 * 
	 * @return 所有热点话题对应的分类
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
			throw new DatabaseAccessException("载入所有热点话题过程中出现数据库访问异常", e);
		}
	}

	/**
	 * 更新指定分类的最近预测时间
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
			throw new RuntimeException("在更新最近预测时间的过程中出现数据库访问异常", e);
		}
	}

	/**
	 * 取得所有的指定分类新增的记录条数
	 * 
	 * @param cluster
	 *            指定的分类
	 * @return 记录的条数
	 */
	public int getChangedNum(DatabaseClusterObject cluster) {
		// TODO 查找记录条数
		return 100;
	}

	/**
	 * 根据给定的类别取得所有类别的帖子(取得的帖子是在上次预测之后的帖子)
	 * 
	 * @param cluster
	 *            指定的类别
	 * @return 帖子的列表
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
	 * 取得以前已经预测过的数据
	 * 
	 * @param cluster
	 *            指定的类别
	 * @return 帖子的列表
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
	 * 取得指定类别cluster对应的所有bss帖子信息(取得的帖子是在上次预测之后的帖子)
	 * 
	 * @param cluster
	 *            指定的类别
	 * @return 对应的bbs的帖子列表
	 */
	@SuppressWarnings("unchecked")
	private List<DatabaseDataObject> getBBSData(DatabaseClusterObject cluster,
			String compareString) throws DatabaseAccessException{
		Session session = getSession();
		StringBuilder sb = new StringBuilder();
		List<DatabaseDataObject> dataList = new ArrayList<DatabaseDataObject>();

		// 查找bbs的帖子列表
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
			throw new DatabaseAccessException("在取回bbs主贴记录的过程中出现数据库访问异常", e);
		}

		return dataList;
	}

	/**
	 * 取得指定类别cluster对应的所有News帖子信息(取得的帖子是在上次预测之后的帖子)
	 * 
	 * @param cluster
	 *            指定的类别
	 * @return 对应的News帖子列表
	 */
	@SuppressWarnings("unchecked")
	private List<DatabaseDataObject> getNewsData(DatabaseClusterObject cluster,
			String compareString) throws DatabaseAccessException{
		Session session = getSession();
		StringBuilder sb = new StringBuilder();
		List<DatabaseDataObject> dataList = new ArrayList<DatabaseDataObject>();

		// 查找news的帖子列表
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
			throw new DatabaseAccessException("在取回新闻记录的过程中出现数据库访问异常", e);
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
			throw new DatabaseAccessException("在取回新闻评论记录的过程中出现数据库访问异常", e);
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
