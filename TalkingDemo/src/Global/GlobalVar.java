package Global;

import java.io.File;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import utils.s;

import DataSaveObject.Contactors;
import DataSaveObject.Question;
import SocketExtrangePackage.GeneralReqObj;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class GlobalVar {
public static String usn;
public static Long mySuperClassId;
public static int Port=1314;	
public static boolean newQuestionReady;
public static Cursor Student_c;
public static Cursor Chatting_c;
public static SQLiteDatabase sb;
public static long nowTalkingWithId;
public static String nowTalkingWithName;
public static long myId=1231231;
public static String myName="OKer";
public static boolean answerSendDone;
public static boolean rightAnswerReady;
public static ArrayList<Contactors> contactorsList=new ArrayList<Contactors>();
public static ArrayList<Contactors> recentContcArray=new ArrayList<Contactors>();
public static ArrayList<Question> questionArray=new ArrayList<Question>();
public static TestObject testObject;
public static Object[] objQueue={};

public static ArrayList<String> sendQueue=new ArrayList<String>();
public static ArrayList<Map<String,Object>> objectBox=new ArrayList<Map<String,Object>>();
public static ArrayList<Long> todayVergin=new ArrayList<Long>();

public static String hostURL="192.168.1.100:8080/";
public static String socketURL="192.168.1.100";
public static int socketPort=1889;
public static Socket sk;

//public static String talkingWith;
public static String rootPath=Environment.getExternalStorageDirectory().getPath()+File.separator;

//the data source of friends list and friends group
public static String[] generalsTypes = new String[] { "aa", "bbb", "ccc","999"};




public static String[][] generals = new String[][] {
      { "rr" },
      { "wwew","fcxc","ecxz","ddfd","deee"},
      {"sdfsda", "dsfsf", "eeee", "sdfsdf", "ddd"},
      {"99","00"}
};

public static void buildDatabaseTable(String tableName){
	 String Name=tableName;
	 String str_sqlBuildTable ="CREATE TABLE " +Name+
                " (REMOTE_ID VARCHAR(20), LOCAL_ID VARCHAR(20),REMOTE_AUDIO VERCHAR, LOCAL_AUDIO VARCHAR,"+
	 	    		 "REMOTE_SAY VARCHAR,LOCAL_SAY VARCHAR,MESSAGE_TYPE VARCHAR(10),"
	 	    		  +"SEND_OUT_TIME VARCHAR ,DURATION varchar);" ;
	 	      
	 GlobalVar.sb.execSQL(str_sqlBuildTable);
} 

public static String packageSentData(Map<String,String> params)
{StringBuffer stringBuffer=new StringBuffer();
 String returnString=new String();
 try
 { 
   for(Map.Entry<String,String> entry:params.entrySet())
   {stringBuffer.append(entry.getKey());
   stringBuffer.append("=");
   stringBuffer.append(URLEncoder.encode(entry.getValue(),"utf-8"));
   stringBuffer.append("&");
    }
    stringBuffer.deleteCharAt(stringBuffer.length()-1);
    returnString=stringBuffer.toString();
    }
    catch(Exception e)
    {e.printStackTrace();}
  return returnString;
}

public static void dropTable(String tableName)
    {String dropTable="drop table if exists '"+tableName+ "';";
    GlobalVar.sb.execSQL(dropTable);
	}
public static  Cursor getNowSuperClassIdDb()
{   
	String getMySuperClass="select superClassId,superClassName from myDetails;";
    Cursor cur=sb.rawQuery(getMySuperClass, null);
    return cur;
}

public static void buildDialogTable(String tableName)
   { String buildTable="CREATE TABLE if not exists "+tableName+
		  " (rowId INTEGER primary key autoincrement,msgId Verchar ,fromId Long,fromUserName varchar"+
			",toId Long,toUserName varcher,audioPath VARCHAR,"+
  		 "Content,msgType Long,"
  		  +"buildTime Long,Duration INTEGER,readLabel INTEGER,Status INTEGER);" ;
      GlobalVar.sb.execSQL(buildTable);
	
	}

public static void delItem_RecentTalkArray(Long Id)
  { if(GlobalVar.recentContcArray.size()>0){
	FOR:for(int i=0;i<=(GlobalVar.recentContcArray.size()-1);i++)
      {if(Id==GlobalVar.recentContcArray.get(i).userId)
      {GlobalVar.recentContcArray.remove(i);
        break FOR;}
   }
  }
  }

public static void del_RecentTalkTable(Long Id)
    {String delRow="delete from recentTalk where userId="+Id+";";
      sb.execSQL(delRow);
     }

public static boolean checkRecentTalkArray(Long Id)
   {boolean exists=true;
	if(GlobalVar.recentContcArray.size()>0)
      {For: for(int i=0;i<recentContcArray.size();i++)
           {if(Id==recentContcArray.get(i).userId)
              return exists;
    	    }
	   }
	 exists=false;
	 return exists;
	}

public static void addRecentTalkArray(Contactors contacor)
     {GlobalVar.recentContcArray.add(contacor);
	 }

public static boolean checkRecentTable(Long Id)
  {String selectRecentTable="select userId from recentTalk where userId="+Id+";";
    Cursor Rs=GlobalVar.sb.rawQuery(selectRecentTable,null);
	if(Rs.getCount()>0)
	{return true;}
	else
	return false;
  }
public static void insertRecentTable(Contactors contactor)
    {String insertTable="insert into recentTalk (userId,userName,role,avatarLink) values" +
    		"("+contactor.userId+",'"+contactor.Name+"',"+contactor.Role+",'"+contactor.avatarLink+"');";
        GlobalVar.sb.execSQL(insertTable);
	}



public static void updRoleBan_ContcTable(Long Id)
   {String updateRoleBan="update ContactorList set role=1802 where userId="+Id+";";
    sb.execSQL(updateRoleBan);
   }

public static void updRoleBan_ContcArray(Long Id)
    {for(int i=0;i<=(GlobalVar.contactorsList.size()-1);i++)
         {if(Id==GlobalVar.contactorsList.get(i).userId)
           {GlobalVar.contactorsList.get(i).Role=1802;
            return;
           }
    	
         }
	}
public static void updRoleBan_recentTalkTable(Long Id)
     { String updateRoleBan="update recentTalk set role=1802 where userId="+Id+";";
	   sb.execSQL(updateRoleBan);
	  }

public static void updRoleBan_recentTalkArray(Long Id)
   {for(int i=0;i<=(GlobalVar.recentContcArray.size()-1);i++)
		 {if(Id==GlobalVar.recentContcArray.get(i).userId)
		   {GlobalVar.recentContcArray.get(i).Role=1802;
			 return;
		   }
	     }
	}


public static  boolean connectServer(Socket socket){
	boolean successFlag=false;
	try{
	socket=new Socket(socketURL,socketPort);
	if(socket.isConnected())
		successFlag=true;
	else 
		successFlag=false;
	}catch(Exception e)
	{successFlag=false;}
	return successFlag;
}

public static void sendRequestObj(long requestorId,int wantedService,ObjectOutputStream objectOps) throws Exception
{s.say("begging::sendRequestObj()");
 GeneralReqObj requestObj=new GeneralReqObj();
 requestObj.requestorId=requestorId;
 requestObj.wantedService=wantedService;
 objectOps.writeObject(requestObj);
 s.say("send request Object OK");
}

}
    
