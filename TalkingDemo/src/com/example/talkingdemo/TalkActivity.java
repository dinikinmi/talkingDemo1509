package com.example.talkingdemo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import com.example.sqlite.Select;
import com.example.talkingdemo.ui.TalkAdapter;

import datatransfer.ChatObjectSend;
import BackGround.MyComparator;
import Global.ChatObject;
import Global.GlobalVar;
import Global.MyAPP;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class TalkActivity extends Activity
{   public static UpdateStatusHandler updateStatusHd;
 	public static long Count;
 	public static int x=5;
	public ArrayList<HashMap<String, String>> talkData;
	public static TalkAdapter talkAdapter;
	protected String Content;
	public static ListView lv;
	public static Cursor cl;
	public long startRecordTime;
	public String Path;
	public String Location;
	private String Root=Environment.getExternalStorageDirectory().getPath();
	public static MediaRecorder mr;
	private ChatObject chatObject=new ChatObject();
	public Intent preActInt;
	public Bundle preIntBdl;
	public Button voiceRecord;
    public Button sendVoiceMsg;
    public Button cancelRecord;
    public  TextView tv_Title;
    public Button sendTextMsg;
    public String  dialogTableName;

public void loadHistory()
 {	 int clGetCount=cl.getCount();
	 Log.e("cl GetCount","clGetCount is "+clGetCount);
	 if(clGetCount>0)
	 {	 /**
		  * if cl=null,the getCount would occur an NullPointer,the Cursor should not be Null even if the 
		  *number of raw is 0
		  */
	//Log.v("getCount show ","the count is "+cl.getCount());
      for(cl.moveToLast();!cl.isBeforeFirst();cl.moveToPrevious())
      {      HashMap<String,String> hm=new HashMap<String,String>();
    		 hm.put("msgId",""+cl.getString(1));
             hm.put("fromId",""+cl.getLong(2));
    		 hm.put("toId",cl.getString(4));
    	     hm.put("audioPath",cl.getString(6));
    	     hm.put("Content", cl.getString(7));
    	     hm.put("msgType",cl.getString(8));
    	     hm.put("buildTime",cl.getString(9));
    	     hm.put("Duration",cl.getString(10));
    	     hm.put("readLabel",cl.getString(11));
    	     hm.put("Status",cl.getString(12));
    	     talkData.add(0,hm);
    	 }
       cl.close();
       talkAdapter.notifyDataSetChanged();
        }
	 
	 

 }
public void getHistoryDialog()
	{  
	 Cursor cl_test = null;
		
//		Cursor cl=null;
		if(Count>x)
		{
			Log.e("getHistroyDialog","Count is "+Count);
			String selectLimit="select * from user"+GlobalVar.nowTalkingWithId+" order by BuildTime asc limit "+(Count-x+1)+","+x+";";
	        cl=GlobalVar.sb.rawQuery(selectLimit,null) ;
	        cl_test=GlobalVar.sb.rawQuery(selectLimit,null);
	        
	        Count=Count-x;  
	        Log.e("getHistroyDialog","Count -x is "+Count);
	    }else
	        {
	        	if(Count>=0){
	        cl=GlobalVar.sb.rawQuery("select * from user"+GlobalVar.nowTalkingWithId+" order by BuildTime asc limit "+0+","+Count+";",null);
	        Count=0; 
	        cl_test=GlobalVar.sb.rawQuery("select * from user"+GlobalVar.nowTalkingWithId+"  order by BuildTime desc limit "+0+","+Count+";",null);
	        	}
	        	Log.e("getHistoryDialogElse","Count is 0 "+Count);
	        	   }
//		return cl;
		Log.e("GetCount test","Count Test="+cl_test.getCount());
		Log.e("GetCount cl","Count cl="+cl.getCount());
		loadHistory();
	}
public Cursor getHistoryDialogFromTable(int rowId)
{String select="select * from CHATTING where fromId="+GlobalVar.nowTalkingWithId+
		" and rowId<"+rowId+",limit 10;";
 Cursor C=GlobalVar.sb.rawQuery(select, null);
return C;
}
public void addDataIntoArrayList(Cursor C)
{ 
	for(C.moveToNext();C.isAfterLast();C.moveToNext())
	{  HashMap<String,String> map=new HashMap<String,String>();
	   map.put("rowId", ""+C.getInt(0));//rawId
	   map.put("msgId", ""+C.getLong(1));
	   map.put("fromId",""+C.getLong(2));
	   map.put("toId",""+C.getLong(3));
	   map.put("audioPath","");//4
	   map.put("Content",C.getString(5));
	   map.put("messageType", ""+C.getInt(6));
	   map.put("buildTime", ""+C.getLong(7));
	   map.put("Duration",""+C.getInt(8));
	   map.put("readLabel",""+C.getInt(11));
	   map.put("Status", C.getString(12));
	   talkData.add(map);
	   }
		
}
public Cursor selectAll(String tableName)
 {  Cursor c;
    String select="select * from "+tableName+" ;";
    c=GlobalVar.sb.rawQuery(select, null);
	return c;
 }
public void firstTimeTalkTableCheck(long nowTalkingWithId){
	dialogTableName="user"+nowTalkingWithId;
	String checkTable="select ExistTable from TABLELIST where ExistTable='"+dialogTableName
	    		   +"';";
	Cursor c=GlobalVar.sb.rawQuery(checkTable,null);
	if(c.getCount()==0)
			{   //if table not exist,build one and record it
		String buildTable="create table "+dialogTableName+" (FromId Long,ToId Long,"+
						"AudioPath VARCHAR,"+
			    		  "Content VARCHAR,MessageType VARCHAR(10),"
			    		  +"BuildTime VARCHAR,Duration VARCHAR,ReadLabel);" ;
		GlobalVar.sb.execSQL(buildTable);
		
		String Str_Insert="insert into TABLELIST (ExistTable) values ('"+dialogTableName
						+"');";
				GlobalVar.sb.execSQL(Str_Insert);
				}
		
	}

	@SuppressWarnings("unchecked")
	@Override
   protected void onCreate(Bundle s)
	  {	
		updateStatusHd=new UpdateStatusHandler();
	//	x=1;
		setContentView(R.layout.talkwin);
		findViewById();
		setOnClicks();
		String tableName="user"+GlobalVar.nowTalkingWithId;
		//here build the table
		GlobalVar.buildDialogTable(tableName);
		talkData= new ArrayList<HashMap<String, String>>();
		//lets check it out how talkAdapter adapt the View
		talkAdapter= new TalkAdapter (this, talkData);
		MyComparator myComparator=new MyComparator();
        Collections.sort(talkData,myComparator);
		lv.setAdapter(talkAdapter);
		preActInt=getIntent();
		preIntBdl=preActInt.getExtras();
		GlobalVar.nowTalkingWithId=preIntBdl.getLong("userId");
       	tv_Title.setText(preIntBdl.getString("userName"));
		Count=Select.getCountOfTable("user"+GlobalVar.nowTalkingWithId);
		Log.e("onCreate","Count is "+ Count);
		//if there are more than X raw in table,read X raw,else,read all
        if(Count>0)
        {
		getHistoryDialog();
        }//load the new get data into listView
        super.onCreate(preIntBdl);  
	  }
	
	
	
	  private void findViewById()
	{
   voiceRecord=(Button)findViewById(R.id.record);
   sendVoiceMsg=(Button)findViewById(R.id.sendRecord);
   cancelRecord=(Button)findViewById(R.id.cancel);
   tv_Title=(TextView)findViewById(R.id.talk_title_tv);
   sendTextMsg=(Button)findViewById(R.id.talk_sendbtn);
   lv=(ListView)findViewById(R.id.talk_lv);
	}
	
       private void setOnClicks()
       {      
    tv_Title.setOnClickListener(new OnClickListener()
       {   @Override
       	public void onClick(View v)
           {Intent intent=new Intent(TalkActivity.this,UserDetails.class);
            intent.putExtras(preIntBdl);
            startActivity(intent);
       	}
       });
       
    voiceRecord.setOnClickListener(new OnClickListener()
    {
    @Override
    public void onClick(View v)
    {   
    	startRecordTime=System.currentTimeMillis();
		String Time=new String().valueOf(startRecordTime);
		Log.v("startTime","startTime"+" "+new String().valueOf(startRecordTime));
		Location=Root+File.separator+"Send"+File.separator;
		Log.v("11", "123");
		String fileName=GlobalVar.myId+"@"+GlobalVar.nowTalkingWithId+"@"+Time+".3gp";
	    File file=new File(Location);
	    if(!file.exists()){
	    	Log.v("build path","build"+Location);
	    	file.mkdirs();}
	    Path=Location+fileName;
	    if(mr==null)
	    {mr=new MediaRecorder();}
	    else
	    {mr.reset();}
	    
	    sendVoiceMsg.setVisibility(View.VISIBLE);
	    cancelRecord.setVisibility(View.VISIBLE);
	    voiceRecord.setText("Recording");
	    voiceRecord.setEnabled(false);
	    
	    try{
	  
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);
		mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//		mr.setMaxDuration(10000);
		mr.setOutputFile(Path);
		Log.v("Path","Path is "+Path);
	    mr.prepare();
	    mr.start();
	    Log.v("Record start","record start");
	    }catch(Exception e){e.printStackTrace();}
		Log.i("Record","Record Start");
    }
    } );  
       
    cancelRecord.setOnClickListener(new OnClickListener()
			{	@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
				if(mr!=null)
				{
					mr.reset();
				}
				cancelRecord.setVisibility(View.GONE);
				sendVoiceMsg.setVisibility(View.GONE);
				voiceRecord.setVisibility(View.VISIBLE);
				voiceRecord.setEnabled(true);
				voiceRecord.setText("Record");
				}
			}
			);	
    
    sendVoiceMsg.setOnClickListener(new OnClickListener()
    {
    @Override
    public void onClick(View v){
    	Log.i("Record","Rcord Finish1");
//		mr.wait();
		long endTime=System.currentTimeMillis();
		Log.v("endRecord","engRecordTime"+endTime);
		long lastLong=(endTime-startRecordTime)/1000;
		Log.v("lastLong","Time"+lastLong);
	    if(mr!=null)
		{
		mr.stop();
	    mr.release();
		mr=null;
		Log.i("Record","Rcord Finish2");
	    }
//		audioPlay(path);
		voiceRecord.setVisibility(View.VISIBLE);
		voiceRecord.setEnabled(true);
		voiceRecord.setText("Record");
		cancelRecord.setVisibility(View.GONE);  
		sendVoiceMsg.setVisibility(View.GONE);
		long msgId=GlobalVar.myId+GlobalVar.nowTalkingWithId+endTime;
		
		String Sql_Insert_UNSEND="INSERT INTO UNSEND (msgId,fromId,toId," +
				"audioPath,MsgType,buildTime)"
		        +" VALUES ("+msgId+"," +GlobalVar.myId+","+GlobalVar.nowTalkingWithId+",'"+Path+"',"+
	            1+","+endTime+");";
		GlobalVar.sb.execSQL(Sql_Insert_UNSEND);
		
		String Sql_Insert="INSERT INTO' user"+GlobalVar.nowTalkingWithId+"'"+"(msgId,fromId,toId,audioPath," +
				"MsgType,buildTime,readLabel)"
		        +" VALUES ("+msgId+","+GlobalVar.myId+","+GlobalVar.nowTalkingWithId+",'"+Path+"',"+
	            1+","+endTime+","+0+");";
		GlobalVar.sb.execSQL(Sql_Insert);
        
		//  this.chatList.clear();
		  HashMap<String,String> hm = new HashMap<String,String>();
	      Log.v("chatting class","ct3");
		  hm.put("fromId",""+GlobalVar.myId);
		  chatObject.fromId=GlobalVar.myId;
		  
		  hm.put("toId", ""+GlobalVar.nowTalkingWithId);
		  chatObject.toId=GlobalVar.nowTalkingWithId;
		  
		  hm.put("audioPath", Path);
		  hm.put("readLabel", "Unread");
		  
          hm.put("msgType","1");
          chatObject.msgType=1;
          
		  hm.put("buildTime",String.valueOf(startRecordTime));
		      		  
          talkData.add(hm);
		  talkAdapter.notifyDataSetChanged();
	  }
    }
    );
    
    sendTextMsg.setOnClickListener(new OnClickListener()
	{	@Override
		public void onClick(View v)
		{	EditText et=(EditText)findViewById(R.id.talk_sendText);
			Content=et.getText().toString()+" ";
	        if(Content.replace(" ", "")=="")
			 return;
			//when user click the send button,all Information
			//of chatObject shall be put into map
			//and be recorded in Sqlite
		    
	        //check the role,is role is 1802=banded..stop sending
	        if(checkRole(GlobalVar.nowTalkingWithId)==1802)
	        {Toast.makeText(TalkActivity.this,"you are deleted from its contactors book",Toast.LENGTH_LONG).show();
	         return;
	        }
	        
	        long sendTime=System.currentTimeMillis();
		    String msgId=""+sendTime+""+GlobalVar.nowTalkingWithId+""+GlobalVar.myId;
		    lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			int msgType=0;
			//write the converstion data into sqlite table
			
			String Sql_Insert="INSERT INTO'user"+GlobalVar.nowTalkingWithId+"'"+"(msgId,fromId,toId," +
					"Content,msgType,buildTime,readLabel)"
			        +" VALUES ("+ msgId+","+GlobalVar.myId+","+GlobalVar.nowTalkingWithId+",'"+
		            Content+"',"+msgType+","+sendTime+",0);";
			GlobalVar.sb.execSQL(Sql_Insert);
          
			String Sql_Insert_UNSEND="INSERT INTO UNSEND (msgId,fromId,toId," +
					"Content,msgType,buildTime,Status)"
			        +" VALUES ("+msgId+","+GlobalVar.myId+","+GlobalVar.nowTalkingWithId+",'"+
		            Content+"',"+msgType+","+sendTime+",2);";
			GlobalVar.sb.execSQL(Sql_Insert_UNSEND);
		
		    /*	
			String Sql_Insert_CHATTING="INSERT INTO CHATTING (FromId,ToId," +
					"Content,MessageType,BuildTime)"
			        +" VALUES ('"+GlobalVar.myId+"','"+GlobalVar.nowTalkingWithId+"','"+
		            "','"+msgType+"','"+sendTime+"');";
			GlobalVar.sb.execSQL(Sql_Insert_CHATTING);
			*/
			
			//this.chatList.clear();
			  HashMap<String,String> item = new HashMap<String,String>();
		      Log.v("chatting class","ct3");
			  item.put("msgId",""+msgId);
		      chatObject.msgId=msgId;
		      
		      item.put("fromName", GlobalVar.myName);
		      chatObject.fromUserName=GlobalVar.myName;
			  
			  item.put("toId",""+GlobalVar.nowTalkingWithId);
			  chatObject.toId=GlobalVar.nowTalkingWithId;
			  
			  item.put("fromId",""+GlobalVar.myId);
			  chatObject.fromId=GlobalVar.myId;
			  
			  item.put("Content",Content);
			  chatObject.Content=Content;
			  
			  item.put("msgType",""+msgType);
			  chatObject.msgType=0;
			  
			  item.put("buildTime",""+sendTime);
			  item.put("readLabel",""+0);
			  item.put("Status","2");
			  talkData.add(item);
			  talkAdapter.notifyDataSetChanged();
			  et.setText("");//i think this is to hide the input method interface
//				((InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
//				.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
              String URL=GlobalVar.hostURL+"/server_app/ChatReceive";
			  ChatObjectSend chatObjectSend=new ChatObjectSend(chatObject,URL);
			  chatObjectSend.start();
			}
	}	);
     
    

	lv.setOnScrollListener(new OnScrollListener() 
	{@Override
	  public void onScrollStateChanged(AbsListView view, int scrollState)
	  {// TODO Auto-generated method stub
		switch (scrollState) 
		{
		// 褰撲笉婊氬姩鏃�
		case OnScrollListener.SCROLL_STATE_IDLE:// 鏄綋灞忓箷鍋滄婊氬姩鏃�
			
			// 鍒ゆ柇婊氬姩鍒板簳閮�
			if (lv.getFirstVisiblePosition() == 0)
			 { Log.v("top","is the top");
				if(Count>0)
				{
//				Cursor cl = null;
		        getHistoryDialog();
//		        loadHistory();
		        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
				}
			}

			// 鍒ゆ柇婊氬姩鍒伴《閮�
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 婊氬姩鏃�
		case OnScrollListener.SCROLL_STATE_FLING:// 鏄綋鐢ㄦ埛鐢变簬涔嬪墠鍒掑姩灞忓箷骞舵姮璧锋墜鎸囷紝灞忓箷浜х敓鎯�婊戝姩鏃�
		}
	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3)
	 {// TODO Auto-generated method stub}
     }
	});
    }
            
       public class UpdateStatusHandler extends Handler
   	{	@Override
   		public void handleMessage(Message msg)
   	    {Log.v("","handler");
   		 Bundle b=msg.getData();
   	     String Status=b.getString("Status");
         Long toId=b.getLong("toId");
   	     if(Status.equals("18028628619"))//that means i am not allow to send msg to the guy
         {GlobalVar.updRoleBan_ContcTable(toId);
          GlobalVar.updRoleBan_ContcArray(toId);
          GlobalVar.updRoleBan_recentTalkTable(toId);
          GlobalVar.updRoleBan_recentTalkArray(toId);
          Toast.makeText(TalkActivity.this,"this user delete you from its contactors",Toast.LENGTH_LONG).show();
          talkAdapter.notifyDataSetChanged();
          }
   	     String msgId=b.getString("msgId");
   	     for(int i=0;i<talkData.size();i++)
   	     { if(msgId.equals(talkData.get(i).get("msgId")))
   	       {talkData.get(i).put("Status",Status);}
   	     }
   	     talkAdapter.notifyDataSetChanged();
   	    }
   		}
             
       
       public class Load_New_Download_Dialog extends Handler
       {@Override
    	    public void handleMessage(Message msg)
            {Bundle b=msg.getData();
             ChatObject chatObj=(ChatObject)b.getParcelable("chatObject");
           if(GlobalVar.nowTalkingWithId==chatObject.fromId)
           {
           HashMap<String, String> map=new HashMap<String,String>();
      	   map.put("msgId",""+chatObj.msgId );
      	   map.put("fromId",""+chatObj.fromId);
      	   map.put("toId",""+chatObj.toId);
      	   map.put("Content",chatObj.Content);
      	   map.put("messageType",""+chatObj.msgType);
      	   map.put("buildTime",""+chatObj.Time);
      	   map.put("Status","1");
      	   talkData.add(map);
      	   talkAdapter.notifyDataSetChanged();
            } 
           }
    	   
       }
       
       public  Load_New_Download_Dialog getLoadDialogHd()
       {Load_New_Download_Dialog loadDialog=new Load_New_Download_Dialog();
         
       
		return loadDialog;
    	   
       }
       
       
       public int checkRole(Long Id)
       {for(int i=0;i<=(GlobalVar.contactorsList.size()-1);i++)
                  {if(Id==GlobalVar.contactorsList.get(i).userId)
                     {return GlobalVar.contactorsList.get(i).Role;}
                  }
    	   return 0;
       }
}
	
	

	
	


	

	
	







