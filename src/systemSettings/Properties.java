package systemSettings;

import java.awt.Toolkit;

/**
 * this class provides some methods to get system properties
 * 
 * @author Kuldeep Saxena
 * 
 */
public class Properties {

	/**
	 * returns screen width
	 * 
	 * @return screen width
	 */
	public static int getScreenWidth() {

		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}

	/**
	 * returns screen height
	 * 
	 * @return screen height
	 */
	public static int getScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
}