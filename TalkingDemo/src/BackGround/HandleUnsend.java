package BackGround;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Global.ChatObject;
import Global.GlobalVar;
import android.database.Cursor;
import android.util.Log;

import com.example.sqlite.*;
import com.example.start.*;

public class HandleUnsend extends Thread {
  /*
	Socket sk;
	ChatObject Co;
	public HandleUnsend(Socket sk){
		this.sk=sk;
	}
	@Override
	public void run(){
	while(true){
Cursor c=Select.WholeTableToRAM(GlobalVar.sb,"Unsend");
     try {
		Thread.sleep(2000);
	} catch (InterruptedException e1)
	{
		e1.printStackTrace();
	}
     Log.i("read unsend","reading unsend");
     if(c.getCount()>0)
    {
    try{
//	Log.i("start socket","ready to build socket");
//	Socket sk=new Socket();
//	sk.connect(new InetSocketAddress("192.168.0.102", 1314));
	Log.i("start socket","socket built");
	for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
    {
	Log.v("read data to Co","read to Chat Oj");
	Co=new ChatObject();
	
	Co.toId=c.getString(0);
	Co.fromId=c.getString(1);
	Co.serverPath=c.getString(2);
	Co.localPath=c.getString(3);
	//the 4 is REMOTE_SAY,usless here
	Co.Content=c.getString(5);
	Co.msgType=c.getString(6);
	Co.Time=c.getString(7);
	//Co.Time is long ,but the 7th column is Send_Out_Time,String type.
	Co.Duration=c.getString(8);
	if(Co.msgType.equals("1"))
	{
		//if it is a voice msg,make a fos
		File f2Send=new File(Co.localPath);
		long fileLength=f2Send.length();
		//byte[] fByte=new byte[(int)fileLength];
		//int.MAX_VALUE is 2^32-1,almost equal to 2.14G,so 
		//it won't occur an outOfBundent Exception
		Co.fileByte=new byte[(int)fileLength];
		//this declacation is very important,whitout it will occur nullpoint
		FileInputStream Fis=new FileInputStream(f2Send);
		Fis.read(Co.fileByte,0,(int)fileLength);
		Log.v("fbyte","read fis into fbyte ok");
//Co.serverPath="C:"+File.separator+Co.fromId+"@"+Co.toId+"@"+Co.Time+".3gp";
//		Log.v("severPath","serverPath make");
//		Co.fos=new FileOutputStream(Co.serverPath);
//		Log.v("fos","ready to write fos");
//		Co.fos.write(fByte,0,fByte.length);
//		Log.v("fos","write ok");
//writing the data into fos is means to reduce the work of sever
//when the sever get the fos in the ChatObject.class ,what it need to 
//do is just flush the fos.but the fault of that method is that it make 
//the server harder to ajust the file saving path.for it is defined b4 sent
		Log.i("write Co","Write Co finish");
	}
	//because we now use the JSON format to pack the data.
	//so we need to Convert the ChatObject Data into Json format
	String jsonMessage=chatObjectToJson();
	BufferedOutputStream bfo=new BufferedOutputStream(sk.getOutputStream());
	bfo.write(jsonMessage.getBytes());
	bfo.flush();
	bfo.close();
	
	
	
//	ObjectOutputStream Objo=new ObjectOutputStream(sk.getOutputStream());
//	Objo.writeObject(Co);
//	Log.v("flush","ready to flush");
//	Objo.flush();
	Log.i("flush Co","Flush Co finish");
	//following is to receive confirm msg from server to check wether the data 
	//is successfully sent
	InputStream ins;
	Log.v("InputStream","ready to get Inputstream");
	byte inByte[]=new byte["2".getBytes().length];
	ins=sk.getInputStream();
	DataInputStream din=new DataInputStream(ins);
	byte severRespone=din.readByte();
	
	switch(severRespone)
	{
	case 1:
		String str_Delete="delete from UNSEND where SEND_OUT_TIME='"+Co.Time+"';";
		GlobalVar.sb.execSQL(str_Delete);
		break;
	}
	
	/*
	Log.v("getInputStream","got inputStream");
	ins.read(inByte,0,inByte.length);
	String Respone=new String(inByte,"UTF-8");
	Log.v("Respone","Respone"+" "+Respone);
	

	if(Respone.equals("2"))
	{
		Log.v("Respone","got Respone==1");
	//if the respone from sever is 1,means it is sent successfully,
	//we can delete the 
	//data in table
//	String str_Delete="delete from UNSEND where SEND_OUT_TIME='"+Co.Time+"';";
//	Global.sb.execSQL(str_Delete);
	}
	replace this code by switch-case structure
	*/
//	sk.close(); Socket 鏇存敼涓洪暱杩炴帴锛屼笉鐢ㄥ叧闂�
}
/*
}catch(Exception e){e.printStackTrace();};
}else{
	try{Thread.sleep(10000);}catch(Exception e){e.printStackTrace();Log.v("catch",e.toString());};} 
// this.start();    
     	}
	}
	
	
	public String chatObjectToJson() throws JSONException{
		JSONArray Message = null;
		JSONObject Jo = null;
		
		Jo.put("Sender", Co.fromId);
		Jo.put("Receiver",Co.toId);
		Jo.put("RecordTime", Co.Time);
		Jo.put("Content", Co.Content);
		Jo.put("MessageType", Co.msgType);
		Jo.put("Duration",Co.Duration);
		Jo.put("FileSize", Co.fileLong);
		Jo.put("fileByte",Co.fileByte);
		Message.put(Jo);		
	    String msg=Message.toString();
		Log.v("JSONArry Message",msg);
		return(msg);
		
		
	}
	
	


}
*/

//代码变更，此类不用,暂且封印