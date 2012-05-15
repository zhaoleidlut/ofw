package com.htong.domain;


/**
 * 用户权限
 * @author 赵磊
 */
public class UserLevel {
	private Integer oid;
	private String name;
	
	private Boolean query;	//查询的权限



	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getQuery() {
		return query;
	}

	public void setQuery(Boolean query) {
		this.query = query;
	}
	
	

	
}
