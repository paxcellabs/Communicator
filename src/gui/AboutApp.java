package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * This class creates a dialog box & adds an image file in its background , this
 * class also uses the glass pane of the dialog box to add a transparent text
 * area above the image to display message in it.
 * 
 * @author Kuldeep Saxena
 */

public class AboutApp extends JDialog implements ActionListener {

	/*
	 * Variable declaration section
	 */

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -7653934805261000951L;

	/**
	 * glass pane of the window
	 */
	private JPanel P_glass;

	/**
	 * text area to hold text
	 */
	private JTextArea TA_text;

	/**
	 * timer object to be called to update message in text area
	 */
	private Timer TM_print;

	/**
	 * message to be printed in the text area
	 */
	private String str_message;

	/**
	 * current index within the message
	 */
	private int int_loop;

	/**
	 * Default Contructor
	 * 
	 * @param container
	 *            parent window
	 */
	public AboutApp(JFrame container) {
		super(container, " About Application ", true);

		str_message = "Its an internal chatting utility. "
				+ "\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "Developed by Kuldeep Saxena"
				+ "\n"
				+ "For any suggestion and feedback mail at \nkuldeep.saxena@paxcel.net"
				+ "\n\n" + "" + "Version 1.0";

		setSize(440, 400);
		setLocation(200, 200);
		setBackground(Color.white);

		// background image
		Image I_back = new ImageIcon("resources/images/paxcel.jpg").getImage();

		P_glass = (JPanel) getGlassPane();
		P_glass.setOpaque(false);
		getContentPane().setBackground(Color.white);
		TA_text = new JTextArea();
		TM_print = new Timer(20, null);
		TA_text.setForeground(Color.black);
		TA_text.setFont(new Font("dialog", Font.BOLD | Font.ITALIC, 15));
		TA_text.setEditable(false);
		TA_text.setOpaque(false);
		TM_print.addActionListener(this);
		P_glass.add(TA_text, BorderLayout.CENTER);
		getContentPane().add(new JLabel(new ImageIcon(I_back)),
				BorderLayout.CENTER);
		P_glass.setVisible(true);
		TM_print.start();
		setResizable(false);
		setVisible(true);
		TA_text.setPreferredSize(new Dimension(380, 380));
		P_glass.setPreferredSize(new Dimension(380, 380));
		TA_text.setMaximumSize(new Dimension(380, 380));
		TA_text.setWrapStyleWord(true);
		TA_text.setLineWrap(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * action event listener called by the timer after the specified interval
	 */
	public void actionPerformed(ActionEvent AE_action) {
		if (int_loop < str_message.length())
			TA_text.append(str_message.charAt(int_loop) + ""); // appending text
																// in the text
																// area

		else {
			TM_print.stop();
		}
		int_loop++;
	}

}
