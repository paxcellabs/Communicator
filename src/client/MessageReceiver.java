package client;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import beans.Message;
import beans.UserSettings;

/**
 * 
 * This interface contains callback method which can be called from the server
 * 
 * @author Kuldeep Saxena
 * 
 */
public interface MessageReceiver extends Remote, Serializable {
	/**
	 * Callback method Called by the server to send the message
	 * 
	 * @param message
	 *            meesage to be send
	 * @return whether transmission was successfull or not
	 * @throws RemoteException
	 *             in case of any error
	 */
	public boolean receiveMessage(Message message) throws RemoteException;

	/**
	 * Callback method Called by the server to update other user status
	 * 
	 * @param userID
	 *            id of the user whom status is changed
	 * @param status
	 *            new status for the user
	 * @throws RemoteException
	 *             in case of any error
	 */
	public void updateUserStatus(String userID, int status)
			throws RemoteException;

	/**
	 * Callback method Called by the server
	 * 
	 * @param newUser
	 *            new user added to the system
	 * @throws RemoteException
	 *             in case of any error
	 */
	public void newUserAdded(UserSettings newUser) throws RemoteException;

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
			throws RemoteException;

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
			boolean online) throws RemoteException;
}
