package com.example.talkingdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestGeneral extends Activity
{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_general);
		Button newTest=(Button)findViewById(R.id.NewTest);
		newTest.setOnClickListener(new OnClickListener()
		{
			
		@Override
		public void onClick(View arg0)
		{
			// jump to NewTest Activity
			Intent intent=new Intent(TestGeneral.this,NewTest.class);
			startActivity(intent);
		}
		}
				);
		
		
		
		
		
}
	
	
}