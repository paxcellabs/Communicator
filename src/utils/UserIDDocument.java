package utils;

import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

/**
 * provides a document for the text field which does not allows \/:*?"<>|$ in
 * related textfield used in userid textfield
 */

public class UserIDDocument extends PlainDocument

{

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -3037184560462920893L;

	String pattern = "\\/:*?\"<>|$";

	/**
	 * overriden to handle text insertion in text component
	 */
	public void insertString(int int_offset, String str_text,
			AttributeSet AS_attributes) {
		boolean right_pattern = true;
		try {
			for (int int_loop = 0; int_loop < str_text.length(); int_loop++) {

				// if there any char in the pattern displays message box
				if (pattern.indexOf(str_text.charAt(int_loop)) != -1) {
					right_pattern = false;
					break;
				}
			}
			if (right_pattern) {
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
