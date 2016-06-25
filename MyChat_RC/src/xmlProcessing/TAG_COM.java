
package xmlProcessing;

public enum TAG_COM implements TAG{
		SESSION("<SESSION>", "</SESSION>"),
		PEER("<PEER>", "</PEER>"),
		PEER_NAME("<PEER_NAME>", "</PEER_NAME>"),
		PORT("<PORT>", "</PORT>"),
		SESSION_KEEP_ALIVE("<SESSION_KEEP_ALIVE>", "</SESSION_KEEP_ALIVE>"),
		STATUS("<STATUS>", "</STATUS>"),
		IP("<IP>", "</IP>"),
		SESSION_ACCEPT("<SESSION_ACCEPT>", "</SESSION_ACCEPT>"),
		SESSION_DENY("","<SESSION_DENY />"),
		CHAT_REQ("<CHAT_REQ>", "</CHAT_REQ>"),
		CHAT_DENY("","<CHAT_DENY />"),
		CHAT_ACCEPT("","<CHAT_ACCEPT />"),
		CHAT_CLOSE("","<CHAT_CLOSE />"),
		CHAT_MSG("<CHAT_MSG>","</CHAT_MSG>");

	private final String openTag;
	private final String closeTag;
	TAG_COM(String open, String close){
		this.openTag = open;
		this.closeTag = close;
	}
	
	public String getOpenTag(){
		if(this.openTag!="")
		return this.openTag;
		else
			return null;
	}
	
	public String getCloseTag(){
		
		return this.closeTag;
		
	}
	
	public String toString(){
		return name();
	}
	
	
	public TAG tagWithString(String vl){
		return valueOf(vl);
	}
	
	
	public String enumType(){
		return "TAG_COM";
	}
	
}
