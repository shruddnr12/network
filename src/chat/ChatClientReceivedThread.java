package chat;

import java.io.BufferedReader;
import java.io.IOException;

public class ChatClientReceivedThread extends Thread {

	private BufferedReader br;
	public ChatClientReceivedThread(BufferedReader br) {
		this.br = br;
	}
	@Override
	public void run()  {
		
		try {
			while(true){
				String request = br.readLine(); //blocking
				if("quit".equals(request))
				{
					break;
				}else
				{
					System.out.println(request);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 


	}
}

