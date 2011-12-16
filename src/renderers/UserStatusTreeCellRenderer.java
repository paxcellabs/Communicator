package renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import beans.UserSettings;
import flags.UserStatus;

/**
 * This class provides a custom renderer for tree used to show users and groups
 * 
 * @author Kuldeep Saxena
 * 
 */
public class UserStatusTreeCellRenderer extends JLabel implements
		TreeCellRenderer {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -5624427122167585L;

	/**
	 * enabled image icon
	 */
	Image enabled = new ImageIcon("resources/images/cbrs.gif").getImage();
	/**
	 * disable image icon
	 */
	Image disabled = new ImageIcon("resources/images/cb.gif").getImage();// .getScaledInstance(25,30,Image.SCALE_SMOOTH));

	/**
	 * overriden to update properties of component
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		UserSettings us = (UserSettings) node.getUserObject();
		setBackground(new Color(100, 100, 200));
		setText(value.toString());
		if (us.isGroup()) {
			if (expanded)
				setIcon(new ImageIcon("resources/images/collapse.gif"));
			else
				setIcon(new ImageIcon("resources/images/expand.gif"));

			if (us.getUserOnlineStatusMsg() != null) {
				setText(value.toString() + "  " + us.getUserOnlineStatusMsg());
			} else
				setText(value.toString());

		} else if (!us.isGroup() && leaf) {
			if (us.getUserStatus() == UserStatus.USER_ONLINE) {
				setIcon(new ImageIcon(enabled));
				setText("<html><body>"
						+ value.toString()
						+ "&nbsp;&nbsp;"
						+ "<i>"
						+ (us.getUserOnlineStatusMsg() == null
								|| us.getUserOnlineStatusMsg().trim().length() < 1 ? ""
								: "(" + us.getUserOnlineStatusMsg() + ") ")
						+ "</i><sub></body></html>");
			} else {
				setIcon(new ImageIcon(disabled));
				setText("<html><body>"
						+ value.toString()
						+ "&nbsp;&nbsp;"
						+ "<i>"
						+ (us.getUserOfflineStatusMsg() == null
								|| us.getUserOfflineStatusMsg().trim().length() < 1 ? ""
								: "(" + us.getUserOfflineStatusMsg() + ") ")
						+ "</i></sub></body></html>");
			}
			setToolTipText(us.getUserID());
		}
		if (selected) {
			setForeground(Color.white);
			setOpaque(true);
		} else {
			setForeground(Color.black);
			setOpaque(false);
		}
		return this;
	}

}
