package thread;

import java.util.List;

public class DigitThread extends Thread {
	private List list;
	 public DigitThread(List list) {
		this.list = list;
	}
	@Override
	public void run() {
		for(int i = 0; i <= 9; i++){
			System.out.print(i);
			list.add(10);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
