package Global;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

public class TestObject implements Serializable {
	
		public String fromId;
		public String questionId;
		public String Time;
		public String Content;
		public String serverPath;
		public String msgType;
		public String localPath;
		public String Duration;
		public Long fileLong;
		public FileOutputStream fos;
		public byte fileByte[];
		public FileInputStream fis ;
		public String rightAnswer;
		public String myAnswer;
		public String fileExtension;
	

}
