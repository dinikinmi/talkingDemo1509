package com.example.talkingdemo.ui;

import java.util.ArrayList;

import com.example.talkingdemo.R;
import com.example.talkingdemo.UserDetails;

import DataSaveObject.Contactors;
//import android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UD_ChildListAdp extends BaseAdapter {
    public Context context;
	public ArrayList<Contactors> childArray;
	public LayoutInflater layoutInflater;
	
	public class viewItemBox
	{public ImageView Avatar;
	 public TextView childName;
	}

	
	public UD_ChildListAdp(Context context,ArrayList<Contactors> childArray)
	{ this.childArray=childArray;
      this.context=context;  
	  layoutInflater=LayoutInflater.from(context);
	 }
	 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.childArray.size();
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
	{   viewItemBox itemBox=new viewItemBox();
		if(convertView==null)
	    {convertView=(LinearLayout)layoutInflater.inflate(R.layout.userdetais_lv_item,null);
	     itemBox.Avatar=(ImageView)convertView.findViewById(R.id.iv_Avatar_Udli);
	     itemBox.childName=(TextView)convertView.findViewById(R.id.tv_userName_Udli);
	     convertView.setTag(itemBox);
		}else
		{itemBox=(viewItemBox)convertView.getTag();}
        //set pic of avatar in formal version 
		//itemBox.Avatar.setBackground()
		 itemBox.childName.setText(childArray.get(Position).Name);
		
		 convertView.setOnClickListener(new OnClickListener()
		 {  @Override
			public void onClick(View arg0)
		    {Bundle b=new Bundle();
		     b.putLong("Id",childArray.get(Position).userId);
		     b.putString("userName",childArray.get(Position).Name);
		    
		     Intent intent=new Intent(context,UserDetails.class);
		     intent.putExtras(b);
		     context.startActivity(intent);
		    }
		 }
		 		 );
		 
		 
		
		
		
		return convertView;
	}

}
