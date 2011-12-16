package controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Provides a gui calendar control, from where user can select date <br>
 * 
 * @author Kuldeep Saxena
 * 
 */
public class CalendarControl extends JPanel implements ActionListener,
		ChangeListener {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 2421202562976650514L;

	boolean dispose = false;
	private boolean bool_yearFirst = false;
	final private int MIN_MONTH = 1;
	final private int MAX_MONTH = 12;
	final private int MIN_YEAR = 1900;
	final private int MAX_YEAR = 2500;
	private JTextField TF_date;
	private Calendar CL_date = null;
	private int int_month = 1, int_year;
	private int int_day = 1;

	/*
	 * GUI related variables
	 */
	final private Color C_buttonBack = new Color(230, 230, 210);
	final private Color C_buttonSelectedBack = new Color(30, 30, 10);
	final private Color C_buttonFore = new Color(0, 0, 0);
	final private Color C_buttonSelectedFore = new Color(255, 100, 100);
	private JTextField TF_month, TF_year;
	private JSpinner SPN_month, SPN_year;
	private JButton B_prev = null;

	private JPanel P_heading;
	private JPanel P_firstWeek;
	private JPanel P_secondWeek;
	private JPanel P_thirdWeek;
	private JPanel P_fourthWeek;
	private JPanel P_fifthWeek;
	private JPanel P_sixthWeek;
	private JPanel P_info;

	private JButton B_days[];

	private JLabel L_year, L_month;

	final private String STR_days[] = { "Su", "Mo", "Tu", "We", "Th", "Fr",
			"Sa", };

	final private String STR_months[] = { "January", "Feburary", "March",
			"April", "May", "June", "July", "Augest", "September", "October",
			"November", "December", };

	/**
	 * contructor requires textfield , day , month & year
	 */
	public CalendarControl(JTextField TF_date, int day, int month, int year) {
		this.int_month = month;
		this.int_year = year;
		this.TF_date = TF_date;
		this.int_day = day;
		init();
	}

	/**
	 * initializes components
	 * 
	 */
	private void init() {
		createComponents();
		setLayout();
		addComponents();
		setComponentsProperties();
		int daysInMonth = daysInMonth(int_month, int_year);
		createCalendar(daysInMonth);
		addListener();
		setPreferredSize(new Dimension(150, 120));
		setMaximumSize(new Dimension(150, 120));
		setMinimumSize(new Dimension(150, 120));
	}

	/**
	 * creates components
	 * 
	 */
	private void createComponents() {
		B_days = new JButton[42];
		CL_date = Calendar.getInstance();
		SPN_month = new JSpinner();
		SPN_year = new JSpinner();
		TF_month = new JTextField();
		TF_year = new JTextField();

		P_info = new JPanel();
		P_heading = new JPanel();
		P_firstWeek = new JPanel();
		P_secondWeek = new JPanel();
		P_thirdWeek = new JPanel();
		P_fourthWeek = new JPanel();
		P_fifthWeek = new JPanel();
		P_sixthWeek = new JPanel();

		L_month = new JLabel("" + STR_months[int_month], JLabel.CENTER);
		L_year = new JLabel("" + int_year);

		for (int i = 0; i < B_days.length; i++) {
			B_days[i] = new JButton();
		}
	}

	/**
	 * update layouts
	 * 
	 */
	private void setLayout() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		P_heading.setLayout(new GridLayout(0, 7, 0, 0));
		P_firstWeek.setLayout(new GridLayout(0, 7, 0, 0));
		P_secondWeek.setLayout(new GridLayout(0, 7, 0, 0));
		P_thirdWeek.setLayout(new GridLayout(0, 7, 0, 0));
		P_fourthWeek.setLayout(new GridLayout(0, 7, 0, 0));
		P_fifthWeek.setLayout(new GridLayout(0, 7, 0, 0));
		P_sixthWeek.setLayout(new GridLayout(0, 7, 0, 0));
	}

	/**
	 * laying component to container
	 * 
	 */
	private void addComponents() {
		// ---- adding heading
		P_info.add(L_month);
		P_info.add(SPN_month);
		P_info.add(L_year);
		P_info.add(SPN_year);
		add(P_info);
		add(P_heading);
		add(new JSeparator());
		for (int i = STR_days.length - 1; i >= 0; i--) {
			JLabel L_head = null;
			P_heading.add(L_head = new JLabel(STR_days[i]), JLabel.CENTER);
			L_head.setFont(new java.awt.Font("regular", Font.PLAIN, 9));
			L_head.setForeground(Color.red);
		}

		int start = 0;

		for (start = 0; start < 7; start++) {
			P_firstWeek.add(B_days[start]);
		}
		add(P_firstWeek);

		for (start = 7; start < 14; start++) {
			P_secondWeek.add(B_days[start]);
		}

		add(P_secondWeek);

		for (start = 14; start < 21; start++) {
			P_thirdWeek.add(B_days[start]);
		}

		add(P_thirdWeek);

		for (start = 21; start < 28; start++) {
			P_fourthWeek.add(B_days[start]);
		}

		add(P_fourthWeek);

		for (start = 28; start < 35; start++) {
			P_fifthWeek.add(B_days[start]);
		}

		add(P_fifthWeek);

		for (start = 35; start < 42; start++) {
			P_sixthWeek.add(B_days[start]);
		}

		add(P_sixthWeek);

	}

	/**
	 * updating component properties
	 * 
	 */
	private void setComponentsProperties() {
		SPN_month.setEditor(TF_month);
		SPN_year.setEditor(TF_year);
		TF_month.setVisible(false);
		TF_year.setVisible(false);
		SPN_month.setBorder(null);
		SPN_year.setBorder(null);
		SPN_month.setValue(new Integer(int_month));
		SPN_year.setValue(new Integer(int_year));
		P_heading.setBackground(new Color(210, 210, 210));
		P_info.setBackground(new Color(110, 110, 110));
		P_firstWeek.setBackground(new Color(230, 230, 210));
		P_secondWeek.setBackground(new Color(230, 230, 210));
		P_thirdWeek.setBackground(new Color(230, 230, 210));
		P_fourthWeek.setBackground(new Color(230, 230, 210));
		P_fifthWeek.setBackground(new Color(230, 230, 210));
		P_sixthWeek.setBackground(new Color(230, 230, 210));

		L_month.setForeground(Color.yellow);
		L_year.setForeground(Color.yellow);

		L_month.setFont(new java.awt.Font("regular", Font.PLAIN | Font.ITALIC,
				9));
		L_year.setFont(new java.awt.Font("regular", Font.PLAIN | Font.ITALIC, 9));
		setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED),
				new CompoundBorder(new BevelBorder(BevelBorder.LOWERED),
						new EmptyBorder(1, 1, 1, 1))));
		for (int i = 0; i < B_days.length; i++) {
			B_days[i].setFont(new java.awt.Font("regular", Font.PLAIN, 9));
			B_days[i].setBorder(null);
			B_days[i].setBackground(C_buttonBack);
			B_days[i].setForeground(C_buttonFore);
			B_days[i].setRequestFocusEnabled(false);
		}
	}

	/**
	 * adding listener for controls
	 * 
	 */
	private void addListener() {
		SPN_month.addChangeListener(this);
		SPN_year.addChangeListener(this);
	}

	/**
	 * populating objects according to selected month and year
	 * 
	 * @param noOfDays
	 *            noofdays in the month
	 */
	private void createCalendar(int noOfDays) {
		CL_date.set(int_year, int_month, 1);
		int startIndex = CL_date.get(Calendar.DAY_OF_WEEK) - 1;
		int currentDay = 0;
		int i = 0;
		for (int j = 0; j < startIndex; j++) {
			B_days[j].setVisible(false);
			B_days[j].setBackground(C_buttonBack);
			B_days[j].setForeground(C_buttonFore);
		}
		for (i = startIndex; i < (noOfDays + startIndex); i++) {
			B_days[i].setText(++currentDay + "");
			B_days[i].setBackground(C_buttonBack);
			B_days[i].setForeground(C_buttonFore);
			if (!bool_yearFirst) {
				B_days[i].setActionCommand(appendLeadingZero(currentDay) + "/"
						+ appendLeadingZero(int_month + 1) + "/" + int_year);
			} else {
				B_days[i].setActionCommand(int_year + "/"
						+ appendLeadingZero(int_month + 1) + "/"
						+ appendLeadingZero(currentDay));
				// System.out.println(int_year + "/" +
				// appendLeadingZero(int_month+1) + "/" +
				// appendLeadingZero(currentDay));
			}
			B_days[i].addActionListener(this);
			// System.out.println(B_days[i].getActionListeners());
			B_days[i].setToolTipText(B_days[i].getActionCommand());
			if (i == (int_day + startIndex - 1)) {
				B_prev = B_days[i];
				B_days[i].setBackground(C_buttonSelectedBack);
				B_days[i].setForeground(C_buttonSelectedFore);
			}
		}
		if (!bool_yearFirst)
			this.TF_date.setText(appendLeadingZero(int_day) + "/"
					+ appendLeadingZero(int_month + 1) + "/" + int_year);
		else
			this.TF_date.setText(int_year + "/"
					+ appendLeadingZero(int_month + 1) + "/"
					+ appendLeadingZero(int_day));
		for (int j = i; j < B_days.length; j++) {
			B_days[j].setBackground(C_buttonBack);
			B_days[j].setForeground(C_buttonFore);
			B_days[j].setVisible(false);
		}
		repaint();
	}

	/**
	 * adds leading 0 for digits < 9
	 * 
	 * @param no
	 *            number
	 * @return 0 appended string for no < 9
	 */
	private String appendLeadingZero(int no) {
		if (no < 10) {
			return "0" + no;
		} else
			return "" + no;
	}

	/**
	 * Action event handler
	 */
	public void actionPerformed(ActionEvent e) {
		if (B_prev != null) {
			B_prev.setBackground(C_buttonBack);
			B_prev.setForeground(C_buttonFore);
		}
		B_prev = (JButton) e.getSource();
		B_prev.setBackground(C_buttonSelectedBack);
		B_prev.setForeground(C_buttonSelectedFore);

		int_day = Integer.parseInt(((JButton) e.getSource()).getText());
		if (TF_date != null) {
			TF_date.setText("" + e.getActionCommand());
		}

		if (dispose) {
			setVisible(false);
			RoundPopupMenu pop = (RoundPopupMenu) getParent();
			pop.setForseVisible(false);
		}
	}

	/**
	 * handles spinner events
	 */
	public void stateChanged(ChangeEvent CE_change) {
		for (int i = 0; i < B_days.length; i++) {
			B_days[i].setText("");
			B_days[i].setVisible(true);
		}
		if (CE_change.getSource() == SPN_month) {
			int value = ((Integer) SPN_month.getValue()).intValue();
			if (value < MIN_MONTH) {
				value = MIN_MONTH;
				SPN_month.setValue(new Integer(value));
			}
			if (value >= MAX_MONTH) {
				value = MAX_MONTH;
				SPN_month.setValue(new Integer(value));
			}
			int_month = value - 1;
			L_month.setText(STR_months[int_month]);
		} else if (CE_change.getSource() == SPN_year) {
			int value = ((Integer) SPN_year.getValue()).intValue();
			if (value < MIN_YEAR) {
				value = MIN_YEAR;
				SPN_year.setValue("" + value);
			}
			if (value > MAX_YEAR) {
				value = MAX_YEAR;
				SPN_year.setValue("" + value);
			}

			int_year = value;
			L_year.setText("" + int_year);
		}
		createCalendar(daysInMonth(int_month, int_year));
	}

	/**
	 * This function calculates days in a month
	 * 
	 * @param int_month
	 *            month in which days has to be calculated
	 * @param int_year
	 *            year of the given month
	 * @return no of days in a given (month, year)
	 */
	private int daysInMonth(int int_month, int int_year) {
		int temp = int_month + 1;
		if (temp == 2) {
			if (int_year % 4 == 0) {
				if (int_year % 100 == 0) {
					if (int_year % 400 == 0)
						return 29;
					else
						return 28;
				} else {
					return 29;
				}
			} else {
				return 28;
			}
		}
		if (temp == 1 || temp == 3 || temp == 5 || temp == 7 || temp == 8
				|| temp == 10 || temp == 12) {
			return 31;
		} else {
			return 30;
		}
	}

	/**
	 * sets the flag whether on selection window should be disposed or not
	 * 
	 * @param dispose
	 *            dispose or not
	 */
	public void disposeOnSelect(boolean dispose) {
		this.dispose = dispose;
	}

}
