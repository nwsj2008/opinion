package edu.opinion.common.db;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * Data access object (DAO) for domain model class TbCluster.
 * 
 * @see edu.opinion.common.db.TbCluster
 * @author MyEclipse Persistence Tools
 */

public class TbClusterDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(TbClusterDAO.class);

	// property constants
	public static final String NAME = "name";

	public void save(TbCluster transientInstance) {
		log.debug("saving TbCluster instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TbCluster persistentInstance) {
		log.debug("deleting TbCluster instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TbCluster findById(java.lang.String id) {
		log.debug("getting TbCluster instance with id: " + id);
		try {
			TbCluster instance = (TbCluster) getSession().get(
					"edu.opinion.common.db.TbCluster", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TbCluster instance) {
		log.debug("finding TbCluster instance by example");
		try {
			List results = getSession().createCriteria(
					"edu.opinion.common.db.TbCluster").add(
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
		log.debug("finding TbCluster instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TbCluster as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findAll() {
		log.debug("finding all TbCluster instances");
		try {
			String queryString = "from TbCluster";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TbCluster merge(TbCluster detachedInstance) {
		log.debug("merging TbCluster instance");
		try {
			TbCluster result = (TbCluster) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TbCluster instance) {
		log.debug("attaching dirty TbCluster instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TbCluster instance) {
		log.debug("attaching clean TbCluster instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}