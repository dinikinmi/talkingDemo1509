package com.example.start;

import BackGround.HandleUnsend;
import Global.GlobalVar;

import android.database.Cursor;

import com.example.sqlite.Select;

public class CheckUnsend extends Thread {
	//this thread is discarded
	//这个线程现在不用了，本是用于检测有没有要发送的信息，
	//现在直接用HandleUnsend 检测就OK了
    void Run(){
    	Cursor c=Select.WholeTableToRAM(GlobalVar.sb, "UNSEND");
    	int i=c.getCount();
    	if(i>0){
    		//start thread handle Unsend
//    		HandleUnsend handleUnsend=new HandleUnsend();
//    		handleUnsend.start();
    	}
    }
}
