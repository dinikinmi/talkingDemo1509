package com.example.talkingdemo.ui;
import java.util.ArrayList;

import com.example.talkingdemo.AnswerContent;
import com.example.talkingdemo.AnswerList;
import com.example.talkingdemo.R;
import com.example.talkingdemo.R.layout;

import DataSaveObject.Answer;
import DataSaveObject.Question;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AnswerListAdapter extends BaseAdapter
{
	private ArrayList<Answer> answerArray;
	private Context context;
	
	
	
    public class ItemBox
    {public TextView Abbr;
    }
    public AnswerListAdapter(Context context,ArrayList<Answer> answerArray)
    {
    	this.context=context;
    	this.answerArray=answerArray;
    }
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return answerArray.size();
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
		{  convertView=LayoutInflater.from(context).inflate(layout.answer_list_item, null);
		   itemBox.Abbr=(TextView)convertView.findViewById(R.id.AnsAbbr_QL);
		   convertView.setTag(itemBox);
		}else
		{
			itemBox=(ItemBox)convertView.getTag();
		}
		itemBox.Abbr.setText(answerArray.get(Position).Abbr);
		//onClick,tansfer the arrayList data to new activity;
		convertView.setOnClickListener(new OnClickListener()
		{  @Override
			public void onClick(View arg0) 
		    {Bundle b=new Bundle();
		     b.putString("ansAbbr", answerArray.get(Position).Abbr);
		     b.putLong("answerPosterId",answerArray.get(Position).userId);
		     b.putString("ansPosterName",answerArray.get(Position).userName);
		     b.putLong("questionId",answerArray.get(Position).quesitonId);
		     b.putLong("answerId",answerArray.get(Position).itselfId);
		     b.putString("questionTitle",answerArray.get(Position).questionTitle);
		     b.putBoolean("ansConAbbrFlag",answerArray.get(Position).ansContentAbbrFlag);
		     b.putInt("ansListPos",Position);
		     Intent intent=new Intent(context,AnswerContent.class);
		     intent.putExtras(b);
			 context.startActivity(intent);
			 ///opmize///infact,because answerArray is static,,so,infactly,just post a Posion would be enough..
			 	
			}
		}
		 );
		return convertView;
	}
	
	
	

}
