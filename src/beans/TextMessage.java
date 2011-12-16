package beans;

import java.awt.Color;
import java.io.Serializable;

/**
 * This class provides a text message presentation
 * 
 * @author Kuldeep Saxena
 * 
 */
public class TextMessage implements Serializable {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -742240856824306368L;

	/**
	 * message text content
	 */
	private String textMessage = "";

	/**
	 * bold attribute for the text
	 */
	private boolean bold = false;

	/**
	 * italic attribute for the text
	 */
	private boolean italic = false;

	/**
	 * underline attribute for the text
	 */
	private boolean underline = false;

	/**
	 * strike through attribute for the text
	 */
	private boolean strikeThrough = false;

	/**
	 * text color for text default black
	 */
	private Color textColor = Color.black;

	/**
	 * font name default Times New Roman
	 */
	private String fontName = "Times New Roman";

	/**
	 * font size default 12
	 */
	private int fontSize = 12;

	/**
	 * @return Returns the bold.
	 */
	public boolean isBold() {
		return bold;
	}

	/**
	 * @param bold
	 *            The bold to set.
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	/**
	 * @return Returns the fontName.
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * @param fontName
	 *            The fontName to set.
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	/**
	 * @return Returns the fontSize.
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * @param fontSize
	 *            The fontSize to set.
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * @return Returns the italic.
	 */
	public boolean isItalic() {
		return italic;
	}

	/**
	 * @param italic
	 *            The italic to set.
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	/**
	 * @return Returns the strikeThrough.
	 */
	public boolean isStrikeThrough() {
		return strikeThrough;
	}

	/**
	 * @param strikeThrough
	 *            The strikeThrough to set.
	 */
	public void setStrikeThrough(boolean strikeThrough) {
		this.strikeThrough = strikeThrough;
	}

	/**
	 * @return Returns the textColor.
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor
	 *            The textColor to set.
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	/**
	 * @return Returns the textMessage.
	 */
	public String getTextMessage() {
		return textMessage;
	}

	/**
	 * @param textMessage
	 *            The textMessage to set.
	 */
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	/**
	 * @return Returns the underline.
	 */
	public boolean isUnderline() {
		return underline;
	}

	/**
	 * @param underline
	 *            The underline to set.
	 */
	public void setUnderline(boolean underline) {
		this.underline = underline;
	}
}
