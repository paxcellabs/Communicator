package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.CheckBoxModel;
import renderers.CheckBoxListCellRenderer;
import utils.Utilities;
import beans.Message;
import beans.TextMessage;
import beans.UserSettings;

import communicator.ClientSideCommunicator;

import flags.MessageType;

/**
 * This class provides user a chatting window to the user the created window
 * object remains same for a given user, any other request to open the window
 * for the same user is handled before creating this window object from this
 * window multiple users can be selected if user is wishing to send same message
 * to multiple users also the default user selected can not be unselected
 * 
 * @author Kuldeep Saxena
 * 
 */

public class ChatWindow extends JFrame implements ActionListener,
		ChangeListener, KeyListener, WindowListener {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -66879069214169953L;

	/*
	 * GUI related variables
	 */
	private JTextPane TP_display, TP_messageBox;
	private JButton B_send;
	private JButton B_bold, B_italic, B_underLine, B_strikeThrough,
			B_showInMessage, B_colorPicker, B_fontPicker;
	private JPanel P_center, P_centerLeft, P_centerRight, P_south, P_southSub1,
			P_southSub2;
	private JScrollPane SP_display, SP_messageBox, SP_users;
	private JList LT_users;
	private JSplitPane SLP_pane;
	final ButtonGroup bg = new ButtonGroup();
	private StyledDocument messageBoxDoc = null;
	private StyledDocument displayBoxDoc = null;
	private Style messageBoxStyle = null;
	private Style displayBoxStyle = null;
	private JDialog colorChooserPanel;
	private Border upperBorder = null;// new CompoundBorder(new
										// BevelBorder(BevelBorder.RAISED),new
										// BevelBorder(BevelBorder.RAISED));
	private Border loweredBorder = new CompoundBorder(new BevelBorder(
			BevelBorder.LOWERED), new BevelBorder(BevelBorder.LOWERED));
	private String selectedFontName = "Times New Roman";
	private int selectedFontSize = 12;
	private JDialog D_font;
	private JComboBox CMB_fontNames, CMB_fontSizes;
	private JButton B_fontOk, B_fontCancel;
	private JButton B_colorOk, B_colorCancel;
	private Color selectedColor = Color.black;
	private JColorChooser colorChooser = new JColorChooser(selectedColor);
	private JMenuBar MB_options;
	private JMenu M_options, M_messageBoxType, M_view, M_actions;
	private JRadioButtonMenuItem RBMI_info, RBMI_warning, RBMI_alert;
	private JMenuItem MI_showRecent, MI_showFullArchive;;
	private JCheckBoxMenuItem CBMI_showTimestamp;

	private JMenuItem MI_textOptions, MI_sendFile, MI_archiveViewer;

	/**
	 * custom model class to contain objects of UserSettings class
	 */
	private CheckBoxModel model = new CheckBoxModel();
	/**
	 * user id for which window is opened
	 */
	private String userID;

	/**
	 * the group in which this user belongs this chat window will contain the
	 * list of all the users within this group
	 */
	private String groupName;

	/**
	 * Text Option Property 3D Text
	 */
	private boolean threeDSelected = false;

	/**
	 * Text Option Property 3D BOX
	 */
	private boolean threeDBoxSelected = false;

	/**
	 * Text Option Property Wave Theme
	 */
	private boolean waveSelected = false;

	/**
	 * Text Option Property Dim Theme
	 */
	private boolean dimSelected = false;

	/**
	 * Text Option Property When no option is selected
	 */
	private boolean noneSelected = true;

	/**
	 * Text Option Property Gradient Text
	 */

	private boolean gradientSelected = false;

	/**
	 * Text Option Property Comic text
	 */
	private boolean comicSelected = false;

	/**
	 * Text Option Property Rose Flower Filled
	 */
	private boolean flowerSelected = false;

	/**
	 * Text Option Property Dancing text
	 */
	private boolean dancingSelected = false;

	/**
	 * Text Option dialog related to the GUI
	 */
	private TextMessageOptions toptions;

	/**
	 * flags to indicate whether Time Stamp is shown
	 */
	private boolean showTimeStamp;
	/**
	 * flags to indicate whether Archieve is shown
	 */
	private boolean showArchive;

	/**
	 * messages already in the window
	 */
	private ArrayList<Message> messages;

	/**
	 * flag indicates whether archieve operation in progress in that case we
	 * don't need to add the message again
	 */
	private boolean redrawing = false;

	/**
	 * flag to reset fulkl archive
	 */
	private boolean showFullArchive;

	/**
	 * Default Contructor initializes GUI
	 * 
	 * @param userID
	 *            userid to whom this window belong
	 * @param groupName
	 *            defualt group for which data need to be loaded
	 */
	public ChatWindow(String userID, String groupName) {
		super("Paxcel Communicator - > " + userID);
		setIconImage(new ImageIcon("resources/images/title.jpg").getImage());
		messages = new ArrayList<Message>();
		this.groupName = groupName;
		this.userID = userID;
		initFontDialog();
		initColorDialog();
		initComponents();
		addWindowListener(this);
		toptions = new TextMessageOptions(this);
		toptions.setAlwaysOnTop(true);
	}

	/**
	 * initializes GUI components
	 * 
	 */
	private void initComponents() {
		MB_options = new JMenuBar();

		M_options = new JMenu("Options");
		M_actions = new JMenu("Actions");
		M_view = new JMenu("View");
		M_messageBoxType = new JMenu("Message Box Type");

		MI_textOptions = new JMenuItem("Text Options");
		MI_archiveViewer = new JMenuItem("Archive Viewer");
		RBMI_alert = new JRadioButtonMenuItem("Alert");
		RBMI_warning = new JRadioButtonMenuItem("Warning");
		RBMI_info = new JRadioButtonMenuItem("Info");

		CBMI_showTimestamp = new JCheckBoxMenuItem("Show Timestamp", true);
		MI_showRecent = new JMenuItem("Show Recent");
		MI_showFullArchive = new JMenuItem("Show Full Archive");
		MI_sendFile = new JMenuItem("Send file");

		MI_textOptions.addActionListener(this);
		MI_archiveViewer.addActionListener(this);

		showTimeStamp = true;
		showArchive = false;
		showFullArchive = false;
		CBMI_showTimestamp.addActionListener(this);
		MI_showFullArchive.addActionListener(this);
		MI_showRecent.addActionListener(this);
		MB_options.add(M_options);
		MB_options.add(M_view);
		MB_options.add(M_actions);
		M_options.setOpaque(false);
		M_view.setOpaque(false);
		M_actions.setOpaque(false);
		M_view.add(CBMI_showTimestamp);
		M_view.add(MI_showRecent);
		M_view.add(MI_showFullArchive);
		M_view.add(MI_archiveViewer);
		M_options.add(MI_textOptions);
		// M_actions.add(MI_sendFile);
		M_messageBoxType.add(RBMI_info);
		M_messageBoxType.add(RBMI_alert);
		M_messageBoxType.add(RBMI_warning);
		M_options.add(M_messageBoxType);

		bg.add(RBMI_alert);
		bg.add(RBMI_warning);
		bg.add(RBMI_info);
		RBMI_info.setSelected(true);
		setJMenuBar(MB_options);
		TP_display = new JTextPane();
		TP_messageBox = new JTextPane();

		messageBoxDoc = TP_messageBox.getStyledDocument();
		displayBoxDoc = TP_display.getStyledDocument();

		messageBoxStyle = messageBoxDoc.addStyle("MyStyle", null);
		displayBoxStyle = displayBoxDoc.addStyle("MyStyle", null);

		B_send = new JButton("Send");

		P_center = new JPanel();
		P_centerLeft = new JPanel();
		P_centerRight = new JPanel();
		P_south = new JPanel();
		P_southSub1 = new JPanel();
		P_southSub2 = new JPanel();

		P_southSub1.setOpaque(false);
		P_southSub2.setOpaque(false);

		B_bold = new JButton(new ImageIcon("resources/images/bold.gif"));

		int iconWidth = B_bold.getIcon().getIconWidth();
		int iconHeight = B_bold.getIcon().getIconHeight();
		B_italic = new JButton(new ImageIcon(new ImageIcon(
				"resources/images/italic.gif").getImage().getScaledInstance(
				iconWidth, iconHeight, Image.SCALE_SMOOTH)));
		B_underLine = new JButton(new ImageIcon(new ImageIcon(
				"resources/images/underline.gif").getImage().getScaledInstance(
				iconWidth, iconHeight, Image.SCALE_SMOOTH)));
		B_strikeThrough = new JButton(new ImageIcon(new ImageIcon(
				"resources/images/strikethrough.gif").getImage()
				.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)));
		B_colorPicker = new JButton(new ImageIcon(new ImageIcon(
				"resources/images/colorpicker.gif").getImage()
				.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)));
		B_fontPicker = new JButton(new ImageIcon(new ImageIcon(
				"resources/images/fontpicker.gif").getImage()
				.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)));
		B_showInMessage = new JButton(new ImageIcon(
				"resources/images/messagebox.gif"));

		B_bold.setToolTipText("Bold");
		B_italic.setToolTipText("Italic");
		B_underLine.setToolTipText("Underline");
		B_strikeThrough.setToolTipText("Strike Through");
		B_colorPicker.setToolTipText("Color Picker");
		B_fontPicker.setToolTipText("Font Picker");
		B_showInMessage.setToolTipText("Show Message in Popup message box");

		B_bold.setMargin(new Insets(0, 0, 0, 0));
		B_italic.setMargin(new Insets(0, 0, 0, 0));
		B_underLine.setMargin(new Insets(0, 0, 0, 0));
		B_strikeThrough.setMargin(new Insets(0, 0, 0, 0));
		B_colorPicker.setMargin(new Insets(0, 0, 0, 0));
		B_showInMessage.setMargin(new Insets(0, 0, 0, 0));
		B_fontPicker.setMargin(new Insets(0, 0, 0, 0));

		LT_users = new JList(model);

		B_send.setEnabled(false);

		populateList(groupName, userID);// , all);
		LT_users.setCellRenderer(new CheckBoxListCellRenderer());
		SP_display = new JScrollPane(TP_display);
		SP_messageBox = new JScrollPane(TP_messageBox);
		SP_users = new JScrollPane(LT_users);

		B_bold.setBorder(upperBorder);
		B_italic.setBorder(upperBorder);
		B_underLine.setBorder(upperBorder);
		B_strikeThrough.setBorder(upperBorder);
		B_colorPicker.setBorder(upperBorder);
		B_showInMessage.setBorder(upperBorder);
		B_fontPicker.setBorder(upperBorder);

		SLP_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		P_center.setLayout(new FlowLayout(FlowLayout.LEFT));
		P_centerLeft.setPreferredSize(new Dimension(500, 300));
		SP_display.setPreferredSize(new Dimension(440, 300));
		SP_users.setPreferredSize(new Dimension(140, 300));
		P_centerRight.setPreferredSize(new Dimension(150, 300));
		P_centerLeft.add(SP_display);
		P_centerRight.add(SP_users);
		P_center.add(SLP_pane, BorderLayout.CENTER);
		P_south.setLayout(new FlowLayout(FlowLayout.LEFT));

		P_south.setPreferredSize(new Dimension(650, 250));
		SP_messageBox.setPreferredSize(new Dimension(420, 80));
		SLP_pane.setLeftComponent(SP_display);
		SLP_pane.setRightComponent(SP_users);
		P_south.add(P_southSub1);
		P_south.add(P_southSub2);
		P_southSub1.add(SP_messageBox);
		P_southSub1.add(B_send);
		P_southSub2.add(B_bold);
		P_southSub2.add(B_italic);
		P_southSub2.add(B_underLine);
		P_southSub2.add(B_strikeThrough);
		P_southSub2.add(B_colorPicker);
		P_southSub2.add(B_fontPicker);
		P_southSub2.add(B_showInMessage);
		setSize(610, 515);
		getContentPane().add(P_center);
		setResizable(false);
		P_center.add(P_south, BorderLayout.SOUTH);
		LT_users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TP_display.setEditable(false);

		StyleConstants.setFontFamily(messageBoxStyle, selectedFontName);
		StyleConstants.setFontSize(messageBoxStyle, selectedFontSize);
		messageBoxDoc.setParagraphAttributes(0, messageBoxDoc.getLength(),
				messageBoxStyle, true);
		B_bold.setContentAreaFilled(false);
		B_italic.setContentAreaFilled(false);
		B_underLine.setContentAreaFilled(false);
		B_strikeThrough.setContentAreaFilled(false);
		B_showInMessage.setContentAreaFilled(false);
		B_colorPicker.setContentAreaFilled(false);
		B_fontPicker.setContentAreaFilled(false);
		B_bold.setToolTipText("Bold");
		B_italic.setToolTipText("Italic");
		B_underLine.setToolTipText("Underline");
		B_strikeThrough.setToolTipText("Strike Through");
		B_colorPicker.setToolTipText("Foregroung color");
		B_showInMessage
				.setToolTipText("Show your message to the recepient in message box");
		B_fontPicker.setToolTipText("Select Font");

		B_bold.addActionListener(this);
		B_italic.addActionListener(this);
		B_underLine.addActionListener(this);
		B_strikeThrough.addActionListener(this);
		B_colorPicker.addActionListener(this);
		B_showInMessage.addActionListener(this);
		B_fontPicker.addActionListener(this);
		MI_sendFile.addActionListener(this);
		TP_messageBox.addKeyListener(this);
		B_send.addActionListener(this);
		CBMI_showTimestamp.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F2, KeyEvent.KEY_LOCATION_UNKNOWN));
		MI_showRecent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
				KeyEvent.KEY_LOCATION_UNKNOWN));
		MI_showFullArchive.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F4, KeyEvent.KEY_LOCATION_UNKNOWN));

		/**
		 * listener to handle list selection event on selection user from the
		 * list
		 */
		LT_users.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					boolean handle = true;

					public void valueChanged(ListSelectionEvent selected) {
						if (!handle) {
							handle = true;
							return;
						}
						if (LT_users.getSelectedIndex() != -1) {
							UserSettings cb;
							cb = ((UserSettings) LT_users.getModel()
									.getElementAt(LT_users.getSelectedIndex()));
							if (((CheckBoxModel) LT_users.getModel())
									.getDefaultUserID() != null)
								if (((CheckBoxModel) LT_users.getModel())
										.getDefaultUserID().equalsIgnoreCase(
												cb.getUserID()))
									return;
							if (Utilities.getDefaultInstance().getOpenedWindow(
									cb.getUserID()) != null
									&& !cb.isSelected()) {
								return;
							}
							cb.setSelected(!cb.isSelected());
							// System.out.println(cb.isSelected());
							if (cb.isSelected()) {
								String userID = cb.getUserID();
								if (Utilities.getDefaultInstance()
										.getOpenedWindow(userID) == null)
									Utilities.getDefaultInstance()
											.addOpenedWindow(userID,
													ChatWindow.this);
							} else {
								Utilities.getDefaultInstance()
										.removeWindowForUser(cb.getUserID());
							}
							handle = false;
							// LT_users.updateUI();
							LT_users.clearSelection();
						}
					}

				});
	}

	/**
	 * action event handler listens for various component events
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == MI_archiveViewer) {
			ArchiveViewer.getDefaultInstance().setVisible(true);
			return;
		}
		if (e.getSource() == MI_sendFile) {
			JFileChooser fc = new JFileChooser("Select a file");
			fc.showOpenDialog(this);
			File f = fc.getSelectedFile();
			String fileName = f.getName();
			if (fileName == null)
				return;
			ClientSideCommunicator.getDefaultInstance()
					.sendFileAcceptanceToUser(getSelectedUsers().get(0),
							Utilities.getDefaultInstance().getCurrentUser(),
							fileName);
			/*
			 * if (inetAddress.equalsIgnoreCase("USER IS OFFLINE")) {
			 * JOptionPane.showMessageDialog(null,
			 * "User is offline, can't send file"); return; } if (inetAddress ==
			 * null) { JOptionPane.showMessageDialog(null,
			 * "user cancelled request"); return; }
			 *//*
				 * try { Socket s = new Socket (inetAddress, 9200); OutputStream
				 * out = s.getOutputStream(); FileInputStream in = new
				 * FileInputStream (f.getAbsolutePath()); byte b [] = new byte
				 * [1024]; out.write((fileName +"|").getBytes()); out.flush();
				 * int noofbytes =0; while ((noofbytes=in.read(b)) != -1) {
				 * out.write(b,0, noofbytes); out.flush(); } in.close();
				 * out.close(); s.close(); } catch (Exception e1) {
				 * e1.printStackTrace(); }
				 */
		}

		if (e.getSource() == MI_showFullArchive) {
			if (MI_showFullArchive.getText().trim()
					.equalsIgnoreCase("Show Full Archive")) {
				MI_showFullArchive.setText("Hide Full Archive");
				MI_showRecent.setEnabled(false);
				showFullArchive = true;
			} else {
				MI_showFullArchive.setText("Show Full Archive");
				showFullArchive = false;
				MI_showRecent.setEnabled(true);
			}
			updateMessages();

		}
		/**
		 * called when show time stamp
		 */

		if (e.getSource() == CBMI_showTimestamp) {
			// sets variable and calls the updateMessages which inserts message
			// in the chat window
			showTimeStamp = CBMI_showTimestamp.isSelected();
			updateMessages();
		}
		if (e.getSource() == MI_showRecent) {
			if (MI_showRecent.getText().trim().equalsIgnoreCase("Show Recent")) {
				MI_showRecent.setText("Hide Recent");
				showArchive = true;
			} else {
				MI_showRecent.setText("Show Recent");
				showArchive = false;
			}

			updateMessages();
			return;
		}

		/**
		 * when send button is clicked
		 */
		if (e.getSource() == B_send) {
			if (getSelectedUsers().size() == 0) {
				JOptionPane.showMessageDialog(ChatWindow.this,
						"Please selected at least one user to send message");
			}

			// creates an message object and initializes it
			Message message = new Message();

			message.setFromUser(Utilities.getDefaultInstance().getCurrentUser());
			ArrayList<String> selected = getSelectedUsers();
			String selectedUsers[] = new String[selected.size()];
			for (int i = 0; i < selected.size(); i++) {
				selectedUsers[i] = selected.get(i);
			}
			message.setToUsers(selectedUsers);
			message.setMessageType(MessageType.TEXT_MESSAGE);
			TextMessage textMessage = new TextMessage();
			Style s = messageBoxDoc.getStyle("MyStyle");
			boolean isBold = StyleConstants.isBold(s);
			boolean isItalic = StyleConstants.isItalic(s);
			boolean isStrikeThrough = StyleConstants.isStrikeThrough(s);
			boolean isUnderLine = StyleConstants.isUnderline(s);
			Color foreGround = StyleConstants.getForeground(s);
			textMessage.setBold(isBold);
			textMessage.setItalic(isItalic);
			textMessage.setStrikeThrough(isStrikeThrough);
			textMessage.setUnderline(isUnderLine);
			textMessage.setFontName(StyleConstants.getFontFamily(s));
			textMessage.setFontSize(StyleConstants.getFontSize(s));
			textMessage.setTextColor(foreGround);
			try {
				textMessage.setTextMessage(messageBoxDoc.getText(0,
						messageBoxDoc.getLength()));
			} catch (Exception e1) {

			}
			message.setTextMessage(textMessage);
			message.setDisplayType(MessageType.DIS_TEXTBOX_MESSAGE);
			message.setThreeD(threeDSelected);
			message.setThreeDBorder(threeDBoxSelected);
			message.setSentTime(new Date());
			message.setShowTimeStamp(true);
			if (dimSelected) {
				message.setSpecialAttribute(MessageType.DIM);
			} else if (waveSelected) {
				message.setSpecialAttribute(MessageType.WAVE);
			} else if (comicSelected) {
				message.setSpecialAttribute(MessageType.COMIC);
			} else if (gradientSelected) {
				message.setSpecialAttribute(MessageType.GRADIENT);
			} else if (flowerSelected) {
				message.setSpecialAttribute(MessageType.FLOWER);
			} else if (dancingSelected) {
				message.setSpecialAttribute(MessageType.DANCING);
			} else
				message.setSpecialAttribute(MessageType.NONE);

			// displays message in the local text component
			showMessage(message, true);

			// sets message other properties, these properties are set later
			// because these are related to displaying message
			// in message box
			if (B_showInMessage.isSelected()) {
				if (RBMI_alert.isSelected())
					message.setDisplayType(MessageType.DIS_ALERT_MESSAGE);
				else if (RBMI_info.isSelected())
					message.setDisplayType(MessageType.DIS_INFO_MESSAGE);
				else if (RBMI_warning.isSelected())
					message.setDisplayType(MessageType.DIS_WARNING_MESSAGE);

			}

			// calls the communicator to send the message
			ClientSideCommunicator.getDefaultInstance().sendMessage(message);
			TP_messageBox.setText("");
			B_send.setEnabled(false);
			return;
		}

		// when color selection option is selected
		if (e.getSource() == B_colorPicker) {
			colorChooserPanel.setLocation(B_send.getLocationOnScreen().x + 30,
					B_send.getLocationOnScreen().y + 25);
			colorChooserPanel.setVisible(true);
		}

		// when bold option is selected
		if (e.getSource() == B_bold) {
			B_bold.setSelected(!B_bold.isSelected());
			if (B_bold.isSelected()) {
				B_bold.setBorder(loweredBorder);
				StyleConstants.setBold(messageBoxStyle, true);
			} else {
				B_bold.setBorder(upperBorder);
				StyleConstants.setBold(messageBoxStyle, false);
			}
		}

		// when italic option selected
		if (e.getSource() == B_italic) {
			B_italic.setSelected(!B_italic.isSelected());
			if (B_italic.isSelected()) {
				B_italic.setBorder(loweredBorder);
				StyleConstants.setItalic(messageBoxStyle, true);
			} else {
				B_italic.setBorder(upperBorder);
				StyleConstants.setItalic(messageBoxStyle, false);
			}
		}

		// when underline option is selected
		if (e.getSource() == B_underLine) {
			B_underLine.setSelected(!B_underLine.isSelected());
			if (B_underLine.isSelected()) {
				B_underLine.setBorder(loweredBorder);
				StyleConstants.setUnderline(messageBoxStyle, true);
			} else {
				B_underLine.setBorder(upperBorder);
				StyleConstants.setUnderline(messageBoxStyle, false);
			}
		}

		// when strike through option is selected
		if (e.getSource() == B_strikeThrough) {
			B_strikeThrough.setSelected(!B_strikeThrough.isSelected());
			if (B_strikeThrough.isSelected()) {
				B_strikeThrough.setBorder(loweredBorder);
				StyleConstants.setStrikeThrough(messageBoxStyle, true);
			} else {
				B_strikeThrough.setBorder(upperBorder);
				StyleConstants.setStrikeThrough(messageBoxStyle, false);
			}
		}

		// when show in message option is selected
		if (e.getSource() == B_showInMessage) {
			B_showInMessage.setSelected(!B_showInMessage.isSelected());
			if (B_showInMessage.isSelected()) {
				B_showInMessage.setBorder(loweredBorder);
			} else {
				B_showInMessage.setBorder(upperBorder);
			}
		}

		// when font selection option is selected
		if (e.getSource() == B_fontPicker) {
			showFontDialog();
			StyleConstants.setFontFamily(messageBoxStyle, selectedFontName);
			StyleConstants.setFontSize(messageBoxStyle, selectedFontSize);
		}

		// when font selection's ok button is clicked
		if (e.getSource() == B_fontOk) {
			selectedFontName = (String) CMB_fontNames.getSelectedItem();
			selectedFontSize = Integer.parseInt((String) CMB_fontSizes
					.getSelectedItem());
			StyleConstants.setFontFamily(messageBoxStyle, selectedFontName);
			StyleConstants.setFontSize(messageBoxStyle, selectedFontSize);
			D_font.setVisible(false);
		}

		// when font selection's cancel button is clicked
		if (e.getSource() == B_fontCancel) {
			D_font.setVisible(false);
		}

		// when color selection ok is clicked
		if (e.getSource() == B_colorOk) {
			StyleConstants.setForeground(messageBoxStyle, selectedColor);
			colorChooserPanel.setVisible(false);
		}

		// when color selection cancel is clicked
		if (e.getSource() == B_colorCancel) {
			selectedColor = StyleConstants.getForeground(messageBoxStyle);
			colorChooserPanel.setVisible(false);
		}
		// when text option is selected
		if (e.getSource() == MI_textOptions) {
			toptions.setVisible(true);
		}
		// applying attribute to the current text component so that effect of
		// change can be visible to the user
		messageBoxDoc.setParagraphAttributes(0, messageBoxDoc.getLength(),
				messageBoxStyle, true);
	}

	/**
	 * returns selected user list
	 * 
	 * @return all the selected users
	 */
	private ArrayList<String> getSelectedUsers() {
		ArrayList<String> selectedUsers = new ArrayList<String>();
		for (int i = 0; i < model.getSize(); i++) {
			UserSettings prop = ((UserSettings) model.getElementAt(i));
			if (prop.isSelected())
				selectedUsers.add(prop.getUserID());
		}
		return selectedUsers;
	}

	/**
	 * function populates user list using the group name
	 * 
	 * @param groupName
	 *            the group for which users need to be loaded
	 * @param userID
	 *            default selected user id
	 */
	private void populateList(String groupName, String userID)// , boolean all)
	{
		/*
		 * group name is used to fetch related users only
		 */

		// is null when message is coming from the remote and a new window is
		// opened at the client
		if (groupName == null) {
			UserSettings us = Utilities.getDefaultInstance().getUserSettings(
					userID);
			// sets property to true
			us.setSelected(true);
			us.setGroup(false);
			model.addElement(us);
			return;
		}

		// calls first User in group which return a map containing user name as
		// key and its setting as value, which
		// is then used to return all the value
		UserSettings userSettings[] = Utilities.getDefaultInstance()
				.getUsersInMap(
						Utilities.getDefaultInstance().getUsersInGroup(
								groupName));
		for (int i = 0; i < userSettings.length; i++) {
			if (userSettings[i].getUserID().equalsIgnoreCase(userID))
				userSettings[i].setSelected(true);
			else
				userSettings[i].setSelected(false);
			model.addElement(userSettings[i]);

		}
	}

	/**
	 * this function is called to show message in the display box called by the
	 * local client as well as by the MessageReceiverImpl for remote message
	 * 
	 * @param message
	 *            message to be displayed
	 * @param local
	 *            indicates whether its a local message or not
	 */
	public void showMessage(Message message, boolean local) {
		// when archieve operation is in progress
		if (!redrawing) {
			messages.add(message);
			if (local)
				message.setType(MessageType.LOCAL);
			else
				message.setType(MessageType.REMOTE);
		}

		// calls Utilities class's show message to display the message
		Utilities.getDefaultInstance().showMessage(message, this, TP_display,
				displayBoxDoc, displayBoxStyle, local);
		// scrolls the display window
		TP_display.setCaretPosition(displayBoxDoc.getLength());
	}

	/**
	 * handles color selection event
	 */
	public void stateChanged(ChangeEvent e) {
		selectedColor = colorChooser.getColor();
	}

	/**
	 * Key Typed Event listener does nothing here
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Key Pressed Event listener does nothing here
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Key Typed Event listener listener for release event for message box and
	 * checks whether the key is enter and if calls the sendMessage function by
	 * creating a false event otherwise it simply enable/disables send button by
	 * checking text data in the field
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == TP_messageBox) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (TP_messageBox.getText() != null
						&& TP_messageBox.getText().trim().length() > 0) {
					actionPerformed(new ActionEvent(B_send,
							ActionEvent.ACTION_PERFORMED, "Send"));
				}
				TP_messageBox.setText("");
			}
			if (messageBoxDoc.getLength() > 0) {
				B_send.setEnabled(true);
			} else {
				B_send.setEnabled(false);
			}

		}

	}

	/**
	 * shows Font Selection dialog on screen
	 */
	private void showFontDialog() {
		CMB_fontNames.setSelectedItem(selectedFontName);
		CMB_fontSizes.setSelectedItem("" + selectedFontSize);
		D_font.setLocation(B_fontPicker.getLocationOnScreen().x,
				B_fontPicker.getLocationOnScreen().y + 20);
		D_font.setVisible(true);
	}

	/**
	 * initializes font selection dialog
	 * 
	 */
	private void initFontDialog() {
		D_font = new JDialog();
		D_font.setTitle("Select font");
		D_font.setAlwaysOnTop(true);
		CMB_fontNames = new JComboBox(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		CMB_fontSizes = new JComboBox(new String[] { "10", "12", "14", "16",
				"18", "20", "22", "24", "28", "32", "36", "42", "48", "52" });
		B_fontOk = new JButton("OK");
		B_fontCancel = new JButton("Cancel");
		CMB_fontNames.setOpaque(false);
		CMB_fontSizes.setOpaque(false);
		B_fontOk.addActionListener(this);
		B_fontCancel.addActionListener(this);
		JPanel P_layout = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel P_layout2 = new JPanel();
		P_layout.setOpaque(false);
		P_layout2.setOpaque(false);
		P_layout2.add(B_fontOk);
		P_layout2.add(B_fontCancel);
		JPanel P_fontName, P_fontSize;
		P_fontName = new JPanel();
		P_fontSize = new JPanel();
		P_layout.add(P_fontName);
		P_fontName.setOpaque(false);
		P_fontSize.setOpaque(false);
		P_fontName.setBorder(new TitledBorder("Font Name"));
		P_fontSize.setBorder(new TitledBorder("Font Size"));
		P_fontName.add(CMB_fontNames);
		P_layout.add(P_fontSize);
		P_fontSize.add(CMB_fontSizes);
		D_font.getContentPane().add(P_layout, BorderLayout.CENTER);
		D_font.getContentPane().add(P_layout2, BorderLayout.SOUTH);
		D_font.setSize(300, 130);
		D_font.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}

	/**
	 * initializes color selection dialog
	 * 
	 */
	private void initColorDialog() {
		colorChooserPanel = new JDialog();
		colorChooserPanel.setAlwaysOnTop(true);
		colorChooserPanel.setTitle("Choose a color");
		colorChooserPanel.add(colorChooser, BorderLayout.CENTER);
		colorChooser.setPreviewPanel(new JPanel());
		colorChooserPanel.setSize(colorChooser.getPreferredSize().width,
				colorChooser.getPreferredSize().height + 50);
		colorChooser.getSelectionModel().addChangeListener(this);
		JPanel P_layout = new JPanel();
		B_colorCancel = new JButton("Cancel");
		B_colorOk = new JButton("OK");
		P_layout.add(B_colorOk);
		P_layout.add(B_colorCancel);
		B_colorCancel.addActionListener(this);
		B_colorOk.addActionListener(this);
		colorChooserPanel.add(P_layout, BorderLayout.SOUTH);
	}

	/**
	 * @return Returns the dimDSelected.
	 */
	public boolean isDimSelected() {
		return dimSelected;
	}

	/**
	 * @param dimSelected
	 *            The dimSelected to set.
	 */
	public void setDimSelected(boolean dimSelected) {
		this.dimSelected = dimSelected;
	}

	/**
	 * @return Returns the noneSelected.
	 */
	public boolean isNoneSelected() {
		return noneSelected;
	}

	/**
	 * @param noneSelected
	 *            The noneSelected to set.
	 */
	public void setNoneSelected(boolean noneSelected) {
		this.noneSelected = noneSelected;
	}

	/**
	 * @return Returns the threeDBoxSelected.
	 */
	public boolean isThreeDBoxSelected() {
		return threeDBoxSelected;
	}

	/**
	 * @param threeDBoxSelected
	 *            The threeDBoxSelected to set.
	 */
	public void setThreeDBoxSelected(boolean threeDBoxSelected) {
		this.threeDBoxSelected = threeDBoxSelected;
	}

	/**
	 * @return Returns the threeDSelected.
	 */
	public boolean isThreeDSelected() {
		return threeDSelected;
	}

	/**
	 * @param threeDSelected
	 *            The threeDSelected to set.
	 */
	public void setThreeDSelected(boolean threeDSelected) {
		this.threeDSelected = threeDSelected;
	}

	/**
	 * @return Returns the waveSelected.
	 */
	public boolean isWaveSelected() {
		return waveSelected;
	}

	/**
	 * @param waveSelected
	 *            The waveSelected to set.
	 */
	public void setWaveSelected(boolean waveSelected) {
		this.waveSelected = waveSelected;
	}

	/**
	 * window open event handler does nothing here
	 */
	public void windowOpened(WindowEvent arg0) {

	}

	/**
	 * window closing event handler hides other related windows
	 */
	public void windowClosing(WindowEvent e) {
		D_font.setVisible(false);
		colorChooserPanel.setVisible(false);
		toptions.setVisible(false);
	}

	/**
	 * window closed event handler does nothing here
	 */
	public void windowClosed(WindowEvent arg0) {

	}

	/**
	 * window iconified event handler does nothing here
	 */
	public void windowIconified(WindowEvent arg0) {

	}

	/**
	 * window deiconified event handler does nothing here
	 */

	public void windowDeiconified(WindowEvent arg0) {

	}

	/**
	 * window activated event handler does nothing here
	 */

	public void windowActivated(WindowEvent arg0) {

	}

	/**
	 * window deactivated event handler does nothing here
	 */

	public void windowDeactivated(WindowEvent arg0) {

	}

	/**
	 * @return Returns the selectedFontName.
	 */
	public String getSelectedFontName() {
		return selectedFontName;
	}

	/**
	 * @param selectedFontName
	 *            The selectedFontName to set.
	 */
	public void setSelectedFontName(String selectedFontName) {
		this.selectedFontName = selectedFontName;
	}

	/**
	 * @return Returns the selectedFontSize.
	 */
	public int getSelectedFontSize() {
		return selectedFontSize;
	}

	/**
	 * @param selectedFontSize
	 *            The selectedFontSize to set.
	 */
	public void setSelectedFontSize(int selectedFontSize) {
		this.selectedFontSize = selectedFontSize;
	}

	public void setGradientSelected(boolean gradientSelected) {
		this.gradientSelected = gradientSelected;
	}

	/**
	 * @return the gradientSelected
	 */
	public boolean isGradientSelected() {
		return gradientSelected;
	}

	/**
	 * @return the selectedColor
	 */
	public Color getSelectedColor() {
		return selectedColor;
	}

	/**
	 * @param selectedColor
	 *            the selectedColor to set
	 */
	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

	/**
	 * @return Returns the comicSelected.
	 */
	public boolean isComicSelected() {
		return comicSelected;
	}

	/**
	 * @param comicSelected
	 *            The comicSelected to set.
	 */
	public void setComicSelected(boolean comicSelected) {
		this.comicSelected = comicSelected;
	}

	/**
	 * @return the flowerSelected
	 */
	public boolean isFlowerSelected() {
		return flowerSelected;
	}

	/**
	 * @param flowerSelected
	 *            the flowerSelected to set
	 */
	public void setFlowerSelected(boolean flowerSelected) {
		this.flowerSelected = flowerSelected;
	}

	/**
	 * called by the archieve and time stamp actions
	 * 
	 */
	private void updateMessages() {
		TP_display.removeAll();
		TP_display.setText("");
		redrawing = true;
		File f = new File("archiveData/"
				+ Utilities.getDefaultInstance().getCurrentUser() + "$"
				+ userID);
		String files[] = null;
		if (f.isDirectory())
			files = f.list();
		else
			return;
		if (files == null || files.length < 1) {
			return;
		}
		Arrays.sort(files);
		if (showFullArchive) {

			// loads old archived messages
			for (int i = 0; i < files.length; i++) {
				ArrayList<Message> oldMessages = Utilities.getDefaultInstance()
						.getArchivedMessages(
								f.getAbsolutePath() + "/" + files[i]);
				if (oldMessages != null)
					for (int j = 0; j < oldMessages.size(); j++) {
						Message m = oldMessages.get(j);
						m.setShowTimeStamp(showTimeStamp);
						showMessage(m, m.getType() == MessageType.LOCAL ? true
								: false);
					}
			}
		} else if (showArchive) {

			// loads old archived messages
			ArrayList<Message> oldMessages = Utilities
					.getDefaultInstance()
					.getArchivedMessages(
							f.getAbsolutePath() + "/" + files[files.length - 1]);
			if (oldMessages != null)
				for (int i = 0; i < oldMessages.size(); i++) {
					Message m = oldMessages.get(i);
					m.setShowTimeStamp(showTimeStamp);
					showMessage(m, m.getType() == MessageType.LOCAL ? true
							: false);
				}
		}
		for (int i = 0; i < messages.size(); i++) {
			Message m = messages.get(i);
			m.setShowTimeStamp(showTimeStamp);
			showMessage(m, m.getType() == MessageType.LOCAL ? true : false);
		}
		redrawing = false;
		TP_display.setCaretPosition(displayBoxDoc.getLength());
	}

	/**
	 * @return Returns the messages.
	 */
	public ArrayList<Message> getMessages() {
		return messages;
	}

	/**
	 * @param messages
	 *            The messages to set.
	 */
	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
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

	public void updateUI() {
		try {
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(colorChooserPanel);
			SwingUtilities.updateComponentTreeUI(D_font);
			SwingUtilities.updateComponentTreeUI(toptions);
			System.out.println(UIManager.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the dancingSelected.
	 */
	public boolean isDancingSelected() {
		return dancingSelected;
	}

	/**
	 * @param dancingSelected
	 *            The dancingSelected to set.
	 */
	public void setDancingSelected(boolean dancingSelected) {
		this.dancingSelected = dancingSelected;
	}

	public boolean isBoldSelected() {
		return B_bold.isSelected();
	}

	public boolean isItalicSelected() {
		return B_italic.isSelected();
	}

}
