/**
 * 
 */
package Client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import mygui.ChatWindow;


import xmlProcessing.TAG_COM;
import xmlProcessing.XML;

import com.ReadWrite;

/**
 * @author Game
 * 
 */
public class ConnectToClient implements Runnable {
	Socket socket;
	ReadWrite stuff;
	InfoPeer otherClientInfo;
	InfoPeer myInfo;
	boolean isConnected = true;
	public ConnectToClient(InfoPeer myInfo, InfoPeer peer) {
		System.out.println(myInfo.peerName+" "+peer.peerName);
		this.otherClientInfo = peer;
		this.myInfo = myInfo;
		try {
			this.socket = new Socket(peer.IP, peer.peerPort); System.out.println(peer.IP+" "+peer.peerPort);
			this.stuff = new ReadWrite(socket.getInputStream(),	socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Port is not open");
			JOptionPane.showMessageDialog(null, "Can not create connection because the other peer is not online");
			this.isConnected = false;
			//e.printStackTrace();
		} 
	}

	public void run() {
		if(!this.isConnected) return;
		System.out.println("/////////Start run Connect to Client Thread////////////");
		String chatReq = TAG_COM.CHAT_REQ.getOpenTag()+TAG_COM.PEER_NAME.getOpenTag()
				+ this.myInfo.peerName + TAG_COM.PEER_NAME.getCloseTag()
				+ TAG_COM.CHAT_REQ.getCloseTag();
		if(this.stuff.write(chatReq)==false) {
			JOptionPane.showMessageDialog(null, "Can not send CHAT_REQ");
			return;}
		String receive = this.stuff.read();
		System.out.println("Connect thread receive "+receive);
		if (receive!=null && receive.equals(TAG_COM.CHAT_ACCEPT.getCloseTag())) {
			System.out.println("59 open chat window");
			new ChatWindow(this.socket, this.otherClientInfo);
		} else {
			if(receive.equals(TAG_COM.CHAT_DENY.getCloseTag())){
			JOptionPane.showMessageDialog(null, this.otherClientInfo.peerName
					+ " does not want to chat with you", "Annoucement",
					JOptionPane.OK_OPTION);
			return;
			}
			else{
				// exception throws
				JOptionPane.showMessageDialog(null, " Have a error occured :(", "Anouncement", JOptionPane.INFORMATION_MESSAGE);
				
			}
		}
	}

}
