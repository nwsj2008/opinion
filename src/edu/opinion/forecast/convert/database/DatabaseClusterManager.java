package edu.opinion.forecast.convert.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.opinion.common.db.TbCluster;
import edu.opinion.common.db.TbClusterDAO;
import edu.opinion.forecast.convert.ClusterManager;
import edu.opinion.forecast.convert.ClusterObject;
import edu.opinion.forecast.exception.convert.DatabaseAccessException;

/**
 * �����ȵ㻰��,��Ҫ�ǹ����ȵ㻰����б�
 * 
 * @author ch
 * 
 */
public class DatabaseClusterManager extends ClusterManager {
	/**
	 * ���е����е��ȵ㻰�������б�
	 */
	private List<DatabaseClusterObject> clusterList;
	/**
	 * �ȵ㻰���б�ķ���ָ�루�α꣩
	 */
	Iterator<DatabaseClusterObject> iterator;

	/**
	 * ���캯��
	 */
	public DatabaseClusterManager() {
		clusterList = new ArrayList<DatabaseClusterObject>();
		iterator = clusterList.iterator();
	}

	/**
	 * �������е��ȵ㻰��
	 */
	public void loadHotTopicCluster() {
		ConvertDAO dao = new ConvertDAO();
		List<TbCluster> list = null;
		try {
			list = dao.getHotTopicList();
		} catch (DatabaseAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		for (TbCluster cluster : list) {
			addCLuster(new DatabaseClusterObject(cluster));
		}
	}

	/**
	 * ����һ���µķ���(�ȵ㻰��)
	 * 
	 * @param cluster
	 */
	private void addCLuster(DatabaseClusterObject cluster) {
		if (!clusterList.contains(cluster)) {
			clusterList.add(cluster);
		} else {
			clusterList.get(clusterList.indexOf(cluster)).setForecastDate(
					cluster.getForecastDate());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterManager#count()
	 */
	public int count() {
		return clusterList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterManager#begin()
	 */
	public ClusterObject begin() {
		iterator = clusterList.iterator();
		if (!iterator.hasNext()) {
			return null;
		}
		return iterator.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterManager#next()
	 */
	public ClusterObject next() {
		if (iterator == null || !iterator.hasNext()) {
			return null;
		}
		return iterator.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterManager#getClusterObjectByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ClusterObject getClusterObjectByName(String name) {
		DatabaseClusterObject clusterObject = new DatabaseClusterObject();
		clusterObject.setName(name);
		int index = clusterList.indexOf(clusterObject);
		if (index != -1) {
			return clusterList.get(index);
		} else {
			TbClusterDAO dao = new TbClusterDAO();
			List<TbCluster> list = dao.findByName(name);
			if (list != null && list.size() > 0) {
				return new DatabaseClusterObject(list.get(0));
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.opinion.forecast.convert.ClusterManager#getClusterObjectByID(java.lang.String)
	 */
	@Override
	public ClusterObject getClusterObjectByID(String id) {
		TbClusterDAO dao = new TbClusterDAO();
		TbCluster cluster = dao.findById(id);
		if (cluster == null) {
			return null;
		}
		return new DatabaseClusterObject(cluster);
	}

}
