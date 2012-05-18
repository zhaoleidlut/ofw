package com.htong.util;

import org.apache.log4j.Logger;

import com.htong.domain.IndexNodeModel;

public class WellUtil {
	private static final Logger log = Logger.getLogger(WellUtil.class);
	/**
	 * 获得路径
	 * @return
	 */
	public static String getPath(IndexNodeModel indexNodeModel) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(indexNodeModel.getName());
		
		while(indexNodeModel.getParentObject() instanceof IndexNodeModel) {
			IndexNodeModel parent = (IndexNodeModel)indexNodeModel.getParentObject();
			strBuilder.insert(0, parent.getName() + "/");
			
			indexNodeModel = (IndexNodeModel)indexNodeModel.getParentObject();
			
		}
		//log.debug(strBuilder.toString());
		return strBuilder.toString();
	}

}
