package model;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import beans.UserSettings;

/**
 * provides a custom model for List control such that UserSettings object can be
 * stored in it.
 * 
 * @author Kuldeep Saxena
 * 
 */

public class CheckBoxModel extends AbstractListModel {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 5588893716760684514L;

	/**
	 * model storage
	 */
	private ArrayList<UserSettings> AL_data = new ArrayList<UserSettings>();

	/**
	 * default user id
	 */
	private String defaultUserID;

	/**
	 * @return list size
	 */
	public int getSize() {
		return AL_data.size();
	}

	/**
	 * @param index
	 *            specified index
	 * @return element at specified location from the model
	 */
	public Object getElementAt(int index) {
		return AL_data.get(index);
	}

	/**
	 * adds element to the model
	 * 
	 * @param us
	 *            UserSettings object to add to model
	 */
	public void addElement(UserSettings us) {
		// Creating new object such that both windows has separate properties
		UserSettings us2 = new UserSettings(us.getUserID(), us.getValue(),
				us.isGroup(), us.getUserStatus());
		us2.setSelected(us.isSelected());
		if (us2.isSelected()) {
			defaultUserID = us.getUserID();
		}
		AL_data.add(us2);
	}

	/**
	 * selects/unselects the value at specified index
	 * 
	 * @param index
	 *            index in the array list to be updates
	 * @param value
	 *            select/unselect flag
	 */
	public void setSelected(int index, boolean value) {
		AL_data.get(index).setSelected(value);
	}

	/**
	 * 
	 * @return returns default user ID
	 */
	public String getDefaultUserID() {
		return defaultUserID;
	}

	/**
	 * sets default user id
	 * 
	 * @param defaultUserID
	 *            new default user id
	 */
	public void setDefaultUserID(String defaultUserID) {
		this.defaultUserID = defaultUserID;
	}
}
