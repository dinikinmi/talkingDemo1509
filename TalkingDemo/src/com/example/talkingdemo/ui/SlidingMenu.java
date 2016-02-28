package com.example.talkingdemo.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;  
import android.os.Build;
import android.util.AttributeSet;  
import android.util.Log;
import android.util.TypedValue;  
import android.view.MotionEvent;  
import android.view.ViewGroup;  
import android.widget.HorizontalScrollView;  
import android.widget.LinearLayout;  

public class SlidingMenu extends HorizontalScrollView  
{  
  /** 
   * 锟斤拷幕锟斤拷锟�
   */  
  private int mScreenWidth;  
  /** 
   * dp 
   */  
  private int mMenuRightPadding = 100;  
  /** 
   * 锟剿碉拷锟侥匡拷锟�
   */  
  private int mMenuWidth;  
  private int mHalfMenuWidth;  

  private boolean once;//if boolean is not in mian[],the default value is false
  private boolean isOpen;  

  public SlidingMenu(Context context, AttributeSet attrs)  
  {  
      super(context, attrs);  
      mScreenWidth = ScreenUtils.getScreenWidth(context); 
      this.scrollTo(333,222);
  }  

  @Override  
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  //call when create window
  {  
      /** 
       * 锟斤拷示锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟�
       */  
      if (!once)  
      {   LinearLayout wrapper = (LinearLayout) getChildAt(0);  //getChildAt() get object to specified layout layers
          ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);  //
          ViewGroup content = (ViewGroup) wrapper.getChildAt(1);// 
          // ViewGroup content2=(ViewGroup) wrapper.getChildAt(2);
          // dp to px  
          mMenuRightPadding = (int) TypedValue.applyDimension
        		  (  
                  TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, 
                  content.getResources().getDisplayMetrics()
                   );  
          /*
           * public static float applyDimension(int unit, float value,
DisplayMetrics metrics)
{
switch (unit) {
case COMPLEX_UNIT_PX:
return value;
case COMPLEX_UNIT_DIP:
return value * metrics.density;
case COMPLEX_UNIT_SP:
return value * metrics.scaledDensity;
case COMPLEX_UNIT_PT:
return value * metrics.xdpi * (1.0f/72);
case COMPLEX_UNIT_IN:
return value * metrics.xdpi;
case COMPLEX_UNIT_MM:
return value * metrics.xdpi * (1.0f/25.4f);
}
return 0;
}
           * */
          
          //DisplayMetrics dm =resources.getDisplayMetrics();//鑾峰緱灞忓箷鍙傛暟锛氫富瑕佹槸鍒嗚鲸鐜囷紝鍍忕礌绛夈�
          //convert the mMenuRightPadding's unit from dip to px
          mMenuWidth = mScreenWidth - mMenuRightPadding;  
          mHalfMenuWidth = mMenuWidth / 2;  
          menu.getLayoutParams().width = mMenuWidth;  
          content.getLayoutParams().width = mScreenWidth;  
//          content2.getLayoutParams().width=mScreenWidth;
      }  
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
   

  }  

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override  
  protected void onScrollChanged(int l, int t, int oldl, int oldt)  
  {  
	   
	  super.onScrollChanged(l, t, oldl, oldt);  
      float scale = l * 1.0f / mMenuWidth;   
      //this is alphabet L,not number 1
      
      LinearLayout wrapper = (LinearLayout) getChildAt(0);  
      ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);  
      ViewGroup content = (ViewGroup) wrapper.getChildAt(1); 
     /* 
      menu.setScaleX(1 - 0.3f * scale);
      Log.v("scroll",""+(1-0.3f*scale));
//      menu.setScaleX((float) 0.6);
      menu.setScaleY(1 - 0.3f * scale);
      menu.setAlpha(0.6f + 0.4f * (1 - scale));
//      menu.setScaleY((float)0.5);
      //alpha瑙嗗浘閫忔槑搴�
      menu.setTranslationX(mMenuWidth * scale * 0.6f);
     
      content.setPivotX(0);
      content.setPivotY(content.getHeight() / 2);
      content.setScaleX(0.8f + scale * 0.2f);
      content.setScaleY(0.8f + scale * 0.2f);
  */
  }
  
  @Override  
  protected void onLayout(boolean changed, int l, int t, int r, int b)  
  {  
      super.onLayout(changed, l, t, r, b);  
      if (changed)  
      {  Log.v("onlayout",""+changed);
         Log.v("pix",l+" "+" "+t+" "+r+" "+b);
          // 锟斤拷锟剿碉拷锟斤拷锟斤拷  
          this.scrollTo(mMenuWidth,0);  
          once = true;  
      }  
  }  

  @Override  
  public boolean onTouchEvent(MotionEvent ev)  
  {  
      int action = ev.getAction();  
      switch (action)  
      {  
      // Up时锟斤拷锟斤拷锟斤拷锟叫断ｏ拷锟斤拷锟斤拷锟绞撅拷锟斤拷锟斤拷锟节菜碉拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟饺拷锟绞撅拷锟斤拷锟斤拷锟斤拷锟斤拷锟� 
      case MotionEvent.ACTION_UP: 
    	  //action_up refer to releasing hand,not move upon
          int scrollX = getScrollX();  
          if (scrollX > mHalfMenuWidth) 
          {
              this.smoothScrollTo(mMenuWidth, 0); 
              Log.v("smoosrc","smoosrc mMenuWidth "+mMenuWidth);
              Log.v("","smoosrc"+"scrX "+scrollX);
              isOpen = false;  
          }
          else  
          {
              this.smoothScrollTo(0, 0); 
              Log.v("smoosrc","smoosrc 0,0");
              Log.v("","smoosrc"+"scrX "+scrollX);
              isOpen = true;  
          }
          Log.v("","smoosrc hmw"+mHalfMenuWidth);
          return true;  
        
      }  
      return super.onTouchEvent(ev);  
  }  
//here is the details about the screen coordinate that i found
  //the offset of x axis is the left_up angle .
  //and the point by which the system calculate the position is the left and top
  //edge of View.and the (0,0) is the left_top point of screen.the axis increase 
  //to the left
  //
  /** 
   * 锟叫伙拷锟剿碉拷状态 
   */  
  public void toggle()  
  {  
      if (isOpen)  
      {  
    	  this.smoothScrollTo(mMenuWidth/2, 0); 
    	  Log.v("isOpen","isOpen"+isOpen);
          isOpen = false;   
      } 
      else  
      {  
    	  this.smoothScrollTo(0, 0); 
    	  Log.v("isOpen","isOpen"+isOpen);
          isOpen = true;    
          
      }  
  }  
}  