package Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.Timer;

import com.ReadWrite;

//import Client.FormClientChat;
import Client.InfoPeer;

//import xmlProcessing.TAG;
import xmlProcessing.TAG_COM;
import xmlProcessing.XML;

public class ThreadSocket extends Thread {
	
	Timer t;
	int count = 0;
	final int mili = 60000;
	final int milisecondsSendListPeer = 15000;
	//SynchronziedOnline isOnline = new SynchronziedOnline();
	Socket socket;
	boolean isReceiveKeepOnline = false;
	boolean isKill = false;
	InfoPeer infoClient;
	ReadWrite stuff;

	public ThreadSocket(Socket pSocket, InfoPeer infoPeer) {
		this.socket = pSocket;
		this.infoClient = infoPeer;
		try {
			this.stuff = new ReadWrite(socket.getInputStream(),
					socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		t = new Timer(mili, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doCheckReceiveKeepOnline();

			}
		});

		t.start();
//		Timer t2 = new Timer(milisecondsSendListPeer, new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				sendListPeer();
//			}
//		});
//		t2.start();
		try {
			
			while (!this.isKill) {
				// Đọc dữ liệu từ Client gửi tới rồi in ra
				String listen = stuff.read();
				System.out.println("\nClient: " + listen);
				// XML xml = new XML(listen);
				if (listen.contains(TAG_COM.SESSION_KEEP_ALIVE.getOpenTag())
						&& listen.contains(TAG_COM.SESSION_KEEP_ALIVE
								.getCloseTag())) {
					System.out.println("Vao duoc ");

					if (checkToKillSocket(listen)) {
						String name = "";
						
						int a = listen.indexOf(TAG_COM.PEER_NAME.getOpenTag()) + 11;
						int b = listen.indexOf(TAG_COM.PEER_NAME.getCloseTag());
						name = listen.substring(a, b);
						System.out.println("Ben server name = " + name);
//						synchronized (Server.listpeers) {
//							for (int i = 0; i < Server.listpeers.size(); i++) {
//								if (Server.listpeers.get(i).peerName == name) {
//
//									Server.listpeers.remove(i);
//
//									break;
//								}
//							}
//						}
//						this.isKill = true;
//						removeClient();
						
						break; // Exit while loop and end the thread
					} else {
						System.out.println("Elsesssss");
						this.isReceiveKeepOnline = true;
						t.restart();
						
						sendListPeer();
					}
				}

				// System.out.println("\nServer: ");
				// Nhập dữ liệu từ bàn phím rồi gửi về Client
				// String ask = inFromServer.readLine();
				// outToClient.writeBytes(ask+"\n");
			}

			System.out.println("Broken!!!!");
			this.isKill = true;
			removeClient();
			System.out.println("Size of list after remove: "+ Server.listpeers.size());
			this.socket.close();
			this.stuff.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void sendListPeer() {
		count++;
		// TODO Auto-generated method stub
		
			if (!this.isKill) {
				synchronized (Server.listpeers) {
					String msgListPeer = XML.xmlAllPeersInNet(Server.listpeers, this.infoClient.peerName);
					System.out.println("Send list peer " + count + " times :"
							+ msgListPeer);
					this.stuff.write(msgListPeer);
				}
			}
		
	}

	protected void doCheckReceiveKeepOnline() {
		// TODO Auto-generated method stub
		if(this.isKill) {
			removeClient();
			System.out.println("Size of list after remove: "+ Server.listpeers.size());
			this.t.stop();
		}
		if (this.isReceiveKeepOnline == false) {
			System.out.println("Server Remove client");
			removeClient();
			this.isKill = true;
			this.t.stop();
		}

		this.isReceiveKeepOnline = false;

	}
	
	private void removeClient(){
		System.out.println("Enter removeClient method 155 ThreadSocket");
		synchronized (Server.listpeers) {
			for (int i = 0; i < Server.listpeers.size(); i++) { //System.out.println("157 "+Server2.listpeers.get(i));
				if (Server.listpeers.get(i).peerName == this.infoClient.peerName) {
					
					Server.listpeers.remove(i);

					break;
				}
			}
		}
	}

	// private int indexOfUsernameInList(String name){
	// synchronized (Server.listpeers) {
	// for(int i = 0; i<Server.listpeers.size(); i++){
	// if(Server.listpeers.get(i).peerName.equals(name)) return i;
	// }
	// }
	//
	//
	// return -1; // err
	// }

	private boolean checkToKillSocket(String str) {
		String s = "";
		String name = "";

		int a = str.indexOf(TAG_COM.PEER_NAME.getOpenTag()) + 11;
		int b = str.indexOf(TAG_COM.PEER_NAME.getCloseTag());

		int x = str.indexOf(TAG_COM.STATUS.getOpenTag()) + 8;
		int y = str.indexOf(TAG_COM.STATUS.getCloseTag());

		s = str.substring(x, y);
		name = str.substring(a, b);

		// System.out.println(s);
		// System.out.println(name);

		if (s.contains("OOPS")) {
			return true;
		}

		return false;
	}
	
	public synchronized void setStop(){
		this.isKill = true;
	}
	

}
