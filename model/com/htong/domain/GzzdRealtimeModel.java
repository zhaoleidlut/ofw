package com.htong.domain;

public class GzzdRealtimeModel {
	private String wellNum;
	private String dtuNum;
	private String faultCode;
	private String faultLevel;
	private Boolean faultFlag;	//报警状态标志
	private Boolean hasConfirm;	//确认标志
	private String gzzdTime;	//诊断时间
	private String deviceTime;	//设备时间
	
	private String faultCodeValue;	//显示的值
	private String faultFlagValue;
	private String hasConfirmValue;
	
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
	public String getGzzdTime() {
		return gzzdTime;
	}
	public void setGzzdTime(String gzzdTime) {
		this.gzzdTime = gzzdTime;
	}
	public String getDeviceTime() {
		return deviceTime;
	}
	public void setDeviceTime(String deviceTime) {
		this.deviceTime = deviceTime;
	}
	public Boolean getFaultFlag() {
		return faultFlag;
	}
	public void setFaultFlag(Boolean faultFlag) {
		this.faultFlag = faultFlag;
	}
	public Boolean getHasConfirm() {
		return hasConfirm;
	}
	public void setHasConfirm(Boolean hasConfirm) {
		this.hasConfirm = hasConfirm;
	}
	public String getFaultCodeValue() {
		return faultCodeValue;
	}
	public void setFaultCodeValue(String faultCodeValue) {
		this.faultCodeValue = faultCodeValue;
	}
	public String getFaultFlagValue() {
		return faultFlagValue;
	}
	public void setFaultFlagValue(String faultFlagValue) {
		this.faultFlagValue = faultFlagValue;
	}
	public String getHasConfirmValue() {
		return hasConfirmValue;
	}
	public void setHasConfirmValue(String hasConfirmValue) {
		this.hasConfirmValue = hasConfirmValue;
	}
	
}
