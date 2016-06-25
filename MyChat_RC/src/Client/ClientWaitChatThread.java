package Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import mygui.ChatWindow;
import mygui.MainWindow;



import xmlProcessing.TAG_COM;
import xmlProcessing.XML;

import com.ReadWrite;

public class ClientWaitChatThread extends Thread {
	int port;
	boolean isClosed = false;
	public boolean isvalidport = true;
	private ChatWindow window;

	public ClientWaitChatThread(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		System.out.println("Thread ClientWait runing on "+this.port);
		ReadWrite stuff;
		ServerSocket serverSocket;
		// JOptionPane msbox = new JOptionPane("", JOptionPane., optionType,
		// icon)
		try {
			serverSocket = new ServerSocket(this.port);
			while (!isClosed) {
				
				System.out.println("-ClientWaitThread waiting...");
				Socket s = serverSocket.accept();
				
				
				stuff = new ReadWrite(s.getInputStream(), s.getOutputStream());
				String msg = stuff.read();
				System.out.println("---ClientWaitThread msg = "+msg);
				if (XML.checkXML(msg) == false)
					continue;
				
				XML xml = new XML(msg);
				if ((TAG_COM) xml.getRootTag() == TAG_COM.CHAT_REQ) {
					//System.out.println("That true "+FormClientChat.getListPeer().size());
					
					//for (InfoPeer p : FormClientChat.getListPeer()) {
						//System.out.println("-----ClientWaitThread Name = "+p.peerName);
						String name = xml.getContentForTag(TAG_COM.PEER_NAME).get(0);
						System.out.println("--------Name = "+name);
						//if (p.peerName.equals(name)) {
							int accept = JOptionPane.showConfirmDialog(window,
									name+" wants to chat with you, agree?", "Announcement",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);

							if (JOptionPane.YES_OPTION == accept) {
								String IP = s.getInetAddress().toString().substring(1);
								int port = 0;
								List<InfoPeer> listpeer = MainWindow.getListPeer();
								for (InfoPeer p : listpeer)
								{
									if (p.peerName.equals(name))
									{
										port = p.peerPort;
										break;
									}
								}
								InfoPeer newPeer = new InfoPeer(name, port, IP);
								
								CreateFormP2PThread runnable = new CreateFormP2PThread(s, newPeer);
								Thread t = new Thread(runnable);
								t.start();
								System.out.println("-------------Accepted");
								stuff.write(TAG_COM.CHAT_ACCEPT.getCloseTag());
								
							}
							else{
								stuff.write(TAG_COM.CHAT_DENY.getCloseTag());
							}
							
							//break;
						//}
					//}
					
					
				}
			}
			serverSocket.close();

		} catch (IOException e) {
			System.out.println("Can not create SocketServer for client");
			isvalidport = false;
			//JOptionPane.showMessageDialog(null, "Error, Maybe there is a port in used by other process", "ERROR", JOptionPane.ERROR_MESSAGE);
			
			return;
			//e.printStackTrace();
		}

	}
	
	public void setStop(){
		this.isClosed = true;
		Socket trick = null;
		try {
			trick = new Socket("localhost", port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			try {
				trick.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			trick=null;
			
		System.out.println("Stop server client");
	}
	
	public boolean getIsValidPort(){
		return this.isvalidport;
	}
}
