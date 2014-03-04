/**
 * 
 */
package edu.opinion.forecast.convert;

import edu.opinion.forecast.convert.database.DatabaseClusterManager;

/**
 * 分类管理器
 * 
 * @author ch
 * 
 */
public abstract class ClusterManager {

	/**
	 * 创建一个新的DatabaseClusterManager对象
	 * 
	 * @return 新创建的ClusterManager对象
	 */
	public static ClusterManager createDatabaseClusterManager() {
		return new DatabaseClusterManager();
	}

	/**
	 * 载入所有分类
	 */
	public abstract void loadHotTopicCluster();

	/**
	 * 取得分类的个数
	 * 
	 * @return 分类个数
	 */
	public abstract int count();

	/**
	 * 将游标指向第一个分类
	 * 
	 * @return 第一个分类,如果没有，返回null
	 */
	public abstract ClusterObject begin();

	/**
	 * 返回下一个分类
	 * 
	 * @return 下一个分类，如果不存在，返回null
	 */
	public abstract ClusterObject next();

	/**
	 * 根据分类的名称取得分类
	 * 
	 * @param name
	 *            分类的名称
	 * @return 取得的分类，如果不存在，返回null
	 */
	public abstract ClusterObject getClusterObjectByName(String name);

	/**
	 * 根据分类的id取得分类
	 * 
	 * @param id
	 *            分类id
	 * @return 取得的分类，如果不存在，返回null
	 */
	public abstract ClusterObject getClusterObjectByID(String id);

}
