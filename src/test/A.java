package test;

import com.htong.gzzd.Point;

public   class A {
	Point p = new Point();
	
	public static void main(String args[]) {
		A a = new A();
		System.out.println(a.getClass().getPackage().getName());
	}
}
