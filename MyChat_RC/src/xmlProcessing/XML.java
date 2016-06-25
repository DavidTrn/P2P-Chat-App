package xmlProcessing;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Client.InfoPeer;

public class XML {
	private String xmlString;
	//private Document documentXML;
	private List<String> allTag = new ArrayList<String>();

	public XML(String xmlString) {

			this.xmlString = xmlString;
	}

	// Get root tag
	public TAG getRootTag() {
		// String tagString = documentXML.getFirstChild().getNodeName();
		// System.out.println(tagString);
		char ch;
		String tagString="";
		int i=0;
		while((ch = this.xmlString.charAt(++i)) !='>' && ch!=' ') tagString+=ch;
		
		//System.out.println(tagString);
		
		for (TAG_COM t : TAG_COM.values()) {
			if (t.toString().equals(tagString)) {
				// System.out.println(t.toString());
				return t;
			}
		}

		for (TAG_FILE t : TAG_FILE.values()) {
			if (t.toString().equals(tagString)) {
				// System.out.println(t.toString());
				return t;
			}
		}

		return TAGNOTFOUND.NOTFOUND; // do not find this tag

	}

	// Instance method convert string to doc xml
	public Document toDocument() {
		return XML.documentFromString(xmlString);
	}

	// Class method Create document from string
	public static Document documentFromString(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					xmlString)));
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Old
	/*
	 * public List<String> getContentForTag(TAG tag){ ArrayList<String> result =
	 * new ArrayList<String>(); NodeList nodeLst =
	 * documentXML.getElementsByTagName(tag.toString()); // Get set of tags have
	 * the same name if(nodeLst.getLength()==0) return null; for(int i = 0;
	 * i<nodeLst.getLength(); i++){
	 * result.add(nodeLst.item(i).getTextContent()); }
	 * 
	 * return result; }
	 */

	// New
	public List<String> getContentForTag(TAG t) {
		// System.out.println(t.getOpenTag()+" "+t.getCloseTag());
		List<String> a1 = new ArrayList<String>();
		int jump = 0, x, y;
		while (true) {
			x = xmlString.indexOf(t.getOpenTag(), jump);
			if (x == -1)
				break;
			y = xmlString.indexOf(t.getCloseTag(), jump);
			a1.add(xmlString.substring(x + t.getOpenTag().length(), y));
			jump = y + t.getCloseTag().length();

			// System.out.println(x + " " + y);
			// a1.add(msg.substring(x, y));
			// System.out.println(a1.get(0) + " " + a1.get(1));
		}
		return a1;
	}

	// convert document XML to string format with XML type
	public static String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// old
	/*
	 * public String toString(){ return
	 * XML.getStringFromDocument(this.documentXML); }
	 */

	// new
	public String toString() {
		return this.xmlString;
	}

	public static String buildMsgWithTagAndContents(TAG tag, String contents) {
		if (contents != null && contents != "")
			return "<" + tag.toString() + ">" + contents + "</" + tag.toString()
					+ ">";
		else
			return "<" + tag.toString() + " />";
	}

	// Create a new String xml contains all peer
	// public static String xmlAllPeersInNet(List<Peer> listPeers) {
	// String result = null;
	// try {
	// DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
	// DocumentBuilder builder = bf.newDocumentBuilder();
	// Document doc = builder.newDocument();
	// Element root = doc.createElement(TAG_COM.SESSION_ACCEPT.toString());
	// doc.appendChild(root);
	//
	// Element peer = doc.createElement(TAG_COM.PEER.toString());
	// root.appendChild(peer);
	//
	// for (Peer p : listPeers) {
	// Element peername = doc.createElement(TAG_COM.PEER_NAME
	// .toString());
	// peername.setTextContent(p.name);
	// peer.appendChild(peername);
	//
	// Element peerip = doc.createElement(TAG_COM.IP.toString());
	// peerip.setTextContent(p.IP);
	// peer.appendChild(peerip);
	//
	// Element peerport = doc.createElement(TAG_COM.PORT.toString());
	// peerport.setTextContent(p.port + "");
	// peer.appendChild(peerport);
	// }
	//
	// result = XML.getStringFromDocument(doc);
	// // Debug
	// System.out.println(result);
	//
	// } catch (ParserConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	public static String xmlAllPeersInNet(List<InfoPeer> listPeers, String thisClient) {
		String msg = TAG_COM.SESSION_ACCEPT.getOpenTag();
		for (InfoPeer p : listPeers) {
			if(p.peerName.equals(thisClient)) continue;
			msg = msg + TAG_COM.PEER.getOpenTag()
					+ TAG_COM.PEER_NAME.getOpenTag() + p.peerName
					+ TAG_COM.PEER_NAME.getCloseTag() + TAG_COM.IP.getOpenTag()
					+ p.IP + TAG_COM.IP.getCloseTag()
					+ TAG_COM.PORT.getOpenTag() + p.peerPort
					+ TAG_COM.PORT.getCloseTag() + TAG_COM.PEER.getCloseTag();

		}

		msg += TAG_COM.SESSION_ACCEPT.getCloseTag();
		return msg;
	}

	public List<InfoPeer> getListPeersOnline() {
		// Get list peers from server

		if (!((TAG_COM) getRootTag() == TAG_COM.SESSION_DENY)) {
			List<String> lst_name = getContentForTag(TAG_COM.PEER_NAME);
			List<String> lst_ip = getContentForTag(TAG_COM.IP);
			List<String> lst_port = getContentForTag(TAG_COM.PORT);
			List<InfoPeer> lstPeers = new ArrayList<InfoPeer>();
			for (int i = 0; i < lst_name.size(); i++) {
				InfoPeer peer = new InfoPeer(lst_name.get(i),
						Integer.parseInt(lst_port.get(i)), lst_ip.get(i));
				lstPeers.add(peer);
			}

			return lstPeers;
		} else {
			System.err.println("It is not xml contain peer's infomation! :(");
			return null;
		}

	}

	// new version XML methods
	public static boolean checkXML(String s) {
		String regex = "^(<\\w+>)(((<\\w+>)(((<\\w+>)([^<>]|(<<|>>))+(</\\w+>))+|([^<>]|(<<|>>))+)(</\\w+>))+|([^<>]|(<<|>>))+)*(</\\w+>)$";
		String regex2 = "^<\\w+ />";
		if(s==null) return false;
		if (s.matches(regex)) {
			String temp = "";
			int o = 0, c = 0;
			String content = "";
			/*ArrayList<String>*/ //allTag = new ArrayList<String>();
			Stack<String> st = new Stack<String>();
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				if (ch == '<') {
					// temp += ch;
					// while ((ch = s.charAt(++i)) == '<');

					if ((s.charAt(i + 1)) == '<' && s.charAt(i + 2) == '/') {
						return false;
					} else {
						if ((s.charAt(i + 1)) == '<') {
							i++;
							continue;
						}
					}
					// temp += '<';
					temp += ch;

					while ((ch = s.charAt(++i)) != '>') {
						temp += ch;

					}
					temp += ch;
					//System.out.println("TAG: " + temp);

					if (isOpenTag(temp)) {
						if (docheckTag(temp) == false) {
							//System.out.println(temp + " checktag false");
							return false;
						} else {
							//System.out.println(c);
							if(c==1 && !st.isEmpty()) return false;
							//System.out.println("push " + temp);
							st.push(temp);
							//allTag.add(temp);
						}
					} else {
						String t1 = temp.substring(2);
						//System.out.println("sub " + temp + " = " + t1);
						String t2 = st.peek();
						//System.out.println("peek " + t2);
						if (t1.equals(t2.substring(1))) {
							//System.out.println("euqal");
							c=0;
							//allTag.add(st.peek());
							//allTag.add(temp);
							st.pop();
							if (st.empty())
								return true;

						}
					}
					temp = "";
				} else {
					//System.out.println(ch);
					c=1;
				}
			}

			return false;
		} else {
			if (s.matches(regex2))
				return true;
		}
		return false;
	}

	/*
	 * private static String replaceAll(String str, List<String> list){ String s
	 * = str; s = s.replaceAll("&", "&amp;"); s = s.replaceAll("<<", "&lt;"); s
	 * = s.replaceAll(">>", "&gt;"); s = s.replaceAll("'", "&apos;"); s =
	 * s.replaceAll("\"", "&quot;"); StringBuffer result=new StringBuffer();
	 * for(int i = 0; i<list.size(); i++){
	 * if(!list.get(i).substring(2).equals(list.get(i+1).substring(1)))
	 * continue; int x = str.indexOf(list.get(i)) + list.get(i).length(); int y
	 * = str.indexOf(list.get(i+1)); result.append(list.get(i));
	 * result.append(str.substring(x, y).replaceAll("&", "&amp;"));
	 * result.append(list.get(i+1));
	 * 
	 * } }
	 */
	private static boolean isOpenTag(String temp) {
		// TODO Auto-generated method stub

		if (temp.charAt(1) != '/') {
			// System.out.println(temp + " true");
			return true;
		}

		return false;
	}

	private static boolean docheckTag(String temp) {
		// TODO Auto-generated method stub
		// System.out.println(temp);
		String[] TAG = { "<SESSION>", "<PEER>", "<PEER_NAME>", "<IP>",
				"<PORT>", "<SESSION_KEEP_ALIVE>", "<SESSION_ACCEPT>",
				"<STATUS>", "<CHAT_REQ>", "<SESSION_DENY />",
				"<CHAT_ACCEPT />", "<CHAT_CLOSE />", "<CHAT_DENY />",
				"<CHAT_MSG>", "<FILE_REQ>", "<FILE_REQ_NOACK />", "<FILE_REQ_ACK>", "<FILE_DATA_BEGIN />",
				"<FILE_DATA>", "<FILE_DATA_END />"};
		// if (temp.equals("<A>") || temp.equals("<B>") || temp.equals("</B>")
		// || temp.equals("</A>") || temp.equals("<SESSION>") ||
		// temp.equals("<PEER>")||
		// temp.equals("<PEER_NAME>") || temp.equals(anObject)) {
		// //System.out.println(true);
		// return true;
		// }
		for (String s : TAG) {
			if (s.equals(temp))
				return true;
		}

		return false;
	}

}
