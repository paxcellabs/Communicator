package beans;

import java.io.Serializable;

/**
 * This class contains a user settings used while his data is at another user's
 * system
 * 
 * @author Kuldeep Saxena
 * 
 */
public class UserSettings implements Serializable {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -1171953166742783418L;

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
	private String value;

	/**
	 * is a group name or user name
	 */
	private boolean isGroup;

	/**
	 * is user selected
	 */
	private boolean selected;

	/**
	 * user online status message
	 */
	private String userOnlineStatusMsg;

	/**
	 * user offline status message
	 */
	private String userOfflineStatusMsg;

	/**
	 * status of the user
	 */
	private int userStatus;

	/**
	 * Default Constructor
	 * 
	 * @param id
	 *            user's id
	 * @param value
	 *            a displayable value (such as user's name)
	 * @param isGroup
	 *            this object may also be used for displaying user in tree, this
	 *            variable distinguish between whether its a user object or
	 *            group object
	 * @param userStatus
	 *            current status of the user
	 */
	public UserSettings(String id, String value, boolean isGroup, int userStatus) {
		setUserID(id);
		setValue(value);
		setGroup(isGroup);
		setUserStatus(userStatus);
	}

	/**
	 * value used to display in the tree node
	 */
	public String toString() {
		return getValue();
	}

	/**
	 * @return Returns the userStatus.
	 */
	public int getUserStatus() {
		return userStatus;
	}

	/**
	 * @param userStatus
	 *            The userStatus to set.
	 */
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * @return Returns the isGroup.
	 */
	public boolean isGroup() {
		return isGroup;
	}

	/**
	 * @param isGroup
	 *            The isGroup to set.
	 */
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	/**
	 * @return Returns the userID.
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            The userID to set.
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value normally userName to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return Returns the selectecd.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            The selectecd to set.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
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
