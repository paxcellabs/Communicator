package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Vector;

import beans.Message;
import beans.UserData;
import beans.UserSettings;
import client.MessageReceiver;

/**
 * Its a remote interface which provides methods which are accessible from the
 * remote server
 * 
 * @author Kuldeep Saxena
 * 
 */
public interface ChatServer extends Remote {
	/**
	 * Sends message to the server
	 * 
	 * @param message
	 *            message to be send
	 * @throws RemoteException
	 *             in case of any error
	 */
	public void sendMessage(Message message) throws RemoteException;

	/**
	 * signs in user at server
	 * 
	 * @param userID
	 *            user to be signed in
	 * @param password
	 *            password
	 * @param client
	 *            callback remote object
	 * @return UserData user's configuration stored at server
	 * @throws RemoteException
	 *             in case of some error
	 */
	public UserData signInUser(String userID, String password,
			MessageReceiver client) throws RemoteException;

	/**
	 * returns user data to the client
	 * 
	 * @param userID
	 *            for which data need to be fetched
	 * @return UserData which is his profile data stored at server
	 * @throws RemoteException
	 *             in case of some error
	 */
	public UserData getUserData(String userID) throws RemoteException;

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
			throws RemoteException;

	/**
	 * returns all the users stored at server
	 * 
	 * @return all user list
	 * @throws RemoteException
	 *             in case of error
	 */
	public HashMap<String, UserSettings> getAllUsers() throws RemoteException;

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
			boolean isNewGroup) throws RemoteException;

	/**
	 * signs out user from the system
	 * 
	 * @param userID
	 *            user sending sign out request
	 * @throws RemoteException
	 *             in case of some error
	 */
	public void signOutUser(String userID) throws RemoteException;

	/**
	 * deletes offline messages
	 * 
	 * @param user
	 *            user sending request
	 * @return status of operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int deleteOffLineMessages(String user) throws RemoteException;

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
			throws RemoteException;

	/**
	 * Sends file send request to user
	 * 
	 * @param toUser
	 *            user to whom request is sent
	 * @param fromUser
	 *            from user request is coming
	 * @param fileName
	 *            file to be send
	 * @return status of operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int sendFileAcceptanceToUser(String toUser, String fromUser,
			String fileName) throws RemoteException;

	/**
	 * Updates user message
	 * 
	 * @param user
	 *            message coming from user
	 * @param message
	 *            message to be updated
	 * @param online
	 *            whether message is online or offline
	 * @return status of operation
	 * @throws RemoteException
	 *             in case of error
	 */
	public int sendUserStatusMessage(String user, String message, boolean online)
			throws RemoteException;
}
