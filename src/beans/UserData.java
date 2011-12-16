package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * This class is a normal bean which contains user's profile data
 * 
 * @author Kuldeep Saxena
 */

public class UserData implements Serializable {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -1453967924612158298L;

	/*
	 * variable declaration section
	 */

	/**
	 * user's id
	 */
	private String userID;

	/**
	 * user's name
	 */
	private String userName;

	/**
	 * user's password
	 */
	private String userPassword;

	/**
	 * user's address
	 */
	private String userAddress;

	/**
	 * user's Date of Birth
	 */
	private String userDOB;

	/**
	 * user's mobile no
	 */
	private String userMobileNo;

	/**
	 * user online status message
	 */
	private String userOnlineStatusMsg = "";

	/**
	 * user offline status message
	 */
	private String userOfflineStatusMsg = "";

	/**
	 * user's group
	 */
	private ArrayList<UserGroupSettings> userGroups;

	/**
	 * status of operation
	 */
	private int status;

	/**
	 * @return the userAddress
	 */
	public String getUserAddress() {
		return userAddress;
	}

	/**
	 * @param userAddress
	 *            the userAddress to set
	 */
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	/**
	 * @return the userDOB
	 */
	public String getUserDOB() {
		return userDOB;
	}

	/**
	 * @param userDOB
	 *            the userDOB to set
	 */
	public void setUserDOB(String userDOB) {
		this.userDOB = userDOB;
	}

	/**
	 * @return the userGroups
	 */
	public ArrayList<UserGroupSettings> getUserGroups() {
		return userGroups;
	}

	/**
	 * @param userGroups
	 *            the userGroups to set
	 */
	public void setUserGroups(ArrayList<UserGroupSettings> userGroups) {
		this.userGroups = userGroups;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the userMobileNo
	 */
	public String getUserMobileNo() {
		return userMobileNo;
	}

	/**
	 * @param userMobileNo
	 *            the userMobileNo to set
	 */
	public void setUserMobileNo(String userMobileNo) {
		this.userMobileNo = userMobileNo;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword
	 *            the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String toString() {
		return "User ID: " + userID + "\nUser Name: " + userName
				+ "\nUser Password: " + userPassword + "\nUser Mobile: "
				+ userMobileNo + "\nUser Address: " + userAddress
				+ "\nUser DOB: " + userDOB + "\nUser Groups: " + userGroups;
	}

	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return Returns the userOfflineStatusMsg.
	 */
	public String getUserOfflineStatusMsg() {
		return userOfflineStatusMsg;
	}

	/**
	 * @param userOfflineStatusMsg
	 *            The userOfflineStatusMsg to set.
	 */
	public void setUserOfflineStatusMsg(String userOfflineStatusMsg) {
		this.userOfflineStatusMsg = userOfflineStatusMsg;
	}

	/**
	 * @return Returns the userOnlineStatusMsg.
	 */
	public String getUserOnlineStatusMsg() {
		return userOnlineStatusMsg;
	}

	/**
	 * @param userOnlineStatusMsg
	 *            The userOnlineStatusMsg to set.
	 */
	public void setUserOnlineStatusMsg(String userOnlineStatusMsg) {
		this.userOnlineStatusMsg = userOnlineStatusMsg;
	}

}
