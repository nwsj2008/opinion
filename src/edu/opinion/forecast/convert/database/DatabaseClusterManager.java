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
 * 管理热点话题,主要是管理热点话题的列表
 * 
 * @author ch
 * 
 */
public class DatabaseClusterManager extends ClusterManager {
	/**
	 * 现有的所有的热点话题类别的列表
	 */
	private List<DatabaseClusterObject> clusterList;
	/**
	 * 热点话题列表的泛型指针（游标）
	 */
	Iterator<DatabaseClusterObject> iterator;

	/**
	 * 构造函数
	 */
	public DatabaseClusterManager() {
		clusterList = new ArrayList<DatabaseClusterObject>();
		iterator = clusterList.iterator();
	}

	/**
	 * 载入所有的热点话题
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
	 * 增加一个新的分类(热点话题)
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
