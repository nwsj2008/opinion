package edu.opinion.common.db;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class TbSensitiveTopic.
 * 
 * @see edu.opinion.common.db.TbSensitiveTopic
 * @author MyEclipse Persistence Tools
 */

public class TbSensitiveTopicDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(TbSensitiveTopicDAO.class);

	// property constants
	public static final String FREQUENCY = "frequency";

	public void save(TbSensitiveTopic transientInstance) {
		log.debug("saving TbSensitiveTopic instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TbSensitiveTopic persistentInstance) {
		log.debug("deleting TbSensitiveTopic instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TbSensitiveTopic findById(java.lang.String id) {
		log.debug("getting TbSensitiveTopic instance with id: " + id);
		try {
			TbSensitiveTopic instance = (TbSensitiveTopic) getSession().get(
					"edu.opinion.common.db.TbSensitiveTopic", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TbSensitiveTopic instance) {
		log.debug("finding TbSensitiveTopic instance by example");
		try {
			List results = getSession().createCriteria(
					"edu.opinion.common.db.TbSensitiveTopic").add(
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
		log.debug("finding TbSensitiveTopic instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TbSensitiveTopic as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByFrequency(Object frequency) {
		return findByProperty(FREQUENCY, frequency);
	}

	public List findAll() {
		log.debug("finding all TbSensitiveTopic instances");
		try {
			String queryString = "from TbSensitiveTopic";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TbSensitiveTopic merge(TbSensitiveTopic detachedInstance) {
		log.debug("merging TbSensitiveTopic instance");
		try {
			TbSensitiveTopic result = (TbSensitiveTopic) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TbSensitiveTopic instance) {
		log.debug("attaching dirty TbSensitiveTopic instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TbSensitiveTopic instance) {
		log.debug("attaching clean TbSensitiveTopic instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}