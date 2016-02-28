package com.example.talkingdemo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.talkingdemo.R;
import com.example.talkingdemo.ui.MyAnswersLvAdapter;
import com.example.talkingdemo.ui.MyQuestionLvAdapter;

import DataSaveObject.Answer;
import Global.GlobalVar;
import Global.SendAndGet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

public class MyAnswers extends Activity 
{  
	private ListView myAnswersLv;
	public  static ArrayList<Answer> myAnswersArray;
    private Answer answer;
    private MyAnswersLvAdapter myAnswersAdapter;
    private String myAnsURL="http://"+GlobalVar.hostURL+"server_app/MyAnswerListRequestHd";
	
	
	protected void onCreate(Bundle S)
	{  	super.onCreate(S);
		setContentView(R.layout.my_answers_list);
		
		myAnswersLv=(ListView)findViewById(R.id.Lv_MAL);
		myAnswersArray=new ArrayList<Answer>();

        dowanloadAnsList();		
		
		
		
		myAnswersAdapter=new MyAnswersLvAdapter(this,myAnswersArray);
		myAnswersLv.setAdapter(myAnswersAdapter);
	    myAnswersAdapter.notifyDataSetChanged();
	    }
	public class AnswerListDlHd extends Handler
	{@Override
		public void handleMessage(Message msg)
	    {String reply=msg.getData().getString("Reply");
	     Toast.makeText(MyAnswers.this,reply,Toast.LENGTH_LONG);
	      try {JSONArray ja=new JSONArray(reply);
	             myAnswersArray.clear();
	              for(int i=0;i<ja.length();i++)
	              {JSONObject jo=ja.getJSONObject(i);
	               Answer answer=new Answer();
	               answer.Abbr=jo.getString("contentAbbr");
	               answer.ansContentAbbrFlag=Boolean.valueOf(jo.getString("abbrFlag"));
	               answer.itselfId=jo.getLong("answerId");
	               answer.quesitonId=jo.getLong("parentId");
	               answer.questionTitle=jo.getString("parentTitle");
	               answer.userId=jo.getLong("fromId");
	               answer.userName=jo.getString("fromUserName");
	               answer.rowIdinSever=jo.getInt("rowId");
	               answer.quePosterId=jo.getLong("quePosterId");
	        	   myAnswersArray.add(answer);
	              }
	              myAnswersAdapter.notifyDataSetChanged();
	         } catch (JSONException e) {e.printStackTrace();}
	    }
	
	}

	public void dowanloadAnsList()
	{AnswerListDlHd ansListDlHd=new AnswerListDlHd();
	 String sendData="ansPosterId="+GlobalVar.myId; 
	SendAndGet sng=new SendAndGet(sendData,myAnsURL,ansListDlHd);
	sng.start();
	}
	
}
