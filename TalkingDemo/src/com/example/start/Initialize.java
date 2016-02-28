package com.example.start;

import java.io.File;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sqlite.MySqlite;
import com.example.sqlite.Select;
import com.example.talkingdemo.MainActivity;
import com.example.talkingdemo.MainWinActivity;
import com.example.talkingdemo.ContactorsList.ChildListHandler;

import BackGround.FileReceive;
import BackGround.HandleUnsend;
import DataSaveObject.Contactors;
import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public  class Initialize extends Activity
{	public  GlobalVar gb=new GlobalVar();
    public Select sl=new Select();
    public MySqlite mySqlite;
    private static String contactorsListURL="http://"+GlobalVar.hostURL+"server_app/ChildRequestHandler";
    static ChildListHandler  contactDlHandler=null;
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{   
		super.onCreate(savedInstanceState);
		String dataBaseName;
		//myId= the id of user who login success
		    dataBaseName="Data"+GlobalVar.myId+".db";
		    mySqlite=new MySqlite(this, dataBaseName, 0);
	        GlobalVar.sb=mySqlite.getWritableDatabase();
      
	    if(checkContactorListTb()==false)  //if the contactorslist is vacumn    
	           downloadContactors_Db();
	    else
	    {getCont_Db_Array();}
      
        fillMyDetailsDb();
        
        Log.i("handleUnsend Thread","handle Unsend Thread build and start");
        Intent i=new Intent(Initialize.this,MainWinActivity.class);
        startActivity(i);
     }

	 private void getCont_Db_Array()
	 {String selectAll="select * from contactorList;";
	  Cursor cur=GlobalVar.sb.rawQuery(selectAll,null);
	  if(cur.getCount()>0)
	  {for(cur.moveToNext();!cur.isAfterLast();cur.moveToNext())
	    {Contactors contactor=new Contactors();
	     contactor.avatarLink="";
	     contactor.Name=cur.getString(1);
	     contactor.userId=cur.getLong(0);
	     contactor.Role=cur.getInt(2);
	     GlobalVar.contactorsList.add(contactor);
		}
	 }
		 
	 }

	public void downloadContactors_Db()
	   {GlobalVar.contactorsList.clear();
	   int i=0;
	   for(i=0;i<10;i++)
	   {   String sendData="wantedId="+GlobalVar.myId;
		    contactDlHandler=new ChildListHandler();
			SendAndGet sendAndGet=new SendAndGet(sendData, contactorsListURL,contactDlHandler);
			sendAndGet.start();
   }

}
     
	 public static void fillRecentContcListDb()
	 {String select="select * from recentTalk ;";
	  Cursor cur=GlobalVar.sb.rawQuery(select,null);
	  if(cur.getCount()>0)
	  {for(cur.moveToNext();!cur.isAfterLast();cur.moveToNext())
	    {Contactors contactor=new Contactors();
	     contactor.avatarLink=cur.getString(3);
	     contactor.Name=cur.getString(1);
	     contactor.Role=cur.getInt(2);
	     contactor.userId=cur.getInt(0);
		 contactor.unreadCount=cur.getInt(4);
		 GlobalVar.recentContcArray.add(contactor);
	    }
		  
		  
	  }
		 
		 
	 }

	 public static void fillMyDetailsDb()
	 {String selectMyId="select myId from myDetails where myId="+GlobalVar.myId;
	    Cursor myIdCur= GlobalVar.sb.rawQuery(selectMyId,null);
	    if(myIdCur.getCount()==0)
	    {String insertMyId="insert into myDetails (myId,myName) values (" 
	    +GlobalVar.myId+",'"+GlobalVar.myName+"');";
	     GlobalVar.sb.execSQL(insertMyId);
	     	
	    }
	 }
	 
	 public static boolean checkContactorListTb()
	    {String selectAll="select * from contactorList;";
	     Cursor cur=GlobalVar.sb.rawQuery(selectAll, null);
	      if(cur.getCount()>0)
	    	  return true;
	      else return false;
		   }
	 
	 public class ChildListHandler extends Handler
		{	@Override
			public void handleMessage(Message msg)
		   {Bundle b=msg.getData();
		    String Reply=b.getString("Reply");
		    try {JSONArray ja=new JSONArray(Reply);
		         GlobalVar.contactorsList.clear();
			     
		         for(int i=0;i<ja.length();i++)
			     {JSONObject jo=ja.getJSONObject(i);
			     Contactors contactors=new Contactors();
			     contactors.Name=jo.getString("childClassName");
			     contactors.userId=jo.getLong("childClass");
			     GlobalVar.contactorsList.add(contactors);
			     GlobalVar.todayVergin.add(contactors.userId);
			     String insertContactorListTb="insert into ContactorList " +
			     		"(userId,userName,role,avartarLink)" +
			      "values("+contactors.userId+",'"+contactors.Name+"',"+0+",'');";
			     GlobalVar.sb.execSQL(insertContactorListTb);
			     }
		         
		    }catch (JSONException e) 
			{e.printStackTrace();	}
		    
		   }
		}	 
}
