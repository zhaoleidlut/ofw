package com.htong.domain;

import java.util.Date;

import org.apache.log4j.Logger;


/**
 * 油井数据
 * 
 * @author 赵磊
 * 
 */
public class WellData {
	
	private static final Logger log = Logger.getLogger(WellData.class);
	
	private String well_num; // 井号
	private String name;	//井名字
	private float[] zaihe; // 载荷
	private float[] weiyi; // 位移
	private float chong_cheng_time; // 冲程时间
	private int chong_ci; // 冲次
	private float[] ib; // 电流
	private float[] power; // 功率
	private float[] power_factor; // 功率因数
	private float[] dgt;	//电功图
	private float yjmk; // 油井模块电压
	private float gtmk; // 功图模块电压
	private Date stop_time; // 上次停井时间
	private Date start_time; // 上次开井时间
	
	private Date device_time;	//设备时间
	private Date save_time;	//存储时间

	private float liquidProduct; // 产液量

	public String getWell_num() {
		return well_num;
	}

	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}

	public float[] getZaihe() {
		return zaihe;
	}

	public void setZaihe(float[] zaihe) {
		this.zaihe = zaihe;
	}

	public float[] getWeiyi() {
		return weiyi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWeiyi(float[] weiyi) {
		this.weiyi = weiyi;
	}

	public float getChong_cheng_time() {
		return chong_cheng_time;
	}

	public void setChong_cheng_time(float chong_cheng_time) {
		this.chong_cheng_time = chong_cheng_time;
	}

	public int getChong_ci() {
		return chong_ci;
	}

	public void setChong_ci(int chong_ci) {
		this.chong_ci = chong_ci;
	}

	public float[] getIb() {
		return ib;
	}

	public void setIb(float[] ib) {
		this.ib = ib;
	}

	public float[] getPower() {
		return power;
	}

	public void setPower(float[] power) {
		this.power = power;
	}

	public float[] getPower_factor() {
		return power_factor;
	}

	public void setPower_factor(float[] power_factor) {
		this.power_factor = power_factor;
	}

	public float getYjmk() {
		return yjmk;
	}

	public void setYjmk(float yjmk) {
		this.yjmk = yjmk;
	}

	public float getGtmk() {
		return gtmk;
	}

	public void setGtmk(float gtmk) {
		this.gtmk = gtmk;
	}

	public Date getStop_time() {
		return stop_time;
	}

	public void setStop_time(Date stop_time) {
		this.stop_time = stop_time;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public float getLiquidProduct() {
		return liquidProduct;
	}

	public void setLiquidProduct(float liquidProduct) {
		this.liquidProduct = liquidProduct;
	}

	public Date getDevice_time() {
		return device_time;
	}

	public void setDevice_time(Date device_time) {
		this.device_time = device_time;
	}

	public Date getSave_time() {
		return save_time;
	}

	public void setSave_time(Date save_time) {
		this.save_time = save_time;
	}

	public float[] getDgt() {
		return dgt;
	}

	public void setDgt(float[] dgt) {
		this.dgt = dgt;
	}

}