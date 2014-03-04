package edu.opinion.common.db;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class TbHotTopic.
 * 
 * @see edu.opinion.common.db.TbHotTopic
 * @author MyEclipse Persistence Tools
 */

public class TbHotTopicDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(TbHotTopicDAO.class);

	// property constants

	public void save(TbHotTopic transientInstance) {
		log.debug("saving TbHotTopic instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TbHotTopic persistentInstance) {
		log.debug("deleting TbHotTopic instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TbHotTopic findById(java.lang.String id) {
		log.debug("getting TbHotTopic instance with id: " + id);
		try {
			TbHotTopic instance = (TbHotTopic) getSession().get(
					"edu.opinion.common.db.TbHotTopic", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TbHotTopic instance) {
		log.debug("finding TbHotTopic instance by example");
		try {
			List results = getSession().createCriteria(
					"edu.opinion.common.db.TbHotTopic").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TbHotTopic instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TbHotTopic as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all TbHotTopic instances");
		try {
			String queryString = "from TbHotTopic";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TbHotTopic merge(TbHotTopic detachedInstance) {
		log.debug("merging TbHotTopic instance");
		try {
			TbHotTopic result = (TbHotTopic) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TbHotTopic instance) {
		log.debug("attaching dirty TbHotTopic instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TbHotTopic instance) {
		log.debug("attaching clean TbHotTopic instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}