package test;

import net.sf.json.JSONArray;

public class MyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONArray json = new JSONArray();
		json.add(0, 1);
		
		System.out.println(json.toString());

	}

}
