package com.example.talkingdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewCommence extends Activity
{  
   private TextView tv_QuestionContent;
   private EditText et_commenceText;
   private boolean expandFlag=false;
   private Intent preActInt;
   private Bundle preIntBdl;
   private int ansListPos;
   private Button bt_submitCommence;
   private String subComURL="http://"+GlobalVar.hostURL+"server_app/CommenceReceiveHd";
   
	@Override
	public void onCreate(Bundle S)
{   super.onCreate(S);
    setContentView(R.layout.add_commence);
    
    getAnsListPos();
    Toast.makeText(NewCommence.this,""+ansListPos,Toast.LENGTH_LONG).show();
    tv_QuestionContent=(TextView)findViewById(R.id.AC_questionContent_Tv);
    et_commenceText=(EditText)findViewById(R.id.AC_commenceField_ED);
    bt_submitCommence=(Button)findViewById(R.id.AC_Submit_Bt);
    expandFlag=false;
    tv_QuestionContent.setSingleLine(true);
    tv_QuestionContent.setEllipsize(TruncateAt.END);
    
     tv_QuestionContent.setOnClickListener(new OnClickListener()
    {   @Override
		public void onClick(View v) 
		{
      	if(!expandFlag)
		   {tv_QuestionContent.setSingleLine(false);
		    tv_QuestionContent.setEllipsize(null);
		    expandFlag=true;
		   }
    	else
		    {tv_QuestionContent.setSingleLine(true);
		     tv_QuestionContent.setEllipsize(null);
		     expandFlag=false;
			}
		 }
     }
    );
      bt_submitCommence.setOnClickListener(new submitCommenceClick());       
}
	
private void getAnsListPos()
    {preActInt=getIntent();
     preIntBdl=preActInt.getExtras();
     ansListPos=preIntBdl.getInt("ansListPos");
    }
	

public class submitCommenceClick implements OnClickListener
{ 
	@Override
	public void onClick(View arg0) 
    {String commenceText=et_commenceText.getText().toString();
	 Map<String,String> parmas=new HashMap<String,String>();
     parmas.put("comPosterId",""+GlobalVar.myId);
     parmas.put("comPosterName",GlobalVar.myName);
     parmas.put("answerId",""+AnswerList.answerArray.get(ansListPos).itselfId);
	 parmas.put("ansPosterId",""+AnswerList.answerArray.get(ansListPos).userId);
	 parmas.put("commenceText",commenceText);
	 parmas.put("ansAbbr",AnswerList.answerArray.get(ansListPos).Abbr);
	 String sendData=GlobalVar.packageSentData(parmas);
	 SubmitCommenceHd subComHd=new SubmitCommenceHd();
	 SendAndGet sendGet=new SendAndGet(sendData,subComURL,subComHd);
	 sendGet.start();
	}
	
}
	
public class SubmitCommenceHd extends Handler
    {public void handleMessage(View msg)
       {
    	
       }
	}

	

}
