package edu.opinion.common.db;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class TbAuRef.
 * 
 * @see edu.opinion.common.db.TbAuRef
 * @author MyEclipse Persistence Tools
 */

public class TbAuRefDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(TbAuRefDAO.class);

	public void save(TbAuRef transientInstance) {
		log.debug("saving TbAuRef instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TbAuRef persistentInstance) {
		log.debug("deleting TbAuRef instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TbAuRef findById(java.lang.String id) {
		log.debug("getting TbAuRef instance with id: " + id);
		try {
			TbAuRef instance = (TbAuRef) getSession().get(
					"edu.opinion.common.db.TbAuRef", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TbAuRef instance) {
		log.debug("finding TbAuRef instance by example");
		try {
			List results = getSession().createCriteria(
					"edu.opinion.common.db.TbAuRef").add(
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
		log.debug("finding TbAuRef instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TbAuRef as model where model."
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
		log.debug("finding all TbAuRef instances");
		try {
			String queryString = "from TbAuRef";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TbAuRef merge(TbAuRef detachedInstance) {
		log.debug("merging TbAuRef instance");
		try {
			TbAuRef result = (TbAuRef) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TbAuRef instance) {
		log.debug("attaching dirty TbAuRef instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TbAuRef instance) {
		log.debug("attaching clean TbAuRef instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}