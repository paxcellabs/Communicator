package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import model.MessageTableModel;
import utils.Utilities;
import beans.Message;

import communicator.ClientSideCommunicator;

/**
 * This class provides a GUI for displaying offline messages
 * 
 * @author Kuldeep Saxena
 * 
 */
public class OffLineMsgWindow extends JDialog implements ActionListener,
		ListSelectionListener, WindowListener {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 7050235989914128233L;

	/**
	 * Default object
	 */
	private static final OffLineMsgWindow offmsg = new OffLineMsgWindow();

	/*
	 * GUI related variables
	 */
	private JPanel P_north, P_center, P_south;
	private JTable TB_messages;
	private JTextPane TP_messages;
	private JButton B_close, B_reply;
	private StyledDocument displayBoxDoc;
	private Style displayBoxStyle;
	private JScrollPane SP_messages, SP_display;

	/**
	 * table model to used to store Message in the model
	 */
	private MessageTableModel tableModel = null;

	/**
	 * offline messages
	 */
	private Message[] messages;

	/**
	 * current selected message
	 */
	private Message selectedMessage;

	/**
	 * Default Constructor initializes components
	 */
	private OffLineMsgWindow() {
		initComponents();
	}

	/**
	 * adds offline message to window initializes message list
	 * 
	 * @param messages
	 *            new messages
	 */
	public void addOffLineMessages(Message[] messages) {
		this.messages = messages;
		initList();
	}

	/**
	 * Accessory method for the current object
	 * 
	 * @return current class object
	 */
	public static OffLineMsgWindow getDefaultInstance() {
		return offmsg;
	}

	/**
	 * initializes message list
	 * 
	 */
	private void initList() {
		tableModel.clear();
		TP_messages.setText("");
		for (int i = 0; i < messages.length; i++) {
			tableModel.addRow(messages[i]);
		}
		// default selects the first message
		selectedMessage = messages[0];
		TB_messages.getSelectionModel().clearSelection();
		TB_messages.updateUI();
	}

	/**
	 * initializes components
	 * 
	 */
	private void initComponents() {
		tableModel = new MessageTableModel(
				new Object[] { "From", "Time", "Msg" }, 0);
		TB_messages = new JTable();
		tableModel.setColumnCount(3);
		TB_messages.setModel(tableModel);
		P_north = new JPanel(new FlowLayout(FlowLayout.LEFT));
		P_center = new JPanel(new FlowLayout(FlowLayout.LEFT));
		P_south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		B_close = new JButton("Close");
		B_reply = new JButton("Reply");

		TP_messages = new JTextPane();

		displayBoxDoc = TP_messages.getStyledDocument();
		displayBoxStyle = displayBoxDoc.addStyle("MyStyle", null);

		P_north.add(SP_messages = new JScrollPane(TB_messages));
		P_center.add(SP_display = new JScrollPane(TP_messages));

		TP_messages.setBorder(new CompoundBorder(new BevelBorder(
				BevelBorder.LOWERED), new EmptyBorder(3, 3, 3, 3)));
		P_south.add(B_close);
		P_south.add(B_reply);
		TB_messages.setPreferredScrollableViewportSize(new Dimension(380, 190));
		setTitle("Offline messages");
		P_south.setPreferredSize(new Dimension(400, 50));
		getContentPane().add(SP_messages, BorderLayout.NORTH);
		getContentPane().add(SP_display, BorderLayout.CENTER);
		getContentPane().add(P_south, BorderLayout.SOUTH);
		B_close.addActionListener(this);
		B_reply.addActionListener(this);
		TB_messages.getSelectionModel().addListSelectionListener(this);
		addWindowListener(this);
		setSize(470, 350);
		TP_messages.setEditable(false);
		TB_messages.getColumnModel().getColumn(0).setPreferredWidth(70);
		TB_messages.getColumnModel().getColumn(1).setPreferredWidth(70);
		TB_messages.getColumnModel().getColumn(2).setPreferredWidth(160);
		TB_messages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * action event handler
	 */
	public void actionPerformed(ActionEvent e) {
		// when close button is clicked
		if (e.getSource() == B_close) {
			// calls to delete offline messages
			ClientSideCommunicator.getDefaultInstance().deleteOfflineMessages(
					Utilities.getDefaultInstance().getCurrentUser());
			messages = null;
			setVisible(false);
			return;
		}
		// when reply button is clicked
		if (e.getSource() == B_reply) {
			// creates chat window and adds it to the opened window list

			ChatWindow cw = Utilities.getDefaultInstance().getOpenedWindow(
					selectedMessage.getFromUser());
			if (cw == null) {
				cw = new ChatWindow(selectedMessage.getFromUser(), null);
				Utilities.getDefaultInstance().addOpenedWindow(
						selectedMessage.getFromUser(), cw);
			}
			cw.showMessage(selectedMessage, false);
			cw.setVisible(true);
			return;
		}
	}

	/**
	 * handles message selection event
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty()) {
			int selectedRow = lsm.getMinSelectionIndex();
			TP_messages.setText("");
			Message msg = tableModel.getMessageAt(selectedRow);
			// shows message in the text box
			Utilities.getDefaultInstance().showMessage(msg, this, TP_messages,
					displayBoxDoc, displayBoxStyle, false);
			selectedMessage = msg;
		}
	}

	/**
	 * overridden to remove default selected message
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			selectedMessage = null;
			TB_messages.updateUI();
		}
	}

	/**
	 * window open event handler does nothing here
	 */
	public void windowOpened(WindowEvent e) {

	}

	/**
	 * window closing event handler creates the false close button event and
	 * makes window hidden
	 */
	public void windowClosing(WindowEvent e) {
		actionPerformed(new ActionEvent(B_close, ActionEvent.ACTION_PERFORMED,
				"Close"));
		setVisible(false);
	}

	/**
	 * window closed event handler does nothing here
	 */
	public void windowClosed(WindowEvent e) {

	}

	/**
	 * window iconified event handler does nothing here
	 */
	public void windowIconified(WindowEvent e) {

	}

	/**
	 * window de iconified event handler does nothing here
	 */
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * window activated event handler does nothing here
	 */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * window deactivated event handler does nothing here
	 */
	public void windowDeactivated(WindowEvent e) {

	}
}
