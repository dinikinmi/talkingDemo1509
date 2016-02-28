package com.example.talkingdemo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.talkingdemo.ui.ContactorsLvAdapter;

import DataSaveObject.Contactors;
import Global.GlobalVar;
import Global.SendAndGet;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ContactorsList extends Activity
{    
	 private String contactorsListURL="http://"+GlobalVar.hostURL+"server_app/ChildRequestHandler";
	 public ListView contactorLv;
	 ContactorsLvAdapter contcLvAdp;
//	 public ArrayList<Contactors> contactorsArray;
	@Override
	public void onCreate(Bundle b)
	{ super.onCreate(b);
	 setContentView(R.layout.contactor_list);
//	 contactorsArray=new ArrayList<Contactors>(); 
	 contactorLv=(ListView)findViewById(R.id.lv_contatcotrList_Cl);
	 contcLvAdp=new ContactorsLvAdapter(this);
	 contactorLv.setAdapter(contcLvAdp);
    if(GlobalVar.contactorsList.size()>0)
    	contcLvAdp.notifyDataSetChanged();
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
		     }
		    contcLvAdp.notifyDataSetChanged();
		  } catch (JSONException e) 
		{e.printStackTrace();	}
	    
	   }
		
	}
       
  
	
	
   }
	

