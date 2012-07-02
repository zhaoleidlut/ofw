package com.htong.domain;

import java.util.Date;

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
		return zxygZ;
	}
	public void setZxygZ(float zxygZ) {
		this.zxygZ = zxygZ;
	}
	public float getZxygJ() {
		return zxygJ;
	}
	public void setZxygJ(float zxygJ) {
		this.zxygJ = zxygJ;
	}
	public float getZxygF() {
		return zxygF;
	}
	public void setZxygF(float zxygF) {
		this.zxygF = zxygF;
	}
	public float getZxygP() {
		return zxygP;
	}
	public void setZxygP(float zxygP) {
		this.zxygP = zxygP;
	}
	public float getZxygG() {
		return zxygG;
	}
	public void setZxygG(float zxygG) {
		this.zxygG = zxygG;
	}
	public float getFxygZ() {
		return fxygZ;
	}
	public void setFxygZ(float fxygZ) {
		this.fxygZ = fxygZ;
	}
	public float getFxygJ() {
		return fxygJ;
	}
	public void setFxygJ(float fxygJ) {
		this.fxygJ = fxygJ;
	}
	public float getFxygF() {
		return fxygF;
	}
	public void setFxygF(float fxygF) {
		this.fxygF = fxygF;
	}
	public float getFxygP() {
		return fxygP;
	}
	public void setFxygP(float fxygP) {
		this.fxygP = fxygP;
	}
	public float getFxygG() {
		return fxygG;
	}
	public void setFxygG(float fxygG) {
		this.fxygG = fxygG;
	}
	public float getZxwgZ() {
		return zxwgZ;
	}
	public void setZxwgZ(float zxwgZ) {
		this.zxwgZ = zxwgZ;
	}
	public float getZxwgJ() {
		return zxwgJ;
	}
	public void setZxwgJ(float zxwgJ) {
		this.zxwgJ = zxwgJ;
	}
	public float getZxwgF() {
		return zxwgF;
	}
	public void setZxwgF(float zxwgF) {
		this.zxwgF = zxwgF;
	}
	public float getZxwgP() {
		return zxwgP;
	}
	public void setZxwgP(float zxwgP) {
		this.zxwgP = zxwgP;
	}
	public float getZxwgG() {
		return zxwgG;
	}
	public void setZxwgG(float zxwgG) {
		this.zxwgG = zxwgG;
	}
	public float getFxwgZ() {
		return fxwgZ;
	}
	public void setFxwgZ(float fxwgZ) {
		this.fxwgZ = fxwgZ;
	}
	public float getFxwgJ() {
		return fxwgJ;
	}
	public void setFxwgJ(float fxwgJ) {
		this.fxwgJ = fxwgJ;
	}
	public float getFxwgF() {
		return fxwgF;
	}
	public void setFxwgF(float fxwgF) {
		this.fxwgF = fxwgF;
	}
	public float getFxwgP() {
		return fxwgP;
	}
	public void setFxwgP(float fxwgP) {
		this.fxwgP = fxwgP;
	}
	public float getFxwgG() {
		return fxwgG;
	}
	public void setFxwgG(float fxwgG) {
		this.fxwgG = fxwgG;
	}
	public float getSyZxygZ() {
		return syZxygZ;
	}
	public void setSyZxygZ(float syZxygZ) {
		this.syZxygZ = syZxygZ;
	}
	public float getSyFxygZ() {
		return syFxygZ;
	}
	public void setSyFxygZ(float syFxygZ) {
		this.syFxygZ = syFxygZ;
	}
	public float getSyZxwgZ() {
		return syZxwgZ;
	}
	public void setSyZxwgZ(float syZxwgZ) {
		this.syZxwgZ = syZxwgZ;
	}
	public float getSyFxwgZ() {
		return syFxwgZ;
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
