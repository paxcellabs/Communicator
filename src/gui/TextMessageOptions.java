package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controls.MultiPurposeControl;

/**
 * This class displays a dialog from which user can user various text related
 * options
 * 
 * @author Kuldeep Saxena
 * 
 */
public class TextMessageOptions extends JDialog implements ActionListener {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -2192605287572626535L;

	/*
	 * GUI related variables
	 */
	private JPanel P_north, P_center, P_south, P_preview, P_controls, P_note,
			P_buttons;
	private JComboBox CMB_textFillingOptions;
	private JCheckBox CB_3dText, CB_textBorder;
	private JButton B_ok, B_cancel, B_apply;
	private JLabel L_textOptionTitle;
	private JTextArea TA_note;
	// private ButtonGroup BG_group;

	/**
	 * reference of chat window for which this window is opened
	 */
	private ChatWindow cw;

	/**
	 * Multipurpose control to show the preview
	 */
	private MultiPurposeControl mpcontrol;

	/**
	 * Default Constructor
	 * 
	 * @param cw
	 *            ChatWindow object initializes components
	 */
	public TextMessageOptions(ChatWindow cw) {
		this.cw = cw;
		mpcontrol = new MultiPurposeControl("Your Text");
		mpcontrol.setFont(new Font(cw.getSelectedFontName(), Font.PLAIN, cw
				.getSelectedFontSize()));
		setTitle("Select option");
		initComponents();
		P_preview.add(mpcontrol);
		setSize(500, 360);
		mpcontrol.setContainerSize(getSize());
		setResizable(false);
	}

	/**
	 * initializes components
	 * 
	 */
	private void initComponents() {
		P_buttons = new JPanel();
		P_center = new JPanel();
		P_controls = new JPanel();
		P_north = new JPanel();
		P_note = new JPanel();
		P_preview = new JPanel();
		P_south = new JPanel();

		// BG_group = new ButtonGroup();

		CMB_textFillingOptions = new JComboBox();
		CMB_textFillingOptions.addItem("None");
		CMB_textFillingOptions.addItem("Gradient");
		CMB_textFillingOptions.addItem("Faded");
		CMB_textFillingOptions.addItem("Wave");
		CMB_textFillingOptions.addItem("Comic");
		CMB_textFillingOptions.addItem("Rose");
		// CMB_textFillingOptions.addItem("Dancing");

		CB_3dText = new JCheckBox("3D text");
		CB_textBorder = new JCheckBox("Text Border (3D)");

		B_apply = new JButton("Apply");
		B_ok = new JButton("OK");
		B_cancel = new JButton("Cancel");

		TA_note = new JTextArea(
				"Note: These options are not applicable in case of message sent after selecting the show in message box option");
		L_textOptionTitle = new JLabel("Text Message Options");

		P_controls.setLayout(new BoxLayout(P_controls, BoxLayout.Y_AXIS));

		P_controls.setPreferredSize(new Dimension(180, 150));
		P_preview.setPreferredSize(new Dimension(280, 200));

		P_preview.setLayout(new BorderLayout());

		getContentPane().add(P_north, BorderLayout.NORTH);
		getContentPane().add(P_center, BorderLayout.CENTER);
		getContentPane().add(P_south, BorderLayout.SOUTH);

		TA_note.setEditable(false);
		TA_note.setLineWrap(true);
		TA_note.setWrapStyleWord(true);
		TA_note.setPreferredSize(new Dimension(470, 40));
		P_south.setPreferredSize(new Dimension(500, 100));
		P_center.setPreferredSize(new Dimension(500, 210));

		P_south.add(P_note);
		P_south.add(P_buttons);

		P_buttons.add(B_ok);
		P_buttons.add(B_cancel);
		P_buttons.add(B_apply);
		P_note.add(TA_note);

		CB_3dText.setOpaque(false);
		CB_textBorder.setOpaque(false);

		P_preview.setOpaque(false);
		P_buttons.setOpaque(false);
		P_controls.setOpaque(false);
		P_north.add(L_textOptionTitle);
		P_center.add(P_controls);
		P_center.add(P_preview);

		P_controls.add(CB_3dText);
		P_controls.add(CB_textBorder);
		P_controls.add(CMB_textFillingOptions);

		CMB_textFillingOptions.setPreferredSize(new Dimension(100, 20));
		CMB_textFillingOptions.setMinimumSize(new Dimension(100, 20));
		CMB_textFillingOptions.setMaximumSize(new Dimension(100, 20));
		P_controls.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Options"));
		P_preview.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Preview"));

		CMB_textFillingOptions.addActionListener(this);
		CB_3dText.addActionListener(this);
		CB_textBorder.addActionListener(this);

		B_apply.addActionListener(this);
		B_ok.addActionListener(this);
		B_cancel.addActionListener(this);
	}

	/**
	 * action event handler
	 */
	public void actionPerformed(ActionEvent e) {
		mpcontrol.setForeColor(cw.getSelectedColor());
		boolean isBoldSelected = cw.isBoldSelected();
		boolean isItaliSelected = cw.isItalicSelected();
		int fontType = Font.PLAIN;
		if (isBoldSelected && isItaliSelected) {
			fontType = Font.ITALIC | Font.BOLD;
		} else if (isBoldSelected)
			fontType = Font.BOLD;
		else if (isItaliSelected)
			fontType = Font.ITALIC;
		mpcontrol.setFont(new Font(cw.getSelectedFontName(), fontType, cw
				.getSelectedFontSize()));
		// if Check box 3D selected
		if (e.getSource() == CB_3dText) {
			if (CB_3dText.isSelected()) {
				P_preview.removeAll();
				mpcontrol.setThreeD(true);
				P_preview.add(mpcontrol);
				P_preview.updateUI();
			} else {
				mpcontrol.setThreeD(false);
				P_preview.updateUI();
			}
			return;

		}
		// when check box text border selected
		if (e.getSource() == CB_textBorder) {
			if (CB_textBorder.isSelected()) {
				P_preview.removeAll();
				mpcontrol.setThreeDBox(true);
				P_preview.add(mpcontrol);
				P_preview.updateUI();
			} else {
				mpcontrol.setThreeDBox(false);
				// P_preview.removeAll();
				P_preview.updateUI();
			}
		}

		// if event from the combo box selection
		if (e.getSource() instanceof JComboBox) {
			P_preview.removeAll();
			mpcontrol.resetAttributes();
			String selectedValue = (String) CMB_textFillingOptions
					.getSelectedItem();

			if (selectedValue.equalsIgnoreCase("Gradient"))
				mpcontrol.setCurrentValue(MultiPurposeControl.GRADIENT);
			else if (selectedValue.equalsIgnoreCase("Faded"))
				mpcontrol.setCurrentValue(MultiPurposeControl.FADED);
			else if (selectedValue.equalsIgnoreCase("Wave"))
				mpcontrol.setCurrentValue(MultiPurposeControl.WAVE);
			else if (selectedValue.equalsIgnoreCase("Comic"))
				mpcontrol.setCurrentValue(MultiPurposeControl.COMIC);
			else if (selectedValue.equalsIgnoreCase("Rose"))
				mpcontrol.setCurrentValue(MultiPurposeControl.FLOWER);
			else if (selectedValue.equalsIgnoreCase("Dancing"))
				mpcontrol.setCurrentValue(MultiPurposeControl.DANCING);

			P_preview.add(mpcontrol);
			P_preview.updateUI();

		}

		// when ok or apply button is selected
		// applying the properties to chatwindow
		if (e.getSource() == B_ok || e.getSource() == B_apply) {
			String selectedValue = (String) CMB_textFillingOptions
					.getSelectedItem();
			cw.setDimSelected(false);
			cw.setWaveSelected(false);
			cw.setComicSelected(false);
			cw.setNoneSelected(false);
			cw.setGradientSelected(false);

			if (selectedValue.equalsIgnoreCase("Faded")) {
				cw.setDimSelected(true);
			} else if (selectedValue.equalsIgnoreCase("Wave")) {
				cw.setWaveSelected(true);
			} else if (selectedValue.equalsIgnoreCase("None")) {
				cw.setNoneSelected(true);
			} else if (selectedValue.equalsIgnoreCase("Comic")) {
				cw.setComicSelected(true);
			} else if (selectedValue.equalsIgnoreCase("Gradient")) {
				cw.setGradientSelected(true);
			} else if (selectedValue.equalsIgnoreCase("Rose")) {
				cw.setFlowerSelected(true);
			} else if (selectedValue.equalsIgnoreCase("Dancing")) {
				cw.setDancingSelected(true);
			}

			if (CB_3dText.isSelected()) {

				cw.setThreeDSelected(true);
			} else {
				cw.setThreeDSelected(false);
			}
			if (CB_textBorder.isSelected()) {
				cw.setThreeDBoxSelected(true);
			} else {
				cw.setThreeDBoxSelected(false);
			}

		}

		// when ok or cancel button is selected
		if (e.getSource() == B_ok || e.getSource() == B_cancel) {
			dispose();
		}

		// when apply button is selected
		if (e.getSource() == B_apply) {
			B_apply.setEnabled(false);
		} else
			B_apply.setEnabled(true);
	}
}
