package com.example.sqlite;

import Global.GlobalVar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class Select 
{
	public static Cursor WholeTableToRAM(SQLiteDatabase db,String Table){
    String sql="select * from "+Table;
    //db.query(MS,"*", null, null, groupBy, having, orderBy, limit);
    Cursor c=db.rawQuery(sql,null);
 	return c;
	}



public static long getCountOfTable(String tableName){
String Str_Count_Sql="select count(*) from "+tableName+";";
 Cursor c=GlobalVar.sb.rawQuery(Str_Count_Sql,null);
 
 c.moveToFirst();
 Long Count=c.getLong(0);
 if(Count>1)
 {
	 Count--;
	 //because the count is the numbers of raw,but the Number is actually start from 0 
 }
 Log.e("Select.getCountOfTable","count is " + Count);
 //the test show that if the talble is nothing inside,the count will be 0
 return Count;
}

}