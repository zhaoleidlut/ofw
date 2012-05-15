package com.htong.dao;

import java.io.Serializable;

/**
 * 框架基本接口，实现基本CURD功能
 * @author 赵磊
 * @param <T>
 */
public interface BaseDao<T> {
	
	/**
	 * 创建、保存
	 * @param domain
	 */
	public void create(T domain);
	
	/**
	 * 读取，获得
	 * @param id
	 * @return
	 */
	public T read(Serializable id);
	
	/**
	 * 更新
	 * @param domain
	 */
	public void update(T domain);
	
	/**
	 *  删除
	 * @param domain
	 */
	public void delete(T domain);
	
	/**
	 * 保存或更新
	 * @param domain
	 */
	public void createOrUpdate(T domain);
	


}
