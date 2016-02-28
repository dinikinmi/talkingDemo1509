package com.example.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
/**
 * 
 * @author Himi
 * @瑙ｉ噴 姝ょ被鎴戜滑鍙渶瑕佷紶寤轰竴涓瀯閫犲嚱鏁�浠ュ強閲嶅啓涓や釜鏂规硶灏監K鍟︺�
 * 
 */
public class MySqlite extends SQLiteOpenHelper {
	public final static int VERSION = 4;
	public final static String TABLE_NAME = "MS";
	public final static String ID = "id";
	public final static String TEXT = "text";
	public static final String DATABASE_NAME = "MyApp.db";
	
	public MySqlite(Context context,String dataBaseName,int Version)
	{ super(context, dataBaseName, null, VERSION); 
	} 
	@Override
	public void onCreate(SQLiteDatabase db) 
	{ 		
	  String str_sqlBuild_MS = "CREATE TABLE MS ( STUDENTS_ID"+
				 " Long PRIMARY KEY ," + "Name VARCHAR(20),"+"Unread VARCHAR," + 
				  "Avatar VARCHAR);";
				db.execSQL(str_sqlBuild_MS);
				
      String str_sqlBuild_myDetails="create table myDetails("+
    		 " superClassId long,superClassName verchar,myId,myName);";
      db.execSQL(str_sqlBuild_myDetails);
				
				
	String str_sqlBuildContactorList="create table ContactorList (" +
			"userId Long primary key," +
			"userName varchar(20)," +
			"role Integer," +
			"avartarLink varchar" +
			
			");";
	db.execSQL(str_sqlBuildContactorList);		
			
	String str_sqlBuild_recentTalking="create table recentTalk (" +
			"userId long primary key," +
			"userName varchar(20)," +
			"role Integer," +
			"avartarLink varchar," +
			"unreadCount integer" +
			");";		
	db.execSQL(str_sqlBuild_recentTalking);
	
      String str_sqlBuild_CHATTING ="CREATE TABLE CHATTING (rowId INTEGER primary key autoincrement,msgId Long ,fromId Long"+
		", toId Long,audioPath VARCHAR,"+
    		 "Content,MessageType INTEGER,"
    		  +"buildTime Long,Duration INTEGER,readLabel INTEGER);" ;
          db.execSQL(str_sqlBuild_CHATTING);
	      Log.i("chat", "chat");
	      
	   String str_sqlBuild_UNSEND="CREATE TABLE UNSEND (msgId Long,fromId Long,toId Long,"+
		"audioPath VARCHAR,"+
    		  "Content VARCHAR,msgType INTEGER,"
    		  +"buildTime Long,Duration INTEGER,Status integer);";
    		   
	    db.execSQL(str_sqlBuild_UNSEND);
	   
	   String str_sqlBuild_UNLOAD="CREATE TABLE UNLOAD (msgId Long,fromId Long"+
				",toId Long,audioPath VERCHAR,"+
	    		  "Content VARCHAR,messageType INTEGER,"
	    		  +"buildTime Long,Duration INTEGER);" ;
	     db.execSQL(str_sqlBuild_UNLOAD);
	
	     String str_sqlBuild_NUST="CREATE TABLE NUST (Nust VARCHAR(20));" ;
		     db.execSQL(str_sqlBuild_NUST);
		
	    String str_sqlBuild_TableList="CREATE TABLE TABLElIST (existTable VARCHAR(20));";
	        db.execSQL(str_sqlBuild_TableList);
	        
	        
	    String str_sqlBuild_Questions="create table QUESTIONS (fromId varchar,questionId varchar,"+
	    		"myAnswer varchar,rightAnswer varchar,audioPath varchar);"; 
	    db.execSQL(str_sqlBuild_Questions);
	    
	
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{ Log.v("Himi", "onUpgrade888");
     //Toast.makeText(null, DATABASE_NAME, 3);
	    Log.i("te", "111");
		onCreate(db);
	} 
//	public void onDowngrade(){};
	
}  