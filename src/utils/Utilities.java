package utils;

import flags.MessageType;
import flags.OperationStatus;
import gui.ChatWindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import beans.Message;
import beans.TextMessage;
import beans.UserData;
import beans.UserGroupSettings;
import beans.UserSettings;
import controls.MultiPurposeControl;

/**
 * This class contains some utility functions. it works as helper class to
 * provide comman functions in one place
 * 
 * @author Kuldeep Saxena
 * 
 */
public class Utilities {
	/**
	 * default object
	 */
	private static final Utilities ub = new Utilities();

	/**
	 * map containing status code to displayable message
	 */
	private static HashMap<Integer, String> messages;
	/**
	 * maps containing link of user to the opened window
	 */
	private static Hashtable<String, ChatWindow> openWindow = new Hashtable<String, ChatWindow>();

	/**
	 * all user list
	 */
	private HashMap<String, HashMap<String, UserSettings>> allUsers = new HashMap<String, HashMap<String, UserSettings>>();
	/**
	 * current user
	 */
	private String currentUser = null;
	/**
	 * current user's profile data
	 */
	private UserData currentUserData;

	/**
	 * static block to initialize message code mapping
	 */
	static {
		messages = new HashMap<Integer, String>();
		messages.put(OperationStatus.INVALID_ID, "Invalid ID");
		messages.put(OperationStatus.INVALID_PASSWORD, "Invalid Password");
		messages.put(OperationStatus.OPERATION_FAILURE, "operation Failed");
		messages.put(OperationStatus.OPERATION_SUCESSFUL,
				"operation Successful");
		messages.put(OperationStatus.USER_ALREADY_EXISTS, "User already exists");
		messages.put(OperationStatus.SUCESSFUL_LOGIN, "Sucessful login");
		messages.put(OperationStatus.USER_REGISTERED_SUCESSFULLY,
				"User Registered Sucessfully");
		messages.put(OperationStatus.USER_UPDATED_SUCESSFULLY,
				"User's Profile updated sucessfully");
		messages.put(OperationStatus.SERVER_NOT_AVAILABLE,
				"Unable to connect to server");
	}

	/**
	 * Default constructor
	 * 
	 */
	private Utilities() {
	}

	/**
	 * default accessor method
	 * 
	 * @return this class object
	 */
	public static Utilities getDefaultInstance() {
		return ub;
	}

	/**
	 * Searches index with in array of objects to find the given text
	 * 
	 * @param STR_text
	 *            text to be compared
	 * @param OBJ_listData
	 *            array of object to be compared with the STR_text
	 * @return visible index of list
	 * 
	 */
	public static int findVisibleIndex(String STR_text, Object OBJ_listData[]) {
		int int_returnValue = -1;
		String STR_data = "";
		STR_text = STR_text.toUpperCase();
		for (int i = 0; i < OBJ_listData.length; i++) // loop until list length
		{
			STR_data = ((String) OBJ_listData[i]).trim().toUpperCase();
			if (STR_data.equals(STR_text)) {
				int_returnValue = i;
				break; // break loop
			}

			else {
				if (STR_data.length() < STR_text.length()) {
					continue;
				}

				String STR_sub = STR_data.substring(0, STR_text.length());
				if (STR_sub.equals(STR_text)) {
					int_returnValue = i;
					break;
				}
			}
		}
		return int_returnValue;
	}

	/**
	 * This function enables popup windows to be scrolled
	 * 
	 * @param COM_curWindow
	 *            window which has to to be scrolled
	 * @param bool_lToR
	 *            direction of scrolling, only left to right scrolling
	 *            functionality is provided
	 */
	public void setScrollWindow(JComponent COM_curWindow, boolean bool_lToR) {
		new Animation(COM_curWindow).start();
	}

	/**
	 * inner class to provide animation
	 * 
	 * @author Kuldeep Saxena
	 * 
	 */
	private class Animation extends Thread {
		private JComponent COM_window;
		private int int_width;

		Animation(JComponent COM_window) {
			this.int_width = COM_window.getPreferredSize().width;
			this.COM_window = COM_window;
		}

		public void run() {
			while (COM_window.getPreferredSize().width > 0) {
				int_width -= 50;
				// manupilating component size
				COM_window.setPreferredSize(new Dimension(int_width, COM_window
						.getPreferredSize().height));
				COM_window.setMaximumSize(new Dimension(int_width, COM_window
						.getPreferredSize().height));
				COM_window.updateUI();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}

		}
	}

	/**
	 * functiont zip files not used till now
	 */
	public static void doCreateZipFile(String newfiles[], String outputDir) {

		String files[] = null;
		File zip = new File(outputDir);
		Hashtable h = new Hashtable();
		if (zip.exists() && zip.isFile()) {
			try {
				ZipFile zf = new ZipFile(outputDir);

				for (Enumeration entries = zf.entries(); entries
						.hasMoreElements();) {
					ZipEntry zipEntry = (ZipEntry) entries.nextElement();
					if (new File(zipEntry.getName()).exists())
						h.put(zipEntry.getName(), "0");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		for (int i = 0; i < newfiles.length; i++) {
			h.put(newfiles[i], "0");
		}

		files = new String[h.size()];
		Enumeration e = h.keys();
		int i = 0;
		while (e.hasMoreElements()) {
			files[i] = (String) e.nextElement();
			// files[i]
			i++;
		}

		if (files == null || files.length < 1) {
			System.out.println("no files are given");
			return;
		}

		byte[] buf = new byte[1024];

		try {

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					outputDir));
			// Compress the files
			for (i = 0; i < files.length; i++) {

				File f = new File(files[i]);
				// JOptionPane.showMessageDialog(null,f.getAbsolutePath());
				if (!f.exists())
					continue;
				FileInputStream in = new FileInputStream(files[i]);

				out.putNextEntry(new ZipEntry(files[i]));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();

		}

	}

	/**
	 * Funtion unzips file not used till now
	 */

	public static void doUnzipFiles(String zipFileName) {

		try {

			ZipFile zf = new ZipFile(zipFileName);
			String dir = null;

			// JOptionPane.showMessageDialog(null,"in Unzip ZipFiles:"+zf.size());
			// Enumerate each entry
			for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {

				// Get the entry and its name
				ZipEntry zipEntry = (ZipEntry) entries.nextElement();
				String zipEntryName = zipEntry.getName().trim();
				// JOptionPane.showMessageDialog(null,zipEntryName);
				if (zipEntryName != null)

				{
					if (zipEntryName.lastIndexOf("/") != -1)
						dir = zipEntryName.substring(0,
								zipEntryName.lastIndexOf("/"));
					else if (zipEntryName.lastIndexOf("\\") != -1)
						dir = zipEntryName.substring(0,
								zipEntryName.lastIndexOf("\\"));

					File f = new File(dir);

					if (!f.exists()) {
						f.mkdirs();
					}

					OutputStream out = new FileOutputStream(zipEntryName);
					InputStream in = zf.getInputStream(zipEntry);

					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					// Close streams
					out.close();
					in.close();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this function loads server settings
	 * 
	 * @return Properties loaded with server settings
	 */
	public Properties readServerSettings() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("resources/server.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * return message text according to code
	 * 
	 * @param code
	 *            for which message to be returned
	 * @return message text according to code
	 */
	public String mapMessageCode(int code) {
		return messages.get(code);
	}

	/**
	 * Adds opened window for the user, such that no duplicate window open for
	 * same user to chat
	 * 
	 * @param forUser
	 *            user id for whom window is open
	 * @param cw
	 *            chat window object
	 */
	public ChatWindow addOpenedWindow(String forUser, ChatWindow cw) {
		if (getOpenedWindow(forUser) == null) {
			openWindow.put(forUser, cw);
			return getOpenedWindow(forUser);
		} else {
			return getOpenedWindow(forUser);
		}

	}

	/**
	 * return opened window if any for the user
	 * 
	 * @param forUser
	 *            user for which window has to be returned
	 * @return chat window object
	 */
	public ChatWindow getOpenedWindow(String forUser) {
		// System.out.println(forUser +"           " + openWindow.get(forUser));
		return openWindow.get(forUser);
	}

	/**
	 * returns all the users for whom chat window is opened
	 * 
	 * @return String [] users
	 */
	public String[] getOpenedUsers() {
		Enumeration<String> e = openWindow.keys();
		String[] users = new String[openWindow.size()];
		int i = 0;
		while (e.hasMoreElements()) {
			users[i] = e.nextElement();
			i++;
		}
		return users;
	}

	/**
	 * Adds users to given group
	 * 
	 * @param group
	 *            group name
	 * @param users
	 *            users within that group
	 */
	public void addUserList(String group, HashMap<String, UserSettings> users) {
		this.allUsers.put(group, users);
	}

	/**
	 * returns user setting by giving id
	 * 
	 * @param userID
	 *            id of the user
	 * @return UserSettings
	 */
	public UserSettings getUserSettings(String userID) {
		HashMap<String, UserSettings> hm = allUsers.get("all");
		System.out.println(hm + userID);
		return hm.get(userID);
	}

	/**
	 * returns users in requested group
	 * 
	 * @param groupName
	 *            name of group
	 * @return users within the group
	 */
	public HashMap<String, UserSettings> getUsersInGroup(String groupName) {
		return allUsers.get(groupName);
	}

	/**
	 * return users within the map provided
	 * 
	 * @param users
	 *            user map
	 * @return UserSettings object [] containing users
	 */
	public UserSettings[] getUsersInMap(HashMap<String, UserSettings> users) {
		UserSettings[] usersToBeReturned = new UserSettings[users.size()];
		Set<String> keys = users.keySet();
		Iterator<String> iter = keys.iterator();
		int i = 0;
		while (iter.hasNext()) {
			usersToBeReturned[i] = users.get(iter.next());
			i++;
		}
		return usersToBeReturned;
	}

	/**
	 * return available group names
	 * 
	 * @return user available group
	 */
	public String[] getUserGroups() {

		Set<String> groups = allUsers.keySet();
		Iterator<String> i = groups.iterator();
		String str_groups[] = new String[groups.size()];
		int j = 0;
		while (i.hasNext()) {
			str_groups[j] = i.next();
			j++;

		}
		return str_groups;
	}

	/**
	 * sets user group data
	 * 
	 * @param userData
	 *            user profile data
	 */
	public void setUserGroupData(UserData userData) {
		ArrayList<UserGroupSettings> userGroups = userData.getUserGroups();
		HashMap<String, UserSettings> users = new HashMap<String, UserSettings>();
		for (int i = 0; i < userGroups.size(); i++) {
			String group = userGroups.get(i).getGroupName();
			UserSettings usersInGroup[] = userGroups.get(i).getUserIDs();
			for (int j = 0; j < usersInGroup.length; j++) {
				users.put(usersInGroup[j].getUserID(), usersInGroup[j]);
			}
			allUsers.put(group, users);
		}
	}

	/**
	 * updates user group data
	 * 
	 * @param groupName
	 *            group name
	 * @param users
	 *            new users
	 */
	public void updateUserGroupData(String groupName,
			HashMap<String, UserSettings> users) {
		allUsers.put(groupName, users);
	}

	/**
	 * current logged in user
	 * 
	 * @param currentUser
	 *            current user
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * returns logged in user
	 * 
	 * @return current user
	 */
	public String getCurrentUser() {
		return this.currentUser;
	}

	/**
	 * sets current user data
	 * 
	 * @param userData
	 *            user's profile data
	 */
	public void setCurrentUserData(UserData userData) {
		this.currentUserData = userData;
		setUserGroupData(userData);
	}

	/**
	 * returns users current profile data
	 * 
	 * @return user daya
	 */
	public UserData getCurrentUserData() {
		return currentUserData;
	}

	/**
	 * returns location of the taskbar
	 * 
	 * @return bounds of taskbar
	 */
	public Rectangle getTaskBarBounds() {
		Rectangle screensize = new Rectangle(new Point(0, 0), Toolkit
				.getDefaultToolkit().getScreenSize());
		Rectangle maxwindow = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();

		Area taskbar = new Area(screensize);
		taskbar.subtract(new Area(maxwindow));
		return taskbar.getBounds();

	}

	/**
	 * remove list from open window
	 * 
	 * @param userID
	 *            user for which window is to be removed
	 * @return ChatWindow removed
	 */
	public ChatWindow removeWindowForUser(String userID) {
		return openWindow.remove(userID);
	}

	/**
	 * called to show message in the text box this method is called by the
	 * normal send as well as to show offline messages
	 * 
	 * @param message
	 *            message to show
	 * @param container
	 *            parent container
	 * @param TP_display
	 *            text box
	 * @param displayBoxDoc
	 *            message box doc
	 * @param displayBoxStyle
	 *            message box style
	 * @param local
	 *            messge is local or remote
	 */
	public void showMessage(Message message, Container container,
			JTextPane TP_display, StyledDocument displayBoxDoc,
			Style displayBoxStyle, boolean local) {

		String fromUser = message.getFromUser();
		Date d = message.getSentTime();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		int am = c.get(Calendar.AM_PM);
		// try{JOptionPane.showMessageDialog(null,"Display Show "
		// +message.getTextMessage().getText(0,
		// message.getTextMessage().getLength()));}catch(Exception
		// e){e.printStackTrace();}
		String timeStamp = "(" + (day < 10 ? "0" + day : day) + "/"
				+ (month < 10 ? "0" + month : month) + "/" + year + "  "
				+ (hour < 10 ? "0" + hour : hour) + ":"
				+ (min < 10 ? "0" + min : min) + ":"
				+ (sec < 10 ? "0" + sec : sec)
				+ (am == Calendar.AM ? " AM" : " PM") + ")";
		if (message.getDisplayType() != MessageType.DIS_TEXTBOX_MESSAGE) {
			String msg = "";
			try {
				msg += message.getTextMessage().getTextMessage();
			} catch (Exception e) {
			}
			if (message.getDisplayType() == MessageType.DIS_ALERT_MESSAGE) {
				JOptionPane.showMessageDialog(container, msg, "Alert ("
						+ fromUser + " " + timeStamp + ")",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (message.getDisplayType() == MessageType.DIS_WARNING_MESSAGE) {
				JOptionPane.showMessageDialog(container, msg, "Warning ("
						+ fromUser + " " + timeStamp + ")",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (message.getDisplayType() == MessageType.DIS_INFO_MESSAGE) {
				JOptionPane.showMessageDialog(container, msg, "Info ("
						+ fromUser + " " + timeStamp + ")",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		} else {
			String msg = "";
			if (message.isShowTimeStamp()) {
				msg = "\n" + fromUser + " " + timeStamp + " : ";

			} else {
				msg = "\n" + fromUser + ": ";
			}

			try {
				StyleConstants.setFontSize(displayBoxStyle, 12);
				StyleConstants.setBold(displayBoxStyle, false);
				StyleConstants.setUnderline(displayBoxStyle, false);
				StyleConstants.setItalic(displayBoxStyle, false);
				StyleConstants.setStrikeThrough(displayBoxStyle, false);
				StyleConstants
						.setFontFamily(displayBoxStyle, "Timew New Roman");
				if (!local) {
					StyleConstants.setForeground(displayBoxStyle, Color.blue);
				} else
					StyleConstants.setForeground(displayBoxStyle, Color.black);
				displayBoxDoc.insertString(displayBoxDoc.getLength(), msg,
						displayBoxStyle);
			} catch (Exception e) {
			}
			TextMessage textMessage = message.getTextMessage();
			StyleConstants.setBold(displayBoxStyle, textMessage.isBold());
			StyleConstants.setItalic(displayBoxStyle, textMessage.isItalic());
			StyleConstants.setUnderline(displayBoxStyle,
					textMessage.isUnderline());
			StyleConstants.setStrikeThrough(displayBoxStyle,
					textMessage.isStrikeThrough());
			StyleConstants.setForeground(displayBoxStyle,
					textMessage.getTextColor());
			StyleConstants.setFontFamily(displayBoxStyle,
					textMessage.getFontName());
			StyleConstants.setFontSize(displayBoxStyle,
					textMessage.getFontSize());
			String text = "";
			try {
				text = textMessage.getTextMessage();
				text = text.replaceAll("\n", "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!message.isThreeD() && !message.isThreeDBorder()
					&& message.getSpecialAttribute() == MessageType.NONE) {
				try {
					displayBoxDoc.insertString(displayBoxDoc.getLength(), text,
							displayBoxStyle);
				} catch (Exception e) {
				}
			} else {
				Style temp = displayBoxDoc.addStyle("Component", null);
				MultiPurposeControl control = new MultiPurposeControl(text);
				control.setContainerSize(new Dimension(TP_display.getWidth(),
						TP_display.getHeight()));
				boolean isBoldSelected = textMessage.isBold();
				boolean isItaliSelected = textMessage.isItalic();
				int fontType = Font.PLAIN;
				if (isBoldSelected && isItaliSelected) {
					fontType = Font.ITALIC | Font.BOLD;
				} else if (isBoldSelected)
					fontType = Font.BOLD;
				else if (isItaliSelected)
					fontType = Font.ITALIC;
				if (textMessage.isUnderline()) {
					control.setUnderLine(true);
				}
				if (textMessage.isStrikeThrough()) {
					control.setStrikeThrough(true);
				}

				control.setFont(new Font(textMessage.getFontName(), fontType,
						textMessage.getFontSize()));
				control.setThreeD(message.isThreeD());
				control.setThreeDBox(message.isThreeDBorder());
				if (message.getSpecialAttribute() == MessageType.GRADIENT)
					control.setCurrentValue(MultiPurposeControl.GRADIENT);
				if (message.getSpecialAttribute() == MessageType.WAVE)
					control.setCurrentValue(MultiPurposeControl.WAVE);
				if (message.getSpecialAttribute() == MessageType.DIM)
					control.setCurrentValue(MultiPurposeControl.FADED);
				if (message.getSpecialAttribute() == MessageType.FLOWER)
					control.setCurrentValue(MultiPurposeControl.FLOWER);
				if (message.getSpecialAttribute() == MessageType.COMIC)
					control.setCurrentValue(MultiPurposeControl.COMIC);
				if (message.getSpecialAttribute() == MessageType.DANCING)
					control.setCurrentValue(MultiPurposeControl.DANCING);
				control.setForeColor(textMessage.getTextColor());
				StyleConstants.setComponent(temp, control);
				try {
					displayBoxDoc.insertString(displayBoxDoc.getLength(),
							"Ignore Text", temp);
					// displayBoxDoc.insertString(displayBoxDoc.getLength(),
					// " ", s);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// TP_display.insertComponent(p);
				// }
			}
		}
	}

	/**
	 * returns archived messages
	 * 
	 * @param fileName
	 *            where archive data is stored
	 * @return message list
	 */
	public ArrayList<Message> getArchivedMessages(String fileName) {
		String fileNameWithDir = fileName;
		ArrayList<Message> oldMessages = null;

		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(fileNameWithDir));
			oldMessages = (ArrayList<Message>) input.readObject();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				input.close();
			} catch (Exception e1) {

			}
		}
		return oldMessages;
	}

	/**
	 * writes archive data
	 * 
	 * @param fileName
	 *            file name for data to be written
	 * @param messages
	 *            messages
	 */
	public void writeArcieveData(String fileName, ArrayList<Message> messages) {
		try {
			if (messages == null || messages.size() < 1)
				return;
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(fileName));
			out.writeObject(messages);
			out.close();
		} catch (Exception e) {

		}
	}

	public String appendLeadingZeros(int no) {
		if (no < 10) {
			return "0" + no;
		} else
			return "" + no;
	}
}
