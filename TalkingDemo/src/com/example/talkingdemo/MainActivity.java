package com.example.talkingdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.start.Initialize;

import utils.syncSocketTask;

import Global.GlobalVar;
import Global.SendAndGet;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	public static String userName;
	public static String Password;
	private String loginURL="http://"+GlobalVar.hostURL+"server_app/Login";
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        final EditText Usn=(EditText)findViewById(R.id.username_edit);
        final EditText Psw=(EditText)findViewById(R.id.password_edit);
               
		((Button)findViewById(R.id.signin_button)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				//娴嬭瘯锛岀洿鎺ヨ烦鍏ヤ富鐣岄潰
//				Intent intent=new Intent(MainActivity.this,MainWinActivity.class);
//				startActivity(intent);
				userName=Usn.getText().toString();
                Password=Psw.getText().toString();
//				syncSocketTask synctask=new syncSocketTask(MainActivity.this,userName,passWord);
//				synctask.execute("182.254.210.160","10000","123","111"); 
				Map<String,String> params=new HashMap<String,String>();
				params.put("userName",userName);
				params.put("Password",Password);
										
				String sendData=GlobalVar.packageSentData(params);
                LoginHandler loginHandler=new LoginHandler();
                SendAndGet sendAndGet=new SendAndGet(sendData,loginURL,loginHandler);
                sendAndGet.start();
				               
				//鎴戝湪鍚庨潰鍔犱簡涓�釜String 鍙傛暟绔熺劧閮芥病鏈夋姤閿欙紝鍙兘涓庡弬鏁版槸 娉涘瀷 鏈夊叧
				//鍙傛暟鎴栬鏄� (string...pram)
				/*
				 * 鍦ㄤ娇鐢ㄧ殑鏃跺�锛屾湁鍑犵偣闇�鏍煎娉ㄦ剰锛�
1.寮傛浠诲姟鐨勫疄渚嬪繀椤诲湪UI绾跨▼涓垱寤恒�
2.execute(Params... params)鏂规硶蹇呴』鍦║I绾跨▼涓皟鐢ㄣ�
3.涓嶈鎵嬪姩璋冪敤onPreExecute()锛宒oInBackground(Params... params)锛宱nProgressUpdate(Progress... values)锛宱nPostExecute(Result result)杩欏嚑涓柟娉曘�
4.涓嶈兘鍦╠oInBackground(Params... params)涓洿鏀筓I缁勪欢鐨勪俊鎭�
5.涓�釜浠诲姟瀹炰緥鍙兘鎵ц涓�锛屽鏋滄墽琛岀浜屾灏嗕細鎶涘嚭寮傚父銆�
				 */
				
				Log.v("execute","execute");
			}			
		});
		
		
		((Button)findViewById(R.id.bt_goRegist_Ma)).setOnClickListener(new OnClickListener()
		{@Override
			public void onClick(View v)
		     {Intent intent= new Intent(MainActivity.this,Regist.class);
			  startActivity(intent);
			 }
		}
			);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public class LoginHandler extends Handler
	{@Override
		public void handleMessage(Message msg)
	    {Bundle b=msg.getData();
	     String Reply=b.getString("Reply");
	    //sever return id if success;
	     if(Reply.equals("null"))
	     {return;}
	     if(Reply.equals(null))
	     {return;}
	     try{
			JSONArray ja=new JSONArray(Reply);
	        JSONObject jo=(JSONObject)ja.get(0);
	        String successFlag=jo.getString("successFlag");
	        if(successFlag.equals("330346275"))
	        {GlobalVar.myId=Long.valueOf(jo.getString("Id"));
	         GlobalVar.myName=userName;
	         Intent intent=new Intent(MainActivity.this,Initialize.class);
	         startActivity(intent);
	        }
	        //should save the username and psw at local as a cookie,
	        //to build a auto_login function
            } catch (JSONException e) 
		    {e.printStackTrace();return;}
	        
	        
	        
	     
	     
	     
	     
	     
	    }
		
	}
	
	
}
