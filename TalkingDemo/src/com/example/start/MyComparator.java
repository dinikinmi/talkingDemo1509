package com.example.start;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class MyComparator implements Comparator {

	
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Log.v("comparator","comparator start on");
		
		
		@SuppressWarnings("unchecked")
		
		
		HashMap<String,String> map1=(HashMap<String,String>)o1;
	    HashMap<String,String> map2=(HashMap<String,String>)o2;	
	    
	    String time1=map1.get("SendTime");
		String time2=map2.get("SendTime");
		long intTime1=Long.parseLong(time1);
        long intTime2=Long.parseLong(time2);
        if(intTime1>intTime2){return 1;}
        else{
		return 0;}
	}

}
