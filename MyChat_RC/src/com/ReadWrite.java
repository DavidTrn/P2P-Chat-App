/**
 * 
 */
package com;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Game
 * 
 */
public class ReadWrite {
	private InputStream in;
	private DataOutputStream out;

	public ReadWrite(InputStream in, OutputStream out) {
		this.in = in;
		this.out = new DataOutputStream(out);
	}

	public boolean write(String msg) {
		try {
			this.out.write(msg.getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			return false;
			//e.printStackTrace();
		}
		return true;
	}

	public String read() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String s = null;
		while (true) {
			// System.out.println(in.available());
			int r;
			try {
				r = in.read();
				if (r == -1)
					break;

				baos.write(r);

				if (in.available() == 0) {
					 s = new String(baos.toByteArray(),
							Charset.forName("UTF-8"));
					
					baos.reset();
					return s;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("ReadWrite error");
				e1.printStackTrace();
				try {
					this.in.close();
					this.out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
				
			}

		}
		return s;
	}

	public void close() {
		if (this.in != null) {
			try {
				this.in.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		if (this.out != null) {
			try {
				this.out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}
