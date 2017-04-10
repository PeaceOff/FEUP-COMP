
public class Utils {
	
	private static String tabs = new String("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
	
	public static int tabIndex = 0;
	
	public static String getTab(){
		return tabs.substring(0, tabIndex);
	}
	
}
