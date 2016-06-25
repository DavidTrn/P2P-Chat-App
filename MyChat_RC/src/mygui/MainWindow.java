package mygui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;


import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import custom lib
import xmlProcessing.TAG_COM;
import xmlProcessing.XML;

import Client.ClientWaitChatThread;
import Client.ConnectToClient;
import Client.InfoPeer;
import Server.Server;

import com.ReadWrite;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;

public class MainWindow {
	/** 
	 * these variables below which are commented will be changed its value dynamically when program running
	 * or will be use as a function on GUI
	 * if it's not commented, it's generated automatically by system when design gui
	 * don't care of them
	 * */

	public JFrame MainWindow;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuSignout; //menu sign out
	private JMenuItem menuExit; //menu exit
	private JMenu menu_1;
	private JMenuItem menuLoginInfo; //menu login info
	private JMenuItem menuAbout; //menu about
	private JPanel panel;
	private JLabel label;
	private JLabel lblSFP;
	private JLabel lblpeerip; // lbl peer ip
	private JLabel lblpeerport;//lbl peer port
	private JPanel panel_1;
	private JTextField txtSearch; //txt search peer name
	private JButton btnClear; //clear search text
	private JList<String> listUser; //list contains peer
	private JLabel lblNewLabel;
	private JLabel lblpeername; //lbl peer name
	
	private Socket socket;
	//private InfoPeer itSelf;
	private static List<InfoPeer> lstpeers;
	private final int miliseconds = 15000; //peer send alive status to server every 15s
	private boolean isClosed = false; //check if a window is closed?
	private String selectedName = null;
	private ClientWaitChatThread threadServerOfClient; //create thread run a serversocket of peer to receive message
	private javax.swing.Timer t;
	private ReadWrite readwrite;
	private InfoPeer info;
	
	
	
	//list peer to insert to list box
	private List<String> listpeername = new ArrayList<String>();
	
	//list peer after search
	private List<String> listsearch = new ArrayList<String>();
	
	private JScrollPane scrollPane;
	private JPopupMenu popupMenu;
	private JMenuItem menuStartchat;
	private JMenuItem menuPeerinfo;
	
	
	/**
	 * Create the application.
	 */
	public MainWindow(Socket socket, InfoPeer itSelf, List<InfoPeer> lstPeers) throws IOException{System.out.println("115 MainWindow");
		this.socket = socket;
		this.info = itSelf;
		lstpeers = lstPeers;
		this.readwrite = new ReadWrite(socket.getInputStream(), socket.getOutputStream());
		initialize();
		
		//update list peer from server to form
		ExtractLstPeerName();
		UpdateListUser(listpeername);
		this.MainWindow.setVisible(true);
		//active timer to send alive status to server
		keepOnline();
		//update new list peer every 15s
		waitForListPeerFromServer();
		System.out.println("cant reach this line :(");

	}

	
	//func update list user to GUI
	public void UpdateListUser(List<String> list){
		if (list.isEmpty())
		{
			DefaultListModel<String> model = new DefaultListModel<>();
			listUser.setModel(model);
			return;
		}
		
		DefaultListModel<String> model = (DefaultListModel<String>) listUser.getModel();
		model.removeAllElements();
		for (int i = 0; i < list.size(); i++){
			model.addElement(list.get(i));
		}
		listUser.setModel(model);
	}
	
	/** Confirm sign out when click menu item sign out */
	private void ConfirmSignout()
	{
		this.MainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );
		if(JOptionPane.showConfirmDialog(null, "You will be disconnected and no longer to be able to chat with people in this server.\nAre you sure to sign out?", "Confirm sign out...", JOptionPane.YES_NO_OPTION) == 0)
		{
			//send disconnect message to server
			
			//return to login window
			new LoginWindow().setVisible(true);
			//then close window
			this.isClosed = true;
			String msg = TAG_COM.SESSION_KEEP_ALIVE.getOpenTag()
					+ TAG_COM.PEER_NAME.getOpenTag() + this.info.peerName
					+ TAG_COM.PEER_NAME.getCloseTag() + TAG_COM.STATUS.getOpenTag()
					+ "OOPS >>>>>>WILL BE KILLED<<<<<< " + TAG_COM.STATUS.getCloseTag()
					+ TAG_COM.SESSION_KEEP_ALIVE.getCloseTag();
			this.readwrite.write(msg);
			
			synchronized (ChatWindow.lstFrmChatP2P) {
				for(ChatWindow p: ChatWindow.lstFrmChatP2P){ System.out.println("291 FormCLientChat:  "+p.getTitle()+"size of list = "+ ChatWindow.lstFrmChatP2P.size());
						
						p.closeFrame();
						//FormChatP2P.lstFrmChatP2P.remove(p);
			}
				
//				for(FormChatP2P p: FormChatP2P.lstFrmChatP2P){
//					FormChatP2P.lstFrmChatP2P.remove(p);
//				}
			}
			
			System.out.println("$$$$Size = "+ChatWindow.lstFrmChatP2P.size());
			
			this.MainWindow.dispose();
		}
	}
	
	/** Confirm exit when close window */
	private void ConfirmExit()
	{
		this.MainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		if(JOptionPane.showConfirmDialog(null, "You will be disconnected and no longer to be able to chat with people in this server.\nAre you sure to exit?", "Confirm exit...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{
			//send disconnect message to server
			
			
//			//then close window
			this.isClosed = true;
			String msg = TAG_COM.SESSION_KEEP_ALIVE.getOpenTag()
					+ TAG_COM.PEER_NAME.getOpenTag() + this.info.peerName
					+ TAG_COM.PEER_NAME.getCloseTag() + TAG_COM.STATUS.getOpenTag()
					+ "OOPS >>>>>>WILL BE KILLED<<<<<< " + TAG_COM.STATUS.getCloseTag()
					+ TAG_COM.SESSION_KEEP_ALIVE.getCloseTag();
			this.readwrite.write(msg);
			
			synchronized (ChatWindow.lstFrmChatP2P) {
				for(ChatWindow p: ChatWindow.lstFrmChatP2P){ System.out.println("291 FormCLientChat:  "+p.getTitle()+"size of list = "+ChatWindow.lstFrmChatP2P.size());
						
						p.closeFrame();
				}			
			}
			
			System.out.println("$$$$Size = "+ChatWindow.lstFrmChatP2P.size());
			//System.exit(0);
			this.threadServerOfClient.setStop();
			//System.out.println("OUT");
			this.MainWindow.dispose();
			
		}
		else{
			
		}
	}
	
	/** Extract list of peer name from obj lstPeers to list<string> listpeername */
	private void ExtractLstPeerName()
	{
		for (InfoPeer p : lstpeers)
		{
			listpeername.add(p.peerName);
		}
	}
	
	/** create connect to chat */
	private void doConnectChat(){
		if(this.listUser.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(null, "Please select one peer!","Warning", JOptionPane.WARNING_MESSAGE);
			
		}else{
			this.selectedName = this.listUser.getSelectedValue().toString();
				System.out.println("Selected name " + this.selectedName);
		if (this.selectedName != null) {
			
			for(ChatWindow frmChat: ChatWindow.lstFrmChatP2P){
				if(this.selectedName.equals(frmChat.getName())){
					boolean check = frmChat.getIsRunning();
					if(check)
					frmChat.setActive(); 
					else{
						JOptionPane.showMessageDialog(MainWindow, "You need to close previous chat window with this peer to create new request.","Warning", JOptionPane.WARNING_MESSAGE);
					}
					return;
				}
			}
			
			InfoPeer other = null;
			synchronized (lstpeers) {
				for(InfoPeer p: lstpeers){
					if(p.peerName == this.selectedName) other = p;
				}
			}
			System.out.println("Index selected =  "+other.peerName);
			Thread connectPeer = new Thread(new ConnectToClient(this.info, other));
			connectPeer.start();
			System.out.println("Created connectPeer thread");
		}
		}
	}

	/** send alive status to server*/
	private void actionKeepOnline() {
		// TODO Auto-generated method stub
		if (isClosed) {
			
			t.stop();
			System.out.println("Stop timer");
			return;
		}
		else
		{
		String msgKeepOnline = TAG_COM.SESSION_KEEP_ALIVE.getOpenTag()
				+ TAG_COM.PEER_NAME.getOpenTag() + this.info.peerName
				+ TAG_COM.PEER_NAME.getCloseTag() + TAG_COM.STATUS.getOpenTag()
				+ "ALIVE" + TAG_COM.STATUS.getCloseTag()
				+ TAG_COM.SESSION_KEEP_ALIVE.getCloseTag();

		if (this.readwrite.write(msgKeepOnline) == false) {System.out.println("Stop timer  2");
			this.t.stop();
			JOptionPane.showMessageDialog(MainWindow, "The Connection to Server is failed", "Connection Failed", JOptionPane.PLAIN_MESSAGE);
			
			return;
		}
		}
	}
	
	private void keepOnline() {
		t = new Timer(this.miliseconds, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionKeepOnline();
			}
		});

		t.start();
		//System.out.println("Start keepOnline");
	}
	
	private void waitForListPeerFromServer() {
		String msg;
		XML xml = null;
		while (!isClosed) {
			System.out.println("still waiting for list peer");
			msg = this.readwrite.read(); // wait message from server
			//System.out.println("FormClientChat receive: " + msg);
			if (!XML.checkXML(msg))
				return;
			xml = new XML(msg);
			if ((TAG_COM) xml.getRootTag() == TAG_COM.SESSION_ACCEPT) {
				synchronized (lstpeers) {
					lstpeers = xml.getListPeersOnline();
				}
				//extract list peer name to listpeername and update to form
				String lastItem = null;
				lastItem = listUser.getSelectedValue();
				listpeername.removeAll(listpeername);
				ExtractLstPeerName();
				UpdateListUser(listpeername);
				if (lastItem != null)
				{
					for (int i = 0; i < listpeername.size(); i++)
						if (listpeername.get(i).equals(lastItem))
						{
							listUser.setSelectedIndex(i);
							break;
						}
				}
			}
		}
		System.out.println("exit loop");
	}
	
	public static List<InfoPeer> getListPeer(){
		synchronized (lstpeers) {
			return lstpeers;
		}
	}
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainWindow window = new MainWindow();
//					window.MainWindow.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		this.MainWindow = new JFrame();
		this.MainWindow.setIconImage(ChatWindow.createImage("/icon/logoBanana.png"));
		this.MainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ConfirmExit();
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				threadServerOfClient = new ClientWaitChatThread(info.peerPort);
				threadServerOfClient.start(); // start listen on info.peerPort
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (!threadServerOfClient.isvalidport)
//				{
//					MainWindow.this.isClosed = true;
//					String msg = TAG_COM.SESSION_KEEP_ALIVE.getOpenTag()
//							+ TAG_COM.PEER_NAME.getOpenTag() + info.peerName
//							+ TAG_COM.PEER_NAME.getCloseTag() + TAG_COM.STATUS.getOpenTag()
//							+ "OOPS >>>>>>WILL BE KILLED<<<<<< " + TAG_COM.STATUS.getCloseTag()
//							+ TAG_COM.SESSION_KEEP_ALIVE.getCloseTag();
//					readwrite.write(msg);
//					MainWindow.this.threadServerOfClient.setStop();
//					MainWindow.dispose();
//					LoginWindow window = new LoginWindow();//.setVisible(true);
//					window.setVisible(true);
//					JOptionPane.showMessageDialog(window, "Invalid Port!\nYou've use port " + info.peerPort + " for another name\nor your custom server is running on this port!", "Error", JOptionPane.ERROR_MESSAGE);
//				}
			}
		});
		this.MainWindow.setResizable(false);
		this.MainWindow.setTitle("Banana Messenger - " + this.info.peerName);
		this.MainWindow.setBounds(100, 100, 375, 723);
		this.MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.MainWindow.setLocationRelativeTo(null);
		
		this.menuBar = new JMenuBar();
		this.MainWindow.setJMenuBar(this.menuBar);
		
		this.menu = new JMenu("Messenger");
		this.menuBar.add(this.menu);
		
		this.menuSignout = new JMenuItem("Sign out");
		this.menuSignout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmSignout();
				
			}
		});
		this.menu.add(this.menuSignout);
		
		this.menuExit = new JMenuItem("Exit Messenger");
		this.menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmExit();
			}
		});
		this.menu.add(this.menuExit);
		
		this.menu_1 = new JMenu("About");
		this.menuBar.add(this.menu_1);
		
		this.menuLoginInfo = new JMenuItem("Login Information");
		this.menuLoginInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(MainWindow, "Nickname: " + info.peerName + "\nIP Address: " + lblpeerip.getText() + "\nPort number: " + info.peerPort, "", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		this.menu_1.add(this.menuLoginInfo);
		
		this.menuAbout = new JMenuItem("About Banana Messenger");
		this.menuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutWindow dialog = new AboutWindow();
				dialog.setLocationRelativeTo(MainWindow);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		this.menu_1.add(this.menuAbout);
		
		this.panel = new JPanel();
		this.panel.setBorder(UIManager.getBorder("DesktopIcon.border"));
		
		this.label = new JLabel("IP Address:");
		
		this.lblSFP = new JLabel("Port:");
		
		CreateServerWindow.GetIP(); //get local ip
		this.lblpeerip = new JLabel(CreateServerWindow.myHost.getHostAddress());//set peer IP from oject info peer
		this.lblpeerip.setForeground(Color.RED);
		this.lblpeerip.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.lblpeerip.setBackground(Color.RED);
		
		this.lblpeerport = new JLabel(this.info.peerPort + "");//set peer port from object info peer
		this.lblpeerport.setForeground(Color.RED);
		this.lblpeerport.setFont(new Font("Tahoma", Font.ITALIC, 11));
		
		this.panel_1 = new JPanel();
		this.panel_1.setBorder(UIManager.getBorder("DesktopIcon.border"));
		
		this.lblNewLabel = new JLabel("Banana Inc Copyright 2014");
		this.lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GroupLayout groupLayout = new GroupLayout(this.MainWindow.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(120)
					.addComponent(this.lblNewLabel)
					.addContainerGap(135, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(this.panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
						.addComponent(this.panel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(this.panel_1, GroupLayout.PREFERRED_SIZE, 493, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(this.lblNewLabel, GroupLayout.DEFAULT_SIZE, 12, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		this.txtSearch = new JTextField();
		this.txtSearch.setToolTipText("Press Enter key to Search...");
		this.txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) { //event press Enter to search peer				
				//clear list for search
				listsearch.removeAll(listsearch);
				//text in search text box
				String textsearch = txtSearch.getText();			
							
				
				try {
					if (arg0.getKeyChar() == '\n') {
						if (txtSearch.getText().equals(""))
							return;
						else {
							for (int i = 0; i < listpeername.size(); i++) {
								if (txtSearch.getText().equals(listpeername.get(i)))
									listsearch.add(listpeername.get(i));
							}
							UpdateListUser(listsearch);
							return;
						}
					}
					if (!arg0.isActionKey())
						textsearch = (txtSearch.getText() + arg0.getKeyChar()).trim();	

					if (textsearch.equals("")) {
						UpdateListUser(listpeername);
						return;
						}
					 else {

						for (int i = 0; i < listpeername.size(); i++) {
							if (textsearch.length() > listpeername.get(i).length())
								continue;
							if (textsearch.equals(listpeername.get(i).substring(0,textsearch.length()))) 
								listsearch.add(listpeername.get(i));							
						}
						UpdateListUser(listsearch);
						return;
					}
				}catch (Exception e) {
					// TODO: handle exception
					return;
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		this.txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (txtSearch.getForeground().toString().equals("java.awt.Color[r=128,g=128,b=128]"))
				{
					//clear water mark text when click onto search box
					txtSearch.setText("");
					txtSearch.setForeground(Color.BLACK);
					txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 11));
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (txtSearch.getForeground().toString().equals("java.awt.Color[r=0,g=0,b=0]") && txtSearch.getText().toString().equals(""))
				{
					//show water mark text when lost focus
					txtSearch.setText("Type a name...");
					txtSearch.setForeground(Color.GRAY);
					txtSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
				}
			}
		});
		this.txtSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
		});
		this.txtSearch.setForeground(Color.GRAY);
		this.txtSearch.setBounds(17, 21, 293, 20);
		this.txtSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.txtSearch.setText("Type a name...");
		this.txtSearch.setColumns(10);
		
		this.btnClear = new JButton("");
		this.btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { //event click btn clear 
				//clear search box
				txtSearch.setForeground(Color.GRAY);
				txtSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
				txtSearch.setText("Type a name...");
				//update list peer
				UpdateListUser(listpeername);
			}
		});
		this.btnClear.setBounds(311, 21, 21, 20);
		this.btnClear.setIcon(new ImageIcon(MainWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose.gif")));
		this.panel_1.setLayout(null);
		this.panel_1.add(this.txtSearch);
		this.panel_1.add(this.btnClear);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		
		this.scrollPane = new JScrollPane();
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		this.scrollPane.setBounds(17, 61, 315, 407);
		this.panel_1.add(this.scrollPane);
		this.listUser = new JList<String>(model);
		this.listUser.setToolTipText("Double click on a name to start chat with him/her");
		this.listUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) { /** event double click to chat */
//				
				if (listUser.isSelectionEmpty())
					return;
				if (arg0.getClickCount() == 2)
				{
					doConnectChat();
				}
			}
		});
		this.scrollPane.setViewportView(this.listUser);
		this.listUser.setForeground(Color.BLUE);
		this.listUser.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		this.listUser.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		this.popupMenu = new JPopupMenu();
		addPopup(this.listUser, this.popupMenu);
		
		this.menuStartchat = new JMenuItem("Start chat");
		this.menuStartchat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!MainWindow.this.listUser.isSelectionEmpty())
					doConnectChat();
			}
		});
		this.popupMenu.add(this.menuStartchat);
		
		this.menuPeerinfo = new JMenuItem("Peer Info");
		this.menuPeerinfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (InfoPeer p : lstpeers)
				{
					if (p.peerName.equals(listUser.getSelectedValue()))
					{
						JOptionPane.showMessageDialog(MainWindow, "Peer name: " + p.peerName + "\nIP Address: " + p.IP + "\nPort number: " + p.peerPort, "Peer Infomation", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
			}
		});
		this.popupMenu.add(this.menuPeerinfo);
		

		
		
		this.lblpeername = new JLabel(this.info.peerName);//set peer name from object info peer
		this.lblpeername.setFont(new Font("Verdana", Font.BOLD, 15));
		GroupLayout gl_panel = new GroupLayout(this.panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(this.lblpeername, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(this.label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(this.lblSFP, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblpeerip, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(this.lblpeerport, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
									.addContainerGap())))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.lblpeername)
					.addGap(13)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.label)
						.addComponent(this.lblpeerip))
					.addGap(13)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblSFP)
						.addComponent(this.lblpeerport))
					.addContainerGap())
		);
		this.panel.setLayout(gl_panel);
		this.MainWindow.getContentPane().setLayout(groupLayout);
		
		
		
	}
	
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger() && !MainWindow.this.listUser.isSelectionEmpty()) {
						showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && !MainWindow.this.listUser.isSelectionEmpty()) {
						showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
