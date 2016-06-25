package Client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import javax.swing.JOptionPane;

import xmlProcessing.TAG_FILE;

public class SendFileThread_old implements Runnable {
	final int MAX_MSG_SIZE = 1024;
	int port;
	String IP;
	String path;

	public SendFileThread_old(String IP, int port, String path) {
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
			byte[] byteArray = new byte[(int) myFile.length()];
			long length = (long)myFile.length();
			System.out.println("Length file = " + length);
			try {
				//nRead = bis.read(byteArray, 0, byteArray.length);
				//System.out.println("nRead = " + nRead + "noi dung = "+new String(byteArray));
				//System.out.println("1025 "+(char)byteArray[1025]+(char)byteArray[1023]);
				os = socket.getOutputStream();
				// send FILE BEGIN
				os.write(TAG_FILE.FILE_DATA_BEGIN.getCloseTag().getBytes());
				
				int off = 0, remain = byteArray.length;
				//int nRead = 0;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int count = 0;
				long i = 0;
				byte[] openTagByteArray = TAG_FILE.FILE_DATA.getOpenTag().getBytes();
				byte[] closeTagByteArray = TAG_FILE.FILE_DATA.getCloseTag().getBytes();
				while (i!=length) {
					
					// byte[] result = new byte[openTagByteArray.length + length
					// + closeTagByteArray.length];
					
					//int index = 0;
					
							baos.write(openTagByteArray);
						
					
					char ch;
					
					
					for ( ; i<length && count<MAX_MSG_SIZE; i++) {
						nRead = bis.read();
						ch = (char)nRead;
						count++;
						if (ch == '<') {
						
							baos.write("<<".getBytes());
							count++;
						}else
						if (ch == '>') {
							baos.write(">>".getBytes());
							count++;
						}
						else{
							baos.write(nRead);
						}
						
					}
					//off = i;
					count = 0;
					
							baos.write(closeTagByteArray);
						


					
				
					
					os.write(baos.toByteArray());
					//remain -= off;
					//System.out.println("----UNIT DATA i = " + i +" "+baos.toString());
					baos.reset();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

//				System.out.println("remain, off " + remain + " " + off);
//				if (remain > 0) {
//					byte[] temp = createUnitFile(byteArray, off);
//					os.write(temp);
//				}
				System.out.println("i = "+i);
				// send END FILE
				os.write(TAG_FILE.FILE_DATA_END.getCloseTag().getBytes());
				System.out.println("End send file");

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

	private byte[] createUnitFile(byte[] u, int offset) throws IOException {
		byte[] openTagByteArray = TAG_FILE.FILE_DATA.getOpenTag().getBytes();
		byte[] closeTagByteArray = TAG_FILE.FILE_DATA.getCloseTag().getBytes();
		// byte[] result = new byte[openTagByteArray.length + length
		// + closeTagByteArray.length];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//int index = 0;
		
				baos.write(openTagByteArray);
			
		
		char ch;
		int count = 0;
		
		for (int i = offset; i < u.length && i < offset + MAX_MSG_SIZE; i++) {
			ch = (char) u[i]; 
			count++;
			if (ch == '<') {
			
				baos.write("<<".getBytes());
				count++;
			}else
			if (ch == '>') {
				baos.write(">>".getBytes());
				count++;
			}
			else{
				baos.write(u[i]);
			}
			if(count==MAX_MSG_SIZE) break;
		}

		
				baos.write(closeTagByteArray);
			


		System.out.println("----UNIT DATA " + baos.toString());
		return baos.toByteArray();

	}
}
