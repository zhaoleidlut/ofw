package test;

public class ThreadTest extends Thread {
	private Integer i;
	private  static  int jj = 100;

	public ThreadTest(Integer i) {
		this.i = i;
	}
	@Override
	public void run() {
		r();
	}
	private synchronized void r() {
		while (true) {
			if (i < 2) {
				jj++;
			} else {
				jj--;
			}
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(i + ":" + jj);
		}
	}
}
