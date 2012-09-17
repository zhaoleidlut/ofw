package com.htong.domain;

public class GzzdRealtimeModel {
	private String wellNum;
	private String dtuNum;
	private Integer faultCode;
	private Integer faultLevel;
	private Boolean faultFlag;
	private String time;
	
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
	public Integer getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(Integer faultCode) {
		this.faultCode = faultCode;
	}
	public Integer getFaultLevel() {
		return faultLevel;
	}
	public void setFaultLevel(Integer faultLevel) {
		this.faultLevel = faultLevel;
	}

	public Boolean getFaultFlag() {
		return faultFlag;
	}
	public void setFaultFlag(Boolean faultFlag) {
		this.faultFlag = faultFlag;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	

}
