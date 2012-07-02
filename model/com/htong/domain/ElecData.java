package com.htong.domain;

import java.util.Date;

/**
 * 电量数据
 * @author 赵磊
 *
 */
public class ElecData {
	private String well_num; // 井号
	private String name;	//井名字
	
	private float ua;
	private float ub;
	private float uc;
	private float ia;
	private float ib;
	private float ic;
	
	private float shygglZ;	//瞬时有功功率总
	private float shygglA;
	private float shygglB;
	private float shygglC;
	
	private float shwgglZ;	//瞬时无功功率总
	private float shwgglA;
	private float shwgglB;
	private float shwgglC;
	
	private float glysZ;	//功率因素总
	private float glysA;
	private float glysB;
	private float glysC;
	
	private float pinlv;	//频率
	private float lxdl;		//零序电流
	private float lxdy;		//零序电压
	private float dlbphd;	//电流不平衡度
	private float dybphd;	//电压不平衡度
	
	private float uab;	//Uab
	private float ubc;
	private float uca;
	private Date save_time;
	
	private String time;	//非数据库字段
	
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Date getSave_time() {
		return save_time;
	}
	public void setSave_time(Date save_time) {
		this.save_time = save_time;
	}
	public float getUa() {
		return ua;
	}
	public void setUa(float ua) {
		this.ua = ua;
	}
	public float getUb() {
		return ub;
	}
	public void setUb(float ub) {
		this.ub = ub;
	}
	public float getUc() {
		return uc;
	}
	public void setUc(float uc) {
		this.uc = uc;
	}
	public float getIa() {
		return ia;
	}
	public void setIa(float ia) {
		this.ia = ia;
	}
	public float getIb() {
		return ib;
	}
	public void setIb(float ib) {
		this.ib = ib;
	}
	public float getIc() {
		return ic;
	}
	public void setIc(float ic) {
		this.ic = ic;
	}
	public float getShygglZ() {
		return shygglZ;
	}
	public void setShygglZ(float shygglZ) {
		this.shygglZ = shygglZ;
	}
	public float getShygglA() {
		return shygglA;
	}
	public void setShygglA(float shygglA) {
		this.shygglA = shygglA;
	}
	public float getShygglB() {
		return shygglB;
	}
	public void setShygglB(float shygglB) {
		this.shygglB = shygglB;
	}
	public float getShygglC() {
		return shygglC;
	}
	public void setShygglC(float shygglC) {
		this.shygglC = shygglC;
	}
	public float getShwgglZ() {
		return shwgglZ;
	}
	public void setShwgglZ(float shwgglZ) {
		this.shwgglZ = shwgglZ;
	}
	public float getShwgglA() {
		return shwgglA;
	}
	public void setShwgglA(float shwgglA) {
		this.shwgglA = shwgglA;
	}
	public float getShwgglB() {
		return shwgglB;
	}
	public void setShwgglB(float shwgglB) {
		this.shwgglB = shwgglB;
	}
	public float getShwgglC() {
		return shwgglC;
	}
	public void setShwgglC(float shwgglC) {
		this.shwgglC = shwgglC;
	}
	public float getGlysZ() {
		return glysZ;
	}
	public void setGlysZ(float glysZ) {
		this.glysZ = glysZ;
	}
	public float getGlysA() {
		return glysA;
	}
	public void setGlysA(float glysA) {
		this.glysA = glysA;
	}
	public float getGlysB() {
		return glysB;
	}
	public void setGlysB(float glysB) {
		this.glysB = glysB;
	}
	public float getGlysC() {
		return glysC;
	}
	public void setGlysC(float glysC) {
		this.glysC = glysC;
	}
	public float getPinlv() {
		return pinlv;
	}
	public void setPinlv(float pinlv) {
		this.pinlv = pinlv;
	}
	public float getLxdl() {
		return lxdl;
	}
	public void setLxdl(float lxdl) {
		this.lxdl = lxdl;
	}
	public float getLxdy() {
		return lxdy;
	}
	public void setLxdy(float lxdy) {
		this.lxdy = lxdy;
	}
	public float getDlbphd() {
		return dlbphd;
	}
	public void setDlbphd(float dlbphd) {
		this.dlbphd = dlbphd;
	}
	public float getDybphd() {
		return dybphd;
	}
	public void setDybphd(float dybphd) {
		this.dybphd = dybphd;
	}
	public float getUab() {
		return uab;
	}
	public void setUab(float uab) {
		this.uab = uab;
	}
	public float getUbc() {
		return ubc;
	}
	public void setUbc(float ubc) {
		this.ubc = ubc;
	}
	public float getUca() {
		return uca;
	}
	public void setUca(float uca) {
		this.uca = uca;
	}
	public String getWell_num() {
		return well_num;
	}
	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
