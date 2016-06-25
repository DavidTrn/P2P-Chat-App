/**
 * INCLUDE ALL TAG XML PROTOCOL
 */
package xmlProcessing;

/**
 * @author Game
 *
 */
public interface TAG {
	public String toString();
	public TAG tagWithString(String val);
	public String enumType();
	public String getOpenTag();
	public String getCloseTag();

	
}

