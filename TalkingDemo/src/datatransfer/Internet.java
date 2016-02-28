package datatransfer;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Internet {
  static Socket sk;
  static FileInputStream fis;
  FileOutputStream fos;
  DataOutputStream datOut;
  static byte fileByte[];
  
  public static void  fileUpload(String hostIP,int Port,String Path){
	 try{
		 int readFileSize=0;
		 sk=new Socket(hostIP,Port);
		 fileByte=new byte[1024*10];
		 fis=new FileInputStream(Path);
	     OutputStream skgos=sk.getOutputStream();
	     
		 while((readFileSize=fis.read(fileByte, 0, fileByte.length))!=-1)
	     {skgos.write(fileByte, 0, fileByte.length);}
	    skgos.flush();
	 }catch(Exception e){}
	  
  }
  }
  

