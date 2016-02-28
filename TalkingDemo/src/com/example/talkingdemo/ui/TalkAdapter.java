package com.example.talkingdemo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.talkingdemo.R;

import datatransfer.ChatObjectSend;

import Global.ChatObject;
import Global.GlobalVar;
import android.R.color;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TalkAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<HashMap<String, String>> al;
	private int[] layid;
	private String[] itemname;
	private int[][] itemid;
	private int kind;
	public static MediaPlayer mp;
	public LayoutInflater layoutInflater;
    	
	public class ItemBox
	{public TextView contentTv;
	 public ImageView avatar;
	 }
	
	public TalkAdapter(Context context, ArrayList<HashMap<String,String>> al)
	{   		
		this.context=context;
		//context=talkActivity
		this.al=al;
		layoutInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return al.size();
	}

	@Override
	public Object getItem(int position)
	{
		return al.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{   Log.i("position","position is "+position);
		String msgType;
		msgType=al.get(position).get("msgType");
	    Log.v("MessageType","get the messageType "+msgType);
		String fromId;
		fromId=al.get(position).get("fromId");
		
		RelativeLayout Layout=null;
		final String Time;
		Time=al.get(position).get("buildTime");
		String Content=al.get(position).get("Content");
		//		String str_getReadLabel="select ReadLabel from "+GlobalVar.nowTalkingWith+" where BuildTime is '"+Time+"';";
		String readLabel=al.get(position).get("readLabel");

//Cursor c=GlobalVar.sb.rawQuery(str_getReadLabel,null);
//c.moveToNext();
//String readLabel=c.getString(0);
		//msgType=0 .text
		//msgType=1. audio
		//msgType=2. pic

		if((fromId.equals(""+GlobalVar.myId))& (msgType.equals("1")))
		{//if i send a voice message
			//take the right voice layout
			LayoutInflater inflater = LayoutInflater.from(context);
			Layout = (RelativeLayout) inflater.inflate(R.layout.talk_right_item_voice, null);
			final TextView talkRightVoice =(TextView)Layout.findViewById(R.id.talklrihgt_tv_voice);
			//if it is unread..tell user
			
			if(readLabel.equals("0"))
			{
			 talkRightVoice.setText("New Voice Message");
			 talkRightVoice.setBackgroundColor(Color.rgb(1,22,133));
			};
			if(readLabel.equals("1"))
			{   
				talkRightVoice.setText("Readed");
				talkRightVoice.setBackgroundColor(Color.rgb(25,0,1));
			}
             
			
			//talkRightVoice.setBackground(null);
				
			//here we set onClick method for it
			
			talkRightVoice.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
				//click to play the audio file
					String AudioPath=al.get(position).get("AudioPath");
				//start the audio play with this path	
			        talkRightVoice.setText("Readed  "+Time);
			        talkRightVoice.setBackgroundColor(Color.rgb(25,0,1));
			        al.get(position).put("ReadLabel", "Readed");
			        String Str_Update_readLabel="UPDATE '"+GlobalVar.nowTalkingWithId+"' set ReadLabel='Readed' where"
                     + " BuildTime ='"+Time+"';"; 			        
                    GlobalVar.sb.execSQL(Str_Update_readLabel);
                    audioPlay(AudioPath);
                  }
			}
			);
			}
		
		
		
		if((fromId.equals(""+GlobalVar.myId))&(msgType.equals("0")))
		{//judge the messageType and Sender
		 //if the message is from me and the type is text	 
			LayoutInflater inflater = LayoutInflater.from(context);
			Layout = (RelativeLayout) inflater.inflate(R.layout.talk_rightitem, null);
			final TextView textContent_tv =(TextView)Layout.findViewById(R.id.talkright_tv);
			final TextView Status_tv=(TextView)Layout.findViewById(R.id.status_tv);
			textContent_tv.setText(al.get(position).get("Content"));
			
			String Status=al.get(position).get("Status");
			
			if(Status.equals("0")||Status.equals("18028628619"))
			{
			 Status_tv.setBackgroundColor(Color.RED);
			 Status_tv.setVisibility(View.VISIBLE);
			 Status_tv.setText("      Fail       ");
			 
			 Status_tv.setOnClickListener(new OnClickListener()
			  {  public void onClick(View v)
			     {Log.v("","status");
				  ChatObject chatObject=new ChatObject();
			      chatObject.Content=al.get(position).get("Content");
			      chatObject.msgId=al.get(position).get("msgId");
			      chatObject.fromId=Long.valueOf(al.get(position).get("fromId"));
			      chatObject.fromUserName=GlobalVar.myName;
			      chatObject.toId=Long.valueOf(al.get(position).get("toId"));
			      chatObject.msgType=Integer.valueOf(al.get(position).get("msgType"));
			      String updateUserStatus="update user"+al.get(position).get("toId")+" set Status=2 where msgId="+al.get(position).get("msgId") ;
			      GlobalVar.sb.execSQL(updateUserStatus);
			      
			      Status_tv.setText("         SENDING       ");
			      Status_tv.setBackgroundColor(123123);
                  ChatObjectSend chatObjectSend=new ChatObjectSend(chatObject,GlobalVar.hostURL);
			       chatObjectSend.start();
				 }
				  
			  }
					  
					  );
			
			}
			if(Status.equals("1"))
			{ Status_tv.setVisibility(View.GONE);
			}
			//if it is sent successfully,invisible
			if(Status.equals("2"))
			{ Status_tv.setText("Sending");
			  Status_tv.setVisibility(View.VISIBLE);
			  
			}
			
			
		    
			
					
		}
		
		
		
		
		if((fromId.equals(""+GlobalVar.nowTalkingWithId))&(msgType.equals("1")))
		{
			//if it is a VoiceMessge from the user you are talking with
			LayoutInflater inflater = LayoutInflater.from(context);
			Layout = (RelativeLayout) inflater.inflate(R.layout.talk_left_item_voice, null);
			final TextView talkLeftVoice =(TextView)Layout.findViewById(R.id.talkleft_tv);
			talkLeftVoice.setText("Unreaded Voice Message");
//			talkRightVoice.setBackground(null);
			talkLeftVoice.setBackgroundColor(color.holo_orange_dark);
			//here we set onClick method for it
			talkLeftVoice.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
				//click to play the audio file
					String AudioPath=al.get(position).get("AudioPath");
				//start the audio play with this path	
			        talkLeftVoice.setText("Readed");
					audioPlay(AudioPath);
				}
			}
		      );	
			
		}
  
		
	   if((fromId.equals(""+GlobalVar.nowTalkingWithId))&(msgType.equals("2")))
	   {
		   //it is a Text Message from other user
		   LayoutInflater inflater = LayoutInflater.from(context);
			Layout = (RelativeLayout) inflater.inflate(R.layout.talk_rightitem, null);
			final TextView talkRightVoice =(TextView)Layout.findViewById(R.id.talkright_tv);
			talkRightVoice.setText(al.get(position).get("Content"));
		}
	    return Layout;
		
		
		
		
		/*
		LayoutInflater inflater = LayoutInflater.from(context);
		RelativeLayout layout=null;
		layout = (RelativeLayout) inflater.inflate(layid[kind], null);
		//layid[0]=the right item(Me,the user) ,the layid[i] is the left item
		//the one i m talking with
		
		for(int i=0;i<itemname.length;i++)
		{   
			
			TextView tv = (TextView) layout.findViewById(itemid[kind][i]);
//  i think	itemId refer to the textView that loading dialog content
			tv.setText(al.get(position).get(itemname[i]));
//	int[][]{{R.id.talkright_tv},{R.id.talkleft_tv}}
			
		}
		
		//TextView time = (TextView) layout.findViewById(R.id.time);
		
		//time.setText(lists.get(position).getTime());
		return layout;
*/
	    	    
	}
	
	
	public static void audioPlay(String Path){
		if(mp==null)
			{mp=new MediaPlayer();Log.i("media player","build mp object ");
			}
			try
			{mp.reset();
			mp.setDataSource(Path);
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.prepare();
			mp.start();
			}catch(Exception e)
			{Log.e("AudioPlay",e.toString());
			}
		}
	

}
