package com.example.talkingdemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.example.sqlite.Select;

import BackGround.TestInternet;
import Global.GlobalSK;
import Global.GlobalVar;
import Global.TestObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

    public class NewTest extends Activity {
	public static TestObject tO_MyAnswer;
	public static TestObject tO_NewQuestion;
	public static TestObject tO_RightAns;
	
	public static  String testAudioDc;
	public static String testAudioPath;
	public static String testAudioFileName;
	public static String questionId;
	
	public static String myAnswer;

	public MediaPlayer Mp;	
	public OutputStream Ops;
	public InputStream Ins;
	public DataOutputStream Dos;
	public DataInputStream Dis;
	
	public ObjectInputStream objIns;
	public ObjectOutputStream objOps;
	
	public static Boolean Ready;
    public static Boolean sendAnswer;
    
	public static Handler newQuestionHan;
	public static Handler rightAnswerHan;
	public static Handler newCreate;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		//
		GlobalVar.newQuestionReady=false;
		GlobalVar.answerSendDone=false;
		GlobalVar.rightAnswerReady=false;
		
		TestInternet testInternet=new TestInternet(0);
		testInternet.start();
		
		boolean B=true;
		while(B)
		{   
			if(GlobalVar.newQuestionReady==true)
			{super.onCreate(savedInstanceState);
			setContentView(R.layout.test_new);
						
			tO_NewQuestion=GlobalVar.testObject;
			GlobalVar.newQuestionReady=false;
			//the data is packaged into TestObject and
			// transfered via GlobaVar 
			buildInterface(); 
			B=false;
			}
        }
	}
		
				
		
	  	public void buildInterface()
	  	{
	  	//build a path to save the TestAudio
		questionId=tO_NewQuestion.questionId;
		testAudioFileName=questionId+tO_NewQuestion.fileExtension;
		testAudioDc=GlobalVar.rootPath+"TestAudio"+File.separator;
		testAudioPath=testAudioDc+testAudioFileName;
		File file=new File(testAudioDc);
		if(!file.exists())
		{
		try 
		      {
			  file.mkdirs();
//			  file.createNewFile();
		      } 
		   catch (Exception e)
		   {e.printStackTrace();
		   }
		}
		try
		{
		FileOutputStream fos=new FileOutputStream(testAudioPath);
		fos.write(tO_NewQuestion.fileByte,0,tO_NewQuestion.fileByte.length);
		fos.flush();
		fos.close();
		
		String Update="update QUESTIONS set audioPath='"+testAudioPath+"' where questionId='" +
				GlobalVar.testObject.questionId+"';";
		GlobalVar.sb.execSQL(Update);
		
		}catch(Exception e)
		{e.printStackTrace();
		}
		
		ImageView tvPlayTestAudio=(ImageView)findViewById(R.id.playTestAudio);
		Button btSubmit=(Button)findViewById(R.id.Submit);
		Button btNextQuestion=(Button)findViewById(R.id.nextQuestion);
		Button btGetAnswer=(Button)findViewById(R.id.getAnswer);
		
		
		final TextView tvRightAnswer=(TextView)findViewById(R.id.tvRightAnswer);
		final TextView tvMyAnswer=(TextView)findViewById(R.id.myAnswer);
		final TextView tvQuestionId=(TextView)findViewById(R.id.questionId);
		
		final EditText edMyAnswer=(EditText)findViewById(R.id.edMyAnswer);
		final String questionId=tO_NewQuestion.questionId;
		
		tvRightAnswer.setText("");
		edMyAnswer.setText("");
		tvQuestionId.setText(tO_NewQuestion.questionId);

		
		tvPlayTestAudio.setOnClickListener(new OnClickListener()
		{   @Override
			public void onClick(View arg0) {
				
				if(Mp==null)
				{Mp=new MediaPlayer();Log.i("Test media player","build Mp object ");
				}
				try
				{
					Mp.reset();
					Mp.setDataSource(testAudioPath);
			        Mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				    Mp.prepare();
				    Mp.start();
				}catch(Exception e)
				{Log.e("TestAudioPlay",e.toString());
				}
			}
		}	
		);
			
		btSubmit.setOnClickListener(new OnClickListener()
		   {
			@Override
			public void onClick(View arg0)
			{   GlobalVar.testObject=new TestObject();
				GlobalVar.testObject.myAnswer=edMyAnswer.getText().toString();	
			    GlobalVar.testObject.questionId=questionId;
			    GlobalVar.testObject.fromId=""+GlobalVar.myId;
			    //package data into tO_MyAnswer and let child thread send it out
			TestInternet testInternet=new TestInternet(4);
				testInternet.start();
				
				
				
			boolean B=true;
			
			while(B)
			{ if(GlobalVar.answerSendDone==true)
			 {  GlobalVar.answerSendDone=false;
				String Sql_insert_QUESTIONS="update "+
				"QUESTIONS set myAnswer='"+GlobalVar.testObject.myAnswer+"' where questionId='"
				+questionId+"';";
				GlobalVar.sb.execSQL(Sql_insert_QUESTIONS);
				tvMyAnswer.setText(GlobalVar.testObject.myAnswer);
				
				B=false;
			}
			}
			}
			}
		);
		
		btNextQuestion.setOnClickListener
		(new OnClickListener()
		{  @Override
		   public void onClick(View v)
		    {
			TestInternet testInternet=new TestInternet(6);
			testInternet.start();
			
			
			boolean B=true;
			while(B)
			{   
			if(GlobalVar.newQuestionReady==true)
			{
			GlobalVar.newQuestionReady=false;
			tO_NewQuestion=GlobalVar.testObject;
			//the data is packaged into TestObject and
			// transfered via GlobaVar
			tvRightAnswer.setText("");
			edMyAnswer.setText("");
			tvQuestionId.setText(tO_NewQuestion.questionId);
			NewTest.questionId=tO_NewQuestion.questionId;
			testAudioFileName=NewTest.questionId+tO_NewQuestion.fileExtension;
			testAudioDc=GlobalVar.rootPath+"TestAudio"+File.separator;
			testAudioPath=testAudioDc+testAudioFileName;
			File file=new File(testAudioDc);
			if(!file.exists())
			{
			file.mkdirs();
			}
			try
			{
			FileOutputStream fos=new FileOutputStream(testAudioPath);
			fos.write(tO_NewQuestion.fileByte,0,tO_NewQuestion.fileByte.length);
			fos.flush();
			
			String Update="update QUESTIONS set audioPath='"+testAudioPath+"'where questionId='" +
					GlobalVar.testObject.questionId+"';";
			GlobalVar.sb.execSQL(Update);
			
			}catch(Exception e)
			{e.printStackTrace();
			}
			B=false;
		    }
				
		}
		}
		}
		);
		
		btGetAnswer.setOnClickListener(new OnClickListener(){
		    @Override
			public void onClick(View v)
		{ String Select="select rightAnswer from QUESTIONS where questionId='"
		+questionId+"';";
		Cursor c=GlobalVar.sb.rawQuery(Select, null);
		if(c.getCount()>0)
		{   c.moveToNext();
			tvRightAnswer.setText(c.getString(0));
		}else
		{
		 TestInternet testInternet=new TestInternet(5);
		 testInternet.start();
		 boolean B=true;
		 while(B)
		 {if(GlobalVar.rightAnswerReady==true)
		 {  String Answer=GlobalVar.testObject.rightAnswer;
		    if(!Answer.equals(""))
		    {tvRightAnswer.setText(GlobalVar.testObject.rightAnswer);
		    }else
		    {
		    	new AlertDialog.Builder(NewTest.this).setTitle("绯荤粺鎻愮ず")//璁剧疆瀵硅瘽妗嗘爣棰�  
		    	 
		    	    .setMessage("the right answer is not yet readied")//璁剧疆鏄剧ず鐨勫唴瀹�  
		    	  
		    	     .setPositiveButton("Ok",new DialogInterface.OnClickListener() {//娣诲姞纭畾鎸夐挳   
		    	      @Override  
		    	       public void onClick(DialogInterface dialog, int which) {//纭畾鎸夐挳鐨勫搷搴斾簨浠�  
		    	       finish();  
		    	       }  
		    	     }).setNegativeButton("Back",new DialogInterface.OnClickListener() {//娣诲姞杩斿洖鎸夐挳   
		    	       @Override  
		    	      public void onClick(DialogInterface dialog, int which) {//鍝嶅簲浜嬩欢   
		    	      Log.i("alertdialog"," 璇蜂繚瀛樻暟鎹紒");  
		    	     }  
		    	     }).show();
		    }
			 
			 tvRightAnswer.setText(GlobalVar.testObject.rightAnswer);
		 B=false;
		 GlobalVar.rightAnswerReady=false;
		 }
			 
		 }
			
		}
		
			
		}
		}
				
				);
		
		
		
	}
}
