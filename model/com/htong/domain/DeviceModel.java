package com.htong.domain;
/**
 * 设备
 * @author 赵磊
 *
 */
public class DeviceModel{
	private String oid;	//设备编号，要求唯一
	private String name;
	
	private String slaveId;
	private String retry;	//重发次数
	private String timeout;	//超时时间
	private String used;
	
	private String channelName;	//通道名
	
	// 离线记数
	public int retryCount;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(String slaveId) {
		this.slaveId = slaveId;
	}

	public String getRetry() {
		return retry;
	}

	public void setRetry(String retry) {
		this.retry = retry;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}



	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}


	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


}
