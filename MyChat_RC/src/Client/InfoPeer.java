package Client;

import java.util.List;

public class InfoPeer {
	public final String peerName;
	public final int peerPort;
	public final String IP;
	
	public InfoPeer(String name, int port, String ip ){
		this.peerName = name;
		this.peerPort = port;
		this.IP = ip;
	}
}
