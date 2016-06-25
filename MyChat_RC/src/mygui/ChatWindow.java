package mygui;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;

import java.io.*;
import javax.sound.sampled.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.Timer;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import java.awt.Toolkit;

//import custom lib
import xmlProcessing.TAG;
import xmlProcessing.TAG_COM;
import xmlProcessing.TAG_FILE;
import xmlProcessing.XML;


import Client.InfoPeer;
import Client.SendFileThread;
import Client.ServerSocketReceiveFileThread;

import com.ReadWrite;
import javax.swing.JToggleButton;

public class ChatWindow extends JFrame {
	/** 
	 * these variables below which are commented will be changed its value dynamically when program running
	 * or will be use as a function on GUI
	 * if it's not commented, it's generated automatically by system when design gui
	 * don't care of them
	 * */
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem menuSendfile; // menu send file
	private JMenuItem menuClose; // menu close chat
	private JMenu mnNewMenu_1;
	private JMenuItem menuInfo; // menu contact info
	private JPanel panel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTextPane Chatbox; // list message sent
	private JButton btnSend; // btn send message
	private JButton btnSendfile; // btn send file
	private JTextArea Messagebox; // text box to type message
	private JFileChooser fc; //file chooser window to send file
	private boolean isRunning = true; //check if the connection is still ok
	private Socket socket;
	private ReadWrite stuffer;
	private InfoPeer otherPeerInfo; //info of other peer
	public static List<ChatWindow> lstFrmChatP2P = new ArrayList<ChatWindow>();//
	private String pathFile = "";
	private ServerSocketReceiveFileThread serverReceiveThread;
	private Timer timerCheckStatusDownload;
	private String filename = "";
	private JToggleButton tglbtnbold;
	private JToggleButton tglbtnitalic;
	//var for play noti  sound
//	private URL url;
//	private AudioInputStream audio;
//	private Clip clip;
	final private static int NEW_MESSAGE = 1;
	final private static int FILE_REQ = 2;
	final private static int FILE_DOWNLOADED = 3;
	//private SoundClipTest sound = new SoundClipTest();
	
	/** Insert text to chat box */
	public void InsertMessage(String name, String text)
	{
		String name1, text1, oldmessage, oBold, cBold, oItalic, cItalic;
		if (tglbtnbold.isSelected())
		{
			oBold = "<b>";
			cBold = "</b>";
		}
		else
		{
			oBold = "";
			cBold = "";
		}
		if (tglbtnitalic.isSelected())
		{
			oItalic = "<i>";
			cItalic = "</i>";
		}
		else
		{
			oItalic = "";
			cItalic = "";		
		}
		int len = Chatbox.getText().length();
		oldmessage = Chatbox.getText().substring(44, len-20);
		text1 = text.replaceAll("<", "&lt;");
		text1 = text1.replaceAll(">", "&gt;");
		text1 = text1.replaceAll("\n", "<br>");
		text1 = text1.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		
		if (name.equals("You"))
		{
			name1 = "<font color = orange><b><u>You:</u> </b></font>";
			text1 = oBold + oItalic + text1 + cItalic + cBold;
		}
		else if (name.equals("System"))//system message
		{
			name1 = "";
			text1 = text1.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			text1 = "<font size = 3 color = gray><i>" + text1 + "</i></font>";
		}
		else
			name1 = "<font color = red><b><u>" + name + "</u>: </b></font>";
		
		Chatbox.setText(oldmessage + name1 + text1 + "<br>");
	}
	
	/** Play notification sound */
	public void PlaySound(int type)
	{
		String link = null;
		File in = null;
		
		switch (type) {
		case 1 :
			link = "/sound/newmsg.wav";
			
			break;
		case 2 :
			link = "/sound/filereq.wav";
			break;
		case 3 :
			link = "/sound/downloaded.wav";
			break;

		default:
			break;
		}
		in = new File(link);
		java.net.URL audioURL = ChatWindow.class.getResource(link);
		AudioInputStream audio;
		Clip clip;
		if (audioURL == null)
			return;
		try {
 	         // Open an audio input stream.
 	         //url = this.getClass().getClassLoader().getResource(link);
 	         audio = AudioSystem.getAudioInputStream(audioURL);
 	         // Get a sound clip resource.
 	         clip = AudioSystem.getClip();
 	         // Open audio clip and load samples from the audio input stream.
 	         clip.open(audio);
 	         clip.start();
 	      } catch (UnsupportedAudioFileException e) {
 	         e.printStackTrace();
 	      } catch (IOException e) {
 	         e.printStackTrace();
 	      } catch (LineUnavailableException e) {
 	         e.printStackTrace();
 	      }
	}
	
	
	/** Confirm exit when close chat window  */
	private void ConfirmExit(){
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
		if(JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confirm exit...", JOptionPane.YES_NO_OPTION) == 0)
		{
			System.out.println("P2P closing");
			
			this.stuffer.write(TAG_COM.CHAT_CLOSE.getCloseTag());
			//Thread.sleep(100);
			this.isRunning = false;
			
			synchronized (ChatWindow.lstFrmChatP2P) {
				ChatWindow.lstFrmChatP2P.remove(this); 
				System.out.println("Can run 203 FormChatP2P size list after remove = "+ChatWindow.lstFrmChatP2P.size());
			}
			this.dispose();
		}
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. this is for set image for a component*/
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ChatWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /** Return an Image or null if path invalid - this is for set icon for a window*/
    public static Image createImage(String path)
    {
    	java.net.URL imgURL = ChatWindow.class.getResource(path);
    	if (imgURL != null)
    		return Toolkit.getDefaultToolkit().getImage(imgURL);
    	else return null;
    }
    
    /** running chat */
    private void run() {
		while (this.isRunning) {
			String newMsg = this.stuffer.read();
			if(newMsg == null) {System.out.println("Show message box"); return;} System.out.println("P2P "+newMsg);
			if (XML.checkXML(newMsg) == false)
				continue;
			XML xml = new XML(newMsg);
			TAG t;
			if ((t = xml.getRootTag()).enumType() == "TAG_COM") {
				TAG_COM tag = TAG_COM.valueOf(t.toString());
				switch (tag) {
				case CHAT_MSG:
					processCHAT_MSG(xml.getContentForTag(TAG_COM.CHAT_MSG).get(
							0));
					break;
				case CHAT_CLOSE: System.out.println("Close chat session");
				JOptionPane.showMessageDialog(this, "Your friend has closed the connection :(\nYour messages and files will not be delivered anymore!", "Warning", JOptionPane.WARNING_MESSAGE);
					try {
						this.isRunning = false;
						this.socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return;
				}
			}
			else {
				if((t = xml.getRootTag()).enumType() == "TAG_FILE"){
					TAG_FILE tagfile = TAG_FILE.valueOf(t.toString());
					switch(tagfile){
					case FILE_REQ: filename = xml.getContentForTag(TAG_FILE.FILE_REQ).get(0);
						PlaySound(ChatWindow.FILE_REQ);
						int accept = JOptionPane.showConfirmDialog(this, this.otherPeerInfo.peerName + " requests to send you file \"" + filename +"\"", "Confirm receive file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if(accept==JOptionPane.YES_OPTION){
							// open choose file dialog
							fc.setSelectedFile(new File(filename));							
							int value = fc.showSaveDialog(ChatWindow.this);   // get option is selected
							
							File file = fc.getSelectedFile();
							if(value == JFileChooser.APPROVE_OPTION){
							InsertMessage("System", "Downloading file " + "\"<font color = blue>" + this.filename + "\"</font>...");	
							// call receive file method
							receiveFile(file.getAbsolutePath());
							
							//Start timer to check status of download each 2s
							this.timerCheckStatusDownload = new Timer(2000, new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent arg0) {
									// TODO Auto-generated method stub
									setStatus();
								}
							});
							
							timerCheckStatusDownload.start();
							
							}
							else{
								this.stuffer.write(TAG_FILE.FILE_REQ_NOACK.getCloseTag()); //deny
							}
						}
						else{
							this.stuffer.write(TAG_FILE.FILE_REQ_NOACK.getCloseTag()); //deny
						}
						break;
					case FILE_REQ_ACK: sendFile(Integer.parseInt(xml.getContentForTag(TAG_FILE.FILE_REQ_ACK).get(0))); // call method send file
					InsertMessage("System", "<u><b>" + otherPeerInfo.peerName + "</u></b> is downloading file " + "\"<font color = blue>" + this.filename + "\"</font>...");
						break;
					case FILE_REQ_NOACK: // other peer deny receives file
						JOptionPane.showMessageDialog(this, this.otherPeerInfo.peerName +" declines to receive file :(", "Resquest Send file Denied", JOptionPane.PLAIN_MESSAGE);
						InsertMessage("System", "<u><b>" + this.otherPeerInfo.peerName + "</u></b> declines to receive file " + "<font color = blue>\"" + this.filename + "\"</font>!!!");
						
					}
				}
			}
		}

	}
    
    /** Send file func */
    private void sendFile(int onPort) {
		
		//create a thread to receive file
		SendFileThread sendfilethread = new SendFileThread(this.otherPeerInfo.IP, onPort, pathFile);
		new Thread(sendfilethread).start(); // start send

	}
    
    /** call a thread to receive file*/
    private void receiveFile(String fileName) {
		//create server socket
		ServerSocket server;
		try {
			server = new ServerSocket(0);	
			int port  = server.getLocalPort();	// system choose a port number is available
			// send port number to that peer
			this.stuffer.write(TAG_FILE.FILE_REQ_ACK.getOpenTag()+port+TAG_FILE.FILE_REQ_ACK.getCloseTag());
			
			// create a thread to receive file
			this.serverReceiveThread = new ServerSocketReceiveFileThread(server,fileName);
			Thread t = new Thread(serverReceiveThread);
			t.start(); // start receive file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
    /** display system message about sending file status */
    private void setStatus() {
		
		if(this.serverReceiveThread != null){
			System.out.println("Start timer");
			String status = this.serverReceiveThread.getState(); // this is status, it will be checked each 5s
			//System.out.println("Status "+status);
			/*set status here
			 * 
			 * 
			 * */
			switch (status) {
			
			case "end" :
			{
				InsertMessage("System", "File <font color = blue>\"" + this.filename + "\"</font> has been downloaded successfully!");
				PlaySound(ChatWindow.FILE_DOWNLOADED);
				this.timerCheckStatusDownload.stop();
				
				break;
			}

			default:
				break;
			}
			
		}
		
	}
    
    
    /** processing message receive from other peer */
    private void processCHAT_MSG(String msg) {
		msg = msg.replaceAll("<<", "<").replaceAll(">>", ">");
		InsertMessage(this.otherPeerInfo.peerName, msg);
		PlaySound(ChatWindow.NEW_MESSAGE);
		this.setFocusableWindowState(true);
		this.setAutoRequestFocus(true);
	}
    
    /** set window to be active */
    public void setActive(){
		this.toFront();
	}
	
    /** get running stt */
	public boolean getIsRunning(){
		return this.isRunning;
	}
	
	/** close frame event */
	public void closeFrame(){
		//this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		this.stuffer.write(TAG_COM.CHAT_CLOSE.getCloseTag());
		//Thread.sleep(100);
		this.isRunning = false;
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ChatWindow.lstFrmChatP2P.remove(this); 
				
			}
		});
		
		this.dispose();
	}

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ChatWindow frame = new ChatWindow();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ChatWindow(Socket s, InfoPeer otherPeerInfo) {
		setIconImage(createImage("/icon/logoBanana.png"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				ConfirmExit();
			}
		});
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 424, 452);		
		//setIconImage(arg0)
		this.otherPeerInfo = otherPeerInfo;
		setTitle(this.otherPeerInfo.peerName + " - " + this.otherPeerInfo.IP);
		setName(this.otherPeerInfo.peerName);
		if(!lstFrmChatP2P.isEmpty()){
			int index = lstFrmChatP2P.size(); 
			Point p = lstFrmChatP2P.get(index-1).getLocation();// System.out.println("120: "+index +" location = "+p);
			p.move(p.x+50, p.y+50);
			this.setLocation(p);
		}
		
		//sound = new SoundClipTest();
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		
		this.mnNewMenu = new JMenu("Action");
		this.menuBar.add(this.mnNewMenu);
		
		this.menuSendfile = new JMenuItem("Send file...");
		this.menuSendfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSendfile.doClick();
			}
		});
		this.mnNewMenu.add(this.menuSendfile);
		
		this.menuClose = new JMenuItem("Close chat");
		this.menuClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmExit();
			}
		});
		this.mnNewMenu.add(this.menuClose);
		
		this.mnNewMenu_1 = new JMenu("Info");
		this.menuBar.add(this.mnNewMenu_1);
		
		this.menuInfo = new JMenuItem("Contact Info");
		this.menuInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {/** event click menu contact info */
				JOptionPane.showMessageDialog(ChatWindow.this, "Peer name: " + ChatWindow.this.otherPeerInfo.peerName + "\nIP Address: " + ChatWindow.this.otherPeerInfo.IP + "\nPort number: " + (ChatWindow.this.otherPeerInfo.peerPort + ""), "Contact Infomation", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		this.mnNewMenu_1.add(this.menuInfo);
		
		this.panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(this.panel, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(this.panel, GroupLayout.PREFERRED_SIZE, 414, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		this.scrollPane = new JScrollPane();
		
		this.scrollPane_1 = new JScrollPane();
		
		this.btnSend = new JButton("Send");
		this.btnSend.setEnabled(false);
		this.btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {/**event click btn Send */
				String message = Messagebox.getText();
				if (!message.trim().equals(""))
				{
					message = message.replaceAll("<", "<<").replaceAll(">", ">>");
					int numBytes = message.getBytes(Charset.forName("UTF-8")).length;					
					//System.out.println(message + " " + numBytes);
					if (numBytes > 1024)
						JOptionPane.showMessageDialog(Messagebox, "This message is too long to be sent, it must be less than 1024 bytes", "Error", JOptionPane.ERROR_MESSAGE);						
					else {
						InsertMessage("You", Messagebox.getText());//put message onto list chat message
						stuffer.write("<CHAT_MSG>" + message + "</CHAT_MSG>"); //send message to other peer
						Messagebox.setText("");//clear message box
					}
					
				}
				else
					Messagebox.setText("");//clear message box
			}
		});
		
		this.fc = new JFileChooser(System.getProperty("user.home") + "\\Desktop") //create file chooser dialog with default path is desktop
		{
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void approveSelection() {
		            File f = getSelectedFile();
		            if (f.exists() && getDialogType() == SAVE_DIALOG) {
		                int result = JOptionPane.showConfirmDialog(this, f.getName() +
		                        " already exists.\nDo you want to overwrite it?", "Existing file",
		                        JOptionPane.YES_NO_OPTION);
		                switch (result) {
		                case JOptionPane.YES_OPTION:
		                    super.approveSelection();
		                    return;
		                
		                default:
		                    return;
		                }
		            }
		            super.approveSelection();
		        }
		    };
		
		
		this.btnSendfile = new JButton("Send file");
		this.btnSendfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {/** Event click btn send file*/
				fc.setSelectedFile(new File(""));
				int returnVal = fc.showOpenDialog(ChatWindow.this);
				int index;
				File file = null;
				
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					file = fc.getSelectedFile();  // get file is selected
					pathFile = file.getPath();	// set file's path to field pathFile
					InsertMessage("System", "Requesting <u><b>" + ChatWindow.this.otherPeerInfo.peerName + "</u></b> to receive file <font color = blue>\"" + file.getName() + "\"</font>...");
					index = pathFile.lastIndexOf("\\"); //get the last index of '\' in path
					filename = pathFile.substring(index+1); // get file name
					System.out.println("366 Formp2p "+ pathFile+" "+filename);
					stuffer.write(TAG_FILE.FILE_REQ.getOpenTag() + filename + TAG_FILE.FILE_REQ.getCloseTag()); // send request msg to that peer
				}
				
				
			}
		});
		this.btnSendfile.setIcon(createImageIcon("/icon/attach.png"));
		
		this.tglbtnbold = new JToggleButton("");
		this.tglbtnbold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isB, isI;
				isB = tglbtnbold.isSelected();
				isI = tglbtnitalic.isSelected();
				if (isB && isI)
					Messagebox.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				else if (isB && !isI)
					Messagebox.setFont(new Font("Tahoma", Font.BOLD, 13));
				else if (!isB && isI)
					Messagebox.setFont(new Font("Tahoma", Font.ITALIC, 13));
				else
					Messagebox.setFont(new Font("Tahoma", Font.PLAIN, 13));
			}
		});
		this.tglbtnbold.setIcon(ChatWindow.createImageIcon("/icon/bold.png"));
		
		this.tglbtnitalic = new JToggleButton("");
		this.tglbtnitalic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isB, isI;
				isB = tglbtnbold.isSelected();
				isI = tglbtnitalic.isSelected();
				if (isB && isI)
					Messagebox.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				else if (isB && !isI)
					Messagebox.setFont(new Font("Tahoma", Font.BOLD, 13));
				else if (!isB && isI)
					Messagebox.setFont(new Font("Tahoma", Font.ITALIC, 13));
				else
					Messagebox.setFont(new Font("Tahoma", Font.PLAIN, 13));
			}
		});
		this.tglbtnitalic.setIcon(ChatWindow.createImageIcon("/icon/italic.png"));
		GroupLayout gl_panel = new GroupLayout(this.panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(this.btnSendfile)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.tglbtnbold, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.tglbtnitalic, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(this.scrollPane_1, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(this.scrollPane)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(this.btnSend))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.scrollPane_1, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(this.btnSendfile)
							.addComponent(this.tglbtnbold, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addComponent(this.tglbtnitalic, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(this.btnSend, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.scrollPane, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
					.addGap(26))
		);
		
		this.Messagebox = new JTextArea();
		this.Messagebox.setWrapStyleWord(true);
		this.Messagebox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {/** Event type on message box */
				String txt = Messagebox.getText();
				//Create a newline if press Shift + enter
				if (e.getKeyChar() == '\n' && ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0))
				{
					int index = Messagebox.getSelectionStart();
					String head = Messagebox.getText().substring(0, index);
					String tail = Messagebox.getText().substring(index);
					Messagebox.setText(head + "\n" + tail + "");
					return;
				}
				//Send message if press enter
				if (e.getKeyChar() == '\n')
				{	
					Messagebox.setText(txt.substring(0, txt.length() - 1));
					btnSend.doClick();//click send
					Messagebox.setText("");
					btnSend.setEnabled(false);
					return;
				}
				//if a key is pressed, enable Send btn, otherwise disable it
				if (!e.isActionKey())
				{
					if ((txt + e.getKeyChar()).trim().equals(""))
						btnSend.setEnabled(false);
					else btnSend.setEnabled(true);
					return;
				}
				
			}
		});
		this.Messagebox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.Messagebox.setLineWrap(true);
		this.scrollPane.setViewportView(this.Messagebox);
		
		this.Chatbox = new JTextPane();
		this.Chatbox.setEditable(false);
		this.Chatbox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		this.scrollPane_1.setViewportView(this.Chatbox);
		this.Chatbox.setContentType("text/html");
		this.Chatbox.setText("<font color = purple size = 3><center><b><i>Hello there. Happy chatting ^^</i></b></center></font>");
		
		this.panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		setVisible(true);

		try {
			this.socket = s;
			this.stuffer = new ReadWrite(s.getInputStream(),
					s.getOutputStream());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Unknown host!");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Connection to other client  has failed!");
		}
		
		lstFrmChatP2P.add(this);
		run();//run network system
	}
}
