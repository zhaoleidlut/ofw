package com.htong.alg;

import java.util.Map;
/**
 * 功图量油算法
 * @author 赵磊
 *
 */
public interface SGTDataComputer {
	/**
	 * 
	 * @param weiyi					位移
	 * @param zaihe					载荷
	 * @param chongci				冲次(累加量)，为0则返回1小时
	 * @param chongchengshijian		冲程时间(ms)
	 * @param bengjing				泵径(mm)
	 * @param oilDensity			油密度
	 * @param hanshuiliang			含水量(%)
	 * @return {liquidProduct(产液量):Float,oilProduct(产油量):Float,chongcheng(冲程):Float,minZaihe(最小载荷):Float，maxZaihe(最大载荷)，gongtuxinxi(功图信息)：String,youxiaochongcheng(有效冲程):Float}
	 */
	public Map<String, Object> calcSGTData(float weiyi[], float zaihe[],
			int chongci, float chongchengshijian,float bengjing, float oilDensity, float hanshuiliang);
}
