package com.example.talkingdemo;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import Global.GlobalVar;
import Global.SendData;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PostNewQuestion extends Activity{
	private  Button bt_SubmitQuestion;
	public EditText et_QuestionTitle;
	private EditText et_QuestionContent;
	public SendData sendData;
	
public PnqHandler pnqHandler=new PnqHandler();
	
     @Override 
	public void onCreate(Bundle b)
     {super.onCreate(b);
      setContentView(R.layout.post_new_question);
      
      bt_SubmitQuestion=(Button)findViewById(R.id.bt_submitQuestion_PNQ);
      et_QuestionTitle=(EditText)findViewById(R.id.et_questionTitle_PNQ);
      et_QuestionContent=(EditText)findViewById(R.id.et_questionContent_PNQ);
      
      bt_SubmitQuestion.setOnClickListener(new OnClickListener()
      {
    	  @Override
    	  public void onClick(View v)
        {    		  
    	 String Title=et_QuestionTitle.getText().toString();
         String Content=et_QuestionContent.getText().toString();
         Map<String,String> paramsMap=new HashMap<String,String>();
         
         long questionId=0;
         long  sendTime=System.currentTimeMillis();
         questionId=sendTime+Title.length()+Content.length();
             
         paramsMap.put("Title",Title);
         paramsMap.put("Content",Content);
         paramsMap.put("fromId",""+GlobalVar.myId);
         paramsMap.put("fromUserName",GlobalVar.myName);
         paramsMap.put("questionId",""+questionId);
         
         String sentData=GlobalVar.packageSentData(paramsMap);
         String URL="http://"+GlobalVar.hostURL+"server_app"+"/"+"QuestionReceive" ;
         sendData=new SendData(sentData,URL, pnqHandler);
         sendData.start();
         bt_SubmitQuestion.setEnabled(false);
         bt_SubmitQuestion.setText("   Sending   ");
         }
     } );
         
      
      
     }
     
     public class PnqHandler extends Handler
     { 
    	@Override
    	 public void handleMessage(Message msg)
    	{ Bundle b=msg.getData();
    		String Reply=b.getString("Reply");
    		if(Reply.equals("1"))
    		{bt_SubmitQuestion.setEnabled(true);
    		 bt_SubmitQuestion.setText("Submit");
       		}
    		if(Reply.equals("462323679"))
    		{
    		}
    	}
     }
	
	  
    
}
