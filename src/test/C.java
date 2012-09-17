package test;

public class C {
	public void a() {
		A a = new A();
		System.out.println(a.getClass().getDeclaringClass().getName());
	}
	
	public static void main(String args[]) {
		C c = new C();
		c.a();
	}
	
}
