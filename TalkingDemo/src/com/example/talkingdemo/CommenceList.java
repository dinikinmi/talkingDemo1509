package com.example.talkingdemo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.talkingdemo.ui.CommenceLvAdapter;

import DataSaveObject.Commence;
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
import android.widget.ListView;
import android.widget.Toast;

public class CommenceList extends Activity 
{   
	private Intent preActInt;
	private Bundle preIntBdl;
	private Button addCommence;
	public static ArrayList<Commence> commenceArray=new ArrayList<Commence>();

	public static CommenceLvAdapter commenceLvAdapter;
	private ListView commenceLv;
	private int ansListPos;
	private String downloadComURL="http://"+GlobalVar.hostURL+"server_app/CommenceListRequestHd";
	
	@Override
 public void onCreate(Bundle S)
   {   super.onCreate(S);
       preActInt=getIntent();
       preIntBdl=preActInt.getExtras();
       ansListPos=preIntBdl.getInt("ansListPos");
       
       
	   setContentView(R.layout.commence_list);
	   commenceLv=(ListView)findViewById(R.id.CM_commenceLv_Lv);
	   addCommence=(Button)findViewById(R.id.CM_addCommence_Bt);
	   
	   addCommence.setOnClickListener(new OnClickListener()
	 {
        @Override
		public void onClick(View arg0)
		{	Bundle b=new Bundle();
		 
        	Intent intent=new Intent(CommenceList.this,NewCommence.class);
		    intent.putExtras(preIntBdl);//inside the bundle is the ansListPostion
        	startActivity(intent);
		}
     }
	   );
	   
	   downloadCommence();
	   
	   
	   
      
        
       commenceLvAdapter=new CommenceLvAdapter(this);
       commenceLv.setAdapter(commenceLvAdapter);
       commenceLvAdapter.notifyDataSetChanged();
     
	   
	    

}
	
	   private void downloadCommence()
	   {String sendData="answerId="+AnswerList.answerArray.get(ansListPos).itselfId;
		DownloadComHd downloadComHd=new DownloadComHd();
	   SendAndGet sg=new SendAndGet(sendData,downloadComURL,downloadComHd);
	   sg.start();
	   }
       
	   public class DownloadComHd extends Handler
       {@Override
		   public void handleMessage(Message msg)
         {
    	   String Reply=msg.getData().getString("Reply");
    	   Toast.makeText(CommenceList.this,Reply,Toast.LENGTH_SHORT).show();
    	   try {
	
    		   JSONArray ja=new JSONArray(Reply);
    		   commenceArray.clear();
    		   for(int i=0;i<ja.length();i++)
			   {JSONObject jo=(JSONObject)ja.getJSONObject(i);
				Commence commence=new Commence();
				commence.Content=jo.getString("comContent");
			    commence.userId=jo.getLong("comPosterId");
			    commence.userName=jo.getString("comPosterName");
				commenceArray.add(commence);
			   }
			commenceLvAdapter.notifyDataSetChanged();
    	   }catch (JSONException e) {e.printStackTrace();}
    	   
         }
       
       }

} 
