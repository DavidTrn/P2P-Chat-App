package Client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketReceiveFileThread implements Runnable {
	private final int MAX_MSG_SIZE = 1024;
	private ServerSocket serverSocket;
	private String path = "";
	private boolean isDownloading = false;
	private boolean isStart = false;
	private boolean isEnd = false;
	
	public ServerSocketReceiveFileThread(ServerSocket serverSocket,
			String fileName) {
		this.serverSocket = serverSocket;
		this.path = fileName;
	}

	@Override
	public void run() {
		try {

			Socket socket = serverSocket.accept();

			System.out
					.println("19 ServerSocketReceiveFileThread: Connected to receive file");

			// save file in this directory
			
			FileOutputStream fos = new FileOutputStream(path);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			InputStream is = socket.getInputStream();
			
			int nRead = 0;
			String tag = "";

			boolean isWrite = false;
			char ch = 0;
			char ch2 = 0;
			ByteArrayOutputStream buff = new ByteArrayOutputStream();
			while (true) {
				nRead = is.read();
				if (nRead == -1)
					break;
				ch = (char) nRead;
				if (ch == '<') { // <<asdf<<<asdf

					if ((ch2 = (char) is.read()) == '<') {
						//buff.write(nRead);
						bos.write(nRead);
						continue;
					}
					tag += "<";
					tag += ch2;
					//System.out.println("MOA " + tag);
					while ((ch = (char) is.read()) != '>')
						tag += ch;
					tag += ch;

					System.out.println("119 Recevie Thread tag = " + tag);
					switch (tag) {
					case "<FILE_DATA_BEGIN />":
						
						this.isStart = true;
						System.out.println("FILEDATABEGIN");
						break;
					case "<FILE_DATA>":
						if (this.isStart){
							isWrite = true;
							this.isDownloading = true;
						}
						System.out.println("FILEDATA + isWrite = " + isWrite);
						break;
					case "</FILE_DATA>":
						System.out.println("FILEDATA close --- ");
						isWrite = false;
						//bos.write(buff.toByteArray());
						
						//buff.reset();
						break;
					case "<FILE_DATA_END />":
						if (bos != null){
							bos.flush();
							bos.close();
							
						}
						if (fos != null)
							fos.close();
						if (is != null)
							is.close();
						this.isEnd = true;
						System.out.println("FILEDATAEND");
						break;
					default: 

					}
					tag = "";
					if (this.isEnd){
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						
						break;
						
					}
				} else {
					if (isWrite) {
						//System.out.println((char) nRead);
						//buff.write(nRead);
						bos.write(nRead);
						if (ch == '>') {

							nRead = is.read();
							if ((char) nRead == '>')
								continue;
							else {
								System.out.println("error........");
								return; // error
							}
						}

					}
				}

			}

			tag = "";
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	
	public String getState(){
		//System.out.println("getState()");
		if(this.isEnd) return "end";
		if(this.isDownloading) return "downloading";
		if(this.isStart) return "starting";
		
		return "starting";
	}
}
