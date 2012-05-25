package com.htong.alg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SGTDataComputerProcess implements SGTDataComputer {
	private static final Logger log = Logger.getLogger(SGTDataComputerProcess.class);
	
	private int standardAreaNum = 25;// 面积划分数量
	@Override
	public Map<String, Object> calcSGTData(float weiyi[], float zaihe[],
			int chongci, float chongchengshijian, float bengjing,
			float oilDensity, float hanshuiliang) {
		
		//{liquidProduct(产液量):Float,oilProduct(产油量):Float,chongcheng(冲程):Float,minZaihe(最小载荷):Float，maxZaihe(最大载荷)，gongtuxinxi(功图信息)：String}
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int standardGuaidianF = 5; // 拐点自由度
		
		int num = studyF(weiyi, zaihe, standardGuaidianF);
		int outWhile = 0;	//跳出while 防止死循环
		while((num<6 || num>10) && outWhile <100) {
			if(num<6) {
				standardGuaidianF--;
//				log.debug("拐点数为：" + num + " 增加拐点-减小拐点自由度...为" + standardGuaidianF);
			} else {
				standardGuaidianF++;
//				log.debug("拐点数为：" + num + "减小拐点-增加拐点自由度...为" + standardGuaidianF);
			}
			num = studyF(weiyi, zaihe, standardGuaidianF);
			outWhile++;
		}
//		log.debug("拐点数：" + num);
		if(num<6 || num>10) {
			log.debug("故障井，拐点数为：" + num);
			resultMap.put("gongtuxinxi", "暂时不明故障");	//添加故障信息
		}

		final int n = weiyi.length; // 数据个数

		int maxFlag = 0; // 上死点
		int minFlag = 0; // 下死点
		// 计算上死点与下死点
		for (int i = 1; i < n; i++) {
			// 上死点
			if (weiyi[maxFlag] < weiyi[i]) {
				maxFlag = i;
			} else if (weiyi[maxFlag] == weiyi[i]) {// 位移相等，则选择载荷最大点
				if (zaihe[maxFlag] < zaihe[i]) {
					maxFlag = i;
				}
			}
			// 下死点
			if (weiyi[minFlag] > weiyi[i]) {
				minFlag = i;
			} else if (weiyi[minFlag] == weiyi[i]) {// 位移相等，则选择载荷最小点
				if (zaihe[minFlag] > zaihe[i]) {
					minFlag = i;
				}
			}
		}
		float chongcheng = weiyi[maxFlag] - weiyi[minFlag];
		BigDecimal chongchengBd = new BigDecimal(chongcheng);
		float newChongCheng = chongchengBd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		resultMap.put("chongcheng", newChongCheng);	//添加冲程
		
		float minZaihe = zaihe[0];	//载荷最小值
		float maxZaihe = zaihe[0];	//载荷最大值
		for(int i = 1;i<n;i++) {
			//最小值
			if(minZaihe>zaihe[i]) {
				minZaihe = zaihe[i];
			} 
			if(maxZaihe<zaihe[i]) {
				maxZaihe = zaihe[i];
			}
		}
		BigDecimal minZaiheBd = new BigDecimal(minZaihe);
		float newMinZaihe = minZaiheBd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		
		BigDecimal maxZaiheBd = new BigDecimal(maxZaihe);
		float newMaxZaihe = maxZaiheBd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		
		resultMap.put("minZaihe", newMinZaihe);	//添加最小载荷
		resultMap.put("maxZaihe", newMaxZaihe);	//添加最大载荷
		

//		log.debug("上死点：" + maxFlag + "：" + weiyi[maxFlag] + "，下死点："
//				+ minFlag + "：" + weiyi[minFlag]);

		float minWeiyi = weiyi[minFlag];
		for (int i = 0; i < weiyi.length; i++) {
			weiyi[i] -= minWeiyi;
		}
		// 为方便计算，若最小位移点不是第0点，那么循环移动数据
		if (minFlag != 0) {
			float tempPositon;
			float tempLoad;
			for (int i = 0; i < minFlag; i++) {
				tempPositon = weiyi[0];
				tempLoad = zaihe[0];
				int j = 1;
				for (; j < n; j++) {
					weiyi[j - 1] = weiyi[j];
					zaihe[j - 1] = zaihe[j];
				}
				weiyi[j - 1] = tempPositon;
				zaihe[j - 1] = tempLoad;
			}
			maxFlag = Math.abs(maxFlag - minFlag);
			minFlag = 0;
		}

//		log.debug("移位后的----上死点：" + maxFlag + "-" + weiyi[maxFlag]
//				+ "，下死点：" + minFlag + "-" + weiyi[minFlag]);

		// 将上下行程分别求出每个区间内载荷的平均值
		float space = (weiyi[maxFlag] - weiyi[minFlag]) / standardAreaNum;
		int j = 0;
		float[] averloadup = new float[standardAreaNum]; // 上冲程载荷平均值
		float[] averloaddown = new float[standardAreaNum]; // 下冲程载荷平均值
		for (int i = 0; i < standardAreaNum; i++) {
			int flag = 0;
			while (weiyi[j] < (1 + i) * space && j < maxFlag) {
				averloadup[i] += zaihe[j++];
				flag++;
			}
			averloadup[i] /= flag; // 第i个面积区间的载荷平均值
		}
		for (int i = 0; i < standardAreaNum; i++) {
			int flag = 0;
			while (j < n && weiyi[j] > (standardAreaNum - i - 1) * space) {
				averloaddown[i] += zaihe[j++];
				flag++;
			}
			averloaddown[i] /= flag;
		}

		float sumarea = 0; // 示功图面积
		for (int i = 0; i < standardAreaNum; i++) {
			float iArea = (averloadup[i] - averloaddown[standardAreaNum - i - 1])
					* space;
			sumarea += iArea;
		}

		// 求凸包
		List<Point> pointList = new ArrayList<Point>();
		for (int i = 0; i < n; i++) {
			Point p = new Point();
			p.setX(weiyi[i]);
			p.setY(zaihe[i]);
			pointList.add(p);
		}
		Melkman melkman = new Melkman(pointList);
		Point[] newPoint = melkman.getTubaoPoint(); // 凸包点
//		log.debug("凸包点个数：" + newPoint.length);
//		for (Point p : newPoint) {
//			log.debug("凸包点：" + p.getX() + ", " + p.getY());
//		}

		// 将位移差小于0.015m的点合并，载荷取平均值
		int flagnum = newPoint.length; // 合并之后的凸包点个数
		for (int i = 0; i < flagnum - 1; i++) {
			if (Math.abs(newPoint[i].getX() - newPoint[i + 1].getX()) <= 0.005) {
				newPoint[i].setY((float) ((newPoint[i].getY() + newPoint[i + 1]
						.getY()) / 2.0));
				for (j = i + 1; j < flagnum - 2; j++) {
					newPoint[j].setX(newPoint[j + 1].getX());
					newPoint[j].setY(newPoint[j + 1].getY());
				}
				flagnum--;
				i--;
			}
		}
		if (Math.abs(newPoint[0].getX() - newPoint[flagnum - 1].getX()) <= 0.005) {
			newPoint[0]
					.setY((float) ((newPoint[0].getY() + newPoint[flagnum - 1]
							.getY()) / 2.0));
			flagnum--;
		}

//		log.debug("合并后的凸包点个数：" + flagnum);

//		for (int i = 0; i < flagnum; i++) {
//			log.debug("合并后的凸包点:" + newPoint[i].getX() + ","
//					+ newPoint[i].getY());
//		}

		// 计算斜率
		float[] slopearr = new float[flagnum]; // 斜率
		slopearr[0] = (newPoint[flagnum - 1].getY() - newPoint[0].getY())
				/ (newPoint[flagnum - 1].getX() - newPoint[0].getX());
		for (int i = 1; i < flagnum; i++)
			slopearr[i] = (newPoint[i].getY() - newPoint[i - 1].getY())
					/ (newPoint[i].getX() - newPoint[i - 1].getX());

//		for (float f : slopearr) {
//			log.debug("斜率：" + String.valueOf(f));
//		}

		// 求斜率的差值
		float[] slopearrsub = new float[flagnum]; // 斜率差值
		slopearrsub[0] = Math.abs(slopearr[flagnum - 1] - slopearr[0]);
		for (int i = 1; i < flagnum; i++) {
			slopearrsub[i] = Math.abs(slopearr[i] - slopearr[i - 1]);
//			log.debug("斜率差值：" + String.valueOf(slopearrsub[i]));
		}

		// 求斜率差值的极值点
		int pointfinal[] = new int[15]; // 极值点位置数组
		int pointNum = 0; // 极值点个数
		for (int i = 1; i < flagnum - 1; i++)
			if (slopearrsub[i] - standardGuaidianF > slopearrsub[i - 1]
					&& slopearrsub[i] - standardGuaidianF > slopearrsub[i + 1]) {
				pointfinal[pointNum] = i - 1;
				pointNum++;
			}
		if (slopearrsub[0] - standardGuaidianF > slopearrsub[flagnum - 1]
				&& slopearrsub[0] - standardGuaidianF > slopearrsub[1]) {
			pointfinal[pointNum] = flagnum - 1;
			pointNum++;
		}
		if (slopearrsub[flagnum - 1] - standardGuaidianF > slopearrsub[flagnum - 2]
				&& slopearrsub[flagnum - 1] - standardGuaidianF > slopearrsub[0]) {
			pointfinal[pointNum] = flagnum - 2;
			pointNum++;
		}
//		log.debug("极值点个数：" + pointNum);
//		for(int i = 0;i<pointNum;i++) {
//			log.debug("极值点：" + newPoint[pointfinal[i]].getX() + "," + newPoint[pointfinal[i]].getY());
//		}

//		if (pointNum == 4) {
//			result[1] = newPoint[pointfinal[2]].getX(); // 标准示功图左上位移
//			result[2] = newPoint[pointfinal[0]].getX(); // 标准示功图右下位移
//			result[3] = (newPoint[pointfinal[2]].getY() + newPoint[pointfinal[1]]
//					.getY()) / 2; // 标准上载线位置
//			result[4] = (newPoint[pointfinal[0]].getY() + newPoint[pointfinal[3]]
//					.getY()) / 2; // 标准下载线位置
//		} else {
//			log.debug("非标准示功图");
//		}
		
		float minBasicZaihe = newPoint[pointfinal[0]].getY();//获取极值点最小载荷点
		for(int i = 1;i<pointNum;i++) {
			if(newPoint[pointfinal[i]].getY()<minBasicZaihe) {
				minBasicZaihe = newPoint[pointfinal[i]].getY();
			}
		}
		log.debug("载荷最小值：" + minBasicZaihe);
		//选取不超过8的点
		List<Integer> houxuanList = new ArrayList<Integer>();
		for(int i=0;i<pointNum;i++) {
			if(Math.abs(newPoint[pointfinal[i]].getY() - minBasicZaihe)<=8) {
				houxuanList.add(pointfinal[i]);
			}
		}
		//取最大的作为有效冲程点
		float youxiaochongcheng = newPoint[houxuanList.get(0)].getX();
		for(int i = 1;i<houxuanList.size();i++) {
			if(newPoint[houxuanList.get(i)].getX()>youxiaochongcheng) {
				youxiaochongcheng = newPoint[houxuanList.get(i)].getX();
			}
		}
		log.debug("有效冲程为：" + youxiaochongcheng);
		
		resultMap.put("youxiaochongcheng", youxiaochongcheng);
		
		
		//产液量
		float liquidProduct = 0;
		log.debug("冲程时间：" + chongchengshijian);
		if(chongci == 0) {//冲次为0，用冲程时间来算一个小时产量

			liquidProduct = (float) (Math.pow(bengjing/2000, 2)*3.1415926*3600*youxiaochongcheng/chongchengshijian);
		}else {
			liquidProduct = (float) (Math.pow(bengjing/2000, 2)*3.1415926*chongci*youxiaochongcheng);
		}
//		log.debug("未保留4位小数前的产液量：" + liquidProduct);
		BigDecimal bd = new BigDecimal(liquidProduct);
		float newLiquidProduct = bd.setScale(4, BigDecimal.ROUND_HALF_UP)
				.floatValue();

		//产油量
		float oilProduct = liquidProduct*(100-hanshuiliang)/100;
		BigDecimal oilBd = new BigDecimal(oilProduct);
		float newOilProduct = oilBd.setScale(4, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		
		log.debug("产液量：" + newLiquidProduct);
		resultMap.put("liquidProduct", newLiquidProduct);
		resultMap.put("oilProduct", newOilProduct);
		
		if(chongcheng - youxiaochongcheng >= 2) {
			resultMap.put("gongtuxinxi", "供液不足");
		} else {
			resultMap.put("gongtuxinxi", "正常");
		}

		return resultMap;
	}

	/**
	 * 计算极值点
	 * @param weiyi
	 * @param zaihe
	 * @param standardGuaidianF
	 * @return
	 */
	private int studyF(float[] weiyi, float[] zaihe, int standardGuaidianF) {
		final int n = weiyi.length; // 数据个数

		int maxFlag = 0; // 上死点
		int minFlag = 0; // 下死点
		// 计算上死点与下死点
		for (int i = 1; i < n; i++) {
			// 上死点
			if (weiyi[maxFlag] < weiyi[i]) {
				maxFlag = i;
			} else if (weiyi[maxFlag] == weiyi[i]) {// 位移相等，则选择载荷最大点
				if (zaihe[maxFlag] < zaihe[i]) {
					maxFlag = i;
				}
			}
			// 下死点
			if (weiyi[minFlag] > weiyi[i]) {
				minFlag = i;
			} else if (weiyi[minFlag] == weiyi[i]) {// 位移相等，则选择载荷最小点
				if (zaihe[minFlag] > zaihe[i]) {
					minFlag = i;
				}
			}
		}

		float minWeiyi = weiyi[minFlag];
		for (int i = 0; i < weiyi.length; i++) {
			weiyi[i] -= minWeiyi;
		}
		// 为方便计算，若最小位移点不是第0点，那么循环移动数据
		if (minFlag != 0) {
			float tempPositon;
			float tempLoad;
			for (int i = 0; i < minFlag; i++) {
				tempPositon = weiyi[0];
				tempLoad = zaihe[0];
				int j = 1;
				for (; j < n; j++) {
					weiyi[j - 1] = weiyi[j];
					zaihe[j - 1] = zaihe[j];
				}
				weiyi[j - 1] = tempPositon;
				zaihe[j - 1] = tempLoad;
			}
			maxFlag = Math.abs(maxFlag - minFlag);
			minFlag = 0;
		}
		// 将上下行程分别求出每个区间内载荷的平均值
		float space = (weiyi[maxFlag] - weiyi[minFlag]) / standardAreaNum;
		int j = 0;
		float[] averloadup = new float[standardAreaNum]; // 上冲程载荷平均值
		float[] averloaddown = new float[standardAreaNum]; // 下冲程载荷平均值
		for (int i = 0; i < standardAreaNum; i++) {
			int flag = 0;
			while (weiyi[j] < (1 + i) * space && j < maxFlag) {
				averloadup[i] += zaihe[j++];
				flag++;
			}
			averloadup[i] /= flag; // 第i个面积区间的载荷平均值
		}
		for (int i = 0; i < standardAreaNum; i++) {
			int flag = 0;
			while (j < n && weiyi[j] > (standardAreaNum - i - 1) * space) {
				averloaddown[i] += zaihe[j++];
				flag++;
			}
			averloaddown[i] /= flag;
		}

		float sumarea = 0; // 示功图面积
		for (int i = 0; i < standardAreaNum; i++) {
			float iArea = (averloadup[i] - averloaddown[standardAreaNum - i - 1])
					* space;
			sumarea += iArea;
		}
		
		// 求凸包
		List<Point> pointList = new ArrayList<Point>();
		for (int i = 0; i < n; i++) {
			Point p = new Point();
			p.setX(weiyi[i]);
			p.setY(zaihe[i]);
			pointList.add(p);
		}
		Melkman melkman = new Melkman(pointList);
		Point[] newPoint = melkman.getTubaoPoint(); // 凸包点
		
		// 将位移差小于0.015m的点合并，载荷取平均值
		int flagnum = newPoint.length; // 合并之后的凸包点个数
		for (int i = 0; i < flagnum - 1; i++) {
			if (Math.abs(newPoint[i].getX() - newPoint[i + 1].getX()) <= 0.005) {
				newPoint[i].setY((float) ((newPoint[i].getY() + newPoint[i + 1]
						.getY()) / 2.0));
				for (j = i + 1; j < flagnum - 2; j++) {
					newPoint[j].setX(newPoint[j + 1].getX());
					newPoint[j].setY(newPoint[j + 1].getY());
				}
				flagnum--;
				i--;
			}
		}
		if (Math.abs(newPoint[0].getX() - newPoint[flagnum - 1].getX()) <= 0.005) {
			newPoint[0]
					.setY((float) ((newPoint[0].getY() + newPoint[flagnum - 1]
							.getY()) / 2.0));
			flagnum--;
		}

		// 计算斜率
		float[] slopearr = new float[flagnum]; // 斜率
		slopearr[0] = (newPoint[flagnum - 1].getY() - newPoint[0].getY())
				/ (newPoint[flagnum - 1].getX() - newPoint[0].getX());
		for (int i = 1; i < flagnum; i++)
			slopearr[i] = (newPoint[i].getY() - newPoint[i - 1].getY())
					/ (newPoint[i].getX() - newPoint[i - 1].getX());

		// 求斜率的差值
		float[] slopearrsub = new float[flagnum]; // 斜率差值
		slopearrsub[0] = Math.abs(slopearr[flagnum - 1] - slopearr[0]);
		for (int i = 1; i < flagnum; i++) {
			slopearrsub[i] = Math.abs(slopearr[i] - slopearr[i - 1]);
		}

		// 求斜率差值的极值点
		int pointfinal[] = new int[15]; // 极值点位置数组
		int pointNum = 0; // 极值点个数
		for (int i = 1; i < flagnum - 1; i++)
			if (slopearrsub[i] - standardGuaidianF > slopearrsub[i - 1]
					&& slopearrsub[i] - standardGuaidianF > slopearrsub[i + 1]) {
				pointfinal[pointNum] = i - 1;
				pointNum++;
			}
		if (slopearrsub[0] - standardGuaidianF > slopearrsub[flagnum - 1]
				&& slopearrsub[0] - standardGuaidianF > slopearrsub[1]) {
			pointfinal[pointNum] = flagnum - 1;
			pointNum++;
		}
		if (slopearrsub[flagnum - 1] - standardGuaidianF > slopearrsub[flagnum - 2]
				&& slopearrsub[flagnum - 1] - standardGuaidianF > slopearrsub[0]) {
			pointfinal[pointNum] = flagnum - 2;
			pointNum++;
		}

		return pointNum;
	}

	public static void main(String args[]) {
		File file = new File("G:/2.txt");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<Float> floatList = new ArrayList<Float>();
		String str = null;
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (str != null) {
			floatList.add(Float.parseFloat(str));
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		float data[] = new float[floatList.size()];
		int i = 0;
		for (Float f : floatList) {
			data[i++] = f;
		}

		// 初始化
		int n = data.length / 2;
		log.debug("数据个数：" + n);
		float[] weiyi = new float[n];
		float[] zaihe = new float[n];
		int ii = 0;
		for (float f1 : data) {
			if (ii < n) {
				weiyi[ii++] = f1;
			} else {
				zaihe[ii - n] = f1;
				ii++;
			}
		}
		weiyi[0] = (weiyi[n - 1] + weiyi[n - 2] + weiyi[n - 3]) / 3;
		zaihe[0] = (zaihe[n - 1] + zaihe[n - 2] + zaihe[n - 3]) / 3;

		weiyi[1] = (weiyi[0] + weiyi[n - 1] + weiyi[n - 2]) / 3;
		zaihe[1] = (zaihe[0] + zaihe[n - 1] + zaihe[n - 2]) / 3;

		weiyi[2] = (weiyi[1] + weiyi[0] + weiyi[n - 1]) / 3;
		zaihe[2] = (zaihe[1] + zaihe[0] + zaihe[n - 1]) / 3;

		for (i = 3; i < n - 1; i++) {
			weiyi[i] = (weiyi[i - 2] + weiyi[i - 1] + weiyi[i]) / 3;
			zaihe[i] = (zaihe[i - 2] + zaihe[i - 1] + zaihe[i]) / 3;
		}

		// 一阶惯性滤波
		float c = 0.9f;
		for (i = 1; i < n - 1; i++) {
			weiyi[i] = c * weiyi[i] + (1 - c) * weiyi[i - 1];
			zaihe[i] = c * zaihe[i] + (1 - c) * zaihe[i - 1];
		}

		// 封闭图形
		weiyi[0] = weiyi[n - 1] = (weiyi[0] + weiyi[n - 1]) / 2;
		zaihe[0] = zaihe[n - 1] = (zaihe[0] + zaihe[n - 1]) / 2;

		SGTDataComputerProcess scp = new SGTDataComputerProcess();
		scp.calcSGTData(weiyi, zaihe, 2, 30f, 80f, 0.9f, 90f);
	}

}
