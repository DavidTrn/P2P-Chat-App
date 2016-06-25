package mygui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;

import recycle.testthread;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
//import lib from Client packet 
import Client.InfoPeer;
import Client.MainClientThread;
import javax.swing.ImageIcon;

public class LoginWindow extends JFrame {

	/** 
	 * these variables below which are commented will be changed its value dynamically when program running
	 * or will be use as a function on GUI
	 * if it's not commented, it's generated automatically by system when designing gui
	 * don't care of them
	 * */
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenu mnNewMenu_1;
	private JMenuItem menuAbout; // menu about
	private JMenuItem menuExit; // menu exit
	private JPanel panel;
	private JLabel lblWelcome;
	private JPanel panel_1;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_4;
	private JPanel panel_2;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;
	private JLabel lblYourNickname;
	private JLabel lblYourPort;
	private JLabel lblNewLabel_9;
	private JLabel lblNewLabel_10;
	private JLabel lblServerIpAddress;
	private JLabel lblServerPort;
	private JTextField txtserverport; // txt box server port
	private JTextField txtserverip; // txtbox server ip
	private JTextField txtpeername; // txt box name of peer
	private JTextField txtpeerport ;// txt box port number of peer
	private JButton btnSignin; // btn sign in
	private JMenuItem menucreateserver;//menu create server
	
	/**check if ip address is valid */
	public static Boolean isValidIP(String ip){
		if (ip.equals("localhost")) //localhost is ok			
			return true;

		String[] arr = ip.split("\\.");
		if (arr.length != 4)
			return false;
		for (String sub : arr) {
			try {
				int part = Integer.parseInt(sub);
				
				if (part < 0 || part > 255)
					return false;
				if ((part + "").length() != sub.length())
					return false;
			} catch (Exception e) {
				//TODO: handle exception
				return false;
			}
		}
		return true;		
	}
	
	/**check if port number is valid */
	public static Boolean isValidPort(String port)
	{
		if (!port.matches("\\d+"))
			return false;
		
		int p = Integer.parseInt(port);
		if (p > 1024 && p <= 65536)
			return true;
		else return false;
	}
	
	/** Confirm exit when close window */
	private void ConfirmExit()
	{
		
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
			if(JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confirm exit...", JOptionPane.YES_NO_OPTION) == 0)
			{
				//close window
				//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				this.dispose();
			
			}
		
	}
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginWindow() {
		setIconImage(ChatWindow.createImage("/icon/logoBanana.png"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				ConfirmExit();
				
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					btnSignin.doClick();
			}
		});
		setResizable(false);
		setTitle("Banana Messenger");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 429, 695);
		
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		
		this.mnNewMenu = new JMenu("Messenger");
		this.menuBar.add(this.mnNewMenu);
		
		this.menucreateserver = new JMenuItem("Become a server...");
		this.menucreateserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //Event click on menu Become server - open window create server
				new CreateServerWindow();				
			}
		});
		this.mnNewMenu.add(this.menucreateserver);
		
		this.menuExit = new JMenuItem("Exit Messenger");
		this.menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConfirmExit();
			}
		});
		this.mnNewMenu.add(this.menuExit);
		
		this.mnNewMenu_1 = new JMenu("About");
		this.menuBar.add(this.mnNewMenu_1);
		
		this.menuAbout = new JMenuItem("About Banana Messenger");
		this.menuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutWindow dialog = new AboutWindow();
				
				dialog.setLocationRelativeTo(LoginWindow.this);		
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		this.mnNewMenu_1.add(this.menuAbout);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		
		this.panel = new JPanel();
		this.panel.setBorder(UIManager.getBorder("DesktopIcon.border"));
		
		this.lblNewLabel_10 = new JLabel("Banana Inc Copyright 2014");
		this.lblNewLabel_10.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(135)
							.addComponent(this.lblNewLabel_10))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(this.panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.panel, GroupLayout.PREFERRED_SIZE, 588, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
					.addComponent(this.lblNewLabel_10)
					.addGap(4))
		);
		
		this.lblWelcome = new JLabel("");
		this.lblWelcome.setIcon(ChatWindow.createImageIcon("/icon/panoBanana.png"));
		this.lblWelcome.setFont(new Font("Ravie", Font.PLAIN, 13));
		this.lblWelcome.setForeground(new Color(0, 128, 0));
		
		this.panel_1 = new JPanel();
		this.panel_1.setBorder(UIManager.getBorder("InternalFrame.border"));
		
		this.panel_2 = new JPanel();
		this.panel_2.setBorder(UIManager.getBorder("InternalFrame.border"));
		
		this.btnSignin = new JButton("Sign In");
		this.btnSignin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//event click Sign in btn
				//check input
				if (txtserverip.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Server IP Address is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (txtserverport.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Server Port is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (txtpeername.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Your name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (txtpeerport.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Your port is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!isValidIP(txtserverip.getText()))
				{
					JOptionPane.showMessageDialog(null, "Server IP Address is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!isValidPort(txtserverport.getText()))
				{
					JOptionPane.showMessageDialog(null, "Server Port is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!isValidPort(txtpeerport.getText()))
				{
					JOptionPane.showMessageDialog(null, "Your port is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//input ok
				// if connect failed
				//JOptionPane.showMessageDialog(null, "Cannot connect to server!\nMake sure the server is started.", "Connection Failure", JOptionPane.INFORMATION_MESSAGE);
				//else connect ok - show main window with list online peer
				//open main window
//				
//				window.MainWindow.setLocationRelativeTo(null);
//				window.MainWindow.setVisible(true);	
				
				
				//use Thread create by MainClientThread to request connect to prefer server
				//put peer name and peer port into object create by class InfoPeer
				InfoPeer infoSelf = new InfoPeer(txtpeername.getText(), Integer.parseInt(txtpeerport.getText()), null);
				//server info
				String[] serverinfo = {txtserverip.getText(), txtserverport.getText()};
				//send info of peer and server to thread
				MainClientThread mainThread = new MainClientThread(serverinfo, infoSelf);
				
				mainThread.start();//run thread to connect				
				try {
					Thread.sleep(200);//request connect to server with timeout 200ms
					//close login window if connect success
					if(mainThread.isConnectSuccess()) 
						dispose();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				System.out.println("MainClientThread is created");
			}
		});
		GroupLayout gl_panel = new GroupLayout(this.panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(this.lblWelcome, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(this.panel_2, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
							.addComponent(this.panel_1, GroupLayout.PREFERRED_SIZE, 356, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(133)
							.addComponent(this.btnSignin, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 147, GroupLayout.PREFERRED_SIZE)))
					.addGap(13))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.lblWelcome, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.panel_1, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(this.panel_2, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(this.btnSignin, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(78, Short.MAX_VALUE))
		);
		
		this.lblNewLabel_5 = new JLabel("Second step:");
		this.lblNewLabel_5.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		this.lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		
		this.lblNewLabel_6 = new JLabel("choose your favorite name and port");
		this.lblNewLabel_6.setForeground(SystemColor.textHighlight);
		
		this.lblNewLabel_7 = new JLabel("then fill in the boxes below");
		this.lblNewLabel_7.setForeground(SystemColor.textHighlight);
		
		this.lblNewLabel_8 = new JLabel("and click Sign In to chat with others");
		this.lblNewLabel_8.setForeground(SystemColor.textHighlight);
		
		this.lblYourNickname = new JLabel("Your Nickname:");
		
		this.lblYourPort = new JLabel("Your Port:");
		
		this.lblNewLabel_9 = new JLabel("Notice: Your port must be a number greater than 1024");
		this.lblNewLabel_9.setFont(new Font("Tahoma", Font.PLAIN, 9));
		this.lblNewLabel_9.setForeground(Color.RED);
		
		this.txtpeername = new JTextField();
		this.txtpeername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n')
					btnSignin.doClick();
			}
		});
		this.txtpeername.setColumns(10);
		
		this.txtpeerport = new JTextField();
		this.txtpeerport.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n')
					btnSignin.doClick();
			}
		});
		this.txtpeerport.setColumns(10);
		GroupLayout gl_panel_2 = new GroupLayout(this.panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(21)
					.addComponent(this.lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(this.lblNewLabel_8)
						.addComponent(this.lblNewLabel_7)
						.addComponent(this.lblNewLabel_6))
					.addContainerGap(61, Short.MAX_VALUE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(57, Short.MAX_VALUE)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(10)
							.addComponent(this.lblNewLabel_9))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(this.lblYourNickname, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.txtpeername, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(this.lblYourPort, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.txtpeerport, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)))
					.addGap(28))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblNewLabel_5)
						.addComponent(this.lblNewLabel_6))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.lblNewLabel_7)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.lblNewLabel_8)
					.addGap(14)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblYourNickname)
						.addComponent(this.txtpeername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(14)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblYourPort)
						.addComponent(this.txtpeerport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addComponent(this.lblNewLabel_9)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		this.panel_2.setLayout(gl_panel_2);
		
		this.lblNewLabel_1 = new JLabel("First step:");
		this.lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC + Font.BOLD, 11));
		this.lblNewLabel_1.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		
		this.lblNewLabel_2 = new JLabel("you want to connect with in the boxes below");
		this.lblNewLabel_2.setForeground(SystemColor.textHighlight);
		
		this.lblNewLabel_4 = new JLabel("fill the IP address and port of the server that");
		this.lblNewLabel_4.setForeground(SystemColor.textHighlight);
		
		this.lblServerIpAddress = new JLabel("Server IP Address:");
		
		this.lblServerPort = new JLabel("Server Port:");
		
		this.txtserverport = new JTextField();
		this.txtserverport.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n')
					btnSignin.doClick();
			}
		});
		this.txtserverport.setColumns(10);
		
		this.txtserverip = new JTextField();
		this.txtserverip.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyChar() == '\n')
					btnSignin.doClick();
			}
		});
		this.txtserverip.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(this.panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(this.lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblNewLabel_2)
								.addComponent(this.lblNewLabel_4)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(29)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblServerIpAddress, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
								.addComponent(this.lblServerPort, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(this.txtserverip, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.txtserverport, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))))
					.addGap(26))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblNewLabel_1)
						.addComponent(this.lblNewLabel_4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.lblNewLabel_2)
					.addGap(19)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(this.lblServerIpAddress)
							.addGap(14)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.lblServerPort)
								.addComponent(this.txtserverport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(this.txtserverip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(52, Short.MAX_VALUE))
		);
		this.panel_1.setLayout(gl_panel_1);
		this.panel.setLayout(gl_panel);
		this.contentPane.setLayout(gl_contentPane);
		this.setLocationRelativeTo(null);
	}

//	private static void addPopup(Component component, final JPopupMenu popup) {
//		component.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				if (e.isPopupTrigger()) {
//					showMenu(e);
//				}
//			}
//			public void mouseReleased(MouseEvent e) {
//				if (e.isPopupTrigger()) {
//					showMenu(e);
//				}
//			}
//			private void showMenu(MouseEvent e) {
//				popup.show(e.getComponent(), e.getX(), e.getY());
//			}
//		});
//	}
}
