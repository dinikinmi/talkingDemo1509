package Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

public class ChatObject implements Serializable {
public long fromId;
public String fromUserName;
public long toId;
public long Time;
public String Content;
public String serverPath;
public int msgType;
public String localPath;
public int Duration;
public Long fileLong;
public FileOutputStream fos;
public byte fileByte[];
public FileInputStream fis ;
public String msgId;
}
