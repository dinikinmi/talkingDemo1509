package com.example.talkingdemo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.start.Initialize;

import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Regist extends Activity
{
	public Button bt_Submit;
	public Button bt_goToLogin;
	
	public EditText et_Nickname;
	public EditText et_Password_1;
	public EditText et_Password_2;
	public  String registURL="http://"+GlobalVar.hostURL+"server_app/RegistReceive";
	public String tempUserId;
	public String userName;
    @Override 
    
   public void onCreate(Bundle b)
    {
	 super.onCreate(b);
	 setContentView(R.layout.regist_xml);
	 bt_Submit=(Button)findViewById(R.id.bt_submit_Reg);
	 bt_goToLogin=(Button)findViewById(R.id.bt_goLogin_Reg);
	 et_Password_1=(EditText)findViewById(R.id.et_password_1_Reg);
	 et_Password_2=(EditText)findViewById(R.id.et_password_2_Reg);
	 et_Nickname=(EditText)findViewById(R.id.et_userName_Reg);
	 bt_Submit.setOnClickListener(new SubmitOnClick());
	 bt_goToLogin.setOnClickListener(new GoToLoginOnClick());
	 bt_goToLogin=(Button)findViewById(R.id.bt_goLogin_Reg);
	 
    }
	 
    public class SubmitOnClick implements OnClickListener
    {
   	@Override
   	public void onClick(View arg0)
   	{
   		String pswOne=et_Password_1.getText().toString();
   		 String pswTwo=et_Password_2.getText().toString();
   		 if(pswOne.equals(pswTwo))
   		 {userName=et_Nickname.getText().toString();
   		  tempUserId=randomIdGenerator(); 
   		  Map<String,String> params=new HashMap<String,String>();
   			
   		  params.put("userName", userName);
   		  params.put("password",pswOne);
   		  params.put("userId",tempUserId);
   		  String sendData=GlobalVar.packageSentData(params);
   		  RegistHandler regHandler=new RegistHandler();
   		  SendAndGet sendAndGet=new SendAndGet(sendData, registURL, regHandler);
   		  sendAndGet.start();	
   		  bt_Submit.setEnabled(false);
   		  bt_Submit.setText("Registing ,,,,,,,");
   	    }
   	}
     }
    
    public class RegistHandler extends Handler
    {@Override
 	 public void handleMessage(Message msg)
         { Bundle b=msg.getData();
           String Reply=b.getString("Reply");
           
           if(Reply.equals("18028628619"))
           {//
        	//toast.makeText(this,"UserName Already Exists",10000);   
        	Toast.makeText(Regist.this, "Username Already Exists", Toast.LENGTH_LONG).show();
            bt_Submit.setText("submit");
            bt_Submit.setEnabled(true);
           }	
           
           if(Reply.equals("462323679"))
           { Toast.makeText(Regist.this, "Connection Error", Toast.LENGTH_LONG).show();
        	   bt_Submit.setText("submit");
               bt_Submit.setEnabled(true);
           }
           if(Reply.equals("330346275"))	   
           {Toast.makeText(Regist.this, "Regist Success", Toast.LENGTH_LONG).show();
           bt_Submit.setText("submit");
           bt_Submit.setEnabled(true);
           GlobalVar.myId=Long.valueOf(tempUserId);
           GlobalVar.myName=userName;
           Intent intent=new Intent(Regist.this,Initialize.class);
           startActivity(intent);
           }
           bt_Submit.setText("submit");
           bt_Submit.setEnabled(true);
           
         }
    	
         }
   

 	public class GoToLoginOnClick implements OnClickListener
 	{    @Override
 		public void onClick(View v)
     	{Intent intent=new Intent(Regist.this,MainActivity.class);
     		startActivity(intent);
		}
			
 	}	 
       
 	public String randomIdGenerator()
    {  StringBuffer strB=new StringBuffer();	 
   	 Random r=new Random();   
       for(int i=0;i<7;i++)
   	 {int a=r.nextInt(10);
   	  strB.append(""+a);
   	  }
     	return strB.toString();
     }
    
 	
 	
 	
 		
 	


}	

 


 
    



 
 
 
 


 
