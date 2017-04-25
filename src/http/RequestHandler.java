package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;

	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// get IOStream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			OutputStream os = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );

			String request = null;
			
			while(true){
				String line = br.readLine();
				if(line == null || "".equals(line)){
					break;
				}
				if(request == null){
					request = line;
				}
			}
			consoleLog(request);
			
			// 요청 분석
			 String[] tokens = request.split(" ");  // 이건 약속!! 정상적인 브라우저라면 명령어,url,프로토콜 순으로 들어갈 것이다. 
			if("GET".equals(tokens[0])){
				responseStaticResource(os, tokens[1], tokens[2]);  
			}else{
				//POST,DELETE,PUT
				//심플 웹 서버에서는 잘못된 요청(Bad Request, 400)로 처리
				response400Error(os,tokens[2]);
			}
			
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
//			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
//			os.write( "\r\n".getBytes() );  //\r\n 개행 시작을 통한 header종료 표시
//			os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
			
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	public void responseStaticResource(OutputStream os,String url,String protocol) throws IOException{
		//HTTP/1.0 200 OK
		//Content-Type:text:html; charset=utf-8
		// /*  내용   */
		//
		//./webapp => Document Root (HTTP는 원래 문서를 교환하는 프로토콜이였다. 그래서 심플한거고)
		if("/".equals(url)){
			url= "/index.html"; //welcome file 처리
		}
		File file = new File("./webapp" + url); // 도메인 뒤에 항상 /로 시작한다.
		if(file.exists() == false){
			//404(File Not Found) response
			response404Error(os,protocol);
			return ;
		}
		
		byte[] body = Files.readAllBytes(file.toPath());   
		String mimeType = Files.probeContentType(file.toPath());  
		//header 전송
		os.write((protocol + "200 OK\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8"));   //리소스마다 타입이 있는데 그걸 mimeType이라고 한다. 
		os.write("\r\n".getBytes("UTF-8"));
		//body 전송
		os.write(body);
	}
	
	public void response404Error(OutputStream os,String protocol)throws IOException{
	     File file = new File("./webapp/error/404.html");
	     byte[] body = Files.readAllBytes(file.toPath());
	     os.write((protocol + " 404 File Not Found\r\n").getBytes("UTF-8"));
	     os.write("Content-Type:text.html; charset = utf-8\r\n".getBytes("UTF-8"));
	     os.write("\r\n".getBytes("UTF-8"));
	     os.write(body);
	}
	public void response400Error(OutputStream os,String protocol)throws IOException{
		File file = new File("./webapp/error/400.html");
		byte[] body = Files.readAllBytes(file.toPath());
		os.write((protocol + " 400 Bad Request\r\n").getBytes("UTF-8"));
		os.write("Content-Type:text.html; charset = utf-8\r\n".getBytes("UTF-8"));
		os.write("\r\n".getBytes("UTF-8"));
		os.write(body);
	}
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}