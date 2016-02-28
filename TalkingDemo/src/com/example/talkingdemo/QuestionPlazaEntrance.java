package com.example.talkingdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QuestionPlazaEntrance extends Activity

{  private Button btNewQuestion;
   private Button btMyQuestion;
   private Button btMyAnswer;
   private Button btPostNewQuestion;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_entrance);
		btNewQuestion=(Button)findViewById(R.id.newQuestionsInQE);
		btMyQuestion=(Button)findViewById(R.id.myQuestionsInQE);
		btMyAnswer=(Button)findViewById(R.id.myAnswersInQE);
		btPostNewQuestion=(Button)findViewById(R.id.postNewQuestionInQE);
		
		
		btPostNewQuestion.setOnClickListener(new OnClickListener()
		{  	@Override
			public void onClick(View v) {
		    Intent intent=new Intent(QuestionPlazaEntrance.this,PostNewQuestion.class);
			startActivity(intent);	
			}
			
			
		  }
				
				);
		
		btNewQuestion.setOnClickListener(new OnClickListener()
		{	@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(QuestionPlazaEntrance.this,NewQuestionList.class);
				startActivity(intent);
		}
			
			
			
		});

		btMyQuestion.setOnClickListener(new OnClickListener()
		{   @Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(QuestionPlazaEntrance.this,MyQuestions.class);
			    startActivity(intent);
			}
			
			
			
		});
		  
		btMyAnswer.setOnClickListener(new OnClickListener()
		{   
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			  Intent intent=new Intent(QuestionPlazaEntrance.this,MyAnswers.class);
			  startActivity(intent);
				
			}
			
			
		}
				
				);
		
				
				
		
		
	}
	
	

}
