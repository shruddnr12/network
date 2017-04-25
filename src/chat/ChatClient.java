package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private static final String SERVER_IP = "192.168.1.44";
	private static final int SERVER_PORT = 9006;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null; 

		try { 
			//1. 키보드 연결
			scanner = new Scanner( System.in );
			//2.  Socket 생성
			socket = new Socket();

			//3. 서버 연결
			socket.connect( new InetSocketAddress( SERVER_IP, SERVER_PORT ) );

			//4. reader/writer 생성
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "utf-8" ) );
			PrintWriter pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "utf-8" ), true );
		
			//5. join 프로토콜				
			System.out.print("닉네임>>");
			String nickname = scanner.nextLine();  //"둘리" 입력
			pw.println("join:" + nickname);   //"join:둘리" 송출 
			pw.flush();
			
			
			String confirm = br.readLine(); //block
			System.out.println(confirm);
			
			new ChatClientReceivedThread(br).start();
			
			while( true ) {
				System.out.print(">>");
				String input = scanner.nextLine();
				if("quit".equals(input) == true){
					//8.quit 프로토콜 처리
					pw.println(input);
					pw.flush();
					break;
				}else{
					//9. 메세지 처리
					  pw.println("message:" + input);
					  pw.flush();
				}
			}

	} catch( IOException e  ){
		e.printStackTrace();
	}finally {
		try {
			if( socket != null && socket.isClosed() == false ) {
				socket.close();
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}
		scanner.close();
	}		


}
}
