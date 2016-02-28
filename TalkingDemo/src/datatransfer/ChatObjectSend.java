package datatransfer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.example.talkingdemo.TalkActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import Global.ChatObject;
import Global.GlobalVar;
import Global.MyAPP;

public class ChatObjectSend extends Thread 
{
	private ChatObject chatObject;
	private String URL_Link;
	private HttpURLConnection conn;
	
	
	public ChatObjectSend(ChatObject chatObject,String URL)
	{
		this.chatObject=chatObject;
	    this.URL_Link=URL;
	    
	}
	

	@Override
	public void run()
	   {boolean checkSendQute=checkSendQute(chatObject);
	    if(checkSendQute==true)
	    { sendData(chatObject);
	    }
		
	 }

//put runnable into new thread lol


private void sendData(ChatObject chatObject)
{          /*
 		  String Content=URLEncoder.encode(chatObject.Content,"utf-8");
 		  String fromId=URLEncoder.encode(""+chatObject.fromId,"utf-8");
 		  String fromUserName=URLEncoder.encode(GlobalVar.myName,"uft-8");
 		  String msgId=URLEncoder.encode(""+chatObject.msgId,"utf-8");
 		  String msgType=URLEncoder.encode(""+chatObject.msgType,"utf-8");
 		  String toId=URLEncoder.encode(""+GlobalVar.nowTalkingWithId,"uft-8");
 		  */		  
        Map<String,String> params = new HashMap<String,String>();
           params.put("Content",chatObject.Content);
           params.put("fromId",""+chatObject.fromId);
           params.put("fromUserName",chatObject.fromUserName);
           params.put("msgId",""+chatObject.msgId);
           params.put("msgType",""+chatObject.msgType);
           params.put("toId",""+chatObject.toId);
           
           String result;
           result=submitPostData(params, "utf-8");
           
           if(result.equals("2"))
           {//if an exception occurs at server
        	deleteInSendQueue();
//        	MyAPP myAPP=(MyAPP)getApplication();
            updateStatusUI("0");
           }
            if(result.equals("1"))
            {String deleteColumnInUnsend="delete from UNSEND where msgId='"+chatObject.msgId+"';";
             String updateStatusInUserTable="update user"+chatObject.toId+" set Status=1 where msgId='"+chatObject.msgId+"';"; 
            
             deleteInSendQueue();
             
             GlobalVar.sb.execSQL(deleteColumnInUnsend);
             GlobalVar.sb.execSQL(updateStatusInUserTable);
             updateStatusUI("1");
            }
            
            if(result.equals("18028628619"))
             {//if client found that the user talking with has already deleted me from contactorlist            	
               updateStatusUI("18028628619");
             }
            	
           
           
         //Toast.makeText(null, result, TRIM_MEMORY_BACKGROUND).show();
 		   Log.v("123",result);
	     
}

public  String submitPostData(Map<String, String> params, String encode) {

byte[] data = getRequestData(params, encode).toString().getBytes();
//getRequesData return a strinBuffer  
try { 
String httpUrl;
httpUrl="http://"+GlobalVar.hostURL+"server_app/ChatReceive";
//must remember to add http:// infront,or an NullpointException would occur
//Toast.makeText(null, httpUrl, TRIM_MEMORY_BACKGROUND).show();
URL url=null;
try{url=new URL(httpUrl);
  }catch(MalformedURLException e){};
//ge zhong set;          	    
  HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
  httpURLConnection.setConnectTimeout(3000);
  httpURLConnection.setDoInput(true);                  
  httpURLConnection.setDoOutput(true);                 
  httpURLConnection.setRequestMethod("POST"); 
  httpURLConnection.setUseCaches(false);
  httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
  httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));

//and write byte[]data into
OutputStream outputStream = httpURLConnection.getOutputStream();
outputStream.write(data);

int response = httpURLConnection.getResponseCode(); 
if(response == HttpURLConnection.HTTP_OK) 
{
 InputStream inptStream = httpURLConnection.getInputStream();
 return dealResponseResult(inptStream); 
}
} catch (Exception e) {
e.printStackTrace();
deleteInSendQueue();
return "an exception occurd";
}
return "";
}
private StringBuffer getRequestData(Map<String, String> params, String encode) 
{
//encode=utf-8    
StringBuffer stringBuffer = new StringBuffer();
   try {
       for(Map.Entry<String, String> entry : params.entrySet()) {
          stringBuffer.append(entry.getKey())
                      .append("=")
                      .append(URLEncoder.encode(entry.getValue(), encode))
                      .append("&");
      }
      stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //ɾ������һ��"&"
  } catch (Exception e) 
  {e.printStackTrace();}
  return stringBuffer;
}
private  String dealResponseResult(InputStream inputStream) 
{
String resultData = null;     
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
byte[] data = new byte[1024];
int len = 0;
try {
while((len = inputStream.read(data)) != -1) 
{byteArrayOutputStream.write(data, 0, len);}
} catch (IOException e) 
{e.printStackTrace();}

resultData = new String(byteArrayOutputStream.toByteArray());    
return resultData;}

private void deleteObjInQute(ChatObject chatObject)
{
	 for(int i=0;i<GlobalVar.sendQueue.size();i++)
	 {if(chatObject.msgId==GlobalVar.sendQueue.get(i))
		 {GlobalVar.sendQueue.remove(i);
		  i=GlobalVar.sendQueue.size()+1;
		 }
	 }
}
private boolean checkSendQute(ChatObject chatObject)
{   for(int i=0;i<GlobalVar.sendQueue.size();i++)
     {if(chatObject.msgId==GlobalVar.sendQueue.get(i))
        return false;
     }   
     String Update="update UNSEND set Status=2 where msgId="+chatObject.msgId;
     GlobalVar.sb.execSQL(Update);
     GlobalVar.sendQueue.add(chatObject.msgId);
     return true;
 }
private void deleteInSendQueue()
{//delete the data in sendQue
	for(int i=0;i<GlobalVar.sendQueue.size();i++)
	{
      if(chatObject.msgId==GlobalVar.sendQueue.get(i))
	   {GlobalVar.sendQueue.remove(i);
	   }
	}
	String updateUNSEND="update UNSEND set Status=0 where msgId="+chatObject.msgId;// 0 for fail-pos
	String updateChatTable="update user"+chatObject.toId+" set Status=0 where msgId="+chatObject.msgId; 
	GlobalVar.sb.execSQL(updateUNSEND);
	GlobalVar.sb.execSQL(updateChatTable);
   }
private void updateStatusUI(String Status)
    {
	Message toTalkActMsg=TalkActivity.updateStatusHd.obtainMessage() ;     	
    Bundle dataBundle=new Bundle();
    dataBundle.putLong("toId",chatObject.toId);
	dataBundle.putString("msgId",chatObject.msgId);
	dataBundle.putString("Status",Status);
    toTalkActMsg.setData(dataBundle);
    toTalkActMsg.sendToTarget();
	}

}


