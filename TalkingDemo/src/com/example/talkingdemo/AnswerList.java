package com.example.talkingdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.talkingdemo.ui.AnswerListAdapter;

import DataSaveObject.Answer;
import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AnswerList extends Activity
{
	private TextView tvTitle;
	private TextView tvQuestionContent;
	private Button btForward;
	private Button btNeAnswer;
	private Button btAddAnswer;
	private ListView lvAnswerList;
	private AnswerListAdapter answerListAdapter;
	public static  ArrayList<Answer> answerArray=new ArrayList<Answer>();
	private static Answer answer;
	private Boolean expandFlag=false;
	private String answerListURL="http://"+GlobalVar.hostURL+"server_app/AnswerListRequest";
    private String questionContentURL="http://"+GlobalVar.hostURL+"server_app/QuestionContentRequest";		
	private Intent preActIntent;
	private Bundle preIntentBdl;
//	public static String questionTitle;
	public static String qContentFull="";///q for question.
	public static String qContentAbbr;
	public static String questionTitle;
	
	
	@Override
	public void onCreate(Bundle S)
	{   super.onCreate(S);
		setContentView(R.layout.answer_list);
	    findViewById();
        preActIntent=getIntent();
        preIntentBdl=preActIntent.getExtras();
//      questionTitle=preIntentBdl.getString("questionTitle");
//      questionContent=preIntentBdl.getString("questionContent");
        qContentAbbr=preIntentBdl.getString("contentAbbr");
        questionTitle=preIntentBdl.getString("questionTitle");
        tvQuestionContent.setText(qContentAbbr);
        tvTitle.setText(questionTitle);
       
        boolean abbrFlag=preIntentBdl.getBoolean("abbrFlag");
        if(abbrFlag==true)
        {downloadQuestionContent();}	
        //if the quesiton content is abbreviated,,download the full content;
        downloadAnswerList();
        
	    
//	    Log.v("","al test "+fromIntentBdl.getLong("questionId"));
	    

		answerListAdapter=new AnswerListAdapter(this,answerArray);
	    lvAnswerList.setAdapter(answerListAdapter);
	    answerListAdapter.notifyDataSetChanged();   
	    	    
	    if(expandFlag==false)
	    { tvQuestionContent.setEllipsize(TruncateAt.valueOf("END"));
	      tvQuestionContent.setSingleLine(true);
	    }
	    
	      tvQuestionContent.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	    	public void onClick(View v)
	    	{
	    		if(expandFlag==false)
	    		{tvQuestionContent.setEllipsize(TruncateAt.valueOf("END"));
	    		 expandFlag=true;
	    		 tvQuestionContent.setSingleLine(expandFlag);
	    		}else
	    		{tvQuestionContent.setEllipsize(null);
	    		 expandFlag=false;
	    		 tvQuestionContent.setSingleLine(expandFlag);
	    		}
	    		
	    	}
	    	
	    	
	    }
	    );
	    
	    btAddAnswer.setOnClickListener(new OnClickListener()
	    {   @Override
			public void onClick(View arg0) 
	        {	// TODO Auto-genrated method stub
			        	    
//	    	    Long quesitonId=b.getLong("questionId");
	    	    Intent intent=new Intent(AnswerList.this,AddAnswer.class);
				intent.putExtras(preIntentBdl);
	    	    startActivity(intent);
			}
	    	}
	      );
	    }
	
	public void downloadQuestionContent()
	{
		//String questionId=""+b.getLong("questionId");
	   String sendQuestionId=""+preIntentBdl.getLong("questionId");
	   Map<String,String> params=new HashMap<String,String>();
	   
	   params.put("questionId",sendQuestionId);
	   String sendData=GlobalVar.packageSentData(params);
	   QuestionContentHd questionContentHd=new QuestionContentHd();
	   SendAndGet sendAndGet=new SendAndGet(sendData, questionContentURL, questionContentHd);
	   sendAndGet.start();
			
	}
	
	public void downloadAnswerList()
	{

	String questionId=""+preIntentBdl.getLong("questionId");

   Map<String,String> params=new HashMap<String,String>();
   params.put("questionId",questionId);
   String sendData=GlobalVar.packageSentData(params);
   AnswerListHd answerListHd=new AnswerListHd();
   SendAndGet sendAndGet=new SendAndGet(sendData, answerListURL, answerListHd);
   sendAndGet.start();
	}
	
	public class QuestionContentHd extends Handler
	{@Override
		public void handleMessage(Message msg)
		{Bundle b=msg.getData();
		 Log.v("","qcHd "+b.getString("Reply"));
		 String Reply=b.getString("Reply");
		 if(!(Reply.equals("2")))
		 {   qContentFull=Reply;//save teh reply into staic string qContentFull,
		                       //for the next activity to use it...
			 tvQuestionContent.setText(Reply);}
		 }
	}
	
	public class AnswerListHd extends Handler
	{@Override
		public void handleMessage(Message msg)
	    {Bundle b=msg.getData();
	     String Reply=b.getString("Reply");
	     Log.v("","alhd "+b.getString("Reply"));
         JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(Reply);
			answerArray.clear();
		    for(int i=0;i<jsonArray.length();i++)
         {	JSONObject jo=(JSONObject)jsonArray.getJSONObject(i);
		    Answer answer=new Answer();
		    answer.Abbr=jo.getString("contentAbbr");
//		    answer.Content=jo.getString("contentAbbr");
		    answer.ansContentAbbrFlag=Boolean.valueOf(jo.getString("abbrFlag"));
		    answer.itselfId=Long.valueOf(jo.getString("answerId"));
		    answer.quesitonId=Long.valueOf(jo.getString("questionId"));
            answer.questionTitle=jo.getString("questionTitle");
		    answer.userId=Long.valueOf(jo.getString("fromId"));
            answer.userName=jo.getString("fromUserName");
            answerArray.add(answer);
         }
		    
	    }catch (JSONException e) 
               {e.printStackTrace();}
		 answerListAdapter.notifyDataSetChanged();      
	    }
		
	    }
	
	public void findViewById()
	{
		tvQuestionContent=(TextView)findViewById(R.id.questionContent_ASL);
	    tvTitle=(TextView)findViewById(R.id.questionTitle_ASL);
		lvAnswerList=(ListView)findViewById(R.id.answerList_ASL);
        btAddAnswer=(Button)findViewById(R.id.addAnswer_ASL);
		
	}
}
    

