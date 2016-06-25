package recycle;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Robot;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JToggleButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class test extends JFrame {

	private JPanel contentPane;
	private JButton btnNewButton;
	private JFileChooser fc;
	Robot r;
	private JToggleButton tglbtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 * @throws AWTException 
	 */
	public test() throws AWTException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();
			}
		});
		r = new Robot();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		
		this.btnNewButton = new JButton("New button");
		this.btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fc.setSelectedFile(new File("abc.exe"));
				fc.showOpenDialog(test.this);
				
			}
		});
		this.btnNewButton.setBounds(110, 82, 89, 23);
		this.contentPane.add(this.btnNewButton);
		
		this.tglbtn = new JToggleButton("btn");
		this.tglbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					r.mousePress(InputEvent.BUTTON1_MASK);
					r.mouseRelease(InputEvent.BUTTON1_MASK);
				}
			}
		});
		this.tglbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		this.tglbtn.setBounds(96, 156, 68, 23);
		this.contentPane.add(this.tglbtn);
		setVisible(true);
		this.fc = new JFileChooser(System.getProperty("user.home") + "\\Desktop");	
	}
	
	protected void exit() {
		// TODO Auto-generated method stub
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		if(JOptionPane.showConfirmDialog(null, "You will be disconnected and no longer to be able to chat with people in this server.\nAre you sure to sign out?", "Confirm sign out...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{
			dispose();
		}
	}
}
