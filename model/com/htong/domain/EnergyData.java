package com.htong.domain;

import java.util.Date;

import com.htong.util.DecimalFormatUtil;

/**
 * 电能数据
 * @author 赵磊
 *
 */
public class EnergyData {
	private String well_num; // 井号
	private String name;	//井名字
	
	private float zxygZ;	//正向有功总
	private float zxygJ;	//正向有功尖
	private float zxygF;
	private float zxygP;
	private float zxygG;
	
	private float fxygZ;	//反向有功总
	private float fxygJ;	//反向有功尖
	private float fxygF;
	private float fxygP;
	private float fxygG;
	
	private float zxwgZ;	//正向无功总
	private float zxwgJ;	//正向无功尖
	private float zxwgF;
	private float zxwgP;
	private float zxwgG;
	
	private float fxwgZ;	//反向无功总
	private float fxwgJ;	//反向无功尖
	private float fxwgF;
	private float fxwgP;
	private float fxwgG;
	
	private float syZxygZ;	//上月正向有功总
	private float syFxygZ;
	private float syZxwgZ;
	private float syFxwgZ;
	
	private Date save_time;
	
	private String time;//非数据库字段
	
	
	
	public Date getSave_time() {
		return save_time;
	}
	public void setSave_time(Date save_time) {
		this.save_time = save_time;
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
	public float getZxygZ() {
		return DecimalFormatUtil.floatToFloat(zxygZ, 2);
	}
	public void setZxygZ(float zxygZ) {
		this.zxygZ = zxygZ;
	}
	public float getZxygJ() {
		return DecimalFormatUtil.floatToFloat(zxygJ, 2);
	}
	public void setZxygJ(float zxygJ) {
		this.zxygJ = zxygJ;
	}
	public float getZxygF() {
		return DecimalFormatUtil.floatToFloat(zxygF, 2);
	}
	public void setZxygF(float zxygF) {
		this.zxygF = zxygF;
	}
	public float getZxygP() {
		return DecimalFormatUtil.floatToFloat(zxygP, 2);
	}
	public void setZxygP(float zxygP) {
		this.zxygP = zxygP;
	}
	public float getZxygG() {
		return DecimalFormatUtil.floatToFloat(zxygG, 2);
	}
	public void setZxygG(float zxygG) {
		this.zxygG = zxygG;
	}
	public float getFxygZ() {
		return DecimalFormatUtil.floatToFloat(fxygZ, 2);
	}
	public void setFxygZ(float fxygZ) {
		this.fxygZ = fxygZ;
	}
	public float getFxygJ() {
		return DecimalFormatUtil.floatToFloat(fxygJ, 2);
	}
	public void setFxygJ(float fxygJ) {
		this.fxygJ = fxygJ;
	}
	public float getFxygF() {
		return DecimalFormatUtil.floatToFloat(fxygF, 2);
	}
	public void setFxygF(float fxygF) {
		this.fxygF = fxygF;
	}
	public float getFxygP() {
		return DecimalFormatUtil.floatToFloat(fxygP, 2);
	}
	public void setFxygP(float fxygP) {
		this.fxygP = fxygP;
	}
	public float getFxygG() {
		return DecimalFormatUtil.floatToFloat(fxygG, 2);
	}
	public void setFxygG(float fxygG) {
		this.fxygG = fxygG;
	}
	public float getZxwgZ() {
		return DecimalFormatUtil.floatToFloat(zxwgZ, 2);
	}
	public void setZxwgZ(float zxwgZ) {
		this.zxwgZ = zxwgZ;
	}
	public float getZxwgJ() {
		return DecimalFormatUtil.floatToFloat(zxwgJ, 2);
	}
	public void setZxwgJ(float zxwgJ) {
		this.zxwgJ = zxwgJ;
	}
	public float getZxwgF() {
		return DecimalFormatUtil.floatToFloat(zxwgF, 2);
	}
	public void setZxwgF(float zxwgF) {
		this.zxwgF = zxwgF;
	}
	public float getZxwgP() {
		return DecimalFormatUtil.floatToFloat(zxwgP, 2);
	}
	public void setZxwgP(float zxwgP) {
		this.zxwgP = zxwgP;
	}
	public float getZxwgG() {
		return DecimalFormatUtil.floatToFloat(zxwgG, 2);
	}
	public void setZxwgG(float zxwgG) {
		this.zxwgG = zxwgG;
	}
	public float getFxwgZ() {
		return DecimalFormatUtil.floatToFloat(fxwgZ, 2);
	}
	public void setFxwgZ(float fxwgZ) {
		this.fxwgZ = fxwgZ;
	}
	public float getFxwgJ() {
		return DecimalFormatUtil.floatToFloat(fxwgJ, 2);
	}
	public void setFxwgJ(float fxwgJ) {
		this.fxwgJ = fxwgJ;
	}
	public float getFxwgF() {
		return DecimalFormatUtil.floatToFloat(fxwgF, 2);
	}
	public void setFxwgF(float fxwgF) {
		this.fxwgF = fxwgF;
	}
	public float getFxwgP() {
		return DecimalFormatUtil.floatToFloat(fxwgP, 2);
	}
	public void setFxwgP(float fxwgP) {
		this.fxwgP = fxwgP;
	}
	public float getFxwgG() {
		return DecimalFormatUtil.floatToFloat(fxwgG, 2);
	}
	public void setFxwgG(float fxwgG) {
		this.fxwgG = fxwgG;
	}
	public float getSyZxygZ() {
		return DecimalFormatUtil.floatToFloat(syZxygZ, 2);
	}
	public void setSyZxygZ(float syZxygZ) {
		this.syZxygZ = syZxygZ;
	}
	public float getSyFxygZ() {
		return DecimalFormatUtil.floatToFloat(syFxygZ, 2);
	}
	public void setSyFxygZ(float syFxygZ) {
		this.syFxygZ = syFxygZ;
	}
	public float getSyZxwgZ() {
		return DecimalFormatUtil.floatToFloat(syZxwgZ, 2);
	}
	public void setSyZxwgZ(float syZxwgZ) {
		this.syZxwgZ = syZxwgZ;
	}
	public float getSyFxwgZ() {
		return DecimalFormatUtil.floatToFloat(syFxwgZ, 2);
	}
	public void setSyFxwgZ(float syFxwgZ) {
		this.syFxwgZ = syFxwgZ;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	

}
