package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT = 6060;


	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;

		// 1. 소켓 생성
		try {
			datagramSocket = new DatagramSocket(PORT);

			while(true){
			//2. 수신 패킷 생성
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
			//3. 데이터 수신 대기

			datagramSocket.receive(receivePacket);
		
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
			String data = format.format(new Date());
			
			byte[] sendData = data.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,receivePacket.getSocketAddress());
			datagramSocket.send(sendPacket);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(datagramSocket != null && datagramSocket.isClosed() == false)
				datagramSocket.close();
		}

	}

}
