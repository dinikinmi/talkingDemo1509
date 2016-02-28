package com.example.talkingdemo;

import java.util.HashMap;
import java.util.Map;

import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddAnswer extends Activity {
	private Button bt_submitAnswer;
	private EditText et_answerContent;
	private String serverURL="http://"+GlobalVar.hostURL+"server_app/AnswerReceive";
    private Bundle fromIntent;
    private Intent fromPreAct;
	
   @Override
   public void onCreate(Bundle S)
   {
	   super.onCreate(S);
	   setContentView(R.layout.add_answer);
	   bt_submitAnswer=(Button)findViewById(R.id.AA_Submit_Bt);
	   bt_submitAnswer.setOnClickListener(new SubmitClick());
	   et_answerContent=(EditText)findViewById(R.id.AA_answerField_ED);
   }

   private class SubmitClick implements OnClickListener
   {
     @Override
      public void onClick(View arg0) 
      {String Content=et_answerContent.getText().toString();
	   //generate questionId
        Long Time=System.currentTimeMillis();
        Long questionId=getIntent().getExtras().getLong("questionId");      
        Long answerId=questionId+Time+GlobalVar.myId;
      
        fromPreAct=getIntent();
        fromIntent=fromPreAct.getExtras();
        Log.v("","aa bdl test "+fromIntent.getLong("questionId"));
        
        Map<String,String> params=new HashMap<String,String>();
        params.put("questionId",""+fromIntent.getLong("questionId"));
        params.put("questionTitle",fromIntent.getString("questionTitle"));
        params.put("askerId",""+getIntent().getExtras().getLong("AskderId"));
        params.put("answerId",""+answerId);
        params.put("answerContent",Content);
        params.put("fromId",""+GlobalVar.myId);
        params.put("fromUserName",GlobalVar.myName);
    
        String sendData=GlobalVar.packageSentData(params);
        AddAnswerHandler addHand=new AddAnswerHandler();
        SendAndGet sendAndGet=new SendAndGet(sendData,serverURL,addHand);
        sendAndGet.start();
        bt_submitAnswer.setEnabled(false);
        
      }
	   
	   
	   
   }
   
   private class AddAnswerHandler extends Handler
   { @Override 
	   public void handleMessage(Message msg)
       {Bundle b=msg.getData();
	    String Reply=b.getString("Reply");
        if(Reply.equals("1"))
        {	
//        	Toast t=Toast.makeText(null,"success",11);
//          t.show();
        }
        bt_submitAnswer.setEnabled(true); 
        
	    if(Reply.equals("2"))
	     {
//	    	Toast t=Toast.makeText(null,"fails",11);
//          t.show();
         }
       }
	   
	   
	   
   }
}
