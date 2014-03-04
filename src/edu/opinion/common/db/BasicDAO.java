package edu.opinion.common.db;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BasicDAO {
	protected static final Log log = LogFactory.getLog(BasicDAO.class);

	// Method

	/**
	 * 保存对象
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void save(Object obj) throws Exception {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(obj);
			tx.commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("save failed", re);
			throw re;
		} finally {
			session.flush();
			session.close();
		}
	}

	/**
	 * 更新对象
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void update(Object obj) throws Exception {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.update(obj);
			tx.commit();
			log.debug("update successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("update failed", re);
			throw re;
		} finally {
			session.flush();
			session.close();
		}
	}

	/**
	 * 删除对象
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void delete(Object obj) throws Exception {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.delete(obj);
			tx.commit();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("delete failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	/**
	 * 更新对象
	 * 
	 * @throws Exception
	 * @throws BException
	 */
	public static void saveOrUpdate(Object obj) throws Exception {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
			tx.commit();
		} catch (RuntimeException re) {
			tx.rollback();
			throw re;
		} finally {
			session.close();
		}
	}

	/**
	 * 根据hql语句查找对象
	 * 
	 * @param hql
	 * @return
	 * @throws Exception
	 */
	public static List queryByHql(String hql) throws Exception {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query q = session.createQuery(hql);
			List temp = q.list();
			tx.commit();
			return temp;
		} catch (RuntimeException re) {
			log.error("querylist failed", re);
			throw re;
		} finally {
			session.close();
		}

	}

	/**
	 * 按id号查找对象
	 * 
	 * @param obj
	 *            对象的实体
	 * @param id
	 *            对象的主键值
	 * @return
	 * @throws Exception
	 */
	public static Object queryById(Class classInfo, String id) throws Exception {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Object object = session.get(classInfo, id);
			tx.commit();
			return object;
		} catch (RuntimeException re) {
			tx.rollback();
			throw re;
		} finally {
			session.close();
		}

	}

	/**
	 * 通过SQL获得对象列表
	 * 
	 * @param SQL
	 *            用来查询的SQL语句
	 * @return 返回符合条件的对象列表
	 * @throws Exception
	 */
	public static List getListBySQL(Class classInfo, String SQL)
			throws Exception {
		Session session = HibernateSessionFactory.getSession();
		SQLQuery query = session.createSQLQuery(SQL);
		query.addEntity(classInfo);
		List list;
		try {
			list = query.list();
			return list;
		} catch (RuntimeException re) {
			throw re;
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * 用于测试查找方法
	 */
	public static boolean testqueryById(String id) throws Exception {
		boolean flag = false;

		// 测试方法
		try {
			// TbParserBbs tpb = BasicDAO.queryById(TbParserBbs, id);
			// System.out.println("Query TbParserBbsObj Successfully!");
			flag = true;
		} catch (Exception e) {
			System.out.println("queryTbParserBbsById error!");
			e.printStackTrace();
		}

		return flag;
	}

	public static void main(String[] args) {

		// 测试添加方法
		// try {
		// TbParserBbsDeal.testAddTbParserBbs();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// 测试查找方法
		try {
			String HQL = "from TbParserBbs";
			List dataList = BasicDAO.queryByHql(HQL);
			if (dataList.size() > 0) {

				TbParserBbs tb = (TbParserBbs) dataList.get(5);

				System.out.println(tb.getId());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
