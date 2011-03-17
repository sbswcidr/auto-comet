package org.auto.comet;

/**
 * 协议相关常量
 *
 * @author XiaohangHu
 * */
public interface Protocol {

	/** 请求参数名：链接ID */
	String CONNECTIONID_KEY = "_C_COMET";
	/** 请求参数名：同步 */
	String SYNCHRONIZE_KEY = "_S_COMET";

	/** 同步值：创建链接 */
	String CONNECTION_VALUE = "C";
	/** 同步值：断开链接 */
	String DISCONNECT_VALUE = "D";

}
