package utils;

import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

/**
 * provides integer document for the text field, only numeric input is allowed
 * to the related textfield
 * 
 * @author Kuldeep Saxena
 */

public class IntegersDocument extends PlainDocument

{

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -3037184560462920893L;

	/**
	 * overriden to handle text insertion in text component
	 */
	public void insertString(int int_offset, String str_text,
			AttributeSet AS_attributes) {
		boolean bool_digit = true;
		try {
			for (int int_loop = 0; int_loop < str_text.length(); int_loop++) {
				// if there any non numeric char display message bix
				if (!Character.isDigit(str_text.charAt(int_loop))) {
					bool_digit = false;
					break;
				}
			}
			if (bool_digit) {
				super.insertString(int_offset, str_text, AS_attributes);
			} else {
				JOptionPane.showMessageDialog(null,
						"This Field can contain only Numeric values ",
						"Warrning ", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
		}
	}

}
