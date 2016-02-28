package com.example.sqlite;

import android.database.sqlite.SQLiteDatabase;

public class Update {
  
//	public MySqlite mySqlite;
//	public SQLiteDatabase db;
	
  public static void addStudents2List(SQLiteDatabase db,String Id,String sName){
   String sql="INSERT INTO MS( ID, Name ) VALUES ( '" + Id +"'," +"'"+sName+"');";
	  db.execSQL(sql);
  }
  
  
}
