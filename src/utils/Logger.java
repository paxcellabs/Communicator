package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * This class is used to deal with logging operation
 * 
 * @author Kuldeep Saxena
 * 
 */
public class Logger {

	/**
	 * default accessor object
	 */
	private static final Logger logger = new Logger();

	/**
	 * output stream
	 */
	private FileOutputStream out;

	/**
	 * default constructor initializes output stream
	 * 
	 */
	private Logger() {

		try {
			Calendar c = Calendar.getInstance();
			File dir = new File("logs");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File("logs/" + c.get(Calendar.DAY_OF_MONTH) + "_"
					+ c.get(Calendar.MONTH) + "_" + c.get(Calendar.YEAR) + "-"
					+ c.get(Calendar.HOUR) + "_" + c.get(Calendar.MINUTE) + "_"
					+ c.get(Calendar.SECOND) + ".log");
			if (!f.exists()) {
				f.createNewFile();
			}
			out = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * default accessor method
	 * 
	 * @return object of this class
	 */
	public static Logger getDefaultInstance() {
		return logger;
	}

	/**
	 * write message to the output stream
	 * 
	 * @param message
	 *            message to be written
	 */
	public void writeLog(String message) {
		try {
			out.write(message.getBytes());
			out.write("\n".getBytes());
		} catch (Exception e) {

		}
	}
}
