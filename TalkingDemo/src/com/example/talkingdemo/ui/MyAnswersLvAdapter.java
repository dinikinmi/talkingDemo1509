package com.example.talkingdemo.ui;

import java.util.ArrayList;

import com.example.talkingdemo.AnswerList;
import com.example.talkingdemo.MyAnswers;

import DataSaveObject.Answer;
import Global.GlobalVar;
import android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyAnswersLvAdapter extends BaseAdapter {
	
	public ArrayList<Answer> myAnswersArray;
	public Context context;
    public LayoutInflater layoutInflater;
    
    public class ItemBox
	{
		public TextView tvTitle;
		public TextView tvAbbr;
	}
    
    
	public MyAnswersLvAdapter(Context context,ArrayList<Answer> myAnswersArray)
	{
	  this.context=context;
	  this.myAnswersArray=myAnswersArray;
	  layoutInflater=LayoutInflater.from(context);
	};
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myAnswersArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int Position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ItemBox itemBox=new ItemBox();
		if(convertView==null)
		{convertView=layoutInflater.inflate(com.example.talkingdemo.R.layout.my_answer_list_item, null);
	     itemBox.tvTitle=(TextView)convertView.findViewById(com.example.talkingdemo.R.id.MAL_questionTitle);
	     itemBox.tvAbbr=(TextView)convertView.findViewById(com.example.talkingdemo.R.id.MAL_ansAbbr);
         convertView.setTag(itemBox);
         }else
         {itemBox=(ItemBox)convertView.getTag();}
		itemBox.tvAbbr.setText(myAnswersArray.get(Position).Abbr);
		itemBox.tvTitle.setText(myAnswersArray.get(Position).questionTitle);
		convertView.setOnClickListener(new OnClickListener()
		{   @Override
			public void onClick(View v) 
		    {Bundle b=new Bundle();
		    ///
		    	b.putLong("questionId",myAnswersArray.get(Position).quesitonId);
//		    	b.putLong("fromId",GlobalVar.questionArray.get(Position).fromId);
		    	b.putString("questionTitle",myAnswersArray.get(Position).questionTitle);
		        b.putString("ContentAbbr","");//no question content abbr or full in myAns List;
		    	b.putLong("askerId",myAnswersArray.get(Position).quePosterId);
//		        b.putLong("rowId",GlobalVar.questionArray.get(Position).rowId);
		        b.putBoolean("abbrFlag",true);	
		            
		     Intent intent=new Intent(context,AnswerList.class);
		     intent.putExtras(b);
			 context.startActivity(intent);
		    }
		});
		return convertView;
	}
}
