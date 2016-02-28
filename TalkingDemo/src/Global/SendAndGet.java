package Global;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SendAndGet extends Thread
{	public String sentString ;
    public String hostURL;
    public Handler handler;
    public URL url;
    public boolean successFlag=true;
    
	 public SendAndGet(String sentString,String URL,Handler handler)
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
            Reader reader=new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferReader=new BufferedReader(reader);
            String Line="";
            StringBuffer stringBuffer=new StringBuffer();
            while((Line=bufferReader.readLine())!=null)
            	{stringBuffer.append(Line);
            	};
            	
           String Reply=stringBuffer.toString();
             Log.v("","sag  "+Reply);  
            //if sever handle successfully,return 1,else return 2;
          if(!(Reply.equals("null")))
          {
             Message msg=handler.obtainMessage();         
            Bundle b=new Bundle();
            b.putString("Reply",Reply);
            msg.setData(b);
            msg.sendToTarget();
          }
            }
		    catch(MalformedURLException e)
		    {e.printStackTrace();
		     successFlag=false; 
		    }
		    catch(IOException e)
		    {successFlag=false;
		     e.printStackTrace(); }
			catch(Exception e)
		   {successFlag=false;}
		   //if an exception occrud ,return 2 ;
		
		  if(successFlag==false)
		   {Message msg=handler.obtainMessage();         
           Bundle b=new Bundle();
           b.putString("Reply","462323679");
           msg.setData(b);
           msg.sendToTarget();
		   }
      }
     	
     	
     	
     	
     	
}