package org.auto.io.processor;

/**
 * @author huxh
 * */
public interface Processor<T> {

	/**
	 * 对数据处理
	 * */
	void process(T t);
}
