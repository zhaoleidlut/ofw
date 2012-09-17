package test;

public class ThreadMain {
	public void test() {
		for(int i=0;i<4;i++) {
			ThreadTest threadTest = new ThreadTest(i);
			threadTest.start();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThreadMain tm = new ThreadMain();
		tm.test();

	}

}
