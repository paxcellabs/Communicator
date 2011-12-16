package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.IntegersDocument;
import utils.UserIDDocument;
import utils.Utilities;
import beans.UserData;

import communicator.ClientSideCommunicator;

import controls.CalendarControl;
import controls.RoundPopupMenu;

/**
 * This class provides a GUI for client to resigster himself/herself to the
 * system
 * 
 * @author Kuldeep Saxena
 * 
 * 
 */
public class RegisterUser extends JDialog implements ActionListener {

	/**
	 * popup menu to display calendar control
	 */
	private RoundPopupMenu pop;
	/**
	 * default accessor object
	 */
	private final static RegisterUser ru = new RegisterUser();

	/**
	 * Default mode
	 */
	private String mode = "Register";

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -7866987548659312661L;

	/*
	 * variable declaration section
	 */

	/*
	 * GUI related variables
	 */
	private JLabel L_userID, L_userName, L_userPassword, L_confirmPassword,
			L_userDOB, L_userAddress, L_userMobNo;
	private JTextField TF_userID, TF_userName, TF_userDOB, TF_userAddress,
			TF_userMobNo;
	private JPasswordField PF_userPassword, PF_confirmPassword;
	private JPanel P_center, P_south;
	private JButton B_register, B_close, B_calendarLink;

	/**
	 * Default Constructor initializes components
	 */
	private RegisterUser() {
		initComponents();
	}

	/**
	 * Default accessory method
	 * 
	 * @return current class object
	 */
	public static RegisterUser getDefaultInstance() {
		return ru;
	}

	/**
	 * initializes components
	 * 
	 */
	private void initComponents() {

		pop = new RoundPopupMenu();

		L_userID = new JLabel("User ID");
		L_userName = new JLabel("Name");
		L_userPassword = new JLabel("Password");
		L_confirmPassword = new JLabel("Confirm password");
		L_userDOB = new JLabel("Date of birth");
		L_userAddress = new JLabel("Address");
		L_userMobNo = new JLabel("Mobile No");

		TF_userID = new JTextField(20);
		TF_userName = new JTextField(20);
		PF_userPassword = new JPasswordField(20);
		PF_confirmPassword = new JPasswordField(20);
		TF_userDOB = new JTextField(20);
		TF_userAddress = new JTextField(20);
		TF_userMobNo = new JTextField(20);

		P_center = new JPanel();
		P_south = new JPanel();
		B_close = new JButton("Close");
		B_register = new JButton("Register");
		B_calendarLink = new JButton(new ImageIcon(
				"resources/images/calendar.gif"));

		TF_userID.setDocument(new UserIDDocument());

		JPanel P_userID = new JPanel();
		JPanel P_userName = new JPanel();
		JPanel P_userPassword = new JPanel();
		JPanel P_confirmPassword = new JPanel();
		JPanel P_userDOB = new JPanel();
		JPanel P_userAddress = new JPanel();
		JPanel P_userMobNo = new JPanel();

		P_userID.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_userName.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_userPassword.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_confirmPassword.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_userDOB.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_userAddress.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_userMobNo.setLayout(new FlowLayout(FlowLayout.LEFT));

		P_center.setLayout(new BoxLayout(P_center, BoxLayout.Y_AXIS));

		P_center.add(P_userID);
		P_center.add(P_userName);
		P_center.add(P_userPassword);
		P_center.add(P_confirmPassword);
		P_center.add(P_userDOB);
		P_center.add(P_userAddress);
		P_center.add(P_userMobNo);

		P_userID.add(L_userID);
		P_userID.add(TF_userID);
		P_userName.add(L_userName);
		P_userName.add(TF_userName);
		P_userPassword.add(L_userPassword);
		P_userPassword.add(PF_userPassword);
		P_confirmPassword.add(L_confirmPassword);
		P_confirmPassword.add(PF_confirmPassword);
		P_userAddress.add(L_userAddress);
		P_userAddress.add(TF_userAddress);
		P_userDOB.add(L_userDOB);
		P_userDOB.add(TF_userDOB);
		P_userMobNo.add(L_userMobNo);
		P_userMobNo.add(TF_userMobNo);

		getContentPane().add(P_center, BorderLayout.CENTER);
		getContentPane().add(P_south, BorderLayout.SOUTH);

		L_userID.setPreferredSize(new Dimension(120, 20));
		L_userName.setPreferredSize(new Dimension(120, 20));
		L_userPassword.setPreferredSize(new Dimension(120, 20));
		L_confirmPassword.setPreferredSize(new Dimension(120, 20));
		L_userAddress.setPreferredSize(new Dimension(120, 20));
		L_userDOB.setPreferredSize(new Dimension(120, 20));
		L_userMobNo.setPreferredSize(new Dimension(120, 20));

		TF_userID.setPreferredSize(new Dimension(150, 20));
		TF_userName.setPreferredSize(new Dimension(150, 20));
		PF_userPassword.setPreferredSize(new Dimension(150, 20));
		PF_confirmPassword.setPreferredSize(new Dimension(150, 20));
		TF_userAddress.setPreferredSize(new Dimension(150, 20));
		TF_userDOB.setPreferredSize(new Dimension(150, 20));
		TF_userMobNo.setPreferredSize(new Dimension(150, 20));
		B_calendarLink.setPreferredSize(new Dimension(40, 20));
		B_calendarLink.setMargin(new Insets(0, 0, 0, 0));
		B_calendarLink.setContentAreaFilled(false);

		TF_userDOB.setEnabled(false);

		P_userDOB.add(B_calendarLink);
		P_south.add(B_register);
		P_south.add(B_close);
		setSize(370, 350);
		B_calendarLink.addActionListener(this);
		B_close.addActionListener(this);
		B_register.addActionListener(this);
		setResizable(false);
		getRootPane().setDefaultButton(B_register);

		// updates calndar control's location when window is moved
		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				pop.setLocation(getRelativeLocation());
			}
		});
		// window close listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionPerformed(new ActionEvent(B_close,
						ActionEvent.ACTION_PERFORMED, "Close"));
			}
		});

		TF_userMobNo.setDocument(new IntegersDocument());
	}

	/**
	 * action event handler
	 */
	public void actionPerformed(ActionEvent e) {
		// when calendar link is selected
		if (e.getSource() == B_calendarLink) {
			pop.removeAll();
			pop.setForseVisible(false);
			CalendarControl c;
			pop.add(c = new CalendarControl(TF_userDOB, Calendar.getInstance()
					.get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(
					Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR)));
			c.disposeOnSelect(true);
			c.setPreferredSize(new Dimension(220, 200));
			c.setMinimumSize(new Dimension(220, 200));
			pop.setLocation(getRelativeLocation());
			pop.setPreferredSize(new Dimension(150, 200));
			pop.setVisible(true);
			return;
		}

		// when close option is selected
		if (e.getSource() == B_close) {
			if (pop != null)
				pop.setForseVisible(false);
			dispose();
			return;
		}

		// when register option is selected
		if (e.getSource() == B_register) {
			// validates form
			if (!validateForm()) {
				return;
			}
			// populates UserData object
			if (mode.equalsIgnoreCase("Edit")) {

				// calls communicator to update the user data
				UserData ud = Utilities.getDefaultInstance()
						.getCurrentUserData();
				populateUserData(ud);
				ClientSideCommunicator.getDefaultInstance().updateUserData(ud);
				// ud =
				// ClientSideCommunicator.getDefaultInstance().validateUser(ud.getUserID(),
				// ud.getUserPassword(),
				// (MessageReceiver)MessageReceiverImpl.getDefaultInstance());
				// Utilities.getDefaultInstance().setCurrentUserData(ud);
			} else {
				UserData ud = populateUserData(null);
				// calls the communicator to register new user
				ClientSideCommunicator.getDefaultInstance().registerUser(ud);
			}
			setVisible(false);

		}

	}

	/**
	 * returns the location for the calendar control
	 */
	private Point getRelativeLocation() {
		return new Point(B_calendarLink.getLocationOnScreen().x + 50,
				B_calendarLink.getLocationOnScreen().y + 15);
	}

	/**
	 * validates form's various fields
	 * 
	 * @return validated successfully or not
	 */
	private boolean validateForm() {
		if (TF_userID.getText() == null
				|| TF_userID.getText().trim().length() < 1) {
			JOptionPane.showMessageDialog(this, "Please enter valid user id");
			return false;
		}
		if (TF_userName.getText() == null
				|| TF_userName.getText().trim().length() < 1) {
			JOptionPane.showMessageDialog(this, "Please enter your name");
			return false;
		}

		if (PF_userPassword.getPassword() == null
				|| new String(PF_userPassword.getPassword()).trim().length() < 1) {
			JOptionPane.showMessageDialog(this, "Password is required");
			return false;
		}

		if (PF_confirmPassword.getPassword() == null
				|| !(new String(PF_confirmPassword.getPassword())
						.equals(new String(PF_userPassword.getPassword())))) {
			JOptionPane.showMessageDialog(this,
					"Password and confirm password did not match");
			return false;
		}
		return true;
	}

	/**
	 * populates user data
	 * 
	 * @param ud
	 *            user data object
	 * @return new user data object
	 */
	private UserData populateUserData(UserData ud) {
		if (ud == null) {
			ud = new UserData();
		}
		ud.setUserID(TF_userID.getText());
		ud.setUserName(TF_userName.getText());
		ud.setUserPassword(new String(PF_userPassword.getPassword()));
		ud.setUserAddress(TF_userAddress.getText());
		ud.setUserDOB(TF_userDOB.getText());
		ud.setUserMobileNo(TF_userMobNo.getText());
		return ud;
	}

	/**
	 * retruns the popup menu
	 * 
	 * @return popup
	 */
	public RoundPopupMenu getPopup() {
		return pop;
	}

	/**
	 * returns the user id
	 * 
	 * @return user id
	 */
	public String getUserID() {
		return TF_userID.getText();
	}

	/**
	 * sets the user id
	 * 
	 * @param userID
	 *            user id
	 */
	public void setUserID(String userID) {
		TF_userID.setText(userID);
	}

	/**
	 * returns user name
	 * 
	 * @return user name
	 */
	public String getUserName() {
		return TF_userName.getText();
	}

	/**
	 * sets user name
	 * 
	 * @param userName
	 *            user name
	 */
	public void setUserName(String userName) {
		TF_userName.setText(userName);
	}

	/**
	 * returns user mobile no
	 * 
	 * @return user mob no
	 */
	public String getUserMobileNo() {
		return TF_userMobNo.getText();
	}

	/**
	 * sets user mob no
	 * 
	 * @param mobNo
	 *            mob no
	 */

	public void setUserMobileNo(String mobNo) {
		TF_userMobNo.setText(mobNo);
	}

	/**
	 * returns user DOB
	 * 
	 * @return DOB
	 */
	public String getUserDOB() {
		return TF_userDOB.getText();
	}

	/**
	 * sets user DOB
	 * 
	 * @param dob
	 *            user DOB
	 */
	public void setUserDOB(String dob) {
		TF_userDOB.setText(dob);
	}

	/**
	 * sets user password
	 * 
	 * @param password
	 *            user password
	 */
	public void setUserPassword(String password) {
		PF_userPassword.setText(password);
	}

	/**
	 * returns user password
	 * 
	 * @return user pasword
	 */
	public String getUserPassword() {
		return new String(PF_userPassword.getPassword());
	}

	/**
	 * sets user confirm password
	 * 
	 * @param password
	 *            user confirm pssword
	 */
	public void setUserConfirmPassword(String password) {
		PF_confirmPassword.setText(password);
	}

	/**
	 * returns user confirm password
	 * 
	 * @return user confirm password
	 */
	public String getUserConfirmPassword() {
		return new String(PF_confirmPassword.getPassword());
	}

	/**
	 * sets user address
	 * 
	 * @param address
	 *            user address
	 */
	public void setUserAddress(String address) {
		TF_userAddress.setText(address);
	}

	/**
	 * returns user address
	 * 
	 * @return String user address
	 */
	public String getUserAddress() {
		return TF_userAddress.getText();
	}

	/**
	 * sets current mode
	 * 
	 * @param mode
	 *            current mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
		B_register.setText(mode);

		if (mode.equalsIgnoreCase("Edit")) {
			TF_userID.setEditable(false);
		} else {
			TF_userID.setEditable(true);
		}
	}

	/**
	 * returns mode
	 * 
	 * @return mode
	 */
	public String getMode() {
		return mode;
	}
}
