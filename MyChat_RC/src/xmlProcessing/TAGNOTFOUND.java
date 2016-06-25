package xmlProcessing;

public enum TAGNOTFOUND implements TAG{
	NOTFOUND;

	@Override
	public String toString(){
		return name();
	}
	
	
	@Override
	public TAG tagWithString(String val) {
		// TODO Auto-generated method stub
		return valueOf(val);
	}
	
	public String enumType(){
		return "TAGNOTFOUND";
	}
	
	public String getOpenTag(){return "()";}
	public String getCloseTag(){return "()";}
	
}
