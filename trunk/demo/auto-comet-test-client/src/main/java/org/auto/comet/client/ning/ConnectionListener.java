package org.auto.comet.client.ning;

/**
 * @author xiaohanghu
 */
public interface ConnectionListener {

	/**
	 * 连接成功
	 * */
	void onConnectioned(ConnectionEvent connectionEvent);

	/**
	 * 连接失败
	 * */
	void onConnectionFailed(ConnectionEvent connectionEvent);

}
