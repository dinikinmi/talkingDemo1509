package com.example.talkingdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.talkingdemo.SearchUser.SearchHandler;
import com.example.talkingdemo.ui.UD_ChildListAdp;

import DataSaveObject.Contactors;
import Global.GlobalVar;
import Global.SendAndGet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetails extends Activity{
	public Intent preActInt;
	public Bundle preIntBdl;
	public String userName;
	public Long  userId;
	public TextView tv_UserName;
	public String extendsActionURL="http://"+GlobalVar.hostURL+"server_app/ExtendsRequestHandler";
	public String getChildURL="http://"+GlobalVar.hostURL+"server_app/ChildRequestHandler" ;
	public String getSupperClassURL="http://"+GlobalVar.hostURL+"server_app/SupperClassRequestHd";
	//it is a servlet in server
	public Button bt_extendsRequest;
	public Button bt_getChildClass;
	public Button bt_getSupperClass;
	public ListView lv_childClassList;
	public String extendsButtonText="extends from it";
	public ArrayList<Contactors> childArray;
	public UD_ChildListAdp childListAdp;
	
    @Override
	public void onCreate(Bundle b)
	{  	setContentView(R.layout.user_detail);
    	preActInt=getIntent();
    	preIntBdl=preActInt.getExtras();
    	userName=preIntBdl.getString("userName");
    	userId=preIntBdl.getLong("Id");
    	bt_extendsRequest=(Button)findViewById(R.id.bt_Extends_Ud);
       	bt_getChildClass=(Button)findViewById(R.id.bt_showChildClass_Ud);
    	bt_getSupperClass=(Button)findViewById(R.id.bt_showSupperClass_Ud);
      	lv_childClassList=(ListView)findViewById(R.id.lv_childList_Ud);
    	tv_UserName=(TextView)findViewById(R.id.tv_userName_Ud);
    	tv_UserName.setText(userName);
    	childArray=new ArrayList<Contactors>();
    	childListAdp=new UD_ChildListAdp(this,childArray);
    	lv_childClassList.setAdapter(childListAdp);
        lv_childClassList.setVisibility(View.GONE);
        bt_getChildClass.setOnClickListener(new GetChildButtonClick())  ;  
    	bt_extendsRequest.setOnClickListener(new ExtendsButtonClick());
    	bt_getSupperClass.setOnClickListener(new GetSupperClassClick());
    	
    	super.onCreate(b);
    }
    
public class ExtendsButtonClick implements OnClickListener
    {
	@Override
	public void onClick(View arg0) 
	{	Long addedId=preIntBdl.getLong("Id");
	    Long requestorId=GlobalVar.myId;
	    if((addedId-requestorId)==0)
	    {Toast.makeText(UserDetails.this,"you can't extends from yourself",Toast.LENGTH_LONG).show();  	
	    return;//can extends form myself
	    }
	    Map<String,String> params=new HashMap<String,String>();
	    params.put("supperClassName",userName );
	    params.put("childClassName",GlobalVar.myName);
	    params.put("addedId",""+addedId);
	    params.put("requestorId",""+requestorId);
	    String sendData=GlobalVar.packageSentData(params);
	    bt_extendsRequest.setText("Handling");
	    bt_extendsRequest.setEnabled(false); 
	    SendAndGet sendAndGet=new SendAndGet(sendData,extendsActionURL,new ExtendsMessageHd());
	    sendAndGet.start();
    }
	   }

public class GetChildButtonClick implements OnClickListener
{ @Override
	public void onClick(View v)
    {Map<String,String> params=new HashMap<String,String>();
     params.put("wantedId",""+userId);
     String sendData=GlobalVar.packageSentData(params);
     ChildListHandler childListHandler=new ChildListHandler();
     SendAndGet sendAndGet=new SendAndGet(sendData,getChildURL,childListHandler);
     sendAndGet.start();
	}
	
}

public class GetSupperClassClick implements OnClickListener
   {
	@Override
	public void onClick(View arg0)
	 {
    GetSupperClassHd getSCH=new GetSupperClassHd();
    String sendData="&userId="+userId;
    SendAndGet sendAndGet=new SendAndGet(sendData,getSupperClassURL,getSCH);
    sendAndGet.start();
	 }
   }

public class ExtendsMessageHd extends Handler
   {@Override
	public void handleMessage(Message msg)
      {Bundle b=msg.getData();
       String Reply=b.getString("Reply");
      try{
       if(Reply.equals("462323679"))
       {Toast toast=Toast.makeText(UserDetails.this,"this is your child class",Toast.LENGTH_LONG);
        toast.show();
        bt_extendsRequest.setText(extendsButtonText);
	    bt_extendsRequest.setEnabled(true); 
	    return;
       }
       if(Reply.equals("330346275"))//success
       {Cursor cur=GlobalVar.getNowSuperClassIdDb();
        if(cur.getCount()>0)
        {cur.moveToNext();
        Long oldSuperClassId=cur.getLong(0);  
        String oldSuperClassTableName="user"+oldSuperClassId;
        GlobalVar.dropTable(oldSuperClassTableName);//delete the old dialog table if exists
        }
        updateSuperClassDb();
        Toast toast=Toast.makeText(UserDetails.this,"Extends Success",Toast.LENGTH_LONG);
        toast.show();
        bt_extendsRequest.setText(extendsButtonText);
  	    bt_extendsRequest.setEnabled(true);      
  	    return;
       }
       
       if(Reply.equals("18028628619"))
       {Toast toast=Toast.makeText(UserDetails.this,"Connection Fail",Toast.LENGTH_LONG);
        toast.show();
        bt_extendsRequest.setText(extendsButtonText);
	    bt_extendsRequest.setEnabled(true); 
        return;
       }
       if(Reply.equals("13580578945"))
       {Toast toast=Toast.makeText(UserDetails.this,"It's Your supperClass",Toast.LENGTH_LONG);
       toast.show();
       bt_extendsRequest.setText(extendsButtonText);
	    bt_extendsRequest.setEnabled(true); 
       return;
       }
       
      }catch(Exception e)
      {
        bt_extendsRequest.setText(extendsButtonText);
	    bt_extendsRequest.setEnabled(true); 
      }
      }
	  }

public class ChildListHandler extends Handler
{
	@Override
	public void handleMessage(Message msg)
   {Bundle b=msg.getData();
    String Reply=b.getString("Reply");
    try {
		JSONArray ja=new JSONArray(Reply);
        childArray.clear();
	    for(int i=0;i<ja.length();i++)
	    {JSONObject jo=ja.getJSONObject(i);
	     Contactors contactors=new Contactors();
	     contactors.Name=jo.getString("childClassName");
	     contactors.userId=jo.getLong("childClass");
	     childArray.add(contactors);
	     }
	    lv_childClassList.setVisibility(View.VISIBLE);
	    childListAdp.notifyDataSetChanged();
	    
    } catch (JSONException e) 
	{e.printStackTrace();	}
    
   }
	
}

public class GetSupperClassHd extends Handler
    {
	 @Override
	  public void handleMessage(Message msg)
	   {Bundle b=msg.getData();
	    String Reply=b.getString("Reply");
	    Toast toast=Toast.makeText(UserDetails.this,Reply,Toast.LENGTH_LONG);
	    toast.show();
		if(Reply==null||Reply.equals("null")||Reply.length()<2)
		{return;
		}
		try 
		{
		JSONArray ja=new JSONArray(Reply);
	    JSONObject jo=ja.getJSONObject(0);
	    Bundle bb=new Bundle();
	    bb.putLong("Id",jo.getLong("supperClass"));
	    bb.putString("userName",jo.getString("supperClassName"));
	    Intent intent =new Intent(UserDetails.this,UserDetails.class);
	    intent.putExtras(bb);
	    startActivity(intent);
		} catch (JSONException e)
          {e.printStackTrace();}
		
		
		 
	   }
	
	}

private void updateSuperClassDb()
 {	String updateSuperClass="update myDetails set superClassId="+userId
          +",superClassName='"+userName+"' where myId="+GlobalVar.myId+";";
    GlobalVar.sb.execSQL(updateSuperClass);
 }


}




