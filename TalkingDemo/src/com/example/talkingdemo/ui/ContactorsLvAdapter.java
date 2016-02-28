package com.example.talkingdemo.ui;

import DataSaveObject.Contactors;
import Global.GlobalVar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.talkingdemo.R;
import com.example.talkingdemo.TalkActivity;


public class ContactorsLvAdapter extends BaseAdapter
{
	 private Context context;
	    private LayoutInflater listContainer;

	    private final class ItemViewBox
	    {private TextView userName;
//	     private TextView unReadCount;
	     private TextView Role;
	     private ImageView Avatar;
	     private LinearLayout itemLayout;
	    }
	    private void updateStatusToFail(Long Id)
	    {String updateFail="update user"+Id+" set Status=0 where Status=2";
	      GlobalVar.sb.execSQL(updateFail);
	     }
	    private boolean checkVergin(Long Id)
	    {
	    	FOR:for(int i=0;i<GlobalVar.todayVergin.size();i++)
	       { if(Id==GlobalVar.todayVergin.get(i))
	         {GlobalVar.todayVergin.remove(i);
	         return true; 
	         }
	       }
	    	return false;
	    }
	    public ContactorsLvAdapter(Context context)
		{
			this.context=context;
			listContainer= LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return GlobalVar.contactorsList.size();
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
		private boolean checkRecentTalkList(Long Id)
		{ String select="select userId from recentTalk where userId=" +
				Id+";";
		Cursor sor=GlobalVar.sb.rawQuery(select,null);
			if(sor.moveToNext())
				return true;
			else{return false;}
		}
		
		private void insertIntoRecentTalkList(Long Id,int Position)
		{String insert="insert into recentTalk (userId,userName,role,avartarLink)" +
				"values("+GlobalVar.contactorsList.get(Position).userId
				+",'"+GlobalVar.contactorsList.get(Position).Name
				+"',"+GlobalVar.contactorsList.get(Position).Role
				+",'"+GlobalVar.contactorsList.get(Position).avatarLink
				+"');";
		GlobalVar.sb.execSQL(insert);
		}
		
		@Override
		public View getView(final int Position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ItemViewBox itemViewBox;
			if(convertView==null)
			{
			itemViewBox=new ItemViewBox();
			convertView = listContainer.inflate(R.layout.contactor_list_item, null); 
			itemViewBox.userName=(TextView)convertView.findViewById(R.id.tv_userName_Cli);
			itemViewBox.Role=(TextView)convertView.findViewById(R.id.tv_role_Cli);
//			itemViewBox.unReadCount=(TextView)convertView.findViewById(R.id.tv_);
			itemViewBox.Avatar=(ImageView)convertView.findViewById(R.id.iv_avatar_Cli);
//			itemViewBox.itemLayout=(LinearLayout)convertView.findViewById(R.layout.mianwin_elv_item);
			convertView.setTag(itemViewBox);
			}else
			{itemViewBox=(ItemViewBox)convertView.getTag();
			}
			itemViewBox.userName.setText(GlobalVar.contactorsList.get(Position).Name);
			Log.v("Count","Count"+GlobalVar.contactorsList.get(Position).unreadCount);
//			itemViewBox.unReadCount.setText(("you got"+GlobalVar.contactorsList.get(Position).unreadCount+"new msg"));
			itemViewBox.Role.setText(""+GlobalVar.contactorsList.get(Position).Role);
			int role=GlobalVar.contactorsList.get(Position).Role;
			if(role==0)
			{convertView.setBackgroundColor(android.graphics.Color.YELLOW);}
			if(role==1)			
			 convertView.setBackgroundColor(android.graphics.Color.BLUE);
			if(role==2)
			{convertView.setBackgroundColor(android.graphics.Color.LTGRAY);}
			 convertView.setOnClickListener
			(new OnClickListener()
			{	@Override
				public void onClick(View v) 
				{
				if(!(v==itemViewBox.Avatar))
				  {GlobalVar.nowTalkingWithId=GlobalVar.contactorsList.get(Position).userId;
				   Intent intent=new Intent(context,TalkActivity.class);
				   Bundle b=new Bundle();
				   b.putString("userName", (String)itemViewBox.userName.getText());
				   b.putLong("userId",GlobalVar.contactorsList.get(Position).userId);
				   intent.putExtras(b);
				   String tableName="user"+GlobalVar.nowTalkingWithId;
				   GlobalVar.buildDialogTable(tableName);
				   //check if this userId is  in the  recentTalk List
				   boolean inRecentTalk=checkRecentTalkList(GlobalVar.contactorsList.get(Position).userId);
				   if(inRecentTalk==false)
				   {insertIntoRecentTalkList(GlobalVar.contactorsList.get(Position).userId,Position);
				    Contactors contactors=new Contactors();
				    contactors.avatarLink=GlobalVar.contactorsList.get(Position).avatarLink;
				    contactors.Name=GlobalVar.contactorsList.get(Position).Name;
				    contactors.Role=GlobalVar.contactorsList.get(Position).Role;
				    contactors.userId=GlobalVar.contactorsList.get(Position).userId;
				    GlobalVar.recentContcArray.add(contactors);
				   }
				   context.startActivity(intent);
				   }else
				  {//if click avatar,go to details activity
				  }
				
			      }
			    });
			return convertView;
		}

		
		
		
	}



