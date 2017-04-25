package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
	private static final int SERVER_PORT = 9006;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		List<Writer> listwriter = new ArrayList<Writer>();
		//1. 키보드 연결
		Scanner scanner = null;
		
		try {
			// 2. 소켓 생성
			serverSocket = new ServerSocket();
			scanner = new Scanner( System.in );
			InetAddress address = InetAddress.getLocalHost();
			String hostAddress = address.getHostAddress();
			serverSocket.bind( new InetSocketAddress(hostAddress, SERVER_PORT ) );
			log("연결 기다림 " + hostAddress + ":" + SERVER_PORT);
			
			while(true){
				//3.연결
				Socket socket = serverSocket.accept(); //blocking
				InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
				int remoteHostPort = remoteAddress.getPort();
				String remotHostAddress =remoteAddress.getAddress().getHostAddress();
				log("연결 성공" + remotHostAddress + ":" +remoteHostPort);
				
				new ChatServerThread(socket,listwriter).start();
			}		
	
		} catch (IOException e) {
			log("error:" + e);
		}finally{
			try {
				if( serverSocket != null && serverSocket.isClosed() == false)
				{
					serverSocket.close();
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	public static void log(String message){
		System.out.println(message);
	}
	
}
