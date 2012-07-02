package com.htong.domain;

/**
 * 通道
 * @author 赵磊
 *
 */
public class ChannelModel {
	protected String oid;	//通道编号，要求唯一
	protected String name;
	protected String protocal;
	
	protected String tcpPort;
	protected String ip;

	protected String comPort;
	protected String baud; // 波特率
	protected String dataBit; // 数据位
	protected String parity; // 校验位
	protected String stopBit; // 停止位
	
	protected String dtuId;
	protected String heartBeat;

	private String interval;// 间隔， 必须项
	private String offline;// 通讯离线，必须项
	private String loopInterval;// 循环间隔
	
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getOffline() {
		return offline;
	}
	public void setOffline(String offline) {
		this.offline = offline;
	}
	public String getLoopInterval() {
		return loopInterval;
	}
	public void setLoopInterval(String loopInterval) {
		this.loopInterval = loopInterval;
	}

	public String getProtocal() {
		return protocal;
	}
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	public String getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getComPort() {
		return comPort;
	}
	public void setComPort(String comPort) {
		this.comPort = comPort;
	}
	public String getBaud() {
		return baud;
	}
	public void setBaud(String baud) {
		this.baud = baud;
	}
	public String getDataBit() {
		return dataBit;
	}
	public void setDataBit(String dataBit) {
		this.dataBit = dataBit;
	}
	public String getParity() {
		return parity;
	}
	public void setParity(String parity) {
		this.parity = parity;
	}
	public String getStopBit() {
		return stopBit;
	}
	public void setStopBit(String stopBit) {
		this.stopBit = stopBit;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

}