package com.htong.util.mongo;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.htong.domain.WellData;

/**
 * 常量
 * @author 赵磊
 *
 */
public class CollectionConstants {
	/**
	 * 采集通道表名
	 */
	public static final String CHANNEL_COLLECTION_NAME = "channel";	
	/**
	 * 设备表名
	 */
	public static final String DEVICE_COLLECTION_NAME = "device";
	/**
	 * 标签索引表名
	 */
	public static final String TAG_COLLECTION_NAME = "tag";	
	/**
	 * 井表名
	 */
	public static final String WELL_COLLECTION_NAME = "well";
	
	public static final String WELL_DATA = "WellData";
	
	public static final String USER = "user";
	
	public static final String USER_LEVEL = "userlevel";
	
	public static String getWellDataCollection(String wellNum, Calendar c) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
		String time = sdf.format(c.getTime());
		return CollectionConstants.WELL_DATA + "_" + wellNum + "_"
		+ time;
	}

}
