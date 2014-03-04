/**
 * 
 */
package edu.opinion.forecast.convert;

import edu.opinion.forecast.convert.database.DatabaseClusterManager;

/**
 * ���������
 * 
 * @author ch
 * 
 */
public abstract class ClusterManager {

	/**
	 * ����һ���µ�DatabaseClusterManager����
	 * 
	 * @return �´�����ClusterManager����
	 */
	public static ClusterManager createDatabaseClusterManager() {
		return new DatabaseClusterManager();
	}

	/**
	 * �������з���
	 */
	public abstract void loadHotTopicCluster();

	/**
	 * ȡ�÷���ĸ���
	 * 
	 * @return �������
	 */
	public abstract int count();

	/**
	 * ���α�ָ���һ������
	 * 
	 * @return ��һ������,���û�У�����null
	 */
	public abstract ClusterObject begin();

	/**
	 * ������һ������
	 * 
	 * @return ��һ�����࣬��������ڣ�����null
	 */
	public abstract ClusterObject next();

	/**
	 * ���ݷ��������ȡ�÷���
	 * 
	 * @param name
	 *            ���������
	 * @return ȡ�õķ��࣬��������ڣ�����null
	 */
	public abstract ClusterObject getClusterObjectByName(String name);

	/**
	 * ���ݷ����idȡ�÷���
	 * 
	 * @param id
	 *            ����id
	 * @return ȡ�õķ��࣬��������ڣ�����null
	 */
	public abstract ClusterObject getClusterObjectByID(String id);

}
