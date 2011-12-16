package communicator;

import java.rmi.Naming;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;

import server.ChatServer;
import utils.Utilities;
import beans.Message;
import beans.UserData;
import beans.UserSettings;
import client.MessageReceiver;
import flags.OperationStatus;

/**
 * This class handles all the communication from the client to the server It
 * maintains the connection to the server and calls remote method on behalf of
 * client application
 * 
 * @author Kuldeep Saxena
 */

public class ClientSideCommunicator {

	/**
	 * Default accessory object
	 */
	private final static ClientSideCommunicator clcommunicator = new ClientSideCommunicator();

	/**
	 * IP address
	 */
	static String ip;
	/**
	 * Port
	 */
	static String port;
	/**
	 * Remote Object name
	 */
	static String OBJECT = "ChatServer";
	/**
	 * Remote server reference
	 */
	static ChatServer cs = null;

	/**
	 * Default Constructor does nothing
	 * 
	 */
	private ClientSideCommunicator() {
	}

	/**
	 * block gets executed when class is loaded loads ip and port info and start
	 * connecting to server
	 */
	static {
		Properties p = Utilities.getDefaultInstance().readServerSettings();
		ip = p.getProperty("IP");
		port = p.getProperty("PORT");
		connectToServer();

	}

	/**
	 * this function looks for connection using the Ip and port
	 * 
	 */
	private static void connectToServer() {
		try {
			cs = (ChatServer) Naming.lookup("rmi://" + ip + ":" + port + "/"
					+ OBJECT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Accessory method to get this class object
	 * 
	 * @return this class object
	 */
	public static ClientSideCommunicator getDefaultInstance() {
		return clcommunicator;
	}

	/**
	 * This function calls register user on remote object
	 * 
	 * @param userData
	 *            userData object to be stored at server
	 */
	public void registerUser(UserData userData) {
		try {
			JOptionPane.showMessageDialog(null, Utilities.getDefaultInstance()
					.mapMessageCode(cs.signUpUser(userData, true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function calls function on remote object
	 * 
	 * @param userID
	 *            user id
	 * @param pwd
	 *            user's password
	 * @param mr
	 *            client remote object
	 * @return user data after validation, or null, or data with status only 1)
	 *         user data with values when user validated 2) user data null when
	 *         user not found 3) user data with status when some error in
	 *         validating like invalid password
	 */
	public UserData validateUser(String userID, String pwd, MessageReceiver mr) {
		UserData d = null;
		try {
			if (cs == null) {
				connectToServer();
			}
			d = cs.signInUser(userID, pwd, mr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return d;
	}

	/**
	 * calls the getUserData on remote object
	 * 
	 * @param userID
	 *            user id for whom data is to be returned
	 * @return UserData user's profile data
	 */
	public UserData getUserData(String userID) {
		try {
			return cs.getUserData(userID);

		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * returns all the available users on the server
	 * 
	 * @return users
	 */
	public HashMap<String, UserSettings> loadAllUsers() {
		try {
			return cs.getAllUsers();
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * calls remote method add user to group
	 * 
	 * @param fromUser
	 *            current user
	 * @param newUserID
	 *            new user to be added to given group
	 * @param groupName
	 *            group name
	 * @param isNewGroup
	 *            is the group is new or existing
	 * @return status of operation
	 */
	public int addUserToGroup(String fromUser, String newUserID,
			String groupName, boolean isNewGroup) {
		try {
			return cs
					.addUserToGroup(fromUser, newUserID, groupName, isNewGroup);
		} catch (Exception e) {
			e.printStackTrace();
			return OperationStatus.OPERATION_FAILURE;
		}
	}

	/**
	 * calls sign out on remote object
	 * 
	 * @param userID
	 *            current user
	 * @return status of the operation
	 */
	public int signOutUser(String userID) {
		try {
			// System.out.println("Calling signout");
			cs.signOutUser(userID);
			return OperationStatus.OPERATION_SUCESSFUL;
		} catch (Exception e) {
			return OperationStatus.OPERATION_FAILURE;
		}
	}

	/**
	 * calls remote delete offline message
	 * 
	 * @param user
	 *            for whom message needs to be deleted
	 * @return status of the operation
	 */
	public int deleteOfflineMessages(String user) {
		try {
			return cs.deleteOffLineMessages(user);
		} catch (Exception e) {
			return OperationStatus.OPERATION_FAILURE;
		}
	}

	/**
	 * calls remote object's sendMessage function
	 * 
	 * @param message
	 *            message to be send
	 * @return status of the operation
	 */
	public int sendMessage(Message message) {
		try {
			cs.sendMessage(message);
			return OperationStatus.OPERATION_SUCESSFUL;
		} catch (Exception e) {
			return OperationStatus.OPERATION_FAILURE;
		}
	}

	/**
	 * calls remote signupuser data with false as second parameter which tells
	 * that its a update to the user data at server
	 * 
	 * @param ud
	 *            userData object
	 * @return status of the operation
	 */
	public int updateUserData(UserData ud) {
		try {
			int status = cs.signUpUser(ud, false);
			JOptionPane.showMessageDialog(null, Utilities.getDefaultInstance()
					.mapMessageCode(status));
			return status;

		} catch (Exception e) {
			return OperationStatus.OPERATION_FAILURE;
		}

	}

	/**
	 * calls remote getOfflineMessage
	 * 
	 * @param user
	 *            for which offline message needs to be retrieved
	 * @return message[] containing offline messages
	 */
	public Message[] getOffLineMessages(String user) {
		try {
			Vector<Message> messages = cs.getOffLineMessages(user);
			if (messages == null)
				return null;
			Message[] msgs = new Message[messages.size()];
			for (int i = 0; i < messages.size(); i++) {
				msgs[i] = messages.get(i);
			}
			return msgs;

		} catch (Exception e) {
			return null;
		}
	}

	public int sendFileAcceptanceToUser(String toUser, String fromUser,
			String fileName) {
		try {
			return cs.sendFileAcceptanceToUser(toUser, fromUser, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return OperationStatus.OPERATION_FAILURE;
		}
	}

	public int setUserStatusMessage(String user, String message, boolean online) {
		try {
			return cs.sendUserStatusMessage(user, message, online);
		} catch (Exception e) {
			e.printStackTrace();
			return OperationStatus.OPERATION_FAILURE;
		}

	}
}
