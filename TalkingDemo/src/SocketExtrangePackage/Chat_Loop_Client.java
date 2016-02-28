package SocketExtrangePackage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.example.talkingdemo.MainWinActivity;
import com.example.talkingdemo.TalkActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import utils.s;
import DataSaveObject.Contactors;
import Global.GlobalVar;

public class Chat_Loop_Client extends Thread
{public Socket chatLoopSk;
 public ObjectInputStream objInps;
 public ObjectOutputStream objOps;
 public GeneralReqObj requestObj;
 public ChatObjAryObj coAryObj;
 public Socket socket;
 public int sleepLastLong=3000;
 @Override
 public void run()
 {While_Out:while(true)
 {try{sleep(10000);}catch(Exception e){}
   GlobalVar.connectServer(socket);
 
 while(socket.isConnected())
 { 
  if(socket.isConnected())
  {try
    {sleep(sleepLastLong);
	  getIOStreamReady(socket);	
     GlobalVar.sendRequestObj(GlobalVar.myId, 1, objOps);
	 coAryObj=(ChatObjAryObj)objInps.readObject();
	 if(coAryObj.chatObjArray.size()>0)
	 {sleepLastLong=3000;
	  LoadDialog loadDialog=new LoadDialog(coAryObj.chatObjArray);
	  loadDialog.start();
	  sleep(1000);
	 }
    }catch(Exception e)
	{addSleepTime();}
  } 
  
 }
  
  }	

 
 
}

private void addSleepTime() 
{if(sleepLastLong<30000)
	sleepLastLong=+2000;
}

public class LoadDialog extends Thread
{   ArrayList<ChatObject> coAry;
	
	public LoadDialog(ArrayList<ChatObject> coAry)
     {this.coAry=coAry;}
	@Override
	public void run()
	{for(int i=0;i<(coAry.size()-1);i++)
	  {loadChatObject(coAry.get(i));
	  }
	
	}
	
	private void loadChatObject(ChatObject chatObject) 
	{setDataInRecentTable(chatObject);
	 setDataInRecentTArray(chatObject);
	 setDataInContactorDb(chatObject);
	}
	
	
	private void setDataInContactorDb(ChatObject chatObject)
	{if(!idFound_In_ContactorTb(chatObject.fromId))
	  {String insertContactorList="insert into contactorList" +
	  		"(userId,userName,role,avartarLink) values(" +
	  		chatObject.fromId+",'"+chatObject.fromUserName+"'," +
	  				"0" +"'');";
	  GlobalVar.sb.execSQL(insertContactorList);
		
	  }
	
	}
	private boolean idFound_In_ContactorTb(long fromId) {
		String select="select * from ContactorList where userId="+fromId
				+";";
		Cursor contactorCur=GlobalVar.sb.rawQuery(select, null);
		if(contactorCur.getCount()>0)
		return true;
		else
			return false;
	}
	private void setDataInRecentTArray(ChatObject chatObject) 
	{boolean foundId_In_RtArray=false;
	 FOR: for(int i=0;i<(GlobalVar.recentContcArray.size()-1);i++)
	   {if(chatObject.fromId==GlobalVar.recentContcArray.get(i).userId)
	      {GlobalVar.recentContcArray.get(i).unreadCount++;
           foundId_In_RtArray=true;  
	       GlobalVar.buildDialogTable("userId"+chatObject.fromId);
           insertDialogTable(chatObject);
           loadNewDialogUI(chatObject);
           break FOR;	      
	      }  
	  }
	  if(foundId_In_RtArray==false)
	   {s.say("not found  id in recentAry,ready to add into it");
		dataFromCo_To_recentTAry(chatObject);
	    recentT_NotifyChange();
	   }
	  
	
	}
	
	private void recentT_NotifyChange()
	{s.say("begging;;recentT_NotifyChange");
	 s.say("reday to tell the ui thred update recentTalk Ary");
	 MainWinActivity mainWinAct=new MainWinActivity();
	 Handler recentT_NotifyChangeHd=mainWinAct.getNotifyHd();
	 Message msg=recentT_NotifyChangeHd.obtainMessage();
	 msg.sendToTarget();
	}
	
	private void loadNewDialogUI(ChatObject chatObject)
	{   TalkActivity talkActivity=new TalkActivity();
        Handler loadDialogHd=talkActivity.getLoadDialogHd();
        Message msg=loadDialogHd.obtainMessage();
        Bundle toTalkActB=new Bundle();
        toTalkActB.putParcelable("chatObject",(Parcelable) chatObject);
        msg.setData(toTalkActB);
        msg.sendToTarget();
	}
	private void insertDialogTable(ChatObject chatObject)
	{s.say("begging :: Chat_Loop_Client ::insertDialogTable");
	 String insert="insert into user"+chatObject.fromId+"(" +
	 		"msgId,fromId,fromUserName,toId,toUserName,Content," +
	 		"msgType,buildTime,Status)values(" +
	 		chatObject.msgId+"," +
	 	    chatObject.fromId+",'" +
	 	    chatObject.fromUserName+"'," +
	 	    GlobalVar.myId+",'" +
	 	    GlobalVar.myName+"','" +
	 	    chatObject.Content+"',"+
	 	    chatObject.msgType+","+
	 	    chatObject.Time+",1);";
	 GlobalVar.sb.execSQL(insert);
	}
	
	private void dataFromCo_To_recentTAry(ChatObject chatObject) 
	{	Contactors contactor=new Contactors();
	    contactor.Name=chatObject.fromUserName;
	    contactor.userId=chatObject.fromId;
	    contactor.Role=0;
	    contactor.unreadCount=1;
	    GlobalVar.recentContcArray.add(0, contactor);
	    s.say("add successfully");
	}
	private void setDataInRecentTable(ChatObject chatObject) 
	{Cursor curRecentTb=check_Exists_RecentTable(chatObject.fromId);
		if(curRecentTb!=null&&curRecentTb.getCount()>0)
		{int oldUnreadCount=curRecentTb.getInt(4);
		 updUnreadCount_RectTb(oldUnreadCount++,chatObject.fromId);	
		 insertRecentTb(chatObject.fromId,chatObject.fromUserName);
		
		}
	}
	private void insertRecentTb(long fromId, String fromUserName) {
	String insert="insert into recentTalk (userId,userName," +
			"role,avartarLink,unreadCount) values(" +
			fromId+","+fromUserName+",0,'',1);";
	GlobalVar.sb.execSQL(insert);
	}
	
	private void updUnreadCount_RectTb(int newUnreadCount, long fromId)
	{String update="update recentTalk set unreadCount=" +
				newUnreadCount+";";
	GlobalVar.sb.execSQL(update);
	}
	private Cursor check_Exists_RecentTable(long fromId)
	{ 
		String select="select * from recentTalk where userId=" +
				+fromId+";";
		Cursor RecentTalkTbcur=GlobalVar.sb.rawQuery(select,null);
		      return RecentTalkTbcur;
		} 	
}

private void getIOStreamReady(Socket socket) throws Exception
{s.say("begginng--getIOStreamdy()");
 objInps=new ObjectInputStream(socket.getInputStream());
 objOps=new ObjectOutputStream(socket.getOutputStream());
 
}



}
