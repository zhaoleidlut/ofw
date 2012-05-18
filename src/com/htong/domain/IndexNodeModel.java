package com.htong.domain;

import java.util.List;
/**
 * 节点
 * @author 赵磊
 *
 */
public class IndexNodeModel {
	private String name;
	private List<IndexNodeModel> indexNodes;
	private Object parentObject;	//父级节点

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<IndexNodeModel> getIndexNodes() {
		return indexNodes;
	}
	public void setIndexNodes(List<IndexNodeModel> indexNodes) {
		this.indexNodes = indexNodes;
	}
	public Object getParentObject() {
		return parentObject;
	}
	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}

}
