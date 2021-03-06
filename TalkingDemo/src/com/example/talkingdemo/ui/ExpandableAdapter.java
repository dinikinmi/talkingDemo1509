package com.example.talkingdemo.ui;

import com.example.talkingdemo.R;

import Global.GlobalVar;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {
   
	Context context;
	//String[] generalsTypes;
    //String[][] generals;
	
	
	
	//here we change the data source to GlobalVar---a class for global varies;
    String[] generalsTypes =GlobalVar.generalsTypes;
//    /new String[] { "aa", "bbb", "ccc" };
    String[][] generals = GlobalVar.generals;
/*    		new String[][] {
            { "rr" },
            { "wwew","fcxc","ecxz","ddfd","deee"},
            {"sdfsda", "dsfsf", "eeee", "sdfsdf", "ddd"}
    };
  */  
	public ExpandableAdapter(Context c)
	{
		this.context=c;
		//String[] generalsTypes = s1;,String[] s1,String[][] s2
        //String[][] generals=s2; 
	}
	
	//
    @SuppressWarnings("deprecation")
	TextView getTextView() 
    //when will this method be called?
    {   AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
        //i think this lp is to set the params to control a layout
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(20);
        textView.setTextColor(Color.YELLOW);
        return textView;
    }
    
	@Override
	public int getGroupCount() {
		return generalsTypes.length;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return generals[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return generalsTypes[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		return generals[groupPosition][childPosition];
	}

	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	@Override
	public boolean hasStableIds() 
	{
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) 
	{    
		//i guess this  method is to make a Outline of ListView Item
		/*LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(0);
        TextView textView = getTextView();
        textView.setTextColor(Color.BLACK);
        textView.setText(getGroup(groupPosition).toString());
        ll.addView(textView);
        return ll;*/
		
		if (convertView == null) 
		{
            convertView = ((LayoutInflater) context
            		.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            		.inflate(R.layout.mianwin_elv_group, null);
        }
        TextView tv1 = (TextView) convertView.findViewById(R.id.mainwin_elv_groupname);
        tv1.setText(getGroup(groupPosition).toString());
        TextView tv2 = (TextView) convertView.findViewById(R.id.mainwin_elv_groupsize);
        tv2.setText("["+String.valueOf(getChildrenCount(groupPosition))+"]");
        return convertView;
	}

	//when will getChildView  and getGroupView be Called? that is a question
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) 
	{
		/*LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(0);
        TextView textView = getTextView();
        textView.setText(getChild(groupPosition, childPosition) .toString());
        ll.addView(textView);
        return ll;*/
		
		if (convertView == null) 
		{
            convertView = ((LayoutInflater) context
            		.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            		.inflate(R.layout.mianwin_elv_item, null);
        }
		
		TextView tv = (TextView)convertView.findViewById(R.id.tv_name_rcli);
        tv.setText(getChild(groupPosition, childPosition).toString());
        Log.v("getChild id","getchild id ="+" "+getChild(groupPosition,childPosition));
        return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
