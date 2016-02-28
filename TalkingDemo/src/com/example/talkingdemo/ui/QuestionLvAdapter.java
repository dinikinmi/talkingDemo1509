package com.example.talkingdemo.ui;

import com.example.talkingdemo.AnswerList;
import com.example.talkingdemo.NewQuestionList;
import com.example.talkingdemo.R;

import Global.GlobalVar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionLvAdapter extends BaseAdapter
{

	private Context context;
	private LayoutInflater questionListContainer;
	
	public QuestionLvAdapter(Context context)
	{
		this.context=context;
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
		public TextView rowId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
    int Count;
    Count=GlobalVar.questionArray.size();
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
		
		itemBox.tvCredit.setText(""+GlobalVar.questionArray.get(Position).Credit);
		itemBox.tvLabel_1.setText(GlobalVar.questionArray.get(Position).Label_1);
		itemBox.tvLabel_2.setText(GlobalVar.questionArray.get(Position).Label_2);
		itemBox.tvLabel_3.setText(GlobalVar.questionArray.get(Position).Label_3);
		itemBox.tvLabel_4.setText(GlobalVar.questionArray.get(Position).Label_4);
		itemBox.tvAbbr.setText(GlobalVar.questionArray.get(Position).Abbreviation);
		itemBox.tvTitle.setText(""+GlobalVar.questionArray.get(Position).Title);
		//would be changed back to be question.Title after test finished;		
		
	    itemBox.tvAbbr.setOnClickListener(new OnClickListener(){
	    @Override 
	    public void onClick(View v)
	    {   
	    	
	     	Intent intent=new Intent(context,AnswerList.class);
	    	Bundle b=new Bundle();
	    	
	    	b.putLong("questionId",GlobalVar.questionArray.get(Position).questionId);
//	    	b.putLong("fromId",GlobalVar.questionArray.get(Position).fromId);
	    	b.putString("questionTitle",GlobalVar.questionArray.get(Position).Title);
	        b.putString("ContentAbbr",GlobalVar.questionArray.get(Position).Abbreviation);
	    	b.putLong("askerId",GlobalVar.questionArray.get(Position).fromId);
	        b.putLong("rowId",GlobalVar.questionArray.get(Position).rowId);
	        b.putBoolean("abbrFlag",GlobalVar.questionArray.get(Position).abbrFlag);	
	        intent.putExtras(b);
	    	Log.v("","qlvadp test "+b.getLong("questionId"));
	    	context.startActivity(intent);
	    }
	    });
	    
	  
	    return convertView;
	}
	
	

}
