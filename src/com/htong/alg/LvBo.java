package com.htong.alg;
/**
 * 滤波
 * @author 赵磊
 *
 */
public class LvBo {
	public static void lvBo(float[] weiyi, float[] zaihe) {
		int n = weiyi.length;
	//	weiyi[0] = (weiyi[n - 1] + weiyi[n - 2] + weiyi[n - 3]) / 3;
		zaihe[0] = (zaihe[n - 1] + zaihe[n - 2] + zaihe[n - 3]) / 3;

	//	weiyi[1] = (weiyi[0] + weiyi[n - 1] + weiyi[n - 2]) / 3;
		zaihe[1] = (zaihe[0] + zaihe[n - 1] + zaihe[n - 2]) / 3;

	//	weiyi[2] = (weiyi[1] + weiyi[0] + weiyi[n - 1]) / 3;
		zaihe[2] = (zaihe[1] + zaihe[0] + zaihe[n - 1]) / 3;

		for (int i = 3; i < n - 1; i++) {
	//		weiyi[i] = (weiyi[i - 2] + weiyi[i - 1] + weiyi[i]) / 3;
			zaihe[i] = (zaihe[i - 2] + zaihe[i - 1] + zaihe[i]) / 3;
		}

		// 一阶惯性滤波
		float c = 0.8f;
		for (int i = 1; i < n - 1; i++) {
	//		weiyi[i] = c * weiyi[i] + (1 - c) * weiyi[i - 1];
			zaihe[i] = c * zaihe[i] + (1 - c) * zaihe[i - 1];
		}

		// 封闭图形
		weiyi[0] = weiyi[n - 1] = (weiyi[0] + weiyi[n - 1]) / 2;
		zaihe[0] = zaihe[n - 1] = (zaihe[0] + zaihe[n - 1]) / 2;
	}

}
