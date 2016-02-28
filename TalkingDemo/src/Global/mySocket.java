package Global;

import java.net.Socket;

public class mySocket{
	
	public static Socket sk;
	
	public Socket publicSocket(Socket sk){
		this.sk=sk;
		return this.sk;
	}
	
}
