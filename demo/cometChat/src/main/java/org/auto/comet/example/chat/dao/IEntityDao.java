package org.auto.comet.example.chat.dao;

import java.util.Collection;

/**
 * IEntityDao 通用的DAO，包含基本的CRUD操作
 * <p>
 * CreateTime: 2010-6-2
 * </p>
 *
 * @author XiaohangHu
 */
public interface IEntityDao<E, ID> {
	/**
	 *
	 * @param id
	 * */
	E get(ID id);

	/**
	 * @param e实体
	 * */
	E save(E entity);

	/**
	 *
	 * @param id
	 * */
	void delete(E entity);

	/**
	 *
	 * */
	Collection<E> getAll();
}
