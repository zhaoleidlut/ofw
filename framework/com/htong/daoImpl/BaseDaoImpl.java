package com.htong.daoImpl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


import com.htong.dao.BaseDao;

/**
 * 框架基本接口实现类，实现基本CURD功能
 * @author 赵磊
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T> {
	
	private Class<T> entityClass;
	
	//构造方法
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void create(T domain) {
	}

	public void delete(T domain) {
	}

	@SuppressWarnings("unchecked")
	public T read(Serializable id) {
		return null;
	}

	public void update(T domain) {
	}

	public void createOrUpdate(T domain) {
	}


}
