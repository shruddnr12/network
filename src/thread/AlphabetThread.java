package thread;

import java.util.List;

public class AlphabetThread extends Thread {
	private List list;
	 public AlphabetThread(List list) {
		this.list = list;
	}
	
	@Override
	public void run() {
		for(char c = 'a'; c <= 'z'; c++){
			System.out.print(c);
			synchronized(list){   //동기화 작업... 객체에 lock을 건다. 다른 곳의 Thread가 완료 될때까지 기다린다.
			list.add(c);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
