package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public  EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//4. 연결 성공
		InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		int remoteHostPort = remoteAddress.getPort();
		String remotHostAddress =remoteAddress.getAddress().getHostAddress();
		consoleLog("connected from client" + remotHostAddress + ":" +remoteHostPort);

		try{
			//5.IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true); // 서블릿에서 PrintWriter는 잘쓰인다.  new PrintWriter(true->자동 플러쉬)
			
			while(true){
			  String message =	br.readLine(); //blocking  
			  if(message == null){
				  //클라이언트가 소켓을 닫음
				  consoleLog(" disconnected by client");
				  break;
			  }
			  
			  
			  consoleLog("received : " + message );
			  //데이터 쓰기
			  pw.println(message);   //	동일함  => pw.print(message + "\n");
			  
			}
		}
		catch(SocketException e){
			//클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
			consoleLog("[server] closed by client");	
		}
		catch(IOException e){    //내부 데이터 소켓 통신 예외처리
			e.printStackTrace();
		}finally{
			try{
				if(socket != null && socket.isClosed() == false){
					socket.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	private void consoleLog(String message){
		System.out.println("[server:" + getId() + "]" + message);  //getId() Thread ID 출력하기
	}
}
