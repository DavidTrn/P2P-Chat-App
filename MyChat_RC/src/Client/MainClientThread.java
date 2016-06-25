package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import recycle.test;

import mygui.LoginWindow;
import mygui.MainWindow;

//import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.swt.widgets.Shell;

//import mainPackage.frame;

import xmlProcessing.TAG_COM;
import xmlProcessing.XML;

import com.ReadWrite;

public class MainClientThread extends Thread {
	private Socket socket;
	private ReadWrite stuff;
	private InfoPeer infoOfSelf;
	private boolean isSuccess = false;
	private String[] serverInfo;
	
	public MainClientThread(String[] serverInfo, InfoPeer mySelf){
	
		this.infoOfSelf = mySelf;
		this.serverInfo = serverInfo;
		
		
		
	}
	
	public void run() {
		ClientWaitChatThread waitChatThread = new ClientWaitChatThread(this.infoOfSelf.peerPort);
		waitChatThread.start();
		
		try {
			Thread.sleep(100);
			if(!waitChatThread.isvalidport){
				System.out.println("IS NOT VALID 58");
				//JOptionPane.showMessageDialog(null, "This port is invalid ",  "ERROR", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, "Invalid Port!\nYou've use port " + this.infoOfSelf.peerPort + " for another name\nor your custom server is running on this port!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else{
				waitChatThread.setStop();
			}
		} catch (InterruptedException e1) {
		
			e1.printStackTrace();
		}
		
		try {
			this.socket = new Socket(serverInfo[0], Integer.parseInt(serverInfo[1])); System.out.println("MainClientThread connecting to Server");
			
			stuff = new ReadWrite(this.socket.getInputStream(), this.socket.getOutputStream());
//			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			JOptionPane.showMessageDialog(null, "Sorry, Server is offline !!\nPlease try again later", "Connection Failure", JOptionPane.PLAIN_MESSAGE);
			return;
			//e.printStackTrace();
			
		}
		
		// Send peer info to server
		String info = TAG_COM.SESSION.getOpenTag()+TAG_COM.PEER_NAME.getOpenTag()
				+ this.infoOfSelf.peerName.replaceAll("<", "<<").replaceAll(">", ">>") + TAG_COM.PEER_NAME.getCloseTag()
				+ TAG_COM.PORT.getOpenTag()+this.infoOfSelf.peerPort +TAG_COM.PORT.getCloseTag()
				+ TAG_COM.SESSION.getCloseTag();
		
		this.stuff.write(info);
		
//		this.mainThreadToRunItSelf = new Thread(this);
//		this.mainThreadToRunItSelf.start();
		
		String msg = this.stuff.read(); // wait for server send confirmation 
		System.out.println("From Server: "+msg);
		if(XML.checkXML(msg)==false) return; // always check msg before create xml
		XML xml = new XML(msg);
		if((TAG_COM)xml.getRootTag() == TAG_COM.SESSION_DENY){
			JOptionPane.showMessageDialog(null, "Sorry,  Your nickname has been used!\nPlease choose another name", "Announcement", JOptionPane.ERROR_MESSAGE);
			this.isSuccess = false;
		}
		else{
			//connect to server successfully
			if(xml.getRootTag() == TAG_COM.SESSION_ACCEPT){ 
				this.isSuccess = true;
				//new test();
				//open Main window which contains list of peer 
				MainWindow window;
				try { System.out.println("89 MainThread");
					window = new MainWindow(this.socket, infoOfSelf, xml.getListPeersOnline());
					//window.MainWindow.setLocationRelativeTo(null);
					//window.MainWindow.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//new FormClientChat(this.socket, infoOfSelf, xml.getListPeersOnline()); 
				System.out.println("MainClientThread has created FormClientChat");
				
			}
		}
		
		
	}
	
	public boolean isConnectSuccess(){
		
		return isSuccess;
		
	}

}
