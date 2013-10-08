package com.htong.gzzd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GTDataComputerProcess {
	private static final Logger log = Logger
			.getLogger(GTDataComputerProcess.class);

	private int fault_code = 0; // 0为正常
	private int fault_level = 0;	//故障严重程度，0为无，10为最严重

	public Map<String, Object> calcSGTData(float weiyi[], float zaihe[],
			int chongci, float chongchengshijian, float bengjing,
			float oilDensity, float hanshuiliang, float llsxzhjz) {

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
		// 减掉最小位移，使得最小位移为0
		float minWeiyi = weiyi[minFlag];
		for (int i = 0; i < weiyi.length; i++) {
			weiyi[i] -= minWeiyi;
		}

		// for(int i = 0;i<weiyi.length;i++) {
		// System.out.println(weiyi[i]);
		// }
		// for(int i = 0;i<weiyi.length;i++) {
		// System.out.println(zaihe[i]);
		// }

		float chongcheng = weiyi[maxFlag] - weiyi[minFlag];

		log.debug("上死点序号为：" + maxFlag);
		log.debug("下死点序号为：" + minFlag);
		
//		log.debug("上死点B  X:" + weiyi[maxFlag] + " Y:" + zaihe[maxFlag]);
//		log.debug("下死点D  X:" + weiyi[minFlag] + " Y:" + zaihe[minFlag]);

		float minZaihe = zaihe[0]; // 载荷最小值
		float maxZaihe = zaihe[0]; // 载荷最大值
		for (int i = 1; i < n; i++) {
			// 最小值
			if (minZaihe > zaihe[i]) {
				minZaihe = zaihe[i];
			}
			if (maxZaihe < zaihe[i]) {
				maxZaihe = zaihe[i];
			}
		}

		// 位移、载荷归一化处理
		float chaWeiyi = weiyi[maxFlag] - weiyi[minFlag];
		float chaZaihe = maxZaihe - minZaihe;

		float oneWeiyi[] = new float[n]; // 归一化的位移
		float oneZaihe[] = new float[n]; // 归一化的载荷
		for (int i = 0; i < n; i++) {
			oneWeiyi[i] = (weiyi[i] - weiyi[minFlag]) / chaWeiyi;
			oneZaihe[i] = (zaihe[i] - minZaihe) / chaZaihe;
		}

		// 求凸包
		List<Point> pointList = new ArrayList<Point>();
		for (int i = 0; i < n; i++) {
			Point p = new Point();
			p.setX(oneWeiyi[i]);
			p.setY(oneZaihe[i]);
			p.setIndex(i);
			pointList.add(p);
		}
		Melkman melkman = new Melkman(pointList);
		Point[] pointArray = melkman.getTubaoPoint(); // 凸包点
		int flagnum = pointArray.length;
		log.debug("凸包点个数：" + pointArray.length);
		// for (Point p : pointArray) {
		// System.out.println(p.getX());
		// }
		// for (Point p : pointArray) {
		// System.out.println(p.getY());
		// }
		// System.out.println();

		// {liquidProduct(产液量):Float,oilProduct(产油量):Float,chongcheng(冲程):Float,minZaihe(最小载荷):Float，maxZaihe(最大载荷)，gongtuxinxi(功图信息)：String}

		QuLvCalc quLv = new QuLvCalc();
		// 计算曲率
		// float[] ki = quLv.getQuLv(pointArray);
		// for(int i = 0;i<flagnum;i++) {
		// System.out.println(ki[i]);
		// }

		// 曲率变化量
		// float[] ski = quLv.getQuLvBHL(pointArray);
		// for(int i=0;i<flagnum;i++) {
		// System.out.println(ski[i]);
		// }

		// 曲率变化量五点平均值
		float[] ski5 = quLv.getQuLvPJBHL(pointArray);
		// for(int i=0;i<flagnum;i++) {
		// System.out.println(ski5[i]);
		// }

		// 上半部分凸包点
		List<Integer> shangIndex = new ArrayList<Integer>();
		for (int i = 0; i < flagnum; i++) {
			if (pointArray[i].getY() > 0.7) {
				shangIndex.add(i);
			}
		}
		int minWeiyiFlag = shangIndex.get(0);// 上半部分位移最小点
		for (int i = 1; i < shangIndex.size(); i++) {
			if (pointArray[shangIndex.get(i)].getX() < pointArray[minWeiyiFlag]
					.getX()) {
				minWeiyiFlag = shangIndex.get(i);
			}
		}
		// 求左上点A
		List<Integer> zsIndex = new ArrayList<Integer>();
		for (int i = 0; i < shangIndex.size(); i++) {
			if (pointArray[shangIndex.get(i)].getX() < (pointArray[minWeiyiFlag]
					.getX() + 0.15) && pointArray[shangIndex.get(i)].getIndex()<100) {
				zsIndex.add(shangIndex.get(i));
			}
		}
		int zsFlag = zsIndex.get(0);// 左上标志
		for (int i = 1; i < zsIndex.size(); i++) {
			if (ski5[zsIndex.get(i)] > ski5[zsFlag]) {
				zsFlag = zsIndex.get(i);
			}
		}
		
//		log.debug("左上点A  X:" + weiyi[pointArray[zsFlag].getIndex()] + " Y:"
//				+ zaihe[pointArray[zsFlag].getIndex()]);

		// 求右上点B
		// List<Integer> ysIndex = new ArrayList<Integer>();
		// for(int i=0;i<flagnum;i++) {
		// if(pointArray[i].getX()>0.5 && pointArray[i].getY()>0.5) {
		// ysIndex.add(i);
		// }
		// }
		// int ysFlag = ysIndex.get(0);//右上标志
		// for(int i=1;i<ysIndex.size();i++) {
		// if(ski5[ysIndex.get(i)]>ski5[ysFlag]) {
		// ysFlag = ysIndex.get(i);
		// }
		// }
		// log.debug("右上点 X:"+pointArray[ysFlag].getX()*chaWeiyi +
		// " Y:"+(pointArray[ysFlag].getY()*chaZaihe+minZaihe));

		// 下半部分凸包点
		List<Integer> xiaIndex = new ArrayList<Integer>();
		for (int i = 0; i < flagnum; i++) {
			if (pointArray[i].getY() < 0.4) {
				xiaIndex.add(i);
			}
		}
		int maxWeiyiFlag = xiaIndex.get(0);// 下半部分位移最大点
		for (int i = 1; i < xiaIndex.size(); i++) {
			//log.debug(weiyi[pointArray[xiaIndex.get(i)].getIndex()]);
			if (pointArray[xiaIndex.get(i)].getX() > pointArray[maxWeiyiFlag]
					.getX()) {
				maxWeiyiFlag = xiaIndex.get(i);
			}
		}
		//log.debug(weiyi[pointArray[xiaIndex.get(maxWeiyiFlag)].getIndex()]);
		// 求有效冲程，右下点C
		List<Integer> yxIndex = new ArrayList<Integer>();
		int yxFlag = 0;
		if (pointArray[maxWeiyiFlag].getX() - 0.1 < 0) {// 有效冲程比较小
			yxFlag = maxWeiyiFlag;
		} else {
			for (int i = 0; i < xiaIndex.size(); i++) {
				if (pointArray[xiaIndex.get(i)].getX() > (pointArray[maxWeiyiFlag]
						.getX() - 0.1) && pointArray[xiaIndex.get(i)].getIndex()>100) {
					yxIndex.add(xiaIndex.get(i));
				}
			}
			yxFlag = yxIndex.get(0);
			for (int i = 1; i < yxIndex.size(); i++) {
				if (ski5[yxIndex.get(i)] > ski5[yxFlag]) {
					yxFlag = yxIndex.get(i);
				}
			}
		}
		
//		log.debug("右下点C  X:" + weiyi[pointArray[yxFlag].getIndex()] + " Y:"
//				+ zaihe[pointArray[yxFlag].getIndex()]);

		// 求左下
		// List<Integer> zxIndex = new ArrayList<Integer>();
		// for(int i=0;i<xiaIndex.size();i++) {
		// if(pointArray[xiaIndex.get(i)].getX()<(pointArray[maxWeiyiFlag].getX()+0.1))
		// {
		// zxIndex.add(xiaIndex.get(i));
		// }
		// }
		// int zxFlag = zxIndex.get(0);
		// for(int i=1;i<zxIndex.size();i++) {
		// if(ski5[zxIndex.get(i)]>ski5[zxFlag]) {
		// zxFlag = zxIndex.get(i);
		// }
		// }
		// log.debug("左下点为 X："+pointArray[zxFlag].getX()*chaWeiyi + " Y:" +
		// (pointArray[zxFlag].getY()*chaZaihe+minZaihe));

		float pjsz = 0; // 平均上载
		if ((maxFlag - pointArray[zsFlag].getIndex()) > 0) {
			for (int i = pointArray[zsFlag].getIndex(); i <= maxFlag; i++) {
				pjsz += zaihe[i];
			}
			pjsz /= (maxFlag - pointArray[zsFlag].getIndex() + 1);
		}

		float pjxz = 0; // 平均下载
		if (minFlag < 100) {
			for (int i = pointArray[yxFlag].getIndex(); i < 200; i++) {
				pjxz += zaihe[i];
			}
			for (int i = 0; i <= minFlag; i++) {
				pjxz += zaihe[i];
			}
			pjxz /= (200 - pointArray[yxFlag].getIndex() + minFlag + 1);
		} else {
			for (int i = pointArray[yxFlag].getIndex(); i <= minFlag; i++) {
				pjxz += zaihe[i];
			}
			pjxz /= (minFlag - pointArray[yxFlag].getIndex() + 1);
		}

		float areaLL = (pjsz - pjxz)
				* (weiyi[maxFlag] - weiyi[pointArray[zsFlag].getIndex()]); // 示功图理论面积

		float gtArea = new AreaCalc().getGTArea(weiyi, zaihe, maxFlag, minFlag); // 示功图实际面积

		float zaiheCha = pjsz - pjxz; // 载荷差
		float maxminZaiheCha = maxZaihe - minZaihe;

		// 故障诊断程序

		boolean gdfel = false;
		boolean ydfel = false;
		float areaGJ = (float) (maxZaihe - minZaihe) * chongcheng; // 用冲程*（最大载荷-最小载荷）估算漏失面积
		log.debug("估算饱满面积值：" + areaGJ);
		if (zaihe[minFlag] >= minZaihe + maxminZaiheCha * 0.25
				&& zaihe[pointArray[yxFlag].getIndex()] >= minZaihe + maxminZaiheCha * 0.25) {
			if (gtArea < 0.8 * areaGJ) {
				gdfel = true;
			}
		}
		if (zaihe[maxFlag] <= maxZaihe - maxminZaiheCha * 0.25
				&& zaihe[pointArray[zsFlag].getIndex()] <= maxZaihe - maxminZaiheCha * 0.02) {
			if (gtArea < 0.8 * areaGJ) {
				ydfel = true;
			}
		}
		if (gdfel && ydfel) {
			log.debug("【故障类型--双凡尔漏失】");
			fault_code = 3;
			
		} else if (gdfel) {
			log.debug("【故障类型--固定凡尔漏失】");
			fault_code = 2;
		} else if (ydfel) {
			log.debug("【故障类型--游动凡尔漏失】");
			fault_code = 1;
		}
		
		int absoluteEIndex = 1000;
		int absoluteFIndex = 1000;

		if (pointArray[yxFlag].getX() < 0.9 * (1 - pointArray[zsFlag].getX())) {// 有效冲程小于理论0.9
			log.debug("【故障诊断】：产液量不足");
			// 求E点
			List<Integer> eIndex = new ArrayList<Integer>();
			for (int i = 0; i < flagnum; i++) {
				if (pointArray[i].getIndex() > maxFlag
						&& pointArray[i].getY() > 0.5
						&& (1 - pointArray[i].getX()) < 0.2
						&& (1 - pointArray[i].getX()) > 0) {
					eIndex.add(i);
				}
			}
			int eFlag = 1000;
			if (!eIndex.isEmpty()) {
				log.debug("E点备用数量：" + eIndex.size());
				eFlag = eIndex.get(0);
				for (int i = 1; i < eIndex.size(); i++) {
					if (ski5[eFlag] < ski5[eIndex.get(i)]) {
						eFlag = eIndex.get(i);
					}
				}
				absoluteEIndex = pointArray[eFlag].getIndex();
			} else {
				absoluteEIndex = maxFlag;
//				log.debug("E点  不存在！用B点代替。");
			}

			
			log.debug("E点  X:" + weiyi[absoluteEIndex] + "  Y:"
					+ zaihe[absoluteEIndex]);

			// 求F
			int startF = absoluteEIndex; // F点起始范围E的索引
			int endF = pointArray[yxFlag].getIndex(); // F点结束范围C的索引

			// 求EF凸包
			// log.debug("E点序列："+startF);
			// log.debug("C点序列："+endF);
			List<Point> fPointList = new ArrayList<Point>();
			for (int i = startF; i <= endF; i++) {
				Point p = new Point();
				p.setX(weiyi[i]);
				p.setY(zaihe[i]);
				p.setIndex(i);
				fPointList.add(p);
			}
			Melkman fMelkman = new Melkman(fPointList);
			Point[] fPointArray;
			if(fPointList.isEmpty()) {
				fPointArray = new Point[0];
			} else {
				fPointArray = fMelkman.getTubaoPoint(); // 凸包点
			}
			
			// log.debug("F点凸包个数："+fPointArray.length);

			QuLvCalc fQuLv = new QuLvCalc();
			log.debug(fPointArray.length);
			float fSki5[] = fQuLv.getQuLvPJBHL(fPointArray);
			int fFlag = 2; // 最大曲率变化值索引
			if(fSki5!=null && fSki5.length>0) {
				for (int i = 3; i <= fSki5.length - 2; i++) {
					if (fSki5[i] > fSki5[fFlag]) {
						fFlag = i;
					}
				}
				absoluteFIndex = fPointArray[fFlag].getIndex();
				log.debug("F点  X:" + weiyi[absoluteFIndex] + "  Y:"
						+ zaihe[absoluteFIndex]);
			}
			
			

			float beLim = 0.02f; // BE阈值，小于此值，则一定为供液不足
			float areaLim = 0.2f; // 面积阈值，小于此值，则可能为气锁
			float be_cd = 0.4f; // CD-BE阈值，小于此值，则可能为气锁

			float be = zaihe[maxFlag] - zaihe[absoluteEIndex];
			if (be < beLim * (pjsz - pjxz)) {
				log.debug("【故障类型--供液不足】");
				fault_code = 4;
			} else {
				float cd = weiyi[pointArray[yxFlag].getIndex()];
				if (pointArray[yxFlag].getX() < 0.4 * (1 - pointArray[zsFlag]
						.getX())
						&& gtArea < areaLim * areaLL
						&& Math.abs(be - cd) < be_cd) {// 面积极小并且CD、BE距离小则为气锁，否则为气体影响
					log.debug("【故障类型--气锁】");
					fault_code = 6;
				} else {
					log.debug("【故障类型--气体影响】");
					fault_code = 5;
				}
			}
			
//			System.out.println(pointArray[yxFlag].getX());
//			System.out.println(1 - pointArray[zsFlag].getX());
//			System.out.println("故障级别：" + fault_level);
			
		}

//		float ski[] = quLv.getCommonQulv(weiyi, zaihe);
//		for (int i = maxFlag - 20; i <= maxFlag + 20; i++) {
//			if (ski[i] >= 50 && weiyi[i]>weiyi[i-1] && weiyi[i]<weiyi[i+1]) {
//				log.debug("【故障类型--泵上碰】");
//				fault_code = 7;
//				break;
//			}
//		}
//		for (int i = 25;; i--) {
//			if (ski[i] >= 50 && weiyi[i]>weiyi[i-1] && weiyi[i]<weiyi[i+1]) {
//				log.debug("【故障类型--泵下碰】");
//				fault_code = 8;
//				break;
//			}
//
//			if (i <= 0) {
//				i = 199;
//			}
//			if (i < 175) {
//				break;
//			}
//		}

		if (maxZaihe < llsxzhjz && gtArea < chongcheng * 10) {
			log.debug("【故障类型--抽油杆断脱】");
			fault_code = 9;
		} else if (minZaihe > 100 && gtArea < chongcheng * 10) {
			log.debug("【故障类型--连抽带喷】");
			fault_code = 10;
		}
		
		
		if(fault_code == 1||fault_code == 2||fault_code == 3) {
			fault_level = (int)((areaGJ - gtArea)*10 / areaGJ);
		} else if(fault_code == 4 || fault_code == 5||fault_code == 6) {
			fault_level = (int)(10-pointArray[yxFlag].getX()*10/(1 - pointArray[zsFlag].getX()));
		}

		// 产液量
		float liquidProduct = 0;
		float youxiaochongcheng = weiyi[pointArray[yxFlag].getIndex()];
		log.debug("冲程时间：" + chongchengshijian);
		if (chongci == 0) {// 冲次为0，用冲程时间来算一个小时产量
			liquidProduct = (float) (Math.pow(bengjing / 2000, 2) * 3.1415926
					* 3600 * youxiaochongcheng / chongchengshijian);
		} else {
			liquidProduct = (float) (Math.pow(bengjing / 2000, 2) * 3.1415926
					* chongci * youxiaochongcheng);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();

		float newChongCheng = new BigDecimal(chongcheng).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		resultMap.put("chongcheng", newChongCheng); // 添加冲程
		log.debug("冲程：" + newChongCheng);

		float newYouxiaochongcheng = new BigDecimal(youxiaochongcheng)
				.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		resultMap.put("youxiaochongcheng", newYouxiaochongcheng);// 添加有效冲程
		log.debug("有效冲程：" + newYouxiaochongcheng);

		float newLiquidProduct = new BigDecimal(liquidProduct).setScale(3,
				BigDecimal.ROUND_HALF_UP).floatValue();
		resultMap.put("liquidProduct", newLiquidProduct); // 添加产液量
		log.debug("理论产液量：" + newLiquidProduct);

		float newMinZaihe = new BigDecimal(minZaihe).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		float newMaxZaihe = new BigDecimal(maxZaihe).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		resultMap.put("minZaihe", newMinZaihe); // 添加最小载荷
		resultMap.put("maxZaihe", newMaxZaihe); // 添加最大载荷
		log.debug("载荷最大值：" + newMaxZaihe);
		log.debug("载荷最小值：" + newMinZaihe);

		float newPjsz = new BigDecimal(pjsz).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		float newPjxz = new BigDecimal(pjxz).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		resultMap.put("pjsz", newPjsz); // 添加平均上载
		resultMap.put("pjxz", newPjxz); // 添加平均下载
		log.debug("平均上载：" + newPjsz);
		log.debug("平均下载：" + newPjxz);
		
		resultMap.put("zaiheCha", newPjsz-newPjxz);	//添加载荷差
		log.debug("载荷差：" + (newPjsz-newPjxz));

		float newGTArea = new BigDecimal(gtArea).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		float newAreaLL = new BigDecimal(areaLL).setScale(2,
				BigDecimal.ROUND_HALF_UP).floatValue();
		resultMap.put("gtArea", newGTArea); // 添加功图面积
		resultMap.put("areaLL", newAreaLL); // 添加理论面积
		log.debug("示功图面积：" + newGTArea);
		log.debug("理论示功图面积：" + newAreaLL);

		resultMap.put("gzzd", fault_code); // 故障诊断
		log.debug("故障诊断代码：" + fault_code);
		
		resultMap.put("fault_level", fault_level);
		log.debug("故障程度：" + fault_level);

		resultMap.put("AX", weiyi[pointArray[zsFlag].getIndex()]);
		resultMap.put("AY", zaihe[pointArray[zsFlag].getIndex()]);
		log.debug("左上点A  X:" + weiyi[pointArray[zsFlag].getIndex()] + " Y:"
				+ zaihe[pointArray[zsFlag].getIndex()]);

		resultMap.put("BX", weiyi[maxFlag]);
		resultMap.put("BY", zaihe[maxFlag]);
		log.debug("上死点B  X:" + weiyi[maxFlag] + " Y:" + zaihe[maxFlag]);

		resultMap.put("CX", weiyi[pointArray[yxFlag].getIndex()]);
		resultMap.put("CY", zaihe[pointArray[yxFlag].getIndex()]);
		log.debug("右下点C  X:" + weiyi[pointArray[yxFlag].getIndex()] + " Y:"
				+ zaihe[pointArray[yxFlag].getIndex()]);

		resultMap.put("DX", weiyi[minFlag]);
		resultMap.put("DY", zaihe[minFlag]);
		log.debug("下死点D  X:" + weiyi[minFlag] + " Y:" + zaihe[minFlag]);

		if(absoluteEIndex<200 && absoluteFIndex<200) {
			resultMap.put("EX", weiyi[absoluteEIndex]);
			resultMap.put("EY", zaihe[absoluteEIndex]);
			resultMap.put("FX", weiyi[absoluteFIndex]);
			resultMap.put("FY", zaihe[absoluteFIndex]);
		}
		

		return resultMap;
	}

}
