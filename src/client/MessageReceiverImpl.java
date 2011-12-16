package client;

import flags.OperationStatus;
import flags.UserStatus;
import gui.ChatWindow;
import gui.Login;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.JOptionPane;

import utils.PopupMessageHandler;
import utils.Utilities;
import beans.Message;
import beans.UserSettings;

/**
 * This class is an implementation of client side message receiver used to
 * handle messages from the remote server to the client
 * 
 * @author Kuldeep Saxena
 * 
 */
public class MessageReceiverImpl implements MessageReceiver, Serializable {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -102293722463436238L;

	/**
	 * Default instance for the class
	 */
	private final static MessageReceiverImpl mrimpl = new MessageReceiverImpl();

	/**
	 * exports object only when its not exported
	 */
	private static boolean exported = false;

	/**
	 * default accessory method
	 * 
	 * @return current class object
	 */
	public static MessageReceiverImpl getDefaultInstance() {
		try {
			if (!exported) {
				// export object such that it can be remote accessible
				UnicastRemoteObject.exportObject((MessageReceiver) mrimpl);
				exported = true;
			}
		} catch (Exception e) {
		}
		return mrimpl;
	}

	/**
	 * Default constructor
	 * 
	 */
	private MessageReceiverImpl() {
	}

	/**
	 * callback function called whenever a new message is received
	 */
	public boolean receiveMessage(Message message) throws RemoteException {
		// checks the already opened window
		ChatWindow cw = Utilities.getDefaultInstance().getOpenedWindow(
				message.getFromUser());
		if (cw == null) {
			cw = new ChatWindow(message.getFromUser(), null);
			Utilities.getDefaultInstance().addOpenedWindow(
					message.getFromUser(), cw);
		}
		// shows message to the user
		cw.showMessage(message, false);
		cw.setVisible(true);
		return true;
	}

	/**
	 * Remote function called by server to update user about other user's status
	 * 
	 * @param userID
	 *            User id of person for whom status is received
	 * @param status
	 *            new status of the user
	 */
	public void updateUserStatus(String userID, int status)
			throws RemoteException {
		// PopupMessageHandler deals with the taskbar related messages
		String groups[] = Utilities.getDefaultInstance().getUserGroups();
		if (status == UserStatus.USER_ONLINE)
			PopupMessageHandler.getDefaultInstance().addNewMessage(
					userID + " is now Online");
		else if (status == UserStatus.USER_OFFLINE)
			PopupMessageHandler.getDefaultInstance().addNewMessage(
					userID + " is now Offline");

		for (int i = 0; i < groups.length; i++) {
			UserSettings us[] = Utilities.getDefaultInstance().getUsersInMap(
					Utilities.getDefaultInstance().getUsersInGroup(groups[i]));
			for (int j = 0; j < us.length; j++) {
				if (us[j].getUserID().equalsIgnoreCase(userID)) {
					us[j].setUserStatus(status);
					break;
				}
			}
		}
		Login.getDefaultInstance().repaintGroupData();
	}

	/**
	 * Called when a new user has signed up telling user that new user is added
	 * 
	 * @param newUser
	 *            New User data
	 */
	public void newUserAdded(UserSettings newUser) throws RemoteException {
		HashMap<String, UserSettings> users = Utilities.getDefaultInstance()
				.getUsersInGroup("all");
		if (users != null) {
			users.put(newUser.getUserID(), newUser);
			Utilities.getDefaultInstance().updateUserGroupData("all", users);
			Login.getDefaultInstance().updateGroupData();
		}
		PopupMessageHandler.getDefaultInstance().addNewMessage(
				newUser.getUserID() + " is added to the server");
	}

	/**
	 * shows user file name, and from user file is coming
	 * 
	 * @param fileName
	 *            file name
	 * @param fromUser
	 *            user id
	 * @return ip address of the client to whom request is coming or null when
	 *         user does not accespts the file
	 */
	public String acceptFile(String fileName, String fromUser)
			throws RemoteException {
		try {
			int returnValue = JOptionPane.showConfirmDialog(null, "User "
					+ fromUser + " is sending you file " + fileName
					+ " accept?", "File", JOptionPane.YES_NO_OPTION);
			if (returnValue == JOptionPane.YES_OPTION)
				return InetAddress.getLocalHost().getHostAddress();
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	/**
	 * keeps the client running
	 * 
	 */
	public void run() {
		while (true) {
			try {
				wait();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * updates user status
	 * 
	 * @param ofUser
	 *            of which user status been updated
	 * @param message
	 *            new message
	 * @param online
	 *            is online status message changed or offline
	 * @return status of operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int updateNewStatusMessage(String ofUser, String message,
			boolean online) throws RemoteException {
		String groups[] = Utilities.getDefaultInstance().getUserGroups();
		for (int i = 0; i < groups.length; i++) {
			UserSettings us[] = Utilities.getDefaultInstance().getUsersInMap(
					Utilities.getDefaultInstance().getUsersInGroup(groups[i]));
			for (int j = 0; j < us.length; j++) {
				if (us[j].getUserID().equalsIgnoreCase(ofUser)) {
					if (online)
						us[j].setUserOnlineStatusMsg(message);
					else
						us[j].setUserOfflineStatusMsg(message);
					break;
				}
			}
		}
		Login.getDefaultInstance().repaintGroupData();
		// Login.getDefaultInstance().updateNewStatusMsg (ofUser, message,
		// online);
		return OperationStatus.OPERATION_SUCESSFUL;
	}
}
