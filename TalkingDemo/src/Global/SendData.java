package Global;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SendData extends Thread
{	public String sentString ;
    public String hostURL;
    public Handler handler;
    public URL url;
    public boolean successFlag=true;
    
	 public SendData(String sentString,String URL,Handler handler)
	 {this.sentString=sentString;
	  this.hostURL=URL;
	  this.handler=handler;
	 }
	 
     	@Override
		public void run()
		{byte data[]=sentString.getBytes();
		try {        
			url=new URL(hostURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000); //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");  //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data); 
            //wait for the sever to reply
            
            InputStream inputStream=httpURLConnection.getInputStream();
            InputStreamReader Reader=new InputStreamReader(inputStream,"utf-8");
            BufferedReader buf=new BufferedReader(Reader);
            String Reply=buf.readLine();
            Log.v("","send data test " + Reply);
            if(Reply.equals("2"))
            {successFlag=false;}
         // DataInputStream dataIns=new DataInputStream(inputStream);
        //  int Reply=dataIns.readInt();
            //if sever handle successfully,return 1,else return 2;
            /*
            Message msg=handler.obtainMessage();         
            Bundle b=new Bundle();
            b.putString("Reply",Reply);
            msg.sendToTarget();
            */
		   }
		    catch(MalformedURLException e)
		    {e.printStackTrace();
		     successFlag=false; 
		    }
      		catch(IOException e)
	         {  successFlag=false;
      		 }

		     catch(Exception e)
		   {successFlag=false;}
		     		   //if an exception occrud ,return 2 ;
		  
		
		   if(successFlag==false)
		   {  Message msg=handler.obtainMessage();         
           Bundle b=new Bundle();
           b.putString("Reply","2");
           msg.setData(b);
           msg.sendToTarget();
		   }
		   if(successFlag==true)
		   {Message msg=handler.obtainMessage();
		    Bundle b=new Bundle();
			b.putString("Reply","1");
			msg.setData(b);
			msg.sendToTarget();
		   }
      }
     	
     	
     	
     	
     	
}