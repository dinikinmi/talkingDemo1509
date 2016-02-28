package com.example.talkingdemo.ui;

import com.example.talkingdemo.CommenceList;
import com.example.talkingdemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommenceLvAdapter extends BaseAdapter
{	
	 public Context context;
	 private LayoutInflater layoutInflater;
	
	
	public CommenceLvAdapter(Context context)
    {this.context=context;
      layoutInflater=LayoutInflater.from(context);
      }
	
	private class ItemBox
	{TextView userName;
	 TextView Content;
	 ImageView Avatar;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CommenceList.commenceArray.size();
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
	public View getView(int Position, View convertView, ViewGroup arg2)
	{   
		ItemBox itemBox=new ItemBox();
		if(convertView==null)
		{ convertView=layoutInflater.inflate(R.layout.commmence_item, null);
		  itemBox.userName=(TextView)convertView.findViewById(R.id.CLI_userName_Tv);
		  itemBox.Avatar=(ImageView)convertView.findViewById(R.id.CLI_userAvatar_Iv);
		  itemBox.Content=(TextView)convertView.findViewById(R.id.CLI_commenceContent_Tv);
		  convertView.setTag(itemBox);
		}else
		{itemBox=(ItemBox)convertView.getTag();
		}
		itemBox.Content.setText(CommenceList.commenceArray.get(Position).Content);
		itemBox.userName.setText(CommenceList.commenceArray.get(Position).userName);
		return convertView;
	}

}
