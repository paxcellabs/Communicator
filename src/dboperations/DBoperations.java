package dboperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.Logger;
import beans.Message;
import beans.UserData;
import beans.UserGroupSettings;
import beans.UserSettings;
import client.MessageReceiver;
import flags.OperationStatus;
import flags.UserStatus;

/**
 * This class is used perform database related operations. currently it is using
 * xml file format but this layer can be changed without changing other part of
 * the application. it also contains several list to maintain highly accessible
 * data in ram.
 * 
 * @author Kuldeep Saxena
 * 
 */

public class DBoperations {

	/**
	 * single object of the class for the jvm
	 */
	private final static DBoperations dbOperations = new DBoperations();

	/**
	 * currently signed in users
	 */
	private Hashtable<String, MessageReceiver> HT_activeUsers = new Hashtable<String, MessageReceiver>();

	/**
	 * all the users accessible data
	 */
	private HashMap<String, UserSettings> allUsers;

	/**
	 * signed in user list which is used by the user status notifier to update
	 * other users
	 */
	private ArrayList<String> signInUsers = new ArrayList<String>(); // new
																		// users
																		// list
	/**
	 * newly registered user list which is used by the user status notifier to
	 * update other users
	 */
	private ArrayList<String> newUsers = new ArrayList<String>(); // new users
																	// list
	/**
	 * signed out user list which is used by the user status notifier to update
	 * other users
	 */
	private ArrayList<String> signOutUsers = new ArrayList<String>(); // sign
																		// out
																		// user
																		// list
	/**
	 * new messages holder
	 */
	private Vector<Message> messages = new Vector<Message>();
	/**
	 * logger object
	 */
	private Logger logger;

	/**
	 * online status list
	 */
	private HashMap<String, String> onlineStatusList = new HashMap<String, String>();

	/**
	 * offline Status List
	 */
	private HashMap<String, String> offlineStatusList = new HashMap<String, String>();

	/**
	 * Default constructor initializes logger
	 */
	private DBoperations() {
		logger = Logger.getDefaultInstance();
	}

	/**
	 * default access method for the class object
	 * 
	 * @return current class object
	 */
	public static DBoperations getDefaultInstance() {
		return dbOperations;
	}

	/**
	 * stores user setting to the DB
	 * 
	 * @param userData
	 *            user data object
	 * @return status of the operation
	 */
	public int storeUserSettings(UserData userData) {
		try {
			// writes data to the system
			// user object is stored in an xml file with the convention
			// userid.xml
			FileOutputStream out = new FileOutputStream("userDB/"
					+ userData.getUserID() + ".xml");
			String password = userData.getUserPassword();
			String DOB = userData.getUserDOB();
			String address = userData.getUserAddress();
			String userName = userData.getUserName();
			String mobileNo = userData.getUserMobileNo();
			String onlineStatusMessage = userData.getUserOnlineStatusMsg();
			String offlineStatusMessage = userData.getUserOfflineStatusMsg();
			// System.out.println(onlineStatusMessage);
			ArrayList<UserGroupSettings> AL_groups = userData.getUserGroups();
			out.write(("<?xml version=\"1.0\"?>\n").getBytes());
			out.write(("<USER>\n").getBytes());
			out.write(("<USERID>" + userData.getUserID() + "</USERID>\n")
					.getBytes());
			out.write(("<PASSWORD>" + password + "</PASSWORD>\n").getBytes());
			out.write(("<NAME>" + userName + "</NAME>\n").getBytes());
			out.write(("<DOB>" + DOB + "</DOB>\n").getBytes());
			out.write(("<ADDRESS>" + address + "</ADDRESS>\n").getBytes());
			out.write(("<MOBNO>" + mobileNo + "</MOBNO>\n").getBytes());
			out.write(("<ONLINEMSG>" + onlineStatusMessage + "</ONLINEMSG>\n")
					.getBytes());
			out.write(("<OFFLINEMSG>" + offlineStatusMessage + "</OFFLINEMSG>\n")
					.getBytes());
			out.write("<GROUPS>\n".getBytes());
			if (AL_groups != null) {
				for (int i = 0; i < AL_groups.size(); i++) {
					String groupName = AL_groups.get(i).getGroupName();
					out.write(("<GROUP name=\"" + groupName + "\">\n")
							.getBytes());
					UserSettings id[] = AL_groups.get(i).getUserIDs();
					String ids = "";
					for (int j = 0; j < id.length; j++) {
						ids += id[j].getUserID() + ",";
					}
					out.write(("<USERS>" + ids + "</USERS>\n").getBytes());
					out.write(("</GROUP>\n").getBytes());
				}
			}
			out.write(("</GROUPS>\n").getBytes());
			out.write(("</USER>\n").getBytes());
			out.close();
			logger.writeLog("User data for " + userData.getUserID()
					+ " save to DB successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.writeLog("Failed to save user data for "
					+ userData.getUserID() + " to the DB\n" + e.getMessage());
			return OperationStatus.OPERATION_FAILURE;
		}

		UserSettings us = new UserSettings(userData.getUserID(),
				userData.getUserName(), false, UserStatus.USER_ONLINE);
		us.setUserOnlineStatusMsg(userData.getUserOnlineStatusMsg());
		us.setUserOfflineStatusMsg(userData.getUserOfflineStatusMsg());
		allUsers.put(userData.getUserID(), us);
		us.setValue(getNameForID(us.getUserID()));

		return OperationStatus.OPERATION_SUCESSFUL;
	}

	/**
	 * updates already existed user data
	 * 
	 * @param userData
	 *            new user data
	 */
	public void updateUserSettings(UserData userData) {
		storeUserSettings(userData);

	}

	/**
	 * This function loads user setting for the given user
	 * 
	 * @param userID
	 *            user id for which setting needs to be loaded
	 * @return UserData object containing user's all the profile data
	 */
	public UserData loadUserSettings(String userID) {

		UserData ud = new UserData();

		// list containing user's group user with their status
		ArrayList<UserGroupSettings> al = new ArrayList<UserGroupSettings>();
		logger.writeLog("Fetching data from the DB for user " + userID);
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = db.parse(new File("userDB/" + userID + ".xml"));
			NodeList nl = doc.getElementsByTagName("USER");
			NodeList childs = nl.item(0).getChildNodes();

			for (int i = 0; i < childs.getLength(); i++) {
				if (childs.item(i).getNodeName().equalsIgnoreCase("USERID")) {
					ud.setUserID(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("NAME")) {
					ud.setUserName(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("PASSWORD")) {
					ud.setUserPassword(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("DOB")) {
					ud.setUserDOB(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("ADDRESS")) {
					ud.setUserAddress(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("MOBNO")) {
					ud.setUserMobileNo(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("ONLINEMSG")) {
					ud.setUserOnlineStatusMsg(childs.item(i).getTextContent());
				}
				if (childs.item(i).getNodeName().equalsIgnoreCase("OFFLINEMSG")) {
					ud.setUserOfflineStatusMsg(childs.item(i).getTextContent());
				}

				if (childs.item(i).getNodeName().equalsIgnoreCase("GROUPS")) {
					Element group = (Element) childs.item(i);
					NodeList groups = group.getElementsByTagName("GROUP");
					for (int k = 0; k < groups.getLength(); k++) {
						UserGroupSettings ugs = new UserGroupSettings();
						Node node = groups.item(k).getAttributes()
								.getNamedItem("name");
						ugs.setGroupName(node.getTextContent());
						Element e = (Element) groups.item(k);
						NodeList users = e.getElementsByTagName("USERS");
						UserSettings uid[] = null;
						for (int j = 0; j < users.getLength(); j++) {
							StringTokenizer st = new StringTokenizer(users
									.item(j).getTextContent(), ",");
							uid = new UserSettings[st.countTokens()];
							int l = 0;
							while (st.hasMoreTokens()) {
								String id = st.nextToken();
								uid[l] = new UserSettings(
										id,
										getNameForID(id),
										false,
										isUserOnline(id) == true ? UserStatus.USER_ONLINE
												: UserStatus.USER_OFFLINE);
								if (getAllUsers().get(id) != null) {
									System.out.println(id
											+ " "
											+ getAllUsers().get(id)
													.getUserOnlineStatusMsg());
									uid[l].setUserOnlineStatusMsg(getAllUsers()
											.get(id).getUserOnlineStatusMsg());
									uid[l].setUserOfflineStatusMsg(getAllUsers()
											.get(id).getUserOfflineStatusMsg());
								}
								l++;
							}
						}
						ugs.setUserIDs(uid);
						al.add(ugs);
					}
				}
			}
			ud.setUserGroups(al);
			ud.setStatus(OperationStatus.OPERATION_SUCESSFUL);
			logger.writeLog("User data loaded for user " + userID);
			return ud;
		} catch (Exception e) {
			ud.setStatus(OperationStatus.OPERATION_FAILURE);
			logger.writeLog("Failed to load user data " + e.getMessage());
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * check whether user is already registered, check whether the file with the
	 * name already exists or no
	 * 
	 * @param userID
	 *            user id
	 * @return whether user already exists or not
	 */
	public boolean isUserAlreadyExists(String userID) {
		if (new File("userDB/" + userID + ".xml").exists())
			return true;
		return false;
	}

	/**
	 * Loads all the registered user list who are registered to the system
	 * 
	 * @return user list
	 */
	public HashMap<String, UserSettings> loadAllUserList() {
		logger.writeLog("loading data for all users");
		allUsers = new HashMap<String, UserSettings>();
		File F_dir = new File("userDB");
		// looks in the userDB directory and searches for all the files with
		// .xml extension
		if (!F_dir.exists())
			F_dir.mkdir();
		if (F_dir.isDirectory()) {
			File files[] = F_dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (!files[i].getName().endsWith(".xml"))
					continue;
				UserData ud = loadUserSettings(files[i].getName().substring(0,
						files[i].getName().indexOf(".xml")));
				// in case xml file format mismatched
				if (ud == null) {
					continue;
				}
				// creates user and adds there needed value to the all user list
				UserSettings us = new UserSettings(ud.getUserID(),
						ud.getUserName(), false, UserStatus.UNKNOWN);
				us.setUserOnlineStatusMsg(ud.getUserOnlineStatusMsg());
				us.setUserOfflineStatusMsg(ud.getUserOfflineStatusMsg());
				allUsers.put(ud.getUserID(), us);
			}
		}

		// sets the user name for the id
		Set<String> keys = allUsers.keySet();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			String user = i.next();
			UserSettings us = allUsers.get(user);
			us.setValue(getNameForID(us.getUserID()));
			allUsers.put(user, us);
		}

		logger.writeLog("done reading data");
		return allUsers;
	}

	/**
	 * return name for the given user id
	 * 
	 * @param id
	 *            given user id
	 * @return user name
	 */
	private String getNameForID(String id) {
		if (allUsers != null) {
			UserSettings us = allUsers.get(id);
			if (us != null)
				return us.getValue();
		}
		return id;
	}

	/**
	 * this function return whether given user is online or not
	 * 
	 * @param userID
	 *            user to be check
	 * @return status
	 */
	public boolean isUserOnline(String userID) {
		return getActiveUsers().containsKey(userID);
	}

	/**
	 * returns the list of all the active users i.e. sign in users
	 * 
	 * @return active users
	 */
	public Hashtable<String, MessageReceiver> getActiveUsers() {
		return HT_activeUsers;
	}

	/**
	 * this function adds given user to the given group first it reads data from
	 * db then it writes back the updated data
	 * 
	 * @param fromUser
	 *            the user id from where request is coming
	 * @param userID
	 *            user to be added to the group
	 * @param groupName
	 *            group in which user is going to be added
	 * @param isNewGroup
	 *            to distinguish between new and old group
	 * @return status of the operation
	 */
	public int addUserToGroup(String fromUser, String userID, String groupName,
			boolean isNewGroup) {
		logger.writeLog("Adding " + userID + " to group " + groupName);
		UserData userData = loadUserSettings(fromUser);
		ArrayList<UserGroupSettings> userGroups = userData.getUserGroups();
		if (isNewGroup) {
			UserGroupSettings ugs = new UserGroupSettings();
			ugs.setGroupName(groupName);
			UserSettings us[] = new UserSettings[1];
			us[0] = new UserSettings(userID, userID, false, UserStatus.UNKNOWN);
			ugs.setUserIDs(us);
			userGroups.add(ugs);
		} else {
			UserGroupSettings ugs = null;
			for (int i = 0; i < userGroups.size(); i++) {
				if (userGroups.get(i).getGroupName()
						.equalsIgnoreCase(groupName)) {
					ugs = userGroups.get(i);
					break;
				}
			}
			if (ugs == null) {
				logger.writeLog("Failed to add user to group UserGroupSetting was found null");
				return OperationStatus.OPERATION_FAILURE;
			}
			UserSettings us[] = ugs.getUserIDs();
			UserSettings usNew[] = new UserSettings[us.length + 1];
			int i = 0;
			for (i = 0; i < us.length; i++) {
				if (us[i].getUserID().equalsIgnoreCase(userID))
					return OperationStatus.OPERATION_SUCESSFUL;
				usNew[i] = us[i];
			}
			usNew[i] = new UserSettings(userID, userID, false,
					UserStatus.UNKNOWN);
			ugs.setUserIDs(usNew);
		}
		userData.setUserGroups(userGroups);
		return storeUserSettings(userData);
	}

	/**
	 * return list of new sign in users
	 * 
	 * @return queue iof the user
	 */
	public synchronized ArrayList<String> getSignInUsers() {
		return signInUsers;
	}

	/**
	 * returns new users
	 * 
	 * @return queue of new user list
	 */
	public synchronized ArrayList<String> getNewUsers() {
		return newUsers;
	}

	/**
	 * return list of user in the sign out queue
	 * 
	 * @return queue of signed out users
	 */
	public synchronized ArrayList<String> getSignOutUsers() {
		return signOutUsers;
	}

	/**
	 * returns all the messages in the message queue
	 * 
	 * @return messages
	 */
	public Vector<Message> getMessages() {
		return messages;
	}

	/**
	 * This function deletes offline messages for the given user
	 * 
	 * @param user
	 *            for which messages has to be deleted
	 * @return status of the operation
	 */
	public int deleteOfflineMessages(String user) {
		try {
			File f = new File("offline_messages/" + user + "_off.msg");
			if (f.exists()) {
				f.delete();
				logger.writeLog("Offline message deleted");
			}
			return OperationStatus.OPERATION_SUCESSFUL;
		} catch (Exception e) {
			logger.writeLog("Error deleting offline messages: "
					+ e.getMessage());
			return OperationStatus.OPERATION_FAILURE;
		}
	}

	/**
	 * reads offline messages for the given user
	 * 
	 * @param user
	 *            for which messages to be loaded
	 * @return messages
	 */
	public Vector<Message> readOffLineMessages(String user) {
		ObjectInputStream in = null;
		try {
			File f = new File("offline_messages/" + user + "_off.msg");
			if (f.exists()) {
				in = new ObjectInputStream(new FileInputStream(f));
				Vector msgs = (Vector<Message>) in.readObject();
				in.close();
				if (msgs == null || msgs.size() < 0) {
					logger.writeLog("Offline messages not found for user "
							+ user);
					return null;
				} else {
					logger.writeLog("Offline messages loaded");
					return msgs;
				}
			} else {
				logger.writeLog("Offline messages not found for user " + user);
				return null;
			}
		} catch (Exception e) {
			logger.writeLog("Error reading offline messages: " + e.getMessage());
			try {
				in.close();
			} catch (Exception e1) {

			}
			return null;
		}

	}

	/**
	 * writes offline messages to the system
	 * 
	 * @param user
	 *            for which message to be written
	 * @param message
	 *            messages to be written
	 * @return status of the operation
	 */
	public int writeOffLineMessage(String user, Message message) {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			Vector<Message> msgs = null;
			File f = new File("offline_messages/" + user + "_off.msg");
			// first reads offline messages if any then adds new message to it
			// and writes it back
			if (f.exists()) {
				in = new ObjectInputStream(new FileInputStream(f));
				msgs = (Vector<Message>) in.readObject();
				in.close();
			}
			if (msgs == null)
				msgs = new Vector<Message>();
			out = new ObjectOutputStream(new FileOutputStream(f));
			msgs.add(message);
			out.writeObject(msgs);
			out.close();
			logger.writeLog("done writing message to system");
		} catch (Exception e) {
			logger.writeLog("Error writing offline message" + e.getMessage());

			try {
				if (in != null) {
					in.close();
				}
				out.close();
			} catch (Exception e1) {
			}
			return OperationStatus.OPERATION_FAILURE;
		}
		return OperationStatus.OPERATION_SUCESSFUL;
	}

	/**
	 * @return Returns the allUsers.
	 */
	public HashMap<String, UserSettings> getAllUsers() {
		return allUsers;
	}

	/**
	 * @param allUsers
	 *            The allUsers to set.
	 */
	public void setAllUsers(HashMap<String, UserSettings> allUsers) {
		this.allUsers = allUsers;
	}

	public HashMap<String, String> getOnlineStatusList() {
		return onlineStatusList;
	}

	public HashMap<String, String> getOfflineStatusList() {
		return offlineStatusList;
	}
}
