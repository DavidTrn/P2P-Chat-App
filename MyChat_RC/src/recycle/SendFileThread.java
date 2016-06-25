package recycle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import javax.swing.JOptionPane;

import xmlProcessing.TAG_FILE;

public class SendFileThread implements Runnable {
	final int MAX_MSG_SIZE = 1024;
	int port;
	String IP;
	String path;

	public SendFileThread(String IP, int port, String path) {
		this.port = port;
		this.IP = IP;
		this.path = path;
	}

	@Override
	public void run() {
		try {

			Socket socket = new Socket(IP, port);
			// show dialog
			// String patch = "\\file";
			File myFile = new File(path);
			FileInputStream fis = new FileInputStream(myFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			OutputStream os = null;
			int nRead = 0;
			byte[] openTagByteArray = TAG_FILE.FILE_DATA.getOpenTag().getBytes();
			byte[] closeTagByteArray = TAG_FILE.FILE_DATA.getCloseTag().getBytes();
			int openLength = openTagByteArray.length;
			int closeLength = closeTagByteArray.length;
			int maxlength = MAX_MSG_SIZE+openLength+closeLength; System.out.println("maxlength = "+maxlength);
			byte[] byteArray = new byte[maxlength];
			for(int i = 0; i<openLength; i++){
				byteArray[i] = openTagByteArray[i];
			}
			long length = (long)myFile.length();
			System.out.println("Length file = " + length);
			try {
				
				os = socket.getOutputStream();
				// send FILE BEGIN
				os.write(TAG_FILE.FILE_DATA_BEGIN.getCloseTag().getBytes());
				
				int count = 0;
				long i = 0;
				
				while (i!=length) {
					
					
					
					char ch;
					int start = openLength;
					
					for ( ; i<length && count<MAX_MSG_SIZE; i++) {
						nRead = bis.read();
						ch = (char)nRead;
						count++;
						if (ch == '<') {
						
							//baos.write("<<".getBytes());
							byteArray[start++] = (byte)'<';
							byteArray[start++] = (byte)'<';
							count++;
						}else
						if (ch == '>') {
							//baos.write(">>".getBytes());
							byteArray[start++] = (byte)'>';
							byteArray[start++] = (byte)'>';
							count++;
						}
						else{
							//baos.write(nRead);
							byteArray[start++] = (byte)nRead;
						}
						
					}
					//off = i;
					count = 0;
					
							//baos.write(closeTagByteArray);
					
					for(int j = 0; j<closeLength && start<maxlength; j++, start++){
						byteArray[start] = closeTagByteArray[j]; 
					}

					//start += closeLength;
				
					
						os.write(byteArray, 0, start);
					
					
					System.out.println("start = "+start);
					
					
				}


//				System.out.println("i = "+i);
				// send END FILE
				os.write(TAG_FILE.FILE_DATA_END.getCloseTag().getBytes());
//				System.out.println("End send file");

			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				if (os != null)
					os.close();
				if (bis != null)
					bis.close();
				if (socket != null)
					socket.close();
			}

		} catch (FileNotFoundException e) {

			JOptionPane.showMessageDialog(null, "FILE NOT FOUND", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "Unknown Host", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
