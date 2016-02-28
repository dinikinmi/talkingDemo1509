package com.example.talkingdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Global.GlobalVar;
import Global.SendAndGet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchUser extends Activity
{  public Button bt_startSearch;
   public ImageButton ib_Avatar;
   public EditText et_userNameInput;
   public String searchURL="http://"+GlobalVar.hostURL+"server_app/SearchUser";
   public String getUserName="";
   public Long getUserId=0L;
	
	@Override
	public void onCreate(Bundle b)
	{   super.onCreate(b);
	    setContentView(R.layout.search_user);
	    bt_startSearch=(Button)findViewById(R.id.bt_startSearch_Su);
	    ib_Avatar=(ImageButton)findViewById(R.id.ib_Avatar_Su);
	    bt_startSearch.setOnClickListener(new StartSearchClick());
        et_userNameInput=(EditText)findViewById(R.id.et_Input_Su);
        ib_Avatar.setVisibility(View.GONE);
        
        ib_Avatar.setOnClickListener(new OnClickListener(){
       @Override
       public void onClick(View v)
        {if(getUserName.equals("")||getUserId==0)
        	return;
        else
        {Intent intent=new Intent(SearchUser.this,UserDetails.class);
         Bundle bb=new Bundle();
         bb.putString("userName",getUserName);
         bb.putLong("Id",getUserId);
         intent.putExtras(bb);
         startActivity(intent);
        }
               	
         }
        		
        }		);
	   
	    
	}
	
	
public class StartSearchClick implements OnClickListener
  { @Override
	public void onClick(View v) 
     {String userName=et_userNameInput.getText().toString();
     SearchHandler searchHandler=new SearchHandler();
     String sendData="&userName="+userName;
     SendAndGet sendAndGet=new SendAndGet(sendData,searchURL,searchHandler);
     sendAndGet.start();
       }
  }
	
public class SearchHandler extends Handler
 {@Override
	public void handleMessage(Message msg)
      {Bundle b=msg.getData();
       String Reply=b.getString("Reply");
       if(Reply.equals("null"))
       { Toast.makeText(SearchUser.this,"User Not Exists",Toast.LENGTH_LONG).show();
    	   return;
       }
       if(Reply.equals(""))
       { Toast.makeText(SearchUser.this,"Connection Fail",Toast.LENGTH_LONG).show();
    	   return;
       }
       if(Reply.equals("462323679"))
       { Toast.makeText(SearchUser.this,"Sever Error",Toast.LENGTH_LONG).show();
    	   return;
       }
    	   try
    	   {
			JSONArray ja=new JSONArray(Reply);
	        JSONObject jo=ja.getJSONObject(0); 
	        getUserId=jo.getLong("Id");
	        getUserName=jo.getString("userName");
	        ib_Avatar.setVisibility(View.VISIBLE);
    	   } catch (JSONException e) {
			e.printStackTrace();
		}
           
       
    	   
       
      }
	
	
 }


  }



