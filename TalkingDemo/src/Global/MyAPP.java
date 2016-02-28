package Global;

import android.app.Application;
import android.os.Handler;

public class MyAPP extends Application
{
Handler handler;

public void setHandler(Handler handler)
{this.handler=handler;}



public Handler getHandler()
{return  handler;
}


}