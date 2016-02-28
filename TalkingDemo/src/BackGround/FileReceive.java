package BackGround;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

import android.os.Environment;
import android.util.Log;

public class FileReceive extends Thread  {
    File file;
    
	Socket sk;
	FileOutputStream fos;
	InputStream inps;
	DataInputStream dis;
	byte readByte[];
	
	public void run(){
		try{
			Log.v("receive","receive Start");
		int readCount=0;	
		sk=new Socket("192.168.0.117",1315);
		file=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"1.txt");
		inps=sk.getInputStream();
	    dis=new DataInputStream(inps);
	    readByte=new byte[1024];
	    fos=new FileOutputStream(file);
	    while(true){
	    readCount=dis.read(readByte,0,readByte.length);
	    if(readCount==-1){break;}
	    fos.write(readByte,0,readCount);
	    }
	    fos.flush();
	    }catch(Exception e){};
	}
}
