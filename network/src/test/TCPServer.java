package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private static final int SERVER_PORT = 5050;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			//1. 서버 소켓 생성
			serverSocket = new ServerSocket();
			
			//2. 바인딩( binding )
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind( new InetSocketAddress(localhostAddress, SERVER_PORT ) );
			System.out.println( "[server] binding "  + localhostAddress + ":" +  SERVER_PORT );
			
			//3. accept(연결 요청을 기다림)
			Socket socket = serverSocket.accept();   // blocking
			
			//4 연결 성공
			InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			int remoteHostPort = remoteAddress.getPort();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
			System.out.println( "[server] connected from " + remoteHostAddress + ":" + remoteHostPort );
			
			try {
				//5. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while( true ) {
					//6. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read( buffer ); //blocking
					if( readByteCount <= -1 ) {
						// 클라이언트가 소켓을 닫은 경우
						System.out.println( "[server] disconnected by client" );
						break;
					}
					String data = new String( buffer, 0, readByteCount, "utf-8" );
					System.out.println( "[server] received : " + data );
					
					//7. 데이터 쓰기
					os.write( data.getBytes( "utf-8" ) );
				}
			} catch( SocketException e ) {
				// 클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
				System.out.println( "[server] closed by client" );
			} catch( IOException e ) {
				e.printStackTrace();
			} finally {
				try {
					if( socket != null && socket.isClosed() == false ) {
						socket.close();
					}
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//자원정리
				if( serverSocket != null && serverSocket.isClosed() == false ){
					serverSocket.close();
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
	}

}
