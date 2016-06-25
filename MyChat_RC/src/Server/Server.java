package Server;
    import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import xmlProcessing.TAG_COM;
import xmlProcessing.XML;

import Client.InfoPeer;

import com.ReadWrite;
     
    public class Server extends Thread{
    	public static List<InfoPeer> listpeers = new ArrayList<InfoPeer>();
    	private ArrayList<ThreadSocket> listThreadSocket = new ArrayList<ThreadSocket>();
    	public boolean isStop = false;
    	private int port;
    	public boolean isvalidport = true;
    	public Server(int port){
    		this.port = port;
    	}
               
        private static boolean isExistUsername(String username){
        	if(listpeers.size()!=0){
        		for(InfoPeer p: listpeers){
        			if(p.peerName.equals(username)) return true;
        		}
        		
        	}
        	
        	return false;
        	
        }
        
        private static void debugPrintList(){
        	for(InfoPeer p: listpeers){
        		System.out.println(p.peerName+" "+p.peerPort+" "+p.IP);
        	}
        }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 
			  ServerSocket serverSocket=null;
			 try{
	               serverSocket = new ServerSocket(this.port);
	                System.out.println("Khởi chạy máy chủ thành công");
	                String msgClients = "";
	                ReadWrite stuff = null;
	               
	                while(!isStop){
	                	Socket socket = serverSocket.accept();
	                	 
	                	//System.out.println("Co 1 ket noi den");
	                	stuff = new ReadWrite(socket.getInputStream(), socket.getOutputStream());
	                	msgClients = stuff.read();  System.out.println("msg from client:"+msgClients);
	                	
	                	if(XML.checkXML(msgClients)== false) continue; 
	                	XML xml = new XML(msgClients);
	                	TAG_COM t = (TAG_COM)xml.getRootTag();
	                	if(t == TAG_COM.SESSION){
	                		String name = xml.getContentForTag(TAG_COM.PEER_NAME).get(0);
	                		                		
	                		if(!isExistUsername(name)){
	                			int port = Integer.parseInt(xml.getContentForTag(TAG_COM.PORT).get(0));
	                    		String IP = socket.getInetAddress().toString().substring(1);// System.out.println(IP);
	                    		//IP = IP.substring(1, IP.indexOf(":"));
	                    		//if(name.equals("giengien")) IP=  "192.168.1.102";
	                    		InfoPeer clientinfo = new InfoPeer(name, port, IP);
	                			listpeers.add(clientinfo);
	                			String lstpeer = XML.xmlAllPeersInNet(listpeers, name);
	                			stuff.write(lstpeer);  System.out.println("Server sent " + lstpeer);
	                			//Create a new Thread for that client's socket
	                           ThreadSocket tsk = new ThreadSocket(socket, clientinfo);
	                           tsk.start();
	                         
	                            this.listThreadSocket.add(tsk);
	                            System.out.println("Có 1 kết nối đến");
	                            debugPrintList();
	                		}
	                		else{
	                			
	                			stuff.write(TAG_COM.SESSION_DENY.getCloseTag()); // send msg deny to client
	                			
	                		}
	                	}
	                	else{
	                		System.err.println("Message from Client is not a SESSION msg");
	                	}
	                    
	                }
	                serverSocket.close();
	               
	                System.out.println("Closed--------");
	            }
	            catch(IOException e){
	            	isvalidport = false;
	                System.out.println("Exception: " +e.getMessage());
	            }
		}
		
		public synchronized void setStop(){
			if(this.listThreadSocket.size()!=0)
			for(ThreadSocket t: this.listThreadSocket){
				t.setStop();
			}
			
			
			this.isStop = true;
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
				
					
			
			
			System.out.println("Set isStop = "+this.isStop);
			
		}
        
    }