package thread;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadEx {

	public static void main(String[] args) {
		List list = new ArrayList();
		
		
		Thread t1 = new AlphabetThread(list);  //Thread를 처음부터 만들겠다고 할때.
		Thread t2 = new DigitThread(list);
		Thread t3 = new DigitThread(list);
		Thread t4 = new Thread(new UpperCaseAlphabetThread());  //기존에 존재하는 클래스를 상속받아 Thread를 사용할 때.
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
//		for(int i = 0; i < 9; i++){
//			System.out.print(i);
//		}

//		for(char c = 'a'; c <= 'z'; c++){
//			System.out.print(c);
//		}
	}

}


//package thread;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MultiThreadEx {
//
//	public static void main(String[] args) {
//		List list = new ArrayList();
//		
//		
//		Thread t1 = new AlphabetThread(list);
//		Thread t2 = new DigitThread(list);
//		Thread t3 = new DigitThread(list);
//		
//		t1.start();
//		t2.start();
//		t3.start();
//		
////		for(int i = 0; i < 9; i++){
////			System.out.print(i);
////		}
//
////		for(char c = 'a'; c <= 'z'; c++){
////			System.out.print(c);
////		}
//	}
//
//}


//package thread;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MultiThreadEx {
//
//	public static void main(String[] args) {
//		
//		Thread t1 = new AlphabetThread();
//		Thread t2 = new DigitThread();
//		Thread t3 = new DigitThread();
//		
//		t1.start();
//		t2.start();
//		t3.start();
//		
////		for(int i = 0; i < 9; i++){
////			System.out.print(i);
////		}
//
////		for(char c = 'a'; c <= 'z'; c++){
////			System.out.print(c);
////		}
//	}
//
//}
