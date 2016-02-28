package BackGround;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.os.Message;

import Global.GlobalSK;
import Global.GlobalVar;
import Global.TestObject;




	



public class TestInternet extends Thread 

{	
	public static TestObject testObject;
	public OutputStream Ops;
	public InputStream Ins;
	public DataOutputStream Dos;
	public DataInputStream Dis;
	
	public ObjectInputStream objIns;
	public ObjectOutputStream objOps;
	
	public int Icase;
	public boolean Lock;
	        
    public TestInternet(int Icase)
	{this.Icase=Icase;
	}
	
	@Override
	public void run()
{
	switch(Icase)
	{case 0:
	  startOn();
	  break;
	case 1:
		
	case 2:
		
	case 3:
		
	case 4:	
	  sendAnswer();  
	  break;
	case 5:  
	  int I5=getRightAnswer();
	  break;
	case 6:
	  getNewQuestion();
	  break;
	}
}
	
	public void startOn()
	{
		try 
	{
		Ops=GlobalVar.sk.getOutputStream();
		
		Dos=new DataOutputStream(Ops);
		Dos.writeInt(6);
		
		//send 6 to tell server to send a new test audio
		
		objIns=new ObjectInputStream(GlobalVar.sk.getInputStream());
		GlobalVar.testObject=(TestObject)objIns.readObject();
//		objIns.close();
//		Ops.close();
//		Dos.close();
		//record this question in sqlite
		//firstly check does it exists
		String Select="select * from QUESTIONS where questionId='"+
		GlobalVar.testObject.questionId+"';";
		
		Cursor C=GlobalVar.sb.rawQuery(Select,null);
		
		if(!(C.getCount()>0))
		{//it the question is not exist.
				 String Insert="Insert into QUESTIONS (fromId,questionId)values('" +
			  GlobalVar.myId+"','"+GlobalVar.testObject.questionId+"');";
				 GlobalVar.sb.execSQL(Insert);
		}
		
		
		
		
		GlobalVar.newQuestionReady=true;
		
	}catch(Exception e)
	{e.printStackTrace();
	};
	}
	
	
    public void sendAnswer()
    {//now.,,this.testObject should be = NewTest.tO_myAnswer or others
    	try 
    	{
		Dos=new DataOutputStream(GlobalVar.sk.getOutputStream());
	 	Dos.writeInt(4);
	 	//send 4 to tell server ready to get Answer
	 	Dis=new DataInputStream(GlobalVar.sk.getInputStream());
    	int Confirm=Dis.readInt();
    	//if sever return 1,means it is well ready
    	if(Confirm==1)
    	{//send the data saved in tO_MyAnswer to server
    		//and finish the after work ,leave the UI operation to MainThread
    	 objOps=new ObjectOutputStream(GlobalVar.sk.getOutputStream());
    	 objOps.writeObject(GlobalVar.testObject);
    	 //wait the server to confirm receive
    	}
    	Confirm=Dis.readInt();
    	if(Confirm==1)
    	{    Message msg=new Message();
    	     msg.what=1;
    	     
    		//if the sever receive process is confirmed 
    		//tell the mainThread to update UI
    		GlobalVar.answerSendDone=true;
    	}
    	
    	
    	
    	
    	} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
    }
    
    public void getNewQuestion()
    {
    	try 
    	{   
    		Ops=GlobalVar.sk.getOutputStream();
    		Dos=new DataOutputStream(Ops);
    		Dos.writeInt(6);
    		//send 6 to tell server to send a new test audio
    		
    		objIns=new ObjectInputStream(GlobalSK.globalSocket.getInputStream());
    		testObject=(TestObject)objIns.readObject();
//    		objIns.close();
    		//transmit the value back to mainThread
    		GlobalVar.testObject=testObject;
    		GlobalVar.newQuestionReady=true;
    		
    	}catch(Exception e)
    	{e.printStackTrace();
    	};
    	
    }
	
    
    public int getRightAnswer()
    {
    	try {
    		
			Dos=new DataOutputStream(GlobalVar.sk.getOutputStream());
			Dos.writeInt(5);
			//the the server to check answer
		    Dis=new DataInputStream(GlobalVar.sk.getInputStream());
		    int svGetRdy=Dis.readInt();
		    
		    if(svGetRdy==1)
		    {Dos.writeUTF(GlobalVar.testObject.questionId);
		     //tell sever what question you want
		     String Answer=Dis.readUTF();
		     GlobalVar.testObject.rightAnswer=Answer;
		     GlobalVar.rightAnswerReady=true;
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
    	
    }
	
}
	
	

