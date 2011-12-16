package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import utils.Logger;
import beans.Message;
import beans.UserData;
import beans.UserSettings;
import client.MessageReceiver;

import communicator.ServerSideCommunicator;

import dboperations.DBoperations;
import flags.OperationStatus;
import flags.UserStatus;

/**
 * this class is the implemenation class of the ChatServer it provides the real
 * functionality to the Server, all the messages which can be called from the
 * remote client are defined here. it makes use of DBOpeation to communicates
 * with the DB and ServerSideCommunicator to communicates with the client To run
 * the server stub for this class and MessageReceiver needs to be created cmd:
 * rmic -classpath . server.ChatServerImpl and rmic -classpath .
 * client.MessageReceiverImpl this class has the main function to start the
 * server
 * 
 * @author Kuldeep Saxena
 * 
 */
public class ChatServerImpl extends JFrame implements ChatServer,
		ActionListener {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -1868390366165707650L;

	/**
	 * all user list
	 */
	// private HashMap<String,UserSettings> HM_users = new
	// HashMap<String,UserSettings> ();

	/*
	 * GUI related variables
	 */
	private JPanel P_controls;
	private JTextArea TA_message;
	private JButton B_adminMessages;

	/**
	 * logger variable to initialize looger service
	 */
	private Logger logger;

	/**
	 * Server constructor initializes logger, initializes components,
	 * initializes users list, export remote object, register the server object
	 */
	public ChatServerImpl() {
		super("Communicator Server");
		ServerSideCommunicator.getDefaultInstance();
		try {
			logger = Logger.getDefaultInstance();
			logger.writeLog("Starting server");
			initComponents();
			initUserList();
			TA_message.setText("\n----Starting server -----   " + new Date());
			UnicastRemoteObject.exportObject((ChatServer) this);
			LocateRegistry.createRegistry(3090).rebind("ChatServer",
					(ChatServer) this);
			logger.writeLog("\n----Server Started -----   " + new Date());
			TA_message.append("\nStarted");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (Error e) {
			e.printStackTrace();
			TA_message.append(e.getMessage() + "\n");
			logger.writeLog(e.getMessage());
			JOptionPane.showMessageDialog(null,
					"Please create stub classes for using this service");
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			TA_message.append(e.getMessage() + "\n");
			logger.writeLog(e.getMessage());
			TA_message.setText(e.getMessage());
			JOptionPane
					.showMessageDialog(null,
							"Failed to register server with local system, Program will exit");
			System.exit(-1);
		}
	}

	/**
	 * Initializes user list
	 * 
	 */
	private void initUserList() {

		try {
			logger.writeLog("Initializing Users list");
			TA_message.append("Initializing Users list\n");
			// uses DBOperation to load the list
			/* HM_users = */DBoperations.getDefaultInstance().loadAllUserList();
			logger.writeLog("Finish initializing user list");
			TA_message.append("Finish initializing user list\n");
		} catch (Exception e) {
			e.printStackTrace();
			logger.writeLog(e.getMessage());
			TA_message.append(e.getMessage() + "\n");
		}
	}

	/**
	 * initializes components
	 * 
	 */
	private void initComponents() {
		setSize(500, 400);
		TA_message = new JTextArea();
		P_controls = new JPanel();
		B_adminMessages = new JButton("Send Admin Message");
		TA_message.setEditable(false);
		TA_message.setWrapStyleWord(true);
		TA_message.setLineWrap(true);
		TA_message.setBorder(new CompoundBorder(new BevelBorder(
				BevelBorder.LOWERED), new EmptyBorder(5, 5, 5, 5)));
		JScrollPane SP_message = new JScrollPane(TA_message);
		getContentPane().add(SP_message, BorderLayout.CENTER);
		getContentPane().add(P_controls, BorderLayout.SOUTH);
		SP_message.setPreferredSize(new Dimension(480, 290));
		TA_message.setPreferredSize(new Dimension(480, 295));
		P_controls.setPreferredSize(new Dimension(480, 80));
		// P_controls.add(B_adminMessages);
		B_adminMessages.addActionListener(this);
	}

	/**
	 * main function
	 * 
	 * @param args
	 *            command line argument
	 */
	public static void main(String args[]) {
		new ChatServerImpl().setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == B_adminMessages) {
			// will be available in next version provides admin message to user
			// from the server
		}

	}

	/**
	 * Sends message to the server
	 * 
	 * @param message
	 *            message to be send
	 * @throws RemoteException
	 *             in case of any error
	 */
	public void sendMessage(Message message) throws RemoteException {
		logger.writeLog("Received message from: " + message.getFromUser());
		TA_message.append("Received message from: " + message.getFromUser()
				+ "\n");
		String toUsers[] = message.getToUsers();
		String toUser = "";
		for (int i = 0; i < toUsers.length; i++) {
			toUser += "{" + toUsers[i] + "}";
		}

		logger.writeLog("Message for: " + toUser);
		TA_message.append("Message for: " + toUser + "\n");
		// adds message to the queue
		DBoperations.getDefaultInstance().getMessages().add(message);
		logger.writeLog("Calling invoker to dispatch message");
		TA_message.append("Calling invoker to dispatch message\n");
		// updates the notifier
		ServerSideCommunicator.getDefaultInstance().updateNewMessageReceived();
	}

	/**
	 * signs in user at server
	 * 
	 * @param userID
	 *            user to be signed in
	 * @param pwd
	 *            password
	 * @param client
	 *            callback remote object
	 * @return UserData user's configuration stored at server
	 * @throws RemoteException
	 *             in case of some error
	 */
	public UserData signInUser(String userID, String pwd, MessageReceiver client)
			throws RemoteException {
		logger.writeLog("Sign in request from : " + userID);
		TA_message.append("Sign in request from : " + userID + "\n");
		logger.writeLog("Validating : " + userID);
		TA_message.append("Validating : " + userID + "\n");
		// calls local validation function
		return validateUser(userID, pwd, client);
	}

	/**
	 * returns user data to the client
	 * 
	 * @param userID
	 *            for which data need to be fetched
	 * @return UserData which is his profile data stored at server
	 * @throws RemoteException
	 *             in case of some error
	 */
	public UserData getUserData(String userID) throws RemoteException {
		logger.writeLog("Requesting user data from the DB for user " + userID);
		TA_message.append("Requesting user data from the DB for user " + userID
				+ "\n");
		// uses DBOperation to load data for userID
		UserData ud = DBoperations.getDefaultInstance()
				.loadUserSettings(userID);
		return ud;
	}

	/**
	 * registers user at remote server
	 * 
	 * @param userData
	 *            user data object
	 * @param isNew
	 *            whether a new user or editing the old data
	 * @return status of the operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int signUpUser(UserData userData, boolean isNew)
			throws RemoteException {
		if (isNew) {
			logger.writeLog("New user sign up request with user id "
					+ userData.getUserID());
			TA_message.append("New user sign up request with user id "
					+ userData.getUserID() + "\n");
		} else {
			logger.writeLog("Update account info request from user id "
					+ userData.getUserID());
			TA_message.append("Update account info request from user id "
					+ userData.getUserID() + "\n");
		}
		int status;
		if (isNew) {
			if (DBoperations.getDefaultInstance().isUserAlreadyExists(
					userData.getUserID())) {
				logger.writeLog("Sign up request failed, User with "
						+ userData.getUserID() + " already exists ");
				TA_message.append("Sign up request failed, User with "
						+ userData.getUserID() + " already exists \n");
				return OperationStatus.USER_ALREADY_EXISTS;
			}
		}
		logger.writeLog("Writing user data to system");
		TA_message.append("Writing user data to system\n");
		if ((status = DBoperations.getDefaultInstance().storeUserSettings(
				userData)) == OperationStatus.OPERATION_SUCESSFUL) {
			UserSettings us = new UserSettings(userData.getUserID(),
					userData.getUserName(), false, UserStatus.UNKNOWN);
			DBoperations.getDefaultInstance().getNewUsers()
					.add(userData.getUserID());
			ServerSideCommunicator.getDefaultInstance().notifyNewUserStatus();
			/* HM_users */DBoperations.getDefaultInstance().getAllUsers()
					.put(userData.getUserID(), us);
			if (isNew)
				return OperationStatus.USER_REGISTERED_SUCESSFULLY;
			else
				return OperationStatus.USER_UPDATED_SUCESSFULLY;
		} else
			return status;

	}

	/**
	 * returns all the users stored at server
	 * 
	 * @return all user list
	 * @throws RemoteException
	 *             in case of error
	 */
	public HashMap<String, UserSettings> getAllUsers() throws RemoteException {
		logger.writeLog("Returning all user list to the user");
		TA_message.append("Returning all user list to the user\n");
		Set s = /* HM_users */DBoperations.getDefaultInstance().getAllUsers()
				.keySet();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			String id = (String) iter.next();
			if (DBoperations.getDefaultInstance().isUserOnline(id)) {
				UserSettings us = /* HM_users */DBoperations.getDefaultInstance()
						.getAllUsers().get(id);
				us.setUserStatus(UserStatus.USER_ONLINE);
				/* HM_users */DBoperations.getDefaultInstance().getAllUsers()
						.put(us.getUserID(), us);
			}
		}
		return /* HM_users */DBoperations.getDefaultInstance().getAllUsers();
	}

	/**
	 * adds a given user to the given group for the current requesting user
	 * 
	 * @param fromUser
	 *            current user
	 * @param userID
	 *            user to be added
	 * @param groupName
	 *            group name
	 * @param isNewGroup
	 *            whether group is new or not
	 * @return status of the operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int addUserToGroup(String fromUser, String userID, String groupName,
			boolean isNewGroup) throws RemoteException {
		logger.writeLog("Request to add user to the group received from user ["
				+ fromUser + "] User [" + userID + "] group name [" + groupName
				+ "] Is a new group [" + (isNewGroup == true ? "Yes" : "No")
				+ "]");
		TA_message
				.append("Request to add user to the group received from user ["
						+ fromUser + "] User [" + userID + "] group name ["
						+ groupName + "] Is a new group ["
						+ (isNewGroup == true ? "Yes" : "No") + "]\n");
		int status = DBoperations.getDefaultInstance().addUserToGroup(fromUser,
				userID, groupName, isNewGroup);
		if (status == OperationStatus.OPERATION_SUCESSFUL) {
			logger.writeLog("User Added successfully");
			TA_message.append("User Added successfully\n");
		} else {
			logger.writeLog("Failed to add user to group");
			TA_message.append("Failed to add user to group\n");
		}
		return status;
	}

	/**
	 * signs out user from the system and updates other users about user status
	 * 
	 * @param userID
	 *            user sending sign out request
	 * @throws RemoteException
	 *             in case of some error
	 */
	public void signOutUser(String userID) throws RemoteException {
		logger.writeLog("Request to sign out the user from user " + userID);
		TA_message.append("Request to sign out the user from user " + userID
				+ "\n");
		UserSettings us = /* HM_users */DBoperations.getDefaultInstance()
				.getAllUsers().get(userID);
		us.setUserStatus(UserStatus.USER_OFFLINE);
		System.out.println("SPL " + us.getUserOnlineStatusMsg());
		/* HM_users */DBoperations.getDefaultInstance().getAllUsers()
				.put(us.getUserID(), us);
		DBoperations.getDefaultInstance().getActiveUsers().remove(userID);
		// adding user to signout queue
		DBoperations.getDefaultInstance().getSignOutUsers().add(userID);
		logger.writeLog("Updating other users about offline user status ");
		TA_message.append("Updating other users about offline user status\n");
		// updateing notifier
		ServerSideCommunicator.getDefaultInstance().notifySignOutUserStatus();
	}

	/**
	 * deletes offline messages
	 * 
	 * @param user
	 *            user sending request
	 * @return status of operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int deleteOffLineMessages(String user) throws RemoteException {
		logger.writeLog("Deleting offline message of " + user);
		TA_message.append("Deleting offline message of " + user + "\n");
		return DBoperations.getDefaultInstance().deleteOfflineMessages(user);
	}

	/**
	 * returns offline messages
	 * 
	 * @param user
	 *            sending the request
	 * @return offline messages
	 * @throws RemoteException
	 *             in case of error
	 */
	public Vector<Message> getOffLineMessages(String user)
			throws RemoteException {
		logger.writeLog("Loading offline messages for user " + user);
		TA_message.append("Loading offline messages for user " + user + "\n");
		return DBoperations.getDefaultInstance().readOffLineMessages(user);
	}

	/* ------------------- Local Functions ------------------------- */

	/**
	 * function called to validate the user, and if its validated it is appended
	 * to the active user list
	 * 
	 * @param userID
	 *            user's id
	 * @param pwd
	 *            user's password
	 * @param client
	 *            remote callback object
	 * @return user's data
	 */
	private UserData validateUser(String userID, String pwd,
			MessageReceiver client) {
		UserData ud = DBoperations.getDefaultInstance()
				.loadUserSettings(userID);
		if (ud == null) {
			ud = new UserData();
			ud.setStatus(OperationStatus.INVALID_ID);
			logger.writeLog("Invalid user ID");
			TA_message.append("Invalid user ID\n");
		} else if (!ud.getUserPassword().equals(pwd)) {
			ud.setStatus(OperationStatus.INVALID_PASSWORD);
			logger.writeLog("Invalid user password");
			TA_message.append("Invalid user password\n");
		}
		if (ud.getStatus() == OperationStatus.OPERATION_SUCESSFUL) {
			ud.setStatus(OperationStatus.SUCESSFUL_LOGIN);
			logger.writeLog("Login successful");
			TA_message.append("Login successful\n");
			// updating lists
			DBoperations.getDefaultInstance().getActiveUsers()
					.put(userID, client);
			DBoperations.getDefaultInstance().getSignInUsers().add(userID);
			logger.writeLog("Calling status updater to update user status to other user about new user status status");
			TA_message
					.append("Calling status updater to update user status to other user about new user status status\n");
			// updating signin notifier
			ServerSideCommunicator.getDefaultInstance()
					.notifySignInUserStatus();
		}
		return ud;
	}

	/**
	 * sends file info to user
	 * 
	 * @param toUser
	 *            user to file being send
	 * @param fromUser
	 *            user sending file
	 * @param fileName
	 *            file name
	 */
	public int sendFileAcceptanceToUser(String toUser, String fromUser,
			String fileName) throws RemoteException {

		if (DBoperations.getDefaultInstance().getActiveUsers().get(toUser) == null)
			return UserStatus.USER_OFFLINE;
		return OperationStatus.OPERATION_SUCESSFUL;
	}

	public int sendUserStatusMessage(String user, String message, boolean online)
			throws RemoteException {
		UserData ud = DBoperations.getDefaultInstance().loadUserSettings(user);
		if (online) {
			ud.setUserOnlineStatusMsg(message);

			UserSettings us = DBoperations.getDefaultInstance().getAllUsers()
					.get(user);
			us.setUserOnlineStatusMsg(message);
			DBoperations.getDefaultInstance().getAllUsers().put(user, us);
			// DBoperations.getDefaultInstance().getAllUsers().get(user);
		} else {
			ud.setUserOfflineStatusMsg(message);
			UserSettings us = DBoperations.getDefaultInstance().getAllUsers()
					.get(user);
			us.setUserOfflineStatusMsg(message);
			DBoperations.getDefaultInstance().getAllUsers().put(user, us);
		}
		DBoperations.getDefaultInstance().storeUserSettings(ud);
		if (online) {
			DBoperations.getDefaultInstance().getOnlineStatusList()
					.put(user, message);
			ServerSideCommunicator.getDefaultInstance().notifyOnlineStatus();
		} else {
			DBoperations.getDefaultInstance().getOfflineStatusList()
					.put(user, message);
			ServerSideCommunicator.getDefaultInstance().notifyOfflineStatus();
		}
		return OperationStatus.OPERATION_SUCESSFUL;
	}

}
