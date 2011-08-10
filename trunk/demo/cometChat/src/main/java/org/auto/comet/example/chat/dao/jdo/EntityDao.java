package org.auto.comet.example.chat.dao.jdo;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.auto.comet.example.chat.dao.IEntityDao;
import org.springframework.orm.jdo.JdoTemplate;
import org.springframework.stereotype.Repository;

/**
 * EntityDao
 * <p>
 * CreateTime: 2010-6-2
 * </p>
 *
 * @author XiaohangHu
 */
@SuppressWarnings("unchecked")
@Repository("entityDaoJDO")
public abstract class EntityDao<E, ID extends Serializable> implements
		IEntityDao<E, ID> {

	protected JdoTemplate jdoTemplate;

	private Class<E> entityClass;

	private String entityName;

	{
		ParameterizedType superClassType = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.entityClass = (Class<E>) superClassType.getActualTypeArguments()[0];
		this.entityName = this.entityClass.getName();
	}

	public void setJdoTemplate(JdoTemplate jdoTemplate) {
		this.jdoTemplate = jdoTemplate;
	}

	protected String getEntityName() {
		return this.entityName;
	}

	protected Class<E> getEntityClass() {
		return this.entityClass;
	}

	public void delete(E entity) {
		this.jdoTemplate.deletePersistent(entity);
	}

	public E get(ID id) {
		return (E) this.jdoTemplate.getObjectById(this.getEntityClass(), id);
	}

	public Collection<E> getAll() {
		return this.jdoTemplate.find(this.getEntityClass());
	}

	public E save(E entity) {
		this.jdoTemplate.makePersistent(entity);
		return null;
	}

}
