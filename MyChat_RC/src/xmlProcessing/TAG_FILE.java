/**
 * TAG WHEN SEND AND RECIVE FILE XML 
 */
package xmlProcessing;

/**
 * @author Game
 *
 */
public enum TAG_FILE implements TAG{
	FILE_REQ("<FILE_REQ>", "</FILE_REQ>"),
	FILE_REQ_NOACK("","<FILE_REQ_NOACK />"),
	FILE_REQ_ACK("<FILE_REQ_ACK>", "</FILE_REQ_ACK>"),
	FILE_DATA_BEGIN("", "<FILE_DATA_BEGIN />"),
	FILE_DATA("<FILE_DATA>", "</FILE_DATA>"),
	FILE_DATA_END("", "<FILE_DATA_END />");
	
	private final String openTag;
	private final String closeTag;
	TAG_FILE(String open, String close){
		this.openTag = open;
		this.closeTag = close;
	}
	public String toString(){
		return name();
	}
	
	
	public TAG tagWithString(String vl){
		return valueOf(vl);
	}
	
	public String enumType(){
		return "TAG_FILE";
	}
	
	public String getOpenTag(){
		if(this.openTag!="")
		return this.openTag;
		else
			return null;}
	public String getCloseTag(){return this.closeTag;}
}
