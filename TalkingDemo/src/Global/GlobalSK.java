package Global;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GlobalSK extends Thread
{
  public static Socket globalSocket;
  
  @Override
  public void run()
  {
  try {
	globalSocket=new Socket("192.168.0.2",8090);
    
  } catch (UnknownHostException e) 
{	e.printStackTrace();
} catch (IOException e) 
{	e.printStackTrace();
}
  
  
  }
	
	
}
