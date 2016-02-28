package com.example.talkingdemo.ui;

import com.example.talkingdemo.R;
import com.example.talkingdemo.TalkActivity;
import Global.GlobalVar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RecentContcAdp extends android.widget.BaseAdapter {
    private Context context;
    private LayoutInflater listContainer;
    public Handler notifyChangeHd;
    
    
    private final class ItemViewBox
    {private TextView userName;
     private TextView unReadCount;
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
    public RecentContcAdp(Context context,Handler notifyChangeHd)
	{   this.notifyChangeHd=notifyChangeHd;
		this.context=context;
		listContainer= LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int i=GlobalVar.recentContcArray.size();
		Log.v("","recentAdp getCount " +i);
		return GlobalVar.recentContcArray.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int arg0) 
	{return 0;
	}
	@Override
	public View getView(final int Position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ItemViewBox itemViewBox;
		if(convertView==null)
		{
		itemViewBox=new ItemViewBox();
		convertView = listContainer.inflate(R.layout.mianwin_elv_item, null); 
		itemViewBox.userName=(TextView)convertView.findViewById(R.id.tv_name_rcli);
		itemViewBox.Role=(TextView)convertView.findViewById(R.id.tv_role_rctl);
		itemViewBox.unReadCount=(TextView)convertView.findViewById(R.id.tv_unreaadLabel_rcli);
		itemViewBox.Avatar=(ImageView)convertView.findViewById(R.id.iv_avatar_rcli);
//		itemViewBox.itemLayout=(LinearLayout)convertView.findViewById(R.layout.mianwin_elv_item);
		convertView.setTag(itemViewBox);
		}else
		{itemViewBox=(ItemViewBox)convertView.getTag();
		}
		//吃完饭，回来时将数据源改一下。。。改为recentContacrsList
		itemViewBox.userName.setText(GlobalVar.recentContcArray.get(Position).Name);
		Log.v("Count","Count"+GlobalVar.recentContcArray.get(Position).unreadCount);
		itemViewBox.unReadCount.setText(("you got "+GlobalVar.recentContcArray.get(Position).unreadCount+" new msg"));
//		itemViewBox.Role.setText(GlobalVar.recentContcArray.get(Position).Role);
		int role=GlobalVar.recentContcArray.get(Position).Role;
		//this place should make a change..the role is defined as integer..
		//and the teacher is not in the list;
	    if(role==1)
	    {convertView.setBackgroundColor(Color.YELLOW);}
	    if(role==2)
	    {convertView.setBackgroundColor(Color.WHITE);}
		
		
		convertView.setOnClickListener
		(new OnClickListener()
		{	@Override
			public void onClick(View v) 
			{
			if(!(v==itemViewBox.Avatar))
			  {Intent intent=new Intent(context,TalkActivity.class);
			   GlobalVar.nowTalkingWithId=GlobalVar.contactorsList.get(Position).userId;
			   GlobalVar.buildDialogTable("user"+GlobalVar.nowTalkingWithId);
			  
			   Bundle b=new Bundle();
			   b.putString("userName", (String)itemViewBox.userName.getText());
			   b.putLong("userId",GlobalVar.contactorsList.get(Position).userId);
			   intent.putExtras(b);
			   boolean checkVergin=checkVergin(GlobalVar.contactorsList.get(Position).userId);
			  
			   if (checkVergin==true)
				   updateStatusToFail(GlobalVar.contactorsList.get(Position).userId);
			       context.startActivity(intent);
			  }else
			  {//if click avatar,go to details activity
			  }
		  }
		});
		
		convertView.setOnLongClickListener(new OnLongClickListener()
		{	@Override
			public boolean onLongClick(View arg0) 
			{
			android.app.AlertDialog.Builder alertDialogB=new AlertDialog.Builder(context);
			alertDialogB.setTitle("you are going to delete this dialog !!");
			alertDialogB.setMessage("this operation is inreversible,are you sure to delte this conversation ?");
		    alertDialogB.setPositiveButton("Ok",new DialogInterface.OnClickListener()
		    {	@Override
				public void onClick(DialogInterface arg0, int arg1) 
		        {GlobalVar.del_RecentTalkTable(GlobalVar.recentContcArray.get(Position).userId);
		         GlobalVar.dropTable("user"+GlobalVar.recentContcArray.get(Position).userId);
		         GlobalVar.delItem_RecentTalkArray(GlobalVar.recentContcArray.get(Position).userId);
				  Message msg=new Message();
				 notifyChangeHd.sendMessage(msg);
		        }
			});
		    final AlertDialog alertD=alertDialogB.create();
		    alertDialogB.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
		    {	@Override
				public void onClick(DialogInterface arg0, int arg1)
		        {alertD.dismiss();
				}
			});
		    alertDialogB.show();
		    
			return false;
			}
			
		});
		
		return convertView;
		
		
		
	}

	
	
	
}
