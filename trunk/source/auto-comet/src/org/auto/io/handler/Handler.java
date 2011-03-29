package org.auto.io.handler;

/**
 * @author huxh
 * */
public interface Handler<T> {

	/**
	 * 对数据处理
	 * */
	void handle(T t);
}
