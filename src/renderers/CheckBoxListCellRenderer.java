package renderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.CheckBoxModel;
import beans.UserSettings;
import flags.UserStatus;

/**
 * This clas provides custom rendering to the user list
 * 
 * @author Kuldeep Saxena
 * 
 */
public class CheckBoxListCellRenderer extends JLabel implements
		ListCellRenderer {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -3764684614792923098L;

	/**
	 * font when user is online
	 */
	private Font online = new Font("times new roman", Font.BOLD, 12);

	/**
	 * font when user is offline
	 */
	private Font offline = new Font("times new roman", Font.PLAIN, 12);

	/**
	 * overridden function to update properties on the basis of user status
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {

		UserSettings cbp = (UserSettings) value;
		setText(cbp.getValue());
		setToolTipText(cbp.getUserID());
		if (cbp.getUserStatus() == UserStatus.USER_ONLINE) {
			setFont(online);
		} else
			setFont(offline);

		if (cbp.getUserID().equalsIgnoreCase(
				((CheckBoxModel) list.getModel()).getDefaultUserID())
				|| cbp.isSelected()) {
			setIcon(new ImageIcon("resources/images/check.gif"));
			return this;
		}
		setIcon(new ImageIcon("/resources/images/uncheckedIcon.gif"));
		return this;
	}

}
