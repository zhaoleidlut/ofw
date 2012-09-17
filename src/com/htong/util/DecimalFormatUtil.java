package com.htong.util;

import java.text.DecimalFormat;

public class DecimalFormatUtil {
	public static float floatToFloat(float f, int lenght) {
		
		DecimalFormat df = null;
		switch(lenght) {
		case 0 : df = new DecimalFormat("#");
				break;
		case 1 : df = new DecimalFormat("#.#");
		break;
		case 2 : df = new DecimalFormat("#.##");
		break;
		case 3 : df = new DecimalFormat("#.###");
		break;
		default : df = new DecimalFormat("#.##");
		break;
		}
		
		return Float.parseFloat(df.format(f));
	}
	
	public static void main(String args[]) {
		float a = 12.2373335f;
		System.out.println(DecimalFormatUtil.floatToFloat(a, 4));
	}

}
