package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class NsLookup {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while(true){
			System.out.print(">");
		String Hostname = scanner.next();
		if(Hostname.equals("exit"))
		{
			break;    
		}
		
		try {
			 
			
			InetAddress[] inetAddress = InetAddress.getAllByName(Hostname);
			
			for(int i = 0; i < inetAddress.length; i++)
			{
				String hostAddress = inetAddress[i].getHostAddress();
				System.out.println(inetAddress[i].getHostName() + " : " + hostAddress);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		}
		scanner.close();
	}
	
}
