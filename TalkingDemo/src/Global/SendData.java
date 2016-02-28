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
            httpURLConnection.setConnectTimeout(3000); //�������ӳ�ʱʱ��
            httpURLConnection.setDoInput(true);                  //�����������Ա�ӷ�������ȡ����
            httpURLConnection.setDoOutput(true);                 //����������Ա���������ύ����
            httpURLConnection.setRequestMethod("POST");  //������Post��ʽ�ύ����
            httpURLConnection.setUseCaches(false);               //ʹ��Post��ʽ����ʹ�û���
            //������������������ı�����
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //����������ĳ���
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //�����������������д������
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