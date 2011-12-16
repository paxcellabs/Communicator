package utils;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import controls.RoundPopupMenu;

/**
 * This class handles the taskbar messages
 * 
 * @author Kuldeep Saxena
 * 
 */
public class PopupMessageHandler extends Thread {

	/**
	 * Default accessory object
	 */
	private static final PopupMessageHandler handler = new PopupMessageHandler();

	/**
	 * Message queue
	 */
	private ArrayList<String> messages = new ArrayList<String>();

	/**
	 * popup menu to display message
	 */
	private RoundPopupMenu pop = null;

	/**
	 * text box containing the text message
	 */
	private JTextArea TA_message = null;

	/**
	 * panel containing the text area
	 */
	private JPanel mainPanel = null;

	/**
	 * Default constructor initializes components starts message display thread
	 */
	private PopupMessageHandler() {
		initComponents();
		start();
	}

	/**
	 * initializes components
	 * 
	 */
	private void initComponents() {
		pop = new RoundPopupMenu();
		mainPanel = new JPanel();
		pop.add(mainPanel);
		TA_message = new JTextArea();
		mainPanel.setOpaque(false);
		mainPanel.add(TA_message);
		TA_message.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC,
				14));
		TA_message.setBorder(new EmptyBorder(3, 3, 3, 3));
		TA_message.setLineWrap(true);
		TA_message.setWrapStyleWord(true);
		// mainPanel.setPreferredSize(new Dimension(150, 50));
		// TA_message.setPreferredSize(new Dimension(150, 50));
		pop.setPreferredSize(new Dimension(150, 50));
		TA_message.setEditable(false);
		TA_message.setOpaque(false);
		TA_message.setOpaque(false);
	}

	/**
	 * Accessory method to get the class object
	 * 
	 * @return current class's object
	 */
	public static PopupMessageHandler getDefaultInstance() {
		return handler;
	}

	/**
	 * called to add new message to queue
	 * 
	 * @param message
	 *            message to be added
	 */
	public void addNewMessage(String message) {
		messages.add(message);
		/**
		 * notifies thread waiting to show the message
		 */
		synchronized (this) {
			notifyAll();
		}
	}

	/**
	 * runs continuously to display messages
	 */
	public void run() {

		try {
			while (true) {
				for (int i = 0; i < messages.size(); i++) {
					TA_message.setText(messages.get(i));
					// pop.setSize(TA_message.getPreferredSize());
					pop.setLocation(
							Utilities.getDefaultInstance().getTaskBarBounds().width
									- mainPanel.getPreferredSize().width,
							Utilities.getDefaultInstance().getTaskBarBounds().y
									- mainPanel.getPreferredSize().height);
					pop.setVisible(true);
					pop.updateUI();
					Thread.sleep(5000);
				}
				messages.clear();
				pop.setForseVisible(false);
				synchronized (this) {
					wait();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
