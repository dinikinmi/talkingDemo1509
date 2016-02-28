package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.talkingdemo.MainActivity;
import com.example.talkingdemo.MainWinActivity;


import Global.GlobalVar;
import Global.mySocket;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;



public class syncSocketTask extends AsyncTask<String, Integer, String> {
	
	Context context;
	int id;
	String Usn;
	String pwd;
	String loginmsg;
	public static boolean Login=false;

	
	//seems like the id is not used
	
	
	public syncSocketTask(Context context,String userName,String pwd)
	{
		
		this.context=context;
		this.Usn=userName;
		this.pwd=pwd;
		
		}

	
    @Override  
    protected void onPreExecute() { 
    	
    	//when i press login,it jump to here
    	JSONObject json=new JSONObject();  
        try {
			json.put("userName", Usn);
			json.put("Password", pwd); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
        loginmsg=json.toString();
        Log.v("Sign","Sign"+loginmsg);
    }  

	@Override
	protected String doInBackground(String... params) {
		try {			
			//绗竴娆¤繍琛屾椂锛宱npreExcecute鎵ц瀹屽悗璺冲埌姝わ紝绗簩娆℃寜sign鐨勬椂鍊欏氨涓嶈烦浜嗐�
			//鎴戠寽浼犺繘鏉ョ殑涓や釜鍙傛暟锛屼竴涓槸 Socket鐨処P鍦板潃锛屼竴涓槸 绔彛鍚�
			//浣嗘槸杩欎袱涓弬鏁版槸浣曟椂浼犲叆鏉�杩欎釜 doInBackgroud鐨勬柟娉曟槸鍦ㄥ摢閲岃璋冪敤鐨勫憿锛燂紵
			//浠嶭og 涓婄湅鏉ワ紝搴旇鏄�excecute锛堚�IP鈥濓紝Port锛夋椂浼犲叆鏉ョ殑 
			Log.v("doInBack","do in back ground start");
			Log.v("test 123",params[2]+" "+params[3]);
			
			
			
			final Socket client = new Socket( params[0],Integer.valueOf(params[1]));
			    			     
			//the program block here
				final InputStream in = client.getInputStream();
				final OutputStream out = client.getOutputStream();	
				out.write(loginmsg.getBytes("gb2312"));
				//灏唎nPreExcute鑾峰緱鐨勫瘑鐮乻end缁�鏈嶅姟鍣�
			    Reader reader = new InputStreamReader(in,"gb2312");  
			    //creat a reader to a certain inputStream and set charSet as gb2312
			    //change the byte flow into char flow
			    Log.v("reader","ready to read the stream from server");
			    CharBuffer charBuffer = CharBuffer.allocate(20000);  
			    //use allocate to set the capacity=1024
                StringBuilder serverRespone=new StringBuilder();
                int i;
                while((i=reader.read(charBuffer))!=-1)
                {
                    charBuffer.flip(); 
                	serverRespone.append(charBuffer,0,i);
                	Log.v("Respone","Respone"+charBuffer.position());
                	//if charBuffer is not cleared for everytime reading..
                	//I guess the start point 0 refer to 
                	//the distance from the cursor,but not the distance from 
                	//the beginnin g of charBuffer
                }
               
                //now to check whether the login is success.if sever response "1"
                //that it means success, if not ,login fail
                switch(Integer.valueOf(serverRespone.toString()))
                {
                case 1:
                //if response is 1 ,means it is 	
                this.Login=true;
                break;
                case 2:
                	//if login fail ,do nothing
                	
                	break;
                }
                //Flips this buffer. The limit is set to the current position 
                //and then the position is set to zero.
                //If the mark is defined then it is discarded.
                
                //now convert the cahrBuffer to string,
                //and use a switch-case to judge it
                
                
                
// client.close(); if login success. then we keep using this socket
                
                in.close();
                out.close();
                
                return charBuffer.toString();
            
             } 
		catch (Exception e) {  
                    e.printStackTrace();  
                }
		return null;
	}


    @Override  
    protected void onPostExecute(String result) {  
//        if (result != null) {      	
        	try {      		
        		//JSONObject  dataJson=new JSONObject(result);
            	//JSONObject  dt=dataJson.getJSONObject("detail");
            	//JSONArray dt=dataJson.getJSONArray("detail");
            	//JSONObject info=dt.getJSONObject(i);
        		if(this.Login){		        
				//if login success,Login=ture...go to MainWin
//        		Intent intent = new Intent(context, MainWinActivity.class);
//				intent.putExtra("json"," result");
//				context.startActivity(intent);
        		}else{
        			//show a dialog to tell user Login fail
        			}
        		
			} 
        	//catch (JSONException e) {
			//	e.printStackTrace();
			//} 
        	catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
//        else {  
            //Toast.makeText(MainActivity.this,  
            //        "get image from network error", Toast.LENGTH_SHORT).show();  
//        }

//    }  

	@Override  
    protected void onCancelled() {  
    	
    }  

}
