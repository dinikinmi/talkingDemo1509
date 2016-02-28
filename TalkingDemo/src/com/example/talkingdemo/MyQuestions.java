package com.example.talkingdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.talkingdemo.ui.MyQuestionLvAdapter;
import com.example.talkingdemo.ui.QuestionLvAdapter;

import DataSaveObject.Question;
import Global.GlobalVar;
import Global.SendAndGet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class MyQuestions extends Activity {

	public static Question question;
	public ListView questionLv;
	public MyQuestionLvAdapter myQuestionLvAdapter;
	public static ArrayList<Question> myQuestionArray=new ArrayList<Question>();
	private String myQHURL="http://"+GlobalVar.hostURL+"server_app/MyQuestionRequest";
	
	@Override
protected void onCreate(Bundle S)
	{
		super.onCreate(S);
		setContentView(R.layout.question_list_new);
		
		questionLv=(ListView)findViewById(R.id.newQuestionList);
		
		myQuestionArray.clear();

		myQuestionLvAdapter=new MyQuestionLvAdapter(this,myQuestionArray);
		questionLv.setAdapter(myQuestionLvAdapter);
	    myQuestionLvAdapter.notifyDataSetChanged();
	    
	    Map<String,String> params=new HashMap<String,String>();
	    params.put("myId",""+GlobalVar.myId);
	    MyQuestionHandler myQuestionHandler=new MyQuestionHandler();
	    String toSendStr=GlobalVar.packageSentData(params);
	    SendAndGet sendAndGet=new SendAndGet(toSendStr,myQHURL,myQuestionHandler);
	    sendAndGet.start();
	    
	}
	
	
	
	public class MyQuestionHandler extends Handler
	{@Override
		public void handleMessage(Message msg)
	    {Bundle b=msg.getData();
		 String Reply=b.getString("Reply");
		 if(!(Reply.equals("462323679")))
				 {
			 try{loadQuestionList_JSON(Reply);}catch(Exception e){}
	             }    
		 }
	}
	
	public void loadQuestionList_JSON(String JSON) throws JSONException
	{JSONArray ja=new JSONArray(JSON);
	   for(int i=0;i<ja.length();i++)
		{JSONObject jo=(JSONObject)ja.getJSONObject(i);
		 Question q=new Question();
		 q.Abbreviation=jo.getString("contentAbbr");
		 q.abbrFlag=Boolean.valueOf(jo.getString("abbrFlag"));
		 q.Credit=Integer.valueOf(jo.getString("Credit"));
		 q.fromId=Long.valueOf(jo.getString("fromId"));
		 q.fromUserName=jo.getString("fromUserName");
		 q.questionId=Long.valueOf(jo.getString("questionId"));
		 q.rowId=Integer.valueOf(jo.getString("rowId"));
		 q.Sort=jo.getString("Sort");
		 q.Title=jo.getString("Title");
		 myQuestionArray.add(q);
		}
	   myQuestionLvAdapter.notifyDataSetChanged();
	}
}
