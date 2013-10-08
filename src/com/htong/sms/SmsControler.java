package com.htong.sms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.Message.MessageEncodings;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

import com.htong.domain.WellModel;
import com.htong.util.FileConstants;


/**
 * 发短信控制器
 * 
 * @author 赵磊
 * 
 */
public enum SmsControler {
	INSTANCE;

	private final Logger log = Logger.getLogger(SmsControler.class);

	private String smsCenter = "13800546500";// 短信中心号码
	private String comName = "COM1";
	
	private boolean isOpen = false;

	private SmsControler() {
//		loadConfig();
//		open();
	}

	private void loadConfig() {
		Properties properties = new Properties();
		try {
			File path = new File(FileConstants.CONFIG_PATH);
			if(!path.exists()) {
				path.mkdir();
			}
			File file = new File(FileConstants.SMS_CONFIG_PATH);
			log.debug(FileConstants.CONFIG_PATH);
			if(!file.exists()) {
				file.createNewFile();
				saveConfig();
				return;
			}
			
			properties.load(new FileInputStream(FileConstants.SMS_CONFIG_PATH));
			this.comName = properties.getProperty("COMName", "COM1");
			this.smsCenter = properties.getProperty("smsCenter", "13800415500");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveConfig() {
		Properties properties = new Properties();
		try {
			properties.setProperty("COMName", this.comName);
			properties.setProperty("smsCenter", this.smsCenter);

			properties.store(
					new FileOutputStream(FileConstants.SMS_CONFIG_PATH), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendSms() throws TimeoutException, SMSLibException, IOException, InterruptedException {
		// TODO: 未实现具体短信报警信息
		StringBuilder sb = new StringBuilder();
		sb.append("SOE事件报警\n");

		String msg = sb.toString();
		
		send("故障诊断报警！", "15042402486");
	}


	/**
	 * 发送短信
	 * 
	 * @param message
	 * @param telephone
	 * @return
	 * @throws GatewayException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws SMSLibException 
	 * @throws TimeoutException 
	 */
	private synchronized boolean send(String message, String telephone) throws TimeoutException, GatewayException, IOException, InterruptedException{
		if (this.isOpen) {
			log.info("正在发送短信至：" + telephone);
			// Send a message synchronously.
			OutboundMessage msg = new OutboundMessage(telephone, message);
			msg.setEncoding(MessageEncodings.ENCUCS2);
			msg.setStatusReport(false);
			Service.getInstance().sendMessage(msg);
			log.info("		>>" + msg);
			log.info("短信发送完毕");
			return true;
		} else {
			log.warn("短信端口未打开。");
			return false;
		}
	}
	
	public synchronized boolean send(WellModel well, String message) {
//		if(this.isOpen) {
//			if(well.getMobileNum() != null && !well.getMobileNum().isEmpty() && well.getMobileNum().length()==11) {
//				String telephone = well.getMobileNum();
//				StringBuilder sb = new StringBuilder();
//				sb.append("故障诊断报警提示：\r\n");
//				sb.append("【" + well.getPath()+"/" +well.getNum() + "】");
//				sb.append(message);
//				String iMessage = sb.toString();
//				log.info("正在发送短信至：" + telephone);
//				log.info("短信内容：" + iMessage);
//				// Send a message synchronously.
//				OutboundMessage msg = new OutboundMessage(telephone, iMessage);
//				msg.setEncoding(MessageEncodings.ENCUCS2);
//				msg.setStatusReport(false);
//				try {
//					Service.getInstance().sendMessage(msg);
//				} catch (TimeoutException e) {
//					e.printStackTrace();
//				} catch (GatewayException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				log.info("		>>" + msg);
//				log.info("短信发送完毕");
//				return true;
//			} else {
//				log.info("手机号码不合法！");
//				return false;
//			}
//			
//		} else {
//			log.warn("短信端口未打开。");
//			return false;
//		}
		return true;
	}

	/**
	 * @throws SMSLibException
	 * @throws TimeoutException
	 * @throws GatewayException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void close() throws SMSLibException, TimeoutException,
			GatewayException, IOException, InterruptedException {
		if (this.isOpen)
			Service.getInstance().stopService();
		this.isOpen = false;
	}

	/**
	 * @throws GatewayException
	 * @throws SMSLibException
	 * @throws TimeoutException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void open() {
		if (this.isOpen) {
			return;
		}
		log.info("正在初始化短信串口");
		
		OutboundNotification outboundNotification = new OutboundNotification();
		log.info(Library.getLibraryDescription());
		log.info("Version: " + Library.getLibraryVersion());
		SerialModemGateway gateway = new SerialModemGateway("modem.com1", this.comName, 9600, "Huawei", "");
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");
		// Explicit SMSC address set is required for some modems.
		// Below is for VODAFONE GREECE - be sure to set your own!
//		gateway.setSmscNumber("+8613800415500");
		gateway.setSmscNumber("+86" + this.smsCenter);
		Service.getInstance().setOutboundMessageNotification(outboundNotification);
		
		try {
			Service.getInstance().addGateway(gateway);
			Service.getInstance().startService(false);
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GatewayException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SMSLibException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		log.debug("===========================");
//		log.debug("Modem Information:");
//		log.debug("  Manufacturer: " + gateway.getManufacturer());
//		log.debug("  Model: " + gateway.getModel());
//		log.debug("  Serial No: " + gateway.getSerialNo());
//		log.debug("  SIM IMSI: " + gateway.getImsi());
//		log.debug("  Signal Level: " + gateway.getSignalLevel() + " dBm");
//		log.debug("  Battery Level: " + gateway.getBatteryLevel() + "%");
//		log.debug("===========================");
		
		this.isOpen = true;
	}

	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(AGateway gateway, OutboundMessage msg)
		{
		}
	}

	public String getSmsCenter() {
		return smsCenter;
	}

	public void setSmsCenter(String smsCenter) {
		this.smsCenter = smsCenter;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}
	
	public static final void main(String args[]) {
		try {
			SmsControler.INSTANCE.sendSms();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SMSLibException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
