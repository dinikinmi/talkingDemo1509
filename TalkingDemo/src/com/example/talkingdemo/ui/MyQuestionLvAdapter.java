package com.example.talkingdemo.ui;

import java.util.ArrayList;

import com.example.talkingdemo.AnswerList;
import com.example.talkingdemo.R;
import com.example.talkingdemo.ui.QuestionLvAdapter.ItemBox;

import DataSaveObject.Question;
import Global.GlobalVar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyQuestionLvAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater questionListContainer;
	public ArrayList<Question> myQuestionArray;
	
	public MyQuestionLvAdapter(Context context,ArrayList<Question> myQuestionArray)
	{
		this.context=context;
		this.myQuestionArray=myQuestionArray;
		questionListContainer= LayoutInflater.from(context);
		
	}
	
	
	public class ItemBox
	{
		public TextView tvTitle;
		public TextView tvCredit;
		public TextView tvAbbr;
		public TextView tvLabel_1;
		public TextView tvLabel_2;
		public TextView tvLabel_3;
		public TextView tvLabel_4;
		public LinearLayout topLayout;
		public LinearLayout buttomLayout;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
    int Count;
    Count=myQuestionArray.size();
    Log.v("Cunt","Count  "+Count);
	return Count;
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
	public View getView(final int Position, View convertView, ViewGroup arg2) 
	{
		// TODO Auto-generated method stub
		ItemBox itemBox;
		Log.v("getViiew","getView Qlv");
		itemBox=new ItemBox();
		
		if(convertView==null)
		{   convertView = questionListContainer.inflate(R.layout.question_list_item, null); 
		   		    
		    itemBox.tvAbbr=(TextView)convertView.findViewById(R.id.Abbre_QLI);
			itemBox.tvCredit=(TextView)convertView.findViewById(R.id.Credit_QLI);
			itemBox.tvLabel_1=(TextView)convertView.findViewById(R.id.Label_1_QLI);
			itemBox.tvLabel_2=(TextView)convertView.findViewById(R.id.Label_2_QLI);
			itemBox.tvLabel_3=(TextView)convertView.findViewById(R.id.Label_3_QLI);
			itemBox.tvLabel_4=(TextView)convertView.findViewById(R.id.Label_4_QLI);
			itemBox.tvTitle=(TextView)convertView.findViewById(R.id.Tile_QLI);
		   
			convertView.setTag(itemBox);
		}
		else
		{itemBox=(ItemBox)convertView.getTag();}
		
		itemBox.tvCredit.setText(""+myQuestionArray.get(Position).Credit);
		itemBox.tvLabel_1.setText(myQuestionArray.get(Position).Label_1);
		itemBox.tvLabel_2.setText(myQuestionArray.get(Position).Label_2);
		itemBox.tvLabel_3.setText(myQuestionArray.get(Position).Label_3);
		itemBox.tvLabel_4.setText(myQuestionArray.get(Position).Label_4);
		itemBox.tvAbbr.setText(myQuestionArray.get(Position).Abbreviation);
		itemBox.tvTitle.setText(myQuestionArray.get(Position).Title);
		itemBox.tvAbbr.setOnClickListener(new OnClickListener(){
	    @Override 
	    public void onClick(View v)
	    {
	    	Intent intent=new Intent(context,AnswerList.class);
	    	Bundle b=new Bundle();
	    	
	    	b.putLong("questionId",myQuestionArray.get(Position).questionId);
//	    	b.putLong("fromId",myQuestionArray.get(Position).fromId);
	    	b.putString("questionTitle",myQuestionArray.get(Position).Title);
	        b.putString("ContentAbbr",myQuestionArray.get(Position).Abbreviation);
	    	b.putLong("askerId",myQuestionArray.get(Position).fromId);
	        b.putLong("rowId",myQuestionArray.get(Position).rowId);
	        b.putBoolean("abbrFlag",myQuestionArray.get(Position).abbrFlag);	
	        intent.putExtras(b);
	    
	    	context.startActivity(intent);
	    }
	    }
	    		);
	  
	    return convertView;
	}
	
}
