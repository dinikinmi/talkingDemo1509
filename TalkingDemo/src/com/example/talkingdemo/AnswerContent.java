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
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerContent extends Activity {
	private TextView tv_questionContent;
	private TextView tv_questionTitle;
	private TextView tv_ansPosterName;
	private String ansPosterName;
	private Long answerPosterId;      
	private TextView tv_ansContent;
	private Button bt_Commence;
	private Button bt_next;
	private Button bt_previous;
	private ImageView iv_ansPosterAva;
	private Intent preActInt;
	private Bundle preIntBdl;
	private String questionTitleStr;
	private String qContentFull;
	private String qContentAbbr;
	private boolean ansConAbbrFlag;
	private String ansConReqURL="http://"+GlobalVar.hostURL+"server_app/AnswerContentRequestHd";
  	private boolean expandFlag=false;
  	public int ansListPos;
	@Override
	public void onCreate(Bundle S)
	{
	super.onCreate(S);
    preActInt=getIntent();
    preIntBdl=preActInt.getExtras();
    ansListPos=preIntBdl.getInt("ansListPos");
    ansConAbbrFlag=AnswerList.answerArray.get(ansListPos).ansContentAbbrFlag;
    ansPosterName=AnswerList.answerArray.get(ansListPos).userName;
    setContentView(R.layout.answer_inside);
	findViewById();
    tv_questionTitle.setText(AnswerList.questionTitle);
    tv_ansPosterName.setText(ansPosterName);
    setQueContent();
	queEllip();
	setNextPreBtEnb(ansListPos);
	setOnClick();
	setAnsContent(); 
	
	}

	
	
	public void setAnsContent()
	{//check whether the content from intent is abbr
		//if yes,download//else ,set directclty
		if(ansConAbbrFlag)
		{downLoadAnsContent();}
		else{tv_ansContent.setText(AnswerList.answerArray.get(ansListPos).Abbr);}
		}
    public void setQueContent()
    {
	if(!AnswerList.qContentFull.equals("")) 
	    {tv_questionContent.setText(AnswerList.qContentFull);		 
	    }else
	    {tv_questionContent.setText(AnswerList.qContentAbbr);}
	 //if there is full answer,set textview's text to be full,else ,put 
	 //abbreviated content into it 
    }
    public void queEllip()
    {expandFlag=false;
   	 if(!expandFlag)
   	 {
   	 tv_questionContent.setEllipsize(TruncateAt.valueOf("END"));
   	 tv_questionContent.setSingleLine(true);
   	 }
    }
    public void downLoadAnsContent()
      {Map<String,String> params =new HashMap<String,String>();
       params.put("answerId",""+AnswerList.answerArray.get(ansListPos).itselfId);
       String sendData=GlobalVar.packageSentData(params);
       AnswerDownloadHd ansDlHd=new AnswerDownloadHd();
       SendAndGet sendAndGet=new SendAndGet(sendData,ansConReqURL,ansDlHd);
       sendAndGet.start();       
        }
    public void findViewById()
    {tv_questionContent=(TextView)findViewById(R.id.AI_questionContent_Tv);
	 tv_ansContent=(TextView)findViewById(R.id.AI_answerContent_Tv);
    tv_questionTitle=(TextView)findViewById(R.id.AI_Title_Tv);
	tv_ansPosterName=(TextView)findViewById(R.id.AI_userName_Tv);
	bt_Commence=(Button)findViewById(R.id.AI_Commence_Bt);
	bt_next=(Button)findViewById(R.id.AI_Next_Bt);
	bt_previous=(Button)findViewById(R.id.AI_Previous_Bt);
    iv_ansPosterAva=(ImageView)findViewById(R.id.AI_userAvatar_Iv);	

    }
    public void setOnClick()
    {tv_questionContent.setOnClickListener(new OnClickListener()
	 {	@Override
		public void onClick(View v)
	 {
		 if(expandFlag==true)
		 { 
	     tv_questionContent.setEllipsize(TruncateAt.valueOf("END"));
	     tv_questionContent.setSingleLine(true);
	     expandFlag=false;
	  	 }else
		 {tv_questionContent.setEllipsize(null);
		  expandFlag=true;
		  tv_questionContent.setSingleLine(false);
	   }
	}
	   }
    );
    
    bt_Commence.setOnClickListener(new OnClickListener()
{	@Override
	public void onClick(View arg0) 
	{Bundle b=new Bundle();
	 b.putInt("ansListPos",ansListPos);
	 Intent intent=new Intent(AnswerContent.this,CommenceList.class);
	 intent.putExtras(b);
	 startActivity(intent);
	}
}
 );
    bt_next.setOnClickListener(new NextPreAnsBtClick());
    bt_previous.setOnClickListener(new NextPreAnsBtClick());
    iv_ansPosterAva.setOnClickListener(new ansPosterAvaClick());
    
    }
    public class AnswerDownloadHd extends Handler
    {public void handleMessage(Message msg)
       {Bundle b=msg.getData();
        String Reply=b.getString("Reply");
        Toast.makeText(AnswerContent.this,Reply,Toast.LENGTH_LONG).show();
        if(Reply.equals("462323679")||Reply.equals("")||Reply==null)
         return;
        else
        {tv_ansContent.setText(Reply);      
        }
        	
       
       }
    	
    }
    public void setNextPreBtEnb(int ansListPos)
    {
   	if((ansListPos+1)==AnswerList.answerArray.size())
      {bt_next.setEnabled(false);}else{bt_next.setEnabled(true);}
     
    if((ansListPos==0))
      {bt_previous.setEnabled(false);}else{bt_previous.setEnabled(true);}
    }
    public class NextPreAnsBtClick implements OnClickListener
    {  int i;
    	@Override
	   public void onClick(View v) 
         
    	{  
    	i=AnswerList.answerArray.size();	
      	 Intent intent=new Intent(AnswerContent.this,AnswerContent.class);
         Bundle b=new Bundle();
    	 if(v==(View)bt_next)
    	    {ansListPos++;}
    	 if(v==(View)bt_previous)
    	 {ansListPos--;}
    	 b.putInt("ansListPos",ansListPos);
    	 intent.putExtras(b);
    	 startActivity(intent);
	    }
    }
    public class ansPosterAvaClick implements OnClickListener
    {    
	@Override
	public void onClick(View arg0) 
	 {Long userId=AnswerList.answerArray.get(ansListPos).userId;
	  String userName=AnswerList.answerArray.get(ansListPos).userName;
	  Bundle b=new Bundle();
	  b.putString("userName", userName);
	  b.putLong("Id",userId);
	  
	  Intent intent=new Intent(AnswerContent.this,UserDetails.class);
	  intent.putExtras(b);
	  startActivity(intent);
	  
	 }
    	
    	
    }
}
