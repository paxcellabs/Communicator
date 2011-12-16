package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import utils.Utilities;

import communicator.ClientSideCommunicator;

/**
 * This class is used to provide window to add user to the group
 * 
 * @author Kuldeep Saxena
 */

public class AddUserWindow extends JDialog implements ActionListener {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 5239593096671949355L;

	/**
	 * default object for the class
	 */
	private static final AddUserWindow uw = new AddUserWindow();

	/*
	 * variable declaration section
	 */

	/*
	 * GUI related variables
	 */
	private JPanel P_north, P_center, P_south, P_groups;
	private JLabel L_userName;
	private JComboBox CMB_groups;
	private JTextField TF_group;
	private JTextField TF_userName;
	private JButton B_add, B_cancel;
	private JRadioButton RB_newGroup, RB_oldGroup;
	private ButtonGroup bg;

	/**
	 * Default Contructor initializes components
	 */
	private AddUserWindow() {
		initComponents();
	}

	/**
	 * initializes components
	 * 
	 */
	private void initComponents() {
		P_center = new JPanel();
		P_north = new JPanel();
		P_south = new JPanel();
		P_groups = new JPanel();
		P_groups.setLayout(null);

		P_groups.setBorder(new TitledBorder("Group"));

		CMB_groups = new JComboBox();

		L_userName = new JLabel("User");

		TF_group = new JTextField(20);
		TF_userName = new JTextField(20);
		B_add = new JButton("Add");
		B_cancel = new JButton("Cancel");

		RB_newGroup = new JRadioButton("New Group");
		RB_oldGroup = new JRadioButton("Old Group");
		bg = new ButtonGroup();

		bg.add(RB_newGroup);
		bg.add(RB_oldGroup);

		P_north.add(L_userName);
		P_north.add(TF_userName);
		P_center.add(P_groups);
		P_groups.add(RB_oldGroup);
		P_groups.add(CMB_groups);
		P_groups.add(RB_newGroup);
		P_groups.add(TF_group);
		P_south.add(B_add);
		P_south.add(B_cancel);
		P_center.add(P_groups);
		P_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		RB_oldGroup.setBounds(40, 10, 100, 22);
		CMB_groups.setBounds(140, 15, 200, 22);
		RB_newGroup.setBounds(40, 50, 100, 22);
		TF_group.setBounds(140, 50, 200, 22);

		P_south.setPreferredSize(new Dimension(400, 40));
		P_north.setPreferredSize(new Dimension(400, 40));
		P_center.setPreferredSize(new Dimension(400, 80));
		P_groups.setPreferredSize(new Dimension(400, 80));
		getContentPane().add(P_north, BorderLayout.NORTH);
		getContentPane().add(P_center, BorderLayout.CENTER);
		getContentPane().add(P_south, BorderLayout.SOUTH);
		setSize(400, 220);
		B_add.addActionListener(this);
		B_cancel.addActionListener(this);
		String groups[] = Utilities.getDefaultInstance().getUserGroups();
		for (int i = 0; i < groups.length; i++) {
			if (groups[i] != null)
				if (!groups[i].equalsIgnoreCase("all"))
					CMB_groups.addItem(groups[i]);
		}
		if (CMB_groups.getItemCount() == 0) {
			RB_oldGroup.setEnabled(false);
			CMB_groups.setEnabled(false);
		}
	}

	/**
	 * Default accessor method for the class object
	 * 
	 * @return current class object
	 */
	public static final AddUserWindow getDefaultInsatnce() {
		return uw;
	}

	/**
	 * used to update various controls before showing it on screen
	 * 
	 * @param id
	 *            selected user
	 */
	public void showWindow(String id) {
		if (CMB_groups.getItemCount() == 0) {
			RB_newGroup.setSelected(true);
		} else {
			RB_oldGroup.setSelected(true);
		}
		TF_userName.setText(id);
		setModal(true);
		setVisible(true);
	}

	/**
	 * Action event handler called when cancel or add button is clicked
	 */
	public void actionPerformed(ActionEvent e) {
		// making window invisible only
		if (e.getSource() == B_cancel) {
			setVisible(false);
			return;
		}

		if (e.getSource() == B_add) {
			String groupName = "";
			boolean isNewGroup = true;

			// Checks whether its a new group or adding to an old group
			if (RB_newGroup.isSelected()) {
				isNewGroup = true;
				groupName = TF_group.getText().trim();
				for (int i = 0; i < CMB_groups.getItemCount(); i++) {
					if (((String) CMB_groups.getItemAt(i))
							.equalsIgnoreCase(groupName)) {
						isNewGroup = false;
						break;
					}
				}
			} else {
				groupName = (String) CMB_groups.getSelectedItem();
				isNewGroup = false;
			}
			// calling Communicator to add the user to the group
			int status = ClientSideCommunicator
					.getDefaultInstance()
					.addUserToGroup(
							Utilities.getDefaultInstance().getCurrentUser(),
							TF_userName.getText().trim(), groupName, isNewGroup);
			// show the status of operation to the user
			JOptionPane.showMessageDialog(null, Utilities.getDefaultInstance()
					.mapMessageCode(status));
		}
	}
}
