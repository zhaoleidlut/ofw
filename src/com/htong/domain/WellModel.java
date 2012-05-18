package com.htong.domain;


/**
 * 油井
 * @author 赵磊
 *
 */
public class WellModel {
	
	private String name;	//井名字
	private String num;		//井号，要求唯一，存数据库标志
	private String path;	//标签索引路径

	private String oilDensity;//油密度
	private String chouyoujiXinghao;	//抽油机型号
	private String bengjing;	//泵径
	private String bengshen;	//泵深
	private String hanshui;		//含水
	private String lilunchongcheng;	//理论冲程
	
//	private String dtuId;	//DTU id
//	private String port;	//端口
//	private String slaveId;	//表地址
//	private String heartBeat;	//心跳信号
	
	private String loss;	//补偿量
	
	private String deviceOid;	//设备id
	
	private String dtuId;
	private String heartBeat;
	private String slaveId;
	private String port;

	public String getOilDensity() {
		return oilDensity;
	}

	public void setOilDensity(String oilDensity) {
		this.oilDensity = oilDensity;
	}

	public String getChouyoujiXinghao() {
		return chouyoujiXinghao;
	}

	public void setChouyoujiXinghao(String chouyoujiXinghao) {
		this.chouyoujiXinghao = chouyoujiXinghao;
	}

	public String getBengjing() {
		return bengjing;
	}

	public void setBengjing(String bengjing) {
		this.bengjing = bengjing;
	}

	public String getBengshen() {
		return bengshen;
	}

	public void setBengshen(String bengshen) {
		this.bengshen = bengshen;
	}

	public String getHanshui() {
		return hanshui;
	}

	public void setHanshui(String hanshui) {
		this.hanshui = hanshui;
	}

	public String getLilunchongcheng() {
		return lilunchongcheng;
	}

	public void setLilunchongcheng(String lilunchongcheng) {
		this.lilunchongcheng = lilunchongcheng;
	}




	public String getLoss() {
		return loss;
	}

	public void setLoss(String loss) {
		this.loss = loss;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	public String getDtuId() {
		return dtuId;
	}

	public void setDtuId(String dtuId) {
		this.dtuId = dtuId;
	}

	public String getHeartBeat() {
		return heartBeat;
	}

	public void setHeartBeat(String heartBeat) {
		this.heartBeat = heartBeat;
	}

	public String getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(String slaveId) {
		this.slaveId = slaveId;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDeviceOid() {
		return deviceOid;
	}

	public void setDeviceOid(String deviceOid) {
		this.deviceOid = deviceOid;
	}
	
}
