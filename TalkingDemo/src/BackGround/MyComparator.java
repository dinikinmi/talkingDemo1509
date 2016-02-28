package BackGround;

import java.util.Comparator;
import java.util.Map;

public class MyComparator implements Comparator {

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Map<String,Object> map1=(Map<String,Object>)o1;
	    Map<String,Object> map2=(Map<String,Object>)o2;	
	    String time1=map1.get("SendTime").toString();
		String time2=map2.get("SendTime").toString();
		long intTime1=Long.parseLong(time1);
        long intTime2=Long.parseLong(time2);
        if(intTime1>intTime2){return 1;}
        else{
		return 0;}
	}

}
