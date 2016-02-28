package BackGround;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class DownLoadMessage extends Thread {
     Socket sk;
     byte readByte[];
     public String Json;
	public void DownLoadMessage(Socket sk){
		this.sk=sk;
	}
   	
	@Override
	public	void run(){
		
		try {
			BufferedInputStream bis=new  BufferedInputStream(sk.getInputStream());
			bis.read(readByte);
			Json=new String(readByte,"UTF-8");
			JSONArray Jo=new JSONArray(Json);
			//把json数据解析出来后存入相应位置
			} catch (Exception e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}
