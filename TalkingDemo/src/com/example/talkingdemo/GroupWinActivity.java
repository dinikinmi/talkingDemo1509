package com.example.talkingdemo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GroupWinActivity extends Activity{

	private ArrayList<HashMap<String, String>> GroupData;
	private SimpleAdapter GroupAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupwin);
		
		GroupData= new ArrayList<HashMap<String, String>>();
		for(int i=0;i<3;i++)
		{
			HashMap<String,String> hm=new HashMap<String,String>();
			hm.put("name","iii"+String.valueOf(i+1));
			GroupData.add(hm);
		}
		GroupAdapter= new SimpleAdapter
				(
				this, GroupData, 
				R.layout.mianwin_elv_item, 
    			new String[]{"name"}, 
    			new int[]{R.id.tv_name_rcli}
				);
		
		ListView lv=((ListView)findViewById(R.id.group_lv));
    	lv.setAdapter(GroupAdapter);
    	
    	lv.setOnItemClickListener(new OnItemClickListener()
    	{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(GroupWinActivity.this,TalkActivity.class);
				startActivity(intent);
			}   		
    	});
		
    	
	}
	
	
}
