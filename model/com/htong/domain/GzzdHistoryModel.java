package com.htong.domain;

public class GzzdHistoryModel {
	private String wellNum;		//井号
	private String dtuNum;		//DTU号
	private String faultCode;	//故障代码
	private String faultLevel;	//故障程度
	private String actionInfo;	//动作信息
	private String actionUser;	//动作者
	private String actionTime;	//动作时间
	
	
	public String getWellNum() {
		return wellNum;
	}
	public void setWellNum(String wellNum) {
		this.wellNum = wellNum;
	}
	public String getDtuNum() {
		return dtuNum;
	}
	public void setDtuNum(String dtuNum) {
		this.dtuNum = dtuNum;
	}
	public String getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	public String getFaultLevel() {
		return faultLevel;
	}
	public void setFaultLevel(String faultLevel) {
		this.faultLevel = faultLevel;
	}
	public String getActionInfo() {
		return actionInfo;
	}
	public void setActionInfo(String actionInfo) {
		this.actionInfo = actionInfo;
	}
	public String getActionUser() {
		return actionUser;
	}
	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	
	

}
