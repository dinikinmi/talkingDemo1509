package com.example.talkingdemo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.start.Initialize;
import com.example.talkingdemo.ui.ExpandableAdapter;
import com.example.talkingdemo.ui.RecentContcAdp;
import com.example.talkingdemo.ui.SlidingMenu;
import BackGround.HandleUnsend;
import BackGround.StartSocket;
import DataSaveObject.Contactors;
import Global.GlobalVar;
import Global.GlobalVar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.PopupWindow;

public class MainWinActivity extends Activity
   {private PopupWindow popwin;
	private ArrayList<HashMap<String, String>> MsgData;
	private SimpleAdapter MsgAdapter;
	public Socket sk;
	public static Contactors contactors=new Contactors();
    private RecentContcAdp recentContc;
    private Button bt_mySupperClass;
    private Button bt_myChildClass;
    private ListView recentContcLv;
    private ListView lv;
	private Button Test;
    private Button btQstEntrance;
    private Button searchUser;
    private Button slidingmenubtn;
	public View customView;
	public Button popmsg;
	public Button popclose;
	public  Button groupbtn;
	public  NotifyChangeHandler notifyChangeHd;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		notifyChangeHd=new NotifyChangeHandler();
		findViewById();
		setOnClicks();
		GlobalVar.recentContcArray.clear();
//		Initialize.fillRecentContcListDb();
		recentContc=new RecentContcAdp(this,notifyChangeHd);
		recentContcLv.setAdapter(recentContc); 
		if(GlobalVar.recentContcArray.size()>0)
		recentContc.notifyDataSetChanged();
		
		
		/*   
		MsgData= new ArrayList<HashMap<String, String>>();
		 for(int i=0;i<15;i++)
		    {HashMap<String,String> hm=new HashMap<String,String>();
		     hm.put("name","iiiis");
		     MsgData.add(hm);
	     	}
		MsgAdapter= new SimpleAdapter
		(this, MsgData, R.layout.mianwin_elv_item, 
    	 new String[]{"name"}, //{}is to initialize
    	 new int[]{R.id.nameInCTL}
		);
		lv.setAdapter(MsgAdapter);
        
		*/
		
	}

	protected void onResume()
	{if(GlobalVar.recentContcArray.size()>0)
	     recentContc.notifyDataSetChanged();
	 super.onResume();
	}
	private void setOnClicks()
	{ searchUser.setOnClickListener(new OnClickListener()
    { @Override
		public void onClick(View arg0) 
      {Intent intent=new Intent(MainWinActivity.this,SearchUser.class);
       startActivity(intent);
      }
}
);
      
	 btQstEntrance.setOnClickListener(new OnClickListener()
     {	@Override
			public void onClick(View arg0)
			   {// TODO Auto-generated method stub
				Intent intent=new Intent(MainWinActivity.this,QuestionPlazaEntrance.class);
			    startActivity(intent);
            }
     	}
      );
	 
	 Test.setOnClickListener
     (new OnClickListener()
     		{   @Override
     			public void onClick(View v)
     			{  //open TestGeneral win
     				Intent intent=new Intent(MainWinActivity.this,TestGeneral.class);
     				startActivity(intent);
     			}
     		} );
		 
	 slidingmenubtn.setOnClickListener(new OnClickListener()
     {@Override
			public void onClick(View v)
			{SlidingMenu mMenu = (SlidingMenu) findViewById(R.id.mainview);
			 mMenu.toggle(); 
			}        	
     });
		
	 popmsg.setOnClickListener(new OnClickListener()
     {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (popwin != null && popwin.isShowing()) 
				{  
	                popwin.dismiss();  
	            } 
				else
				{   popwin = new PopupWindow(customView,LayoutParams.FILL_PARENT, 
	                		LayoutParams.FILL_PARENT, true);
	                MsgAdapter.notifyDataSetChanged(); 
	                //popwin.showAsDropDown(v, -400, 0);
	                //.setOutsideTouchable(true);
	                //showAsDropDown(v, 0, 5);  
	                popwin.setAnimationStyle(R.style.popAnimationFade);
	                popwin.showAtLocation(getLayoutInflater().inflate(R.layout.mainwin,null),
	                		Gravity.LEFT | Gravity.TOP, 0, 0);
	            }  
			}        	
     });
	
	 popclose.setOnClickListener(new OnClickListener()
     {
			@Override
			public void onClick(View v) {
				if (popwin != null && popwin.isShowing()) 
				{   Log.v("popwin","popwin si showing");
	                popwin.dismiss();  
	            } 
			}
     });
	
	 groupbtn.setOnClickListener(new OnClickListener()
     {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainWinActivity.this, GroupWinActivity.class);
				startActivity(intent);
			}
     });
	 
	 bt_myChildClass.setOnClickListener(new OnClickListener()
	      {	@Override
			public void onClick(View arg0) 
	        {Intent intent=new Intent(MainWinActivity.this,ContactorsList.class);
	    	  startActivity(intent);
			}
		   });
	 
    bt_mySupperClass.setOnClickListener(new OnClickListener()
       {@Override
		public void onClick(View arg0) 
        {Intent intent=new Intent(MainWinActivity.this,TalkActivity.class);
          String selectMySuperC="select superClassId,superClassName from myDetails where " +
         		"myId="+GlobalVar.myId+" and superClassId!='' ;";
         Cursor cur=GlobalVar.sb.rawQuery(selectMySuperC,null);
         Contactors contactor;
        int count=cur.getCount();
         if(cur.getCount()>0)// if i do have a superClass/teacher
         	{   //add to recentTalk
                cur.moveToNext();
        	    contactor=new Contactors();
                contactor.userId=cur.getLong(0);
                contactor.Name=cur.getString(1);
                contactor.avatarLink="";
                contactor.Role=1;
        	 /*
        	    boolean checkRecTable=GlobalVar.checkRecentTable(contactor.userId);//0 is the userId collumn
         		if(checkRecTable==true)
                  GlobalVar.insertRecentTable(contactor);
         		boolean checkRecArray=GlobalVar.checkRecentTalkArray(contactor.userId);
         		    if(checkRecArray==true)
         		    	GlobalVar.addRecentTalkArray(contactor);
         		*/    
         		//GlobalVar.insertRecentTable();      	
         	    GlobalVar.nowTalkingWithId=contactor.userId;
         	    GlobalVar.nowTalkingWithName=contactor.Name; 
         		Bundle b=new Bundle();
         		b.putLong("userId",contactor.userId);
         		b.putString("userName",contactor.Name);
         		intent.putExtras(b);
         	    startActivity(intent);
               }
          else
           Toast.makeText(MainWinActivity.this,"you don't have any superClass",Toast.LENGTH_LONG).show();
    	 }
    	  });
	 
	 
	}
    private void findViewById()
    {
   	Test=(Button)findViewById(R.id.Test);
    btQstEntrance=(Button)findViewById(R.id.questionInSLM);
    searchUser=(Button)findViewById(R.id.searchUsersInSLM);
    slidingmenubtn=(Button)findViewById(R.id.mainwin_title_btn);
    customView = getLayoutInflater().inflate(R.layout.popwin_getmsg, null, false);  
	lv=((ListView) customView.findViewById(R.id.popwin_lv));
	popmsg=(Button)findViewById(R.id.mainwin_title_btn_pop);
	popclose=(Button)customView.findViewById(R.id.popwin_close);
	groupbtn=(Button)findViewById(R.id.mainwin_body_btn_group); 
    bt_myChildClass=(Button)findViewById(R.id.bt_myChildClass_Mw);
    bt_mySupperClass=(Button)findViewById(R.id.bt_mySupperClass_Mw);
    recentContcLv=(ListView)findViewById(R.id.lv_recentContactor_Mv); 
    }
  
    public class NotifyChangeHandler extends Handler
   { @Override
	   public void handleMessage(Message msg)
              {if(GlobalVar.recentContcArray.size()>=0)
	               recentContc.notifyDataSetChanged();
              }
	   
   }
    
    public NotifyChangeHandler getNotifyHd()
    { NotifyChangeHandler notifyChangeHd=new NotifyChangeHandler();
      return notifyChangeHd;
    }
  
   }



