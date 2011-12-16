package beans;

import java.io.Serializable;

/**
 * This class contains group related info mapped to group name to user ids
 * 
 * @author Kuldeep Saxena
 */

public class UserGroupSettings implements Serializable {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 2678192171817116403L;

	/**
	 * group name
	 */
	private String groupName;

	/**
	 * user ids in this group an array of UserSetting which contains user info
	 * including user's status
	 */
	private UserSettings userIDs[];

	/**
	 * @return Returns the groupName.
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            The groupName to set.
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return Returns the userIDs.
	 */
	public UserSettings[] getUserIDs() {
		return userIDs;
	}

	/**
	 * @param userIDs
	 *            The userIDs to set.
	 */
	public void setUserIDs(UserSettings[] userIDs) {
		this.userIDs = userIDs;
	}
}