package com.example.talkingdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.example.talkingdemo.ui.QuestionLvAdapter;
import com.google.gson.Gson;
import DataSaveObject.Question;
import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class NewQuestionList extends Activity 
{
	public Question question;
	public ListView questionLv;
	public QuestionLvAdapter questionLvAdp;
	public NewQuestionHandler newQuestionHandler;
	public String questionServerLink="http://"+GlobalVar.hostURL+"server_app/QuestionListRequest";
	
	
	@Override
protected void onCreate(Bundle S)
	{   super.onCreate(S);
		setContentView(R.layout.question_list_new);
		questionLv=(ListView)findViewById(R.id.newQuestionList);
		GlobalVar.questionArray.clear();
		/* Test Code
		for(int i=0;i<10;i++)
		{question=new Question();
		 question.Label_1="a";
		 question.Label_2="b";
		 question.Label_3="c";
		 question.Label_4="d";
		 question.Credit=i+10;
		 question.Title=""+i+"T";
		 question.Abbreviation=""+i+"R";
		 GlobalVar.questionArray.add(question);
		 }
		 */
		
		questionLvAdp=new QuestionLvAdapter(this);
		questionLv.setAdapter(questionLvAdp);
	    questionLvAdp.notifyDataSetChanged();
	    
	    Map<String,String> params=new HashMap<String,String>();
	    params.put("Direction","0");
	    String dataToSend=GlobalVar.packageSentData(params);
	    newQuestionHandler=new NewQuestionHandler();
	    SendAndGet sendAndGet=new SendAndGet(dataToSend, questionServerLink,newQuestionHandler);
	    sendAndGet.start();
	
	    questionLv.setOnScrollListener(new OnScrollListener()
	    {  @Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) 
	        {
				
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState)
			{  Log.v("","bottom "+questionLv.getBottom());
		       Log.v("","last visible "+questionLv.getLastVisiblePosition());
				switch (scrollState) 
				{
				case OnScrollListener.SCROLL_STATE_IDLE:
				//means user is  flinging screen
					if(questionLv.getFirstVisiblePosition() == 0 && questionLv.getLastVisiblePosition() == (questionLv.getCount()-1) )
					{Log.v("top","is the top");
			         listViewTopListener();	    
					}else
					{
					if (questionLv.getFirstVisiblePosition() == 0) 
					{	Log.v("top","is the top");
					  listViewTopListener();  
					}
					 if (questionLv.getLastVisiblePosition() == (questionLv.getCount()-1)) 
					{   Log.v("top","is the bottom");
					   listViewBottomListener();   
					}
					}
				}
			}
	    	 }
	   	);
	   	}
	
	public class NewQuestionHandler extends Handler
	{ @Override
		public void handleMessage(Message msg)
	   {Bundle b=msg.getData();
	   String questionJSONStr=b.getString("Reply");
	   if(!(questionJSONStr.equals("null")) && !(questionJSONStr.equals("fail")))
	   {
	   questionListUpdate(questionJSONStr,false);   
	   }else
	   {Log.v("","no data availabe");}
	   /* Test Code
		Log.v("","vv cc"+questionJSONStr);
	
		JSONArray ja=JSONArray.fromObject(questionJSONStr) ;
						
		for(int i=0;i<ja.size();i++)
		{Question question=new Question();
		 JSONObject jo=ja.getJSONObject(i);
		 question.fromId=Long.valueOf(jo.getString("fromId"));
		 question.fromUserName=jo.getString("fromUserName");
		 question.Abbreviation=jo.getString("contentAbbr");
		 question.abbrFlag=Integer.valueOf(jo.getString("abbrFlag"));
		 question.Credit=Integer.valueOf(jo.getString("Credit"));
		 GlobalVar.questionArray.add(question);	
		}
		 questionLvAdp.notifyDataSetChanged();
		*/
	   }
		
	
	
	}

	public class TopQuestionHandler extends Handler
	{  @Override
	   public void handleMessage(Message msg)
	   {Bundle b=msg.getData();
	   String questionJSONStr=b.getString("Reply");
	   if( !(questionJSONStr.equals("null")) && !(questionJSONStr.equals("fail")))
	   {   int l=questionJSONStr.length();
		   
		   questionListUpdate(questionJSONStr,true); }  
	}
	}

	public class BottomQuestionHandler extends Handler
	{  @Override
	   public void handleMessage(Message msg)
	   {Bundle b=msg.getData();
	   String questionJSONStr=b.getString("Reply");
	   
	   if(!(questionJSONStr.equals("null")) && !(questionJSONStr.equals("fail")))
	   {  Log.v("",""+questionJSONStr.length());
	      String s=questionJSONStr+"1234";
	      Log.v("","1234 + "+s );
		   questionListUpdate(questionJSONStr,false);}  
	}
	}
	public void listViewTopListener()
	{ questionLv.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
    //get the rowId of this item
    int rowId=GlobalVar.questionArray.get(0).rowId;
    Map<String,String> params=new HashMap<String,String>();
    params.put("Offset",""+rowId);
    params.put("Direction","1");
    String sendData=GlobalVar.packageSentData(params);
    TopQuestionHandler topHandler=new TopQuestionHandler();
    SendAndGet sendAndGet=new SendAndGet(sendData,questionServerLink,topHandler);
    sendAndGet.start();
		
	}
    public void listViewBottomListener()
    {Log.v("top","is the bottom");
    questionLv.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
    //get the rowId of this item
    int rowId=GlobalVar.questionArray.get((questionLv.getCount()-1)).rowId;
    Map<String,String> params=new HashMap<String,String>();
    params.put("Offset",""+rowId);
    params.put("Direction","2");
    String sendData=GlobalVar.packageSentData(params);
    BottomQuestionHandler bottomHandler=new BottomQuestionHandler();
    SendAndGet sendAndGet=new SendAndGet(sendData,questionServerLink,bottomHandler);
    sendAndGet.start();
    }
   	public void questionListUpdate(String questionJSONStr,boolean addToTop)
	{ JSONTokener jt=new JSONTokener(questionJSONStr);
		   try
		   {
			 JSONArray ja=(JSONArray)jt.nextValue();
	     	 for(int i=0;i<ja.length();i++)
	     	 {JSONObject jo=(JSONObject) ja.get(i);
	     	  Question q=new Question();
	     	  q.Abbreviation=jo.getString("contentAbbr");
	     	  q.abbrFlag=Boolean.valueOf((jo.getString("abbrFlag")));
	     	  q.Credit=Integer.valueOf(jo.getString("Credit"));
	     	  q.fromId=Long.valueOf(jo.getString("fromId"));
	     	  q.fromUserName=jo.getString("fromUserName");
	     	  q.questionId=Long.valueOf(jo.getString("questionId"));
	     	  q.Sort=jo.getString("Sort");
	     	  q.Title=jo.getString("Title");
	     	  q.rowId=Integer.valueOf(jo.getString("rowId"));
	     	 
	     	  if(addToTop==true)//true for top
	     	  {GlobalVar.questionArray.add(0,q);}
	     	  if(addToTop==false)
	     	  {GlobalVar.questionArray.add(q);}
	     }
	     	 questionLvAdp.notifyDataSetChanged();
	       } catch (JSONException e) 
		    {e.printStackTrace();}
    }
    }	
	
	
