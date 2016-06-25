package recycle;

import mygui.CreateServerWindow;

public class testthread extends Thread{
	public void run()
	{
		new CreateServerWindow().setVisible(true);
	}

}
