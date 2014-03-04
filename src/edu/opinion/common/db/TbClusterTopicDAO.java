package edu.opinion.common.db;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class TbClusterTopic.
 * 
 * @see edu.opinion.common.db.TbClusterTopic
 * @author MyEclipse Persistence Tools
 */

public class TbClusterTopicDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(TbClusterTopicDAO.class);

	// property constants

	public void save(TbClusterTopic transientInstance) {
		log.debug("saving TbClusterTopic instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TbClusterTopic persistentInstance) {
		log.debug("deleting TbClusterTopic instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TbClusterTopic findById(java.lang.String id) {
		log.debug("getting TbClusterTopic instance with id: " + id);
		try {
			TbClusterTopic instance = (TbClusterTopic) getSession().get(
					"edu.opinion.common.db.TbClusterTopic", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TbClusterTopic instance) {
		log.debug("finding TbClusterTopic instance by example");
		try {
			List results = getSession().createCriteria(
					"edu.opinion.common.db.TbClusterTopic").add(
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
		log.debug("finding TbClusterTopic instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TbClusterTopic as model where model."
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
		log.debug("finding all TbClusterTopic instances");
		try {
			String queryString = "from TbClusterTopic";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TbClusterTopic merge(TbClusterTopic detachedInstance) {
		log.debug("merging TbClusterTopic instance");
		try {
			TbClusterTopic result = (TbClusterTopic) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TbClusterTopic instance) {
		log.debug("attaching dirty TbClusterTopic instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TbClusterTopic instance) {
		log.debug("attaching clean TbClusterTopic instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}