package BackGround;

import java.net.Socket;

import Global.GlobalVar;

public class StartSocket extends Thread
{  
	@Override
	public void run()
	{
	try{
		GlobalVar.sk=new Socket(GlobalVar.hostURL,GlobalVar.Port);
//		HandleUnsend hdl=new HandleUnsend(this.sk);
//		hdl.start();
	}catch(Exception e)
	{
		e.printStackTrace();
		}
	}

}
