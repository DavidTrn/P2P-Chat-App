package mygui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AboutWindow extends JDialog {
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblInstructor;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JButton btnOK;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;
	
	//exit when click OK
	private void Exit()
	{
		this.dispose();
	}


	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			AboutWindow dialog = new AboutWindow("aaa.png");
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public AboutWindow() {
		setIconImage(ChatWindow.createImage("/icon/logoBanana.png"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("About Banana Messenger");
		setResizable(false);
		setBounds(100, 100, 353, 300);
		getContentPane().setLayout(null);
		
		
		this.panel = new JPanel();
		this.panel.setBackground(new Color(255, 255, 204));
		this.panel.setBorder(UIManager.getBorder("DesktopIcon.border"));
		this.panel.setBounds(0, 0, 347, 272);
		getContentPane().add(this.panel);
		
		this.lblNewLabel = new JLabel("Developer Group:");
		this.lblNewLabel.setForeground(new Color(0, 255, 255));
		this.lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		
		this.lblNewLabel_1 = new JLabel("Trần Anh Gien");
		this.lblNewLabel_1.setForeground(new Color(102, 0, 0));
		
		this.lblNewLabel_2 = new JLabel("Bùi Tiến Đạt");
		this.lblNewLabel_2.setForeground(new Color(102, 0, 0));
		
		this.lblNewLabel_3 = new JLabel("Trần Quốc Đại");
		this.lblNewLabel_3.setForeground(new Color(102, 0, 0));
		
		this.lblInstructor = new JLabel("Instructor:");
		this.lblInstructor.setForeground(new Color(0, 255, 255));
		this.lblInstructor.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		
		this.lblNewLabel_4 = new JLabel("Mr. Đoàn Việt Hưng");
		this.lblNewLabel_4.setForeground(Color.GREEN);
		
		this.lblNewLabel_5 = new JLabel("");
		this.lblNewLabel_5.setIcon(ChatWindow.createImageIcon("/icon/banana-man1.gif"));
		
		this.btnOK = new JButton("OK");
		this.btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Exit();
			}
		});
		
		this.lblNewLabel_6 = new JLabel("Please e-mail to chjcken123@gmail.com ");
		this.lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		this.lblNewLabel_7 = new JLabel("if you have any question.");
		this.lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		this.lblNewLabel_8 = new JLabel("Banana Inc Copyright 2014");
		this.lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 9));
		GroupLayout gl_panel = new GroupLayout(this.panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(this.lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(30)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblNewLabel_2)
								.addComponent(this.lblNewLabel_1)
								.addComponent(this.lblNewLabel_3)))
						.addComponent(this.lblNewLabel)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(this.lblInstructor)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(this.lblNewLabel_4))
							.addComponent(this.btnOK, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(30, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(this.lblNewLabel_6)
						.addComponent(this.lblNewLabel_7))
					.addContainerGap(144, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(112, Short.MAX_VALUE)
					.addComponent(this.lblNewLabel_8)
					.addGap(107))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(this.lblNewLabel)
							.addGap(18)
							.addComponent(this.lblNewLabel_1)
							.addGap(18)
							.addComponent(this.lblNewLabel_2)
							.addGap(18)
							.addComponent(this.lblNewLabel_3)
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.lblInstructor)
								.addComponent(this.lblNewLabel_4)))
						.addComponent(this.lblNewLabel_5))
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(19)
							.addComponent(this.lblNewLabel_6)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.lblNewLabel_7)
							.addGap(11)
							.addComponent(this.lblNewLabel_8))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(18)
							.addComponent(this.btnOK, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))))
		);
		this.panel.setLayout(gl_panel);

	}
}
