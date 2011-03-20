package org.auto.comet;

import java.io.Serializable;

import org.auto.json.JsonObject;

/**
 *
 * @author XiaohangHu
 */
public class JsonProtocolUtils {

	public static String getCloseCommend() {
		JsonObject commend = new JsonObject();
		commend.put(Protocol.SYNCHRONIZE_KEY, Protocol.DISCONNECT_VALUE);
		return commend.toString();
	}

	public static String getConnectionCommend(Serializable socketId) {
		JsonObject commend = new JsonObject();
		commend.put(Protocol.CONNECTIONID_KEY, socketId);
		return commend.toString();
	}

	// suppress default constructor for noninstantiability
	private JsonProtocolUtils() {
		throw new AssertionError();
	}

}
