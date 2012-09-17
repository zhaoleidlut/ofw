package com.htong.domain;

import java.text.DecimalFormat;
import java.util.Date;

import com.htong.util.DecimalFormatUtil;

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
		return DecimalFormatUtil.floatToFloat(ua, 2);
	}
	public void setUa(float ua) {
		this.ua = ua;
	}
	public float getUb() {
		return DecimalFormatUtil.floatToFloat(ub, 2);
	}
	public void setUb(float ub) {
		this.ub = ub;
	}
	public float getUc() {
		return DecimalFormatUtil.floatToFloat(uc, 2);
	}
	public void setUc(float uc) {
		this.uc = uc;
	}
	public float getIa() {
		return DecimalFormatUtil.floatToFloat(ia, 2);
	}
	public void setIa(float ia) {
		this.ia = ia;
	}
	public float getIb() {
		return DecimalFormatUtil.floatToFloat(ib, 2);
	}
	public void setIb(float ib) {
		this.ib = ib;
	}
	public float getIc() {
		return DecimalFormatUtil.floatToFloat(ic, 2);
	}
	public void setIc(float ic) {
		this.ic = ic;
	}
	public float getShygglZ() {
		return DecimalFormatUtil.floatToFloat(shygglZ, 2);
	}
	public void setShygglZ(float shygglZ) {
		this.shygglZ = shygglZ;
	}
	public float getShygglA() {
		return DecimalFormatUtil.floatToFloat(shygglA, 2);
	}
	public void setShygglA(float shygglA) {
		this.shygglA = shygglA;
	}
	public float getShygglB() {
		return DecimalFormatUtil.floatToFloat(shygglB, 2);
	}
	public void setShygglB(float shygglB) {
		this.shygglB = shygglB;
	}
	public float getShygglC() {
		return DecimalFormatUtil.floatToFloat(shygglC, 2);
	}
	public void setShygglC(float shygglC) {
		this.shygglC = shygglC;
	}
	public float getShwgglZ() {
		return DecimalFormatUtil.floatToFloat(shwgglZ, 2);
	}
	public void setShwgglZ(float shwgglZ) {
		this.shwgglZ = shwgglZ;
	}
	public float getShwgglA() {
		return DecimalFormatUtil.floatToFloat(shwgglA, 2);
	}
	public void setShwgglA(float shwgglA) {
		this.shwgglA = shwgglA;
	}
	public float getShwgglB() {
		return DecimalFormatUtil.floatToFloat(shwgglB, 2);
	}
	public void setShwgglB(float shwgglB) {
		this.shwgglB = shwgglB;
	}
	public float getShwgglC() {
		return DecimalFormatUtil.floatToFloat(shwgglC, 2);
	}
	public void setShwgglC(float shwgglC) {
		this.shwgglC = shwgglC;
	}
	public float getGlysZ() {
		return DecimalFormatUtil.floatToFloat(glysZ, 2);
	}
	public void setGlysZ(float glysZ) {
		this.glysZ = glysZ;
	}
	public float getGlysA() {
		return DecimalFormatUtil.floatToFloat(glysA, 2);
	}
	public void setGlysA(float glysA) {
		this.glysA = glysA;
	}
	public float getGlysB() {
		return DecimalFormatUtil.floatToFloat(glysB, 2);
	}
	public void setGlysB(float glysB) {
		this.glysB = glysB;
	}
	public float getGlysC() {
		return DecimalFormatUtil.floatToFloat(glysC, 2);
	}
	public void setGlysC(float glysC) {
		this.glysC = glysC;
	}
	public float getPinlv() {
		return DecimalFormatUtil.floatToFloat(pinlv, 2);
	}
	public void setPinlv(float pinlv) {
		this.pinlv = pinlv;
	}
	public float getLxdl() {
		return DecimalFormatUtil.floatToFloat(lxdl, 2);
	}
	public void setLxdl(float lxdl) {
		this.lxdl = lxdl;
	}
	public float getLxdy() {
		return DecimalFormatUtil.floatToFloat(lxdy, 2);
	}
	public void setLxdy(float lxdy) {
		this.lxdy = lxdy;
	}
	public float getDlbphd() {
		return DecimalFormatUtil.floatToFloat(dlbphd, 2);
	}
	public void setDlbphd(float dlbphd) {
		this.dlbphd = dlbphd;
	}
	public float getDybphd() {
		return DecimalFormatUtil.floatToFloat(dybphd, 2);
	}
	public void setDybphd(float dybphd) {
		this.dybphd = dybphd;
	}
	public float getUab() {
		return DecimalFormatUtil.floatToFloat(uab, 2);
	}
	public void setUab(float uab) {
		this.uab = uab;
	}
	public float getUbc() {
		return DecimalFormatUtil.floatToFloat(ubc, 2);
	}
	public void setUbc(float ubc) {
		this.ubc = ubc;
	}
	public float getUca() {
		return DecimalFormatUtil.floatToFloat(uca, 2);
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
