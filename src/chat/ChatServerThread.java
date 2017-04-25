package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ChatServerThread extends Thread {
	private String nickname;  //입력 받아야 하는 놈.
	private Socket socket;
	List<Writer> listwriter;
	public ChatServerThread(Socket socket,List<Writer> listwriter ) {
		this.socket = socket;
		this.listwriter = listwriter;
	}


	@Override
	public void run() {
		try{
			//5.IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true); // 서블릿에서 PrintWriter는 잘쓰인다.  new PrintWriter(true->자동 플러쉬)
			
			while(true){
				String request = br.readLine(); //blocking  
				if(request == null){
					//클라이언트가 소켓을 닫음
					ChatServer.log(" 클라이언트로 부터 연결 끊김");
					//doQuit(pw);
					break;
				}
				
				String [] tokens = request.split(":");  
				if("join".equals(tokens[0])){    //"join:둘리"
					doJoin(tokens[1],pw);
				}else if("message".equals(tokens[0])){
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])){
					 doQuit(pw);
				}else{
					ChatServer.log("에러:알수 없는 요청(" + tokens[0] + ")");
				}


				// consoleLog("received : " + message );
				//데이터 쓰기
				

			}
		}
		catch(SocketException e){
			//클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
			//consoleLog("[server] closed by client");	
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
	public void log(String message)
	{
		System.out.println(message);
	}
	private void doJoin(String nickname, PrintWriter writer){
		this.nickname = nickname;   //"둘리"
		String data = nickname + "님이 입장하셨습니다.";
		
		broadcast(data);   //"join:둘리님이 입장하셨습니다."

		/* writer pool에 저장*/
		addWriter(writer);   

		//ack
		writer.println("ok");
		writer.flush();
	}
	private void addWriter(Writer writer){
		synchronized (listwriter) {
			listwriter.add(writer);
		}
	}
	private void broadcast(String data){
		synchronized (listwriter) {
			for(Writer writer : listwriter){
				PrintWriter printWriter = (PrintWriter)writer;
				printWriter.println(data);
				printWriter.flush();
			}
		}
	}
	private void doMessage(String message){
		String data = message;
		broadcast(nickname + ":" + data);
	}
	
	private void doQuit(PrintWriter writer){
		removeWriter(writer);
		String data =nickname + "님이 퇴장 하였습니다.";
		broadcast(data);
	}

	private void removeWriter(PrintWriter writer){
		synchronized (listwriter){
			int removeindex = listwriter.indexOf(writer);
			listwriter.remove(removeindex);
		}
	}
}
