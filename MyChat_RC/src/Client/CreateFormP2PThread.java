package Client;

import java.net.Socket;

import mygui.ChatWindow;

public class CreateFormP2PThread implements Runnable {
	Socket socket;
	InfoPeer otherinfo;
	public CreateFormP2PThread(Socket s, InfoPeer otherInfo){
		this.socket = s;
		this.otherinfo = otherInfo;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("===Start create form chat p2p===");
		
			new ChatWindow(this.socket, this.otherinfo);
			System.out.println("===Completed create form chat p2p===");
	
		
	}

}
