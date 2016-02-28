package datatransfer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class FileTransfer extends Thread {
	 int read;
	 Socket sk;
	 FileInputStream fis;
	 DataOutputStream dataOut;
	 public static String hostIP;
	 public static int Port;
	 public static String Path;
	 byte[] fileByte=null;

	
	
 public void run(){
	 Log.i("11", "sock start");
	 fileByte=new byte[10240];
//	 chatting.audioPlay(Path);
	 try{
	 sk=new Socket(hostIP,Port);	 
     OutputStream skgos=sk.getOutputStream();
     dataOut=new DataOutputStream(skgos);
     
    try 
    { File file=new File(Path);
     fis=new FileInputStream(file);
    }catch(FileNotFoundException e){Log.v("",e.toString());}
	 
	while((read=fis.read(fileByte,0,fileByte.length))!=-1)
	{   
		Log.v("read","read"+" "+read);
		dataOut.write(fileByte,0,read);
		}
	  dataOut.flush();
	 
//	  dataOut.close();
	 }catch(Exception e){Log.e("sock","sock"+" "+e.toString());}
 }
 public FileTransfer(String Path,String hostIP,int Port) 
 {  
	this.Path=Path;
	this.hostIP=hostIP;
	this.Port=Port;
	
	}
 
 }

