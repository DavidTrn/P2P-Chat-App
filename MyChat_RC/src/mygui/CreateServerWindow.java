package mygui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Timer;

import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

import Server.Server;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.awt.Toolkit;

public class CreateServerWindow extends JFrame {

	/** 
	 * these variables below which are commented will be changed its value dynamically when program running
	 * or will be use as a function on GUI
	 * if it's not commented, it's generated automatically by system when designing gui
	 * don't care of them
	 * */
	private JPanel contentPane;
	private JLabel lblFillTheBox;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JMenuBar menuBar;
	private JMenu menuAction;
	private JMenuItem menuActionOnServer; // menu item start/stop server
	private JMenuItem menuExit; // menu item exit
	private JLabel lblNewLabel_2;
	private JLabel lblIP; // ip address 
	private JLabel lblNewLabel_4;
	private JTextField txtport; // port
	private JButton btnAction; // button start/stop server
	static public InetAddress myHost; // get local ip
	private JLabel lblNewLabel_3;
	private Server s; //start server by this var
	//5 vars below is to decorate form :D
	private JLabel lblStatus; //status of server
	private javax.swing.Timer timer; //timer - change style of stt if server is running ervery 1s
	private int statusCode = 0;
	private int count = 0;
	private String errorport;
	
	/**function confirm exit when close window */
	private void ConfirmExit()
	{
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
		if (btnAction.getText().equals("Start server")) //if server is not started
			if(JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confirm exit...", JOptionPane.YES_NO_OPTION) == 0)
				this.dispose();
		if (btnAction.getText().equals("Stop server")) //if server is started
			if(JOptionPane.showConfirmDialog(this, "Your server will be stopped.\nAre you sure to exit?", "Confirm exit...", JOptionPane.YES_NO_OPTION) == 0)
			{
				s.setStop();
				s = null;
				this.dispose();
			}
	}
	
	/**function get local ip address */
	static public void GetIP()
	{
		try {
			CreateServerWindow.myHost = InetAddress.getLocalHost();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
	}
	
	/** function set status of server - 0 : stop, 1 : running, 2 : error*/
	private void setStatus(int statusCode)
	{
		
		if (statusCode == 0)
		{
			lblStatus.setText("Server is not running.");
			
		}
		else if (statusCode == 1)
		{
			if (count % 4 == 0)
			{
				lblStatus.setText("Server is running at port " +  txtport.getText());
				count++;
			}
			else if (count % 4 == 1)
			{
				lblStatus.setText("Server is running at port " +  txtport.getText() + ".");
				count++;
			}
			else if (count % 4 == 2)
			{
				lblStatus.setText("Server is running at port " +  txtport.getText() + "..");
				count++;
			}
			else
			{
				lblStatus.setText("Server is running at port " +  txtport.getText() + "...");
				count = 0;
			}
			
		}	
		else
		{
			lblStatus.setText("Error: cannot create server at port " +  errorport + "!!!");
		}
			

		
	}

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					CreateServerWindow frame = new CreateServerWindow();
//					frame.setVisible(true);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public CreateServerWindow() {
		setIconImage(ChatWindow.createImage("/icon/logoBanana.png"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				ConfirmExit();
			}
		});
		setResizable(false);
		setTitle("Create Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 422, 335);
		setVisible(true);
		
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		
		this.menuAction = new JMenu("Action");
		this.menuBar.add(this.menuAction);
		
		this.menuActionOnServer = new JMenuItem("Start/Stop server");
		this.menuActionOnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAction.doClick();
			}
		});
		this.menuAction.add(this.menuActionOnServer);
		
		this.menuExit = new JMenuItem("Exit");
		this.menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//event click menu exit
				ConfirmExit();
			}
		});
		this.menuAction.add(this.menuExit);
		this.contentPane = new JPanel();
		this.contentPane.setForeground(SystemColor.textHighlight);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		
		this.lblFillTheBox = new JLabel("Fill the box below with your favorite port number");
		this.lblFillTheBox.setForeground(SystemColor.textHighlight);
		
		this.lblNewLabel = new JLabel("and click Start server button then share your IP address");
		this.lblNewLabel.setForeground(SystemColor.textHighlight);
		
		this.lblNewLabel_1 = new JLabel("and port number to others so they could connect to your server.");
		this.lblNewLabel_1.setForeground(SystemColor.textHighlight);
		
		this.lblNewLabel_2 = new JLabel("Your IP Address:");
		this.lblNewLabel_2.setForeground(Color.MAGENTA);
		
		this.lblIP = new JLabel("ip");
		this.lblIP.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.lblIP.setForeground(Color.DARK_GRAY);
		CreateServerWindow.GetIP();
		this.lblIP.setText(myHost.getHostAddress());
		
		this.lblNewLabel_4 = new JLabel("Your Port:");
		this.lblNewLabel_4.setForeground(Color.MAGENTA);
		
		this.txtport = new JTextField();
		this.txtport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		this.txtport.setForeground(Color.DARK_GRAY);
		this.txtport.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.txtport.setColumns(10);
		
		
		this.btnAction = new JButton("Start server");
		this.btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//event click button Start/Stop server
				//if server has not started yet then start it 
				if (btnAction.getText().equals("Start server"))
				{
					//check port
					if (!LoginWindow.isValidPort(txtport.getText()))
					{
						errorport = txtport.getText();
						statusCode = 2;//error
						JOptionPane.showMessageDialog(CreateServerWindow.this, "Your port number is invalid!", "Error", JOptionPane.ERROR_MESSAGE);						
						return;
					}
					else //port is valid
					{				
						//start server
						s = new Server(Integer.parseInt(txtport.getText()));
						s.start();
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// set status on gui if create server success
						if (s.isvalidport)
						{
							statusCode = 1;//running
							btnAction.setText("Stop server");
							txtport.setEnabled(false);
							JOptionPane.showMessageDialog(CreateServerWindow.this, "Your server has been hosted successfully at\n" + lblIP.getText() + " : " + txtport.getText(), "Announment", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							errorport = txtport.getText();
							statusCode = 2;//error
							JOptionPane.showMessageDialog(CreateServerWindow.this, "Another server is running at port " + txtport.getText(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						
					}
				}
				//if server is running then stop it
				else
				{
					if (JOptionPane.showConfirmDialog(CreateServerWindow.this, "All people who is connecting to your server will be disconnected.\n Stop server?", "Confirm Stop server...", JOptionPane.YES_NO_OPTION) == 0)
					{
						//set status on gui
						statusCode = 0;//stop
						btnAction.setText("Start server");
						txtport.setEnabled(true);
						//stop server
						s.setStop();
						s = null;
					}
				}
			}
		});
		
		this.lblNewLabel_3 = new JLabel("Banana Inc Copyright 2014");
		this.lblNewLabel_3.setForeground(new Color(255, 204, 51));
		this.lblNewLabel_3.setBackground(new Color(204, 255, 0));
		this.lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 9));
		
		this.lblStatus = new JLabel("Ready");
		this.lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 9));
		GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(72)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblNewLabel_2)
								.addComponent(this.lblNewLabel_4))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblIP, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.txtport, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(22)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblFillTheBox)
								.addComponent(this.lblNewLabel_1)
								.addComponent(this.lblNewLabel)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(154)
							.addComponent(this.btnAction)))
					.addContainerGap(73, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(this.lblStatus, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
					.addGap(47)
					.addComponent(this.lblNewLabel_3))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addComponent(this.lblFillTheBox)
					.addGap(13)
					.addComponent(this.lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(this.lblNewLabel_1)
					.addGap(40)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblNewLabel_2)
						.addComponent(this.lblIP))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblNewLabel_4)
						.addComponent(this.txtport, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addComponent(this.btnAction)
					.addPreferredGap(ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblNewLabel_3)
						.addComponent(this.lblStatus)))
		);
		this.contentPane.setLayout(gl_contentPane);
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStatus(statusCode);
			}
		});
		timer.start();
	}
}
