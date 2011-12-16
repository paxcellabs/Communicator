/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import model.ArchieveMessageModel;
import renderers.UserStatusTreeCellRenderer;
import utils.Utilities;
import beans.Message;
import beans.UserSettings;
import flags.MessageType;
import flags.UserStatus;

/**
 * @author kuldeep
 * 
 */
public class ArchiveViewer extends JFrame implements ComponentListener,
		TreeSelectionListener, ListSelectionListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6911716820729686122L;

	private static final ArchiveViewer archview = new ArchiveViewer();

	private JSplitPane SLP_pane;
	private JPanel P_right, P_rightTop, P_rightBottom, P_south, P_center;
	private JTree TR_users;
	private JTable TB_dates;
	private JTextPane TP_messages;
	private StyledDocument displayDoc = null;
	private Style displayStyle = null;
	private JButton B_close;
	private String msgDir = "archiveData";
	private DefaultMutableTreeNode root = null;
	private ArchieveMessageModel tableModel;

	public static ArchiveViewer getDefaultInstance() {
		return archview;
	}

	private ArchiveViewer() {
		super("Archive Viewer - Paxcel Communicator");
		try {
			// UIManager.setLookAndFeel(new GradientLookAndFeel());
		} catch (Exception e) {
		}

		initComponents();
	}

	public void initMessageList() {
		try {
			File f = new File(msgDir);
			// JOptionPane.showMessageDialog(null,f.getAbsolutePath());
			if (f != null) {
				if (!f.isDirectory()) {
					f.mkdirs();
					// System.err.println("Not a valid Dir");
					return;
				}
				Vector<String> directories = new Vector<String>();
				File dirs[] = f.listFiles();
				for (int i = 0; i < dirs.length; i++) {
					if (dirs[i].isDirectory()) {
						try {
							String with = dirs[i].getName().substring(
									dirs[i].getName().indexOf("$") + 1);
							directories.add(with);
						} catch (Exception e1) {

						}

					}
					// System.out.println(dirs[i].getName());
				}
				((DefaultTreeModel) TR_users.getModel())
						.setRoot(populateMessages(directories));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Not a valid dir");
		}
	}

	private void initComponents() {
		SLP_pane = new JSplitPane();
		P_right = new JPanel();
		P_rightBottom = new JPanel(new BorderLayout());
		P_rightTop = new JPanel(new BorderLayout());
		P_south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		P_center = new JPanel();
		TR_users = new JTree();
		// System.out.println(populateMessages(null));
		((DefaultTreeModel) TR_users.getModel())
				.setRoot(populateMessages(null));
		TR_users.setCellRenderer(new UserStatusTreeCellRenderer());
		TB_dates = new JTable();
		TP_messages = new JTextPane();
		B_close = new JButton("Close");
		TP_messages.setEditable(false);
		TR_users.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		// TR_users.setOpaque(false);
		// TR_users.setBackground(Color.white);
		TR_users.setRowHeight(22);

		tableModel = new ArchieveMessageModel(new Object[] { "Date" }, 0);
		tableModel.setColumnCount(1);
		TB_dates.setModel(tableModel);
		TB_dates.getSelectionModel().addListSelectionListener(this);
		setSize(700, 500);
		P_center.setPreferredSize(getSize());
		// P_r.setPreferredSize(getSize());
		TR_users.setPreferredSize(new Dimension(220, 500));
		P_right.setLayout(new BoxLayout(P_right, BoxLayout.Y_AXIS));
		SLP_pane.setLeftComponent(new JScrollPane(TR_users));
		SLP_pane.setRightComponent(P_right);
		P_right.add(P_rightTop);
		P_right.add(P_rightBottom);

		P_rightTop.setPreferredSize(new Dimension(470, 100));
		P_rightTop.setMinimumSize(new Dimension(470, 100));
		P_rightTop.setMaximumSize(new Dimension(470, 100));
		P_rightBottom.setPreferredSize(new Dimension(470, 300));
		P_center.add(SLP_pane);
		getContentPane().add(P_center, BorderLayout.CENTER);
		getContentPane().add(P_south, BorderLayout.SOUTH);
		P_south.add(B_close);
		P_rightTop.add(new JScrollPane(TB_dates), BorderLayout.CENTER);
		P_rightBottom.add(new JScrollPane(TP_messages));
		addComponentListener(this);
		TR_users.addTreeSelectionListener(this);

		displayDoc = TP_messages.getStyledDocument();
		displayStyle = displayDoc.addStyle("MyStyle", null);
		B_close.addActionListener(this);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		// componentResized(new ComponentEvent(this,0));
	}

	public static void main(String args[]) {

		ArchiveViewer.getDefaultInstance().setMsgDir("archiveData");
		ArchiveViewer.getDefaultInstance().setVisible(true);
	}

	public void componentResized(ComponentEvent e) {
		P_center.setPreferredSize(new Dimension(getWidth(), getHeight() - 100));
		P_right.setPreferredSize(P_center.getPreferredSize());
		SLP_pane.setPreferredSize(P_center.getPreferredSize());
		SLP_pane.updateUI();
		P_rightTop.setPreferredSize(new Dimension(
				P_right.getPreferredSize().width, 100));
		P_rightTop.setMaximumSize(P_rightTop.getPreferredSize());
		// P_rightTop.setMinimumSize(P_rightTop.getPreferredSize());
		P_center.updateUI();

	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	private DefaultMutableTreeNode populateMessages(Vector<String> messages) {
		if (root != null) {
			// removes all elements
			((DefaultTreeModel) TR_users.getModel()).setRoot(null);
		}
		root = new DefaultMutableTreeNode(new UserSettings("Messages",
				"Messages", true, UserStatus.UNKNOWN));
		HashMap<String, UserSettings> users = Utilities.getDefaultInstance()
				.getUsersInGroup("all");
		// ((DefaultTreeModel)TR_users.getModel()).setRoot(null);
		root.setParent(null);
		if (messages != null) {
			for (int i = 0; i < messages.size(); i++) {
				if (users != null) {
					if (users.containsKey(messages.get(i)))
						root.add(new DefaultMutableTreeNode(users.get(messages
								.get(i))));
					else
						root.add(new DefaultMutableTreeNode(new UserSettings(
								messages.get(i), messages.get(i), false,
								UserStatus.UNKNOWN)));
				} else
					root.add(new DefaultMutableTreeNode(new UserSettings(
							messages.get(i), messages.get(i), false,
							UserStatus.UNKNOWN)));
			}
		}
		return root;
	}

	/**
	 * @return Returns the msgDir.
	 */
	public String getMsgDir() {
		return msgDir;
	}

	/**
	 * @param msgDir
	 *            The msgDir to set.
	 */
	public void setMsgDir(String msgDir) {
		this.msgDir = msgDir;
		initMessageList();
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (e.getSource() == TR_users) {
			if (TR_users.getLastSelectedPathComponent() == null)
				return;
			TP_messages.setText("");
			TB_dates.getSelectionModel().clearSelection();
			tableModel.clear();
			TB_dates.updateUI();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) TR_users
					.getLastSelectedPathComponent();
			UserSettings us = (UserSettings) node.getUserObject();
			String selectedUser = Utilities.getDefaultInstance()
					.getCurrentUser() + "$" + us.getUserID();
			File f = new File("archiveData/" + selectedUser);

			File[] fileNames = f.listFiles();
			if (fileNames != null)
				for (int i = 0; i < fileNames.length; i++)
					tableModel.addRow(fileNames[i]);
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		if (TB_dates.getSelectedRow() == -1)
			return;
		File f = tableModel.getMessageAt(TB_dates.getSelectedRow());
		TP_messages.setText("");
		ArrayList<Message> messages = Utilities.getDefaultInstance()
				.getArchivedMessages(f.toString());
		if (messages != null)
			for (int i = 0; i < messages.size(); i++) {
				Message m = messages.get(i);
				m.setShowTimeStamp(true);
				Utilities.getDefaultInstance().showMessage(
						messages.get(i),
						this,
						TP_messages,
						displayDoc,
						displayStyle,
						messages.get(i).getType() == MessageType.LOCAL ? false
								: true);
			}

	}

	public void clearContents() {
		tableModel.clear();
		TP_messages.setText("");
		((DefaultTreeModel) TR_users.getModel()).setRoot(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == B_close) {
			setVisible(false);
		}
	}
}
