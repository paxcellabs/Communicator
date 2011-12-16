package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import renderers.UserStatusTreeCellRenderer;
import systemSettings.Properties;
import utils.UserIDDocument;
import utils.Utilities;
import beans.Message;
import beans.UserData;
import beans.UserGroupSettings;
import beans.UserSettings;
import client.FileServer;
import client.MessageReceiver;
import client.MessageReceiverImpl;

import communicator.ClientSideCommunicator;

import controls.GradientPanel;
import controls.TransparentScrollPane;
import flags.OperationStatus;
import flags.UserStatus;

/**
 * This class is the starting point of the application at the client side. it
 * provides a gui to user from where he/she can log in and use the services.
 * 
 * @author Kuldeep Saxena
 * 
 */

public class Login extends JFrame implements ActionListener,
		TreeSelectionListener, MouseListener, WindowListener, ChangeListener,
		KeyListener {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -2642629517141137821L;

	/*
	 * variable declaration section
	 */

	/*
	 * GUI related variables
	 */
	private JTree TR_userGroups;
	private JPanel P_newUser, P_register, P_enterChat, P_options, P_button;
	private JMenuBar MB_bar;
	private JMenu M_options, M_view, M_help, M_onlineStatusMsg,
			M_offlineStatusMsg, M_theme;
	private JMenuItem MI_signout, MI_editprofile, MI_add, MI_close, MI_about,
			MI_archiveViewer;
	private TitledBorder TTB_newUser, TTB_enterChat, TTB_options;
	private JButton B_register, B_enter, B_exit;
	private JLabel L_chatName, L_password;
	private JTextField TF_userID;
	private JPasswordField PF_password;
	private JCheckBox CB_remember;
	private JPanel P_desktop;
	private float float_windowWidthRatio = 0;
	private float float_windowHeightRatio = 0;
	// private JPanel P_menu;
	private DefaultMutableTreeNode root = null;
	private JRadioButtonMenuItem RBMI_gradient, RBMI_default, RBMI_metal;
	private ButtonGroup theme;
	// private JPanel P_status;
	private FileServer fileServer = null;
	private JTextField TF_onLineStatus, TF_offlineStatus;

	/**
	 * online status message
	 */
	private String onlineMsg = "";

	/**
	 * offline status message
	 */
	private String offlineMsg = "";

	/**
	 * indicates whether used is logged in or not
	 */
	private boolean loggedIn = false;

	/**
	 * currently selected user from the user list
	 */
	private UserSettings currentSelectedUser;

	/**
	 * hash containing all the users
	 */
	HashMap<String, UserSettings> allUsers;

	/**
	 * default instance of the class
	 */
	private static final Login loginWindow = new Login();

	private UserSettings rootUser = null;

	/**
	 * Default Constructor
	 * 
	 */
	private Login() {

		super("Paxcel Communicator");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		setIconImage(new ImageIcon("resources/images/title.jpg").getImage());
		Double dou = new Double(Properties.getScreenHeight() / 1.5);
		setSize(Properties.getScreenWidth() / 3, dou.intValue());
		setLocation(Properties.getScreenWidth() - getWidth(), 0);

		P_desktop = new JPanel();
		// P_menu = new JPanel () ;
		P_newUser = new JPanel();
		P_register = new JPanel();
		P_enterChat = new JPanel();
		P_options = new JPanel();
		P_button = new JPanel();

		// P_status = new JPanel();

		MI_add = new JMenuItem("Add", 'A');
		P_register.setLayout(null);
		P_enterChat.setLayout(null);
		P_options.setLayout(new GridLayout(3, 1, 5, 0));

		TF_onLineStatus = new JTextField(30);
		TF_offlineStatus = new JTextField(30);

		dou = new Double(getWidth() / 1.3);
		int wid = dou.intValue();

		P_desktop.setPreferredSize(getSize());
		P_newUser.setPreferredSize(new Dimension(getWidth() - 15,
				getHeight() / 6));
		P_register.setPreferredSize(new Dimension(getWidth(), P_newUser
				.getPreferredSize().height / 3));
		P_enterChat.setPreferredSize(new Dimension(getWidth() - 15,
				getHeight() / 2));
		P_options.setBounds((getWidth() / 2) - (wid / 2), 90, wid,
				getHeight() / 6);

		MB_bar = new JMenuBar();

		M_options = new JMenu("Options");
		M_view = new JMenu("View");
		M_help = new JMenu("Help");
		M_theme = new JMenu("Theme");

		MI_signout = new JMenuItem("Sign Out", 'O');
		MI_editprofile = new JMenuItem("Edit profile", 'E');
		MI_archiveViewer = new JMenuItem("Archive Viewer", 'V');
		M_onlineStatusMsg = new JMenu("Online Status");
		M_offlineStatusMsg = new JMenu("Offline Status");
		MI_about = new JMenuItem("About", 'A');
		MI_close = new JMenuItem("Close", 'C');
		theme = new ButtonGroup();
		RBMI_default = new JRadioButtonMenuItem("Default", true);
		RBMI_gradient = new JRadioButtonMenuItem("Gradient");
		RBMI_metal = new JRadioButtonMenuItem("Metal");
		TTB_newUser = new TitledBorder(new EtchedBorder(), " New User ");
		TTB_enterChat = new TitledBorder(new EtchedBorder(),
				" Enter Chat Here ");
		TTB_options = new TitledBorder(new EtchedBorder(), " Options ");
		theme.add(RBMI_default);
		theme.add(RBMI_gradient);
		theme.add(RBMI_metal);
		B_register = new JButton("Register Now");
		B_enter = new JButton("Enter");
		B_exit = new JButton("Exit");

		L_chatName = new JLabel("User ID");
		L_password = new JLabel("Password");

		TF_userID = new JTextField();
		PF_password = new JPasswordField();

		CB_remember = new JCheckBox("Remember my ID/Password");
		CB_remember.setOpaque(false);
		P_options.setOpaque(false);
		P_button.setOpaque(false);
		M_options.setOpaque(false);
		M_view.setOpaque(false);
		M_view.add(M_theme);
		M_theme.add(RBMI_default);
		M_theme.add(RBMI_gradient);
		M_theme.add(RBMI_metal);

		M_help.setOpaque(false);

		JPanel online = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel offline = new JPanel(new FlowLayout(FlowLayout.LEFT));
		online.setBackground(Color.white);
		offline.setBackground(Color.white);
		M_onlineStatusMsg.add(online);
		online.add(TF_onLineStatus);
		M_offlineStatusMsg.add(offline);
		offline.add(TF_offlineStatus);

		B_enter.setMnemonic('N');
		B_exit.setMnemonic('X');
		B_register.setMnemonic('O');
		M_options.setMnemonic('O');
		M_options.setMnemonic('V');

		CB_remember.setMnemonic('M');
		L_chatName.setDisplayedMnemonic('C');
		L_password.setDisplayedMnemonic('D');

		L_chatName.setLabelFor(TF_userID);
		L_password.setLabelFor(PF_password);

		MB_bar.add(M_options);
		MB_bar.add(M_view);
		MB_bar.add(M_help);

		M_options.add(MI_signout);
		M_options.add(MI_editprofile);
		M_options.add(MI_add);
		M_options.add(MI_close);
		M_view.add(M_onlineStatusMsg);
		M_view.add(M_offlineStatusMsg);
		M_view.add(MI_archiveViewer);
		M_help.add(MI_about);

		setJMenuBar(MB_bar);

		TF_userID.setDocument(new UserIDDocument());
		P_newUser.setBorder(TTB_newUser);
		P_enterChat.setBorder(TTB_enterChat);
		P_options.setBorder(TTB_options);

		P_newUser.add(B_register);

		TF_onLineStatus.setBorder(new TitledBorder("My Online Status Message"));
		TF_offlineStatus
				.setBorder(new TitledBorder("My Offline Status Message"));

		L_chatName.setBounds(10, 20, getWidth() / 2 - 30, 23);
		TF_userID.setBounds(L_chatName.getX() + L_chatName.getWidth() + 2, 20,
				getWidth() / 2 - 10, 23);
		L_password.setBounds(10, 55, getWidth() / 2 - 30, 23);
		PF_password.setBounds(L_password.getX() + L_password.getWidth() + 2,
				55, getWidth() / 2 - 10, 23);

		P_enterChat.add(L_chatName);
		P_enterChat.add(TF_userID);
		P_enterChat.add(L_password);
		P_enterChat.add(PF_password);

		P_enterChat.add(P_options);

		P_options.add(CB_remember);

		P_button.add(B_enter);
		P_button.add(B_exit);

		((JPanel) getGlassPane()).add(P_desktop);

		P_desktop.add(P_newUser);
		P_desktop.add(P_register);
		P_desktop.add(P_enterChat);
		P_desktop.add(P_button);

		MB_bar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		B_register.addActionListener(this);
		B_enter.addActionListener(this);
		MI_signout.addActionListener(this);
		MI_archiveViewer.addActionListener(this);
		RBMI_default.addActionListener(this);
		RBMI_gradient.addActionListener(this);
		RBMI_metal.addActionListener(this);

		// MI_onlineStatusMsg.addChangeListener(this) ;
		// MI_offlineStatusMsg.addChangeListener(this) ;
		B_exit.addActionListener(this);
		MI_editprofile.addActionListener(this);
		MI_about.addActionListener(this);
		getRootPane().setDefaultButton(B_enter);

		float_windowWidthRatio = ((float) getWidth())
				/ ((float) Properties.getScreenWidth());
		float_windowHeightRatio = ((float) getHeight())
				/ ((float) Properties.getScreenHeight());
		setResizable(false);
		setVisible(true);
		setBackground(Color.white);
		getGlassPane().setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		TF_onLineStatus.addKeyListener(this);
		TF_offlineStatus.addKeyListener(this);

		M_onlineStatusMsg.addChangeListener(this);

		addWindowListener(this);

		try {
			// remebmer my id/password settigns handler
			java.util.Properties p = new java.util.Properties();
			p.load(new FileInputStream("usersettings.config"));
			String user = (String) p.get("username");
			String pwd = (String) p.get("password");
			if (user != null && pwd != null) {
				TF_userID.setText(user);
				PF_password.setText(pwd);
				actionPerformed(new ActionEvent(B_enter,
						ActionEvent.ACTION_PERFORMED, "Enter"));
				CB_remember.setSelected(true);
			}

		} catch (Exception e) {

		}
	}

	/**
	 * default accessor method to get the object of the class
	 * 
	 * @return current window instance
	 */
	public static Login getDefaultInstance() {
		return loginWindow;
	}

	/**
	 * updates component's UI
	 */
	private void updateComponents() {
		SwingUtilities.updateComponentTreeUI(this);
		String user[] = Utilities.getDefaultInstance().getOpenedUsers();
		for (int i = 0; i < user.length; i++) {
			// UIManager.setLookAndFeel(new WindowsLookAndFeel());
			Utilities.getDefaultInstance().getOpenedWindow(user[i]).updateUI();
		}
		SwingUtilities
				.updateComponentTreeUI(ArchiveViewer.getDefaultInstance());
	}

	/**
	 * Action Event handler for various options
	 */
	public void actionPerformed(ActionEvent AE_action) {

		if (AE_action.getSource() == RBMI_default) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
				updateComponents();

			} catch (Exception e) {

			}
			TR_userGroups.updateUI();
			return;
		}
		if (AE_action.getSource() == RBMI_gradient) {
			try {
				UIManager.setLookAndFeel("gradient.GradientLookAndFeel");
				/*
				 * SwingUtilities.updateComponentTreeUI(this); String user []=
				 * Utilities.getDefaultInstance().getOpenedUsers(); for (int
				 * i=0; i < user.length; i++) { //UIManager.setLookAndFeel(new
				 * GradientLookAndFeel());
				 * Utilities.getDefaultInstance().getOpenedWindow
				 * (user[i]).updateUI(); }
				 */
				updateComponents();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			TR_userGroups.updateUI();
			return;
		}
		if (AE_action.getSource() == RBMI_metal) {
			try {
				UIManager.setLookAndFeel(new MetalLookAndFeel());
				/*
				 * SwingUtilities.updateComponentTreeUI(this); String user []=
				 * Utilities.getDefaultInstance().getOpenedUsers(); for (int
				 * i=0; i < user.length; i++) { //UIManager.setLookAndFeel(new
				 * MetalLookAndFeel());
				 * Utilities.getDefaultInstance().getOpenedWindow
				 * (user[i]).updateUI(); }
				 */
				updateComponents();
			} catch (Exception e) {

			}
			TR_userGroups.updateUI();
			return;
		}

		if (AE_action.getSource() == MI_archiveViewer) {
			ArchiveViewer.getDefaultInstance().setMsgDir("archiveData");
			ArchiveViewer.getDefaultInstance().setVisible(true);
			return;
		}

		/**
		 * when enter button is clicked
		 */
		if (AE_action.getSource() == B_enter) {

			// loads the userData by using the communicator
			UserData ud = ClientSideCommunicator.getDefaultInstance()
					.validateUser(
							TF_userID.getText(),
							new String(PF_password.getPassword()),
							(MessageReceiver) MessageReceiverImpl
									.getDefaultInstance());
			if (ud == null) {
				JOptionPane.showMessageDialog(
						null,
						Utilities.getDefaultInstance().mapMessageCode(
								OperationStatus.SERVER_NOT_AVAILABLE));
				return;
			}
			if (ud.getStatus() != OperationStatus.SUCESSFUL_LOGIN) {
				// displays any error message
				JOptionPane.showMessageDialog(null, Utilities
						.getDefaultInstance().mapMessageCode(ud.getStatus()));
				return;
			}
			try {

				if (CB_remember.isSelected()) {
					java.util.Properties p = new java.util.Properties();
					p.put("username", TF_userID.getText());
					p.put("password", new String(PF_password.getPassword()));
					p.store(new FileOutputStream("usersettings.config"),
							"User Settings");
				} else {
					File f = new File("usersettings.config");
					if (f.exists())
						f.delete();
				}
			} catch (Exception e) {

			}
			// sets various accesible object to the center location
			Utilities.getDefaultInstance().setCurrentUser(ud.getUserID());
			Utilities.getDefaultInstance().setCurrentUserData(ud);

			allUsers = ClientSideCommunicator.getDefaultInstance()
					.loadAllUsers();

			Utilities.getDefaultInstance().addUserList("all", allUsers);
			allUsers.remove(Utilities.getDefaultInstance().getCurrentUser());

			// adds listener
			MI_add.addActionListener(this);
			MI_close.addActionListener(this);
			// MI_signout.addActionListener(this);

			if (ud.getUserOnlineStatusMsg() != null
					&& ud.getUserOnlineStatusMsg().trim().length() > 0) {
				onlineMsg = ud.getUserOnlineStatusMsg();
			}
			if (ud.getUserOfflineStatusMsg() != null
					&& ud.getUserOfflineStatusMsg().trim().length() > 0) {
				offlineMsg = ud.getUserOfflineStatusMsg();
			}

			// loads the tree control
			TR_userGroups = new JTree(populateGroups());
			// getContentPane().setBackground(Color.white);
			TR_userGroups.setBackground(Color.white);
			// custom cell renderer to set the image according to user's status
			UserStatusTreeCellRenderer r = new UserStatusTreeCellRenderer();
			TR_userGroups.setCellRenderer(r);
			TR_userGroups.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			TR_userGroups.setOpaque(false);
			Utilities.getDefaultInstance().setScrollWindow(P_desktop, true);
			GradientPanel gp = new GradientPanel(new Color(238, 228, 218),
					new Color(180, 155, 130));
			getContentPane().add(gp, BorderLayout.CENTER);
			TransparentScrollPane tp;
			gp.setLayout(new BorderLayout());
			gp.add(tp = new TransparentScrollPane(TR_userGroups),
					BorderLayout.CENTER);
			gp.setPreferredSize(getSize());
			TR_userGroups.addTreeSelectionListener(this);
			TR_userGroups.addMouseListener(this);
			getRootPane().setDefaultButton(null);
			TR_userGroups.setRowHeight(30);
			MB_bar.setVisible(true);
			loggedIn = true;
			/*
			 * if (fileServer == null) fileServer = new FileServer();
			 */// after login calls the communicator to load offline message if
				// any
			Message[] messages = ClientSideCommunicator.getDefaultInstance()
					.getOffLineMessages(
							Utilities.getDefaultInstance().getCurrentUser());
			showOffLineMessageWindow(messages);
			ArchiveViewer.getDefaultInstance().clearContents();
			ArchiveViewer.getDefaultInstance().initMessageList();

		}

		// when close button is clicked
		if (AE_action.getSource() == MI_close) {
			windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			// dispose () ;
			// System.exit (0) ;
			return;
		}

		// when edit profile option is selected
		if (AE_action.getSource() == MI_editprofile) {

			UserData ud = Utilities.getDefaultInstance().getCurrentUserData();
			// creates the GUI window populated with the current data
			RegisterUser registerUser = RegisterUser.getDefaultInstance();
			registerUser.setUserID(ud.getUserID());
			registerUser.setUserName(ud.getUserName());
			registerUser.setUserAddress(ud.getUserAddress());
			registerUser.setUserConfirmPassword(ud.getUserPassword());
			registerUser.setUserPassword(ud.getUserPassword());
			registerUser.setUserDOB(ud.getUserDOB());
			registerUser.setUserMobileNo(ud.getUserMobileNo());
			registerUser.setMode("Edit");
			registerUser.setVisible(true);
		}

		// when sign out option is selected
		if (AE_action.getSource() == MI_signout) {
			// calls the communicator to sign out the user from the server
			ClientSideCommunicator.getDefaultInstance().signOutUser(
					Utilities.getDefaultInstance().getCurrentUser());
			getGlassPane().setVisible(true);
			if (!CB_remember.isSelected()) {
				TF_userID.setText("");
				PF_password.setText("");
			}
			getContentPane().removeAll();
			getRootPane().setDefaultButton(B_enter);
			P_desktop.setPreferredSize(getSize());
			TF_userID.requestFocus();
			P_desktop.updateUI();
			loggedIn = false;

			// writes archieve data
			writeArchiveData();

			// clean up tasks
			cleanUpUserData();

			return;
		}

		// when register option is selected
		if (AE_action.getSource() == B_register) {
			RegisterUser.getDefaultInstance().setMode("Register");
			RegisterUser.getDefaultInstance().setVisible(true);
		}

		// when add user to group option is selected
		if (AE_action.getSource() == MI_add) {
			if (currentSelectedUser == null) {
				JOptionPane.showMessageDialog(this, "Please select a user");
				return;
			}
			if (!currentSelectedUser.isGroup())
				AddUserWindow.getDefaultInsatnce().showWindow(
						currentSelectedUser.getUserID());
			// calls the communicator to add the user and updates the local data
			// accordingly
			Utilities.getDefaultInstance().setCurrentUserData(
					ClientSideCommunicator.getDefaultInstance().getUserData(
							Utilities.getDefaultInstance().getCurrentUser()));
			// updates group data
			updateGroupData();
		}

		// when exit option is selected
		if (AE_action.getSource() == B_exit) {
			windowClosing(null);
		}
		// when about the application option is selected
		if (AE_action.getSource() == MI_about) {
			// neds to be changed to singleton class
			new AboutApp(this);
		}

	}

	/**
	 * updates the tree by loading it again
	 */
	public void updateGroupData() {
		((DefaultTreeModel) TR_userGroups.getModel()).setRoot(populateGroups());
	}

	/**
	 * main function
	 * 
	 * @param args
	 *            command line arguments if any
	 */
	public static void main(String args[]) {
		Login.getDefaultInstance();
		// ArchiveViewer.getDefaultInstance().setMsgDir("archiveData");
	}

	/**
	 * this function populates user groups
	 * 
	 * @return root node of the tree been populated
	 */
	private DefaultMutableTreeNode populateGroups() {
		if (root != null) {
			// removes all elements
			((DefaultTreeModel) TR_userGroups.getModel()).setRoot(null);
		}
		// root element with the logged in user name
		rootUser = new UserSettings(TF_userID.getText(), TF_userID.getText(),
				true, UserStatus.UNKNOWN);
		rootUser.setUserOnlineStatusMsg(onlineMsg);
		root = new DefaultMutableTreeNode(rootUser);
		UserData userData = Utilities.getDefaultInstance().getCurrentUserData();
		// users created groups
		ArrayList<UserGroupSettings> userGroups = userData.getUserGroups();
		for (int i = 0; i < userGroups.size(); i++) {
			String groupName = userGroups.get(i).getGroupName();
			UserSettings us[] = userGroups.get(i).getUserIDs();
			DefaultMutableTreeNode group = new DefaultMutableTreeNode(
					new UserSettings(groupName, groupName, true,
							UserStatus.UNKNOWN));
			// iterates all the users in the group and adds to the tree parent
			// group node
			for (int j = 0; j < us.length; j++) {
				DefaultMutableTreeNode users = new DefaultMutableTreeNode(us[j]);
				group.add(users);
				// System.out.println(us[j].getUserID() +
				// us[j].getUserOfflineStatusMsg());
			}
			root.add(group);
		}

		// all group data is added in the last
		DefaultMutableTreeNode all = new DefaultMutableTreeNode(
				new UserSettings("all", "All Users", true, UserStatus.UNKNOWN));

		allUsers = Utilities.getDefaultInstance().getUsersInGroup("all");
		Set s = allUsers.keySet();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			String userID = (String) iter.next();
			all.add(new DefaultMutableTreeNode(allUsers.get(userID)));
			// System.out.println("Online Status : "+ userID +
			// allUsers.get(userID).getUserOnlineStatusMsg());
			// System.out.println("offline Status : "+ userID +
			// allUsers.get(userID).getUserOfflineStatusMsg());
		}
		root.add(all);
		return root;
	}

	/**
	 * tree selection event handler
	 */
	public void valueChanged(TreeSelectionEvent selectionEvent) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) TR_userGroups
				.getLastSelectedPathComponent();
		if (node != null) {
			currentSelectedUser = (UserSettings) node.getUserObject();
			// System.out.println(currentSelectedUser);
		}
	}

	/**
	 * mouse click event handler
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) TR_userGroups
					.getSelectionPath().getLastPathComponent();
			UserSettings us = (UserSettings) node.getUserObject();
			if (!us.isGroup()) {
				String parent = ((UserSettings) ((DefaultMutableTreeNode) node
						.getParent()).getUserObject()).getUserID();
				// checks whether a window is already opened and if it shows
				// that window and if not adds a new window and shows that
				if (Utilities.getDefaultInstance().getOpenedWindow(
						us.getUserID()) == null) {
					Utilities.getDefaultInstance().addOpenedWindow(
							us.getUserID(),
							new ChatWindow(us.getUserID(), parent));
				}
				Utilities.getDefaultInstance().getOpenedWindow(us.getUserID())
						.setVisible(true);
			}
		}
	}

	/**
	 * mouse enter event hanlder
	 */
	public void mouseEntered(MouseEvent arg0) {

	}

	/**
	 * mouse exit event hanlder
	 */
	public void mouseExited(MouseEvent arg0) {

	}

	/**
	 * mouse pressed event hanlder
	 */

	public void mousePressed(MouseEvent arg0) {

	}

	/**
	 * mouse released event hanlder
	 */
	public void mouseReleased(MouseEvent arg0) {

	}

	public void windowOpened(WindowEvent arg0) {

	}

	public void windowClosing(WindowEvent WE_close) {
		if (!loggedIn) {
			dispose();
			System.exit(0);
		}
		if (JOptionPane.showConfirmDialog(null,
				"Closing this window will signout you from the messenger",
				"Warning", JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION)
			return;

		actionPerformed(new ActionEvent(MI_signout,
				ActionEvent.ACTION_PERFORMED, "SignOut"));
		// ClientSideCommunicator.getDefaultInstance().signOutUser(Utilities.getDefaultInstance().getCurrentUser());
		dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent arg0) {

	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	private void cleanUpUserData() {
		String users[] = Utilities.getDefaultInstance().getOpenedUsers();
		if (users != null) {
			for (int i = 0; i < users.length; i++) {
				ChatWindow cw = Utilities.getDefaultInstance().getOpenedWindow(
						users[i]);
				cw.dispose();
				cw = null;
				Utilities.getDefaultInstance().removeWindowForUser(users[i]);
			}
			ArchiveViewer.getDefaultInstance().clearContents();
		}
	}

	/**
	 * shows all the offline message
	 * 
	 * @param messages
	 *            messages to be displayed
	 */
	private void showOffLineMessageWindow(Message[] messages) {
		if (messages == null || messages.length < 1)
			return;
		OffLineMsgWindow.getDefaultInstance().addOffLineMessages(messages);
		OffLineMsgWindow.getDefaultInstance().setVisible(true);
	}

	/**
	 * writing archieve data to the system
	 * 
	 */
	private void writeArchiveData() {

		// gets all the opened users
		String users[] = Utilities.getDefaultInstance().getOpenedUsers();

		for (int i = 0; i < users.length; i++) {
			System.out.println(users[i]);
			// get the chat window associated with the user
			ChatWindow cw = Utilities.getDefaultInstance().getOpenedWindow(
					users[i]);
			// String withUserID = cw.getUserID();
			String myUserID = Utilities.getDefaultInstance().getCurrentUser();
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			String dayStr = Utilities.getDefaultInstance().appendLeadingZeros(
					day);
			String monthStr = Utilities.getDefaultInstance()
					.appendLeadingZeros(month);
			File dir = new File("archiveData/" + myUserID + "$" + users[i]);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File("archiveData/" + myUserID + "$" + users[i] + "/"
					+ year + monthStr + dayStr + ".arch");
			ArrayList<Message> oldMessages = null;
			if (f.exists()) {
				try {
					// reads old messages
					oldMessages = Utilities.getDefaultInstance()
							.getArchivedMessages(f.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (oldMessages == null) {
					oldMessages = cw.getMessages();
				} else {
					// adds new messages to the archieve
					for (int j = 0; j < cw.getMessages().size(); j++) {
						oldMessages.add(cw.getMessages().get(j));
					}
				}
			} else {
				oldMessages = new ArrayList<Message>();
				for (int j = 0; j < cw.getMessages().size(); j++) {
					oldMessages.add(cw.getMessages().get(j));
				}
			}
			Utilities.getDefaultInstance().writeArcieveData(
					f.getAbsolutePath(), oldMessages);
		}
	}

	public void stateChanged(ChangeEvent e) {
		TF_onLineStatus.setText(onlineMsg);
		TF_offlineStatus.setText(offlineMsg);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == TF_onLineStatus) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				onlineMsg = TF_onLineStatus.getText() == null ? ""
						: TF_onLineStatus.getText();
				M_view.doClick();
				rootUser.setUserOnlineStatusMsg(onlineMsg);
				TR_userGroups.updateUI();
				ClientSideCommunicator
						.getDefaultInstance()
						.setUserStatusMessage(
								Utilities.getDefaultInstance().getCurrentUser(),
								onlineMsg.replaceAll("\n", ""), true);
			}
			return;
		}

		if (e.getSource() == TF_offlineStatus) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				offlineMsg = TF_offlineStatus.getText() == null ? ""
						: TF_offlineStatus.getText();
				M_view.doClick();
				ClientSideCommunicator
						.getDefaultInstance()
						.setUserStatusMessage(
								Utilities.getDefaultInstance().getCurrentUser(),
								offlineMsg.replaceAll("\n", ""), false);
			}
		}
	}

	public void repaintGroupData() {
		if (TR_userGroups != null) {
			TR_userGroups.updateUI();
		}
	}
}
