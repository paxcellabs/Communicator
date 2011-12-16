package beans;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is a normal bean which is used to contain message contents
 * 
 * @author Kuldeep Saxena
 */

public class Message implements Serializable {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -1012676572510453853L;

	/*
	 * variable declaration section
	 */

	/**
	 * id from which message been sent
	 */
	private String fromUser;

	/**
	 * ids to whom message been sent
	 */
	private String[] toUsers;

	/**
	 * type of message i.e. text or multimedia
	 */
	private int messageType;

	/**
	 * text message
	 */
	private TextMessage textMessage;

	/**
	 * multimedia message
	 */
	private byte[] multiMediaMessage;

	/**
	 * display type i.e. display on message box or in the chat box
	 */
	private int displayType;

	/**
	 * contains special attribute like three D
	 */
	private int specialAttribute;

	/**
	 * file name for multimedia message
	 */
	private String fileName;

	/**
	 * three D attribute
	 */
	private boolean threeD;

	/**
	 * three D box border
	 */
	private boolean threeDBorder;

	/**
	 * message sent time, set at server
	 */
	private Date sentTime;

	/**
	 * show time stamp flag
	 */

	private boolean showTimeStamp = true;

	/**
	 * message type local or remote
	 */
	private int type;

	/**
	 * 
	 * @return the displayType
	 */
	public int getDisplayType() {
		return displayType;
	}

	/**
	 * @param displayType
	 *            the displayType to set
	 */
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fromUser
	 */
	public String getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser
	 *            the fromUser to set
	 */
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @return the messageType
	 */
	public int getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the multiMediaMessage
	 */
	public byte[] getMultiMediaMessage() {
		return multiMediaMessage;
	}

	/**
	 * @param multiMediaMessage
	 *            the multiMediaMessage to set
	 */
	public void setMultiMediaMessage(byte[] multiMediaMessage) {
		this.multiMediaMessage = multiMediaMessage;
	}

	/**
	 * @return the textMessage
	 */
	public TextMessage getTextMessage() {
		return textMessage;
	}

	/**
	 * @param textMessage
	 *            the textMessage to set
	 */
	public void setTextMessage(TextMessage textMessage) {
		this.textMessage = textMessage;
	}

	/**
	 * @return the toUsers
	 */
	public String[] getToUsers() {
		return toUsers;
	}

	/**
	 * @param toUsers
	 *            the toUsers to set
	 */
	public void setToUsers(String[] toUsers) {
		this.toUsers = toUsers;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            type of message
	 */
	public void setType(int type) {
		this.type = type;
	}

	public int getSpecialAttribute() {
		return specialAttribute;
	}

	public void setSpecialAttribute(int specialAttribute) {
		this.specialAttribute = specialAttribute;
	}

	/**
	 * @return Returns the threeD.
	 */
	public boolean isThreeD() {
		return threeD;
	}

	/**
	 * @param threeD
	 *            The threeD to set.
	 */
	public void setThreeD(boolean threeD) {
		this.threeD = threeD;
	}

	/**
	 * @return Returns the threeDBorder.
	 */
	public boolean isThreeDBorder() {
		return threeDBorder;
	}

	/**
	 * @param threeDBorder
	 *            The threeDBorder to set.
	 */
	public void setThreeDBorder(boolean threeDBorder) {
		this.threeDBorder = threeDBorder;
	}

	/**
	 * @return the sentTime
	 */
	public Date getSentTime() {
		return sentTime;
	}

	/**
	 * @param sentTime
	 *            the sentTime to set
	 */
	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	/**
	 * @return Returns the showTimeStamp.
	 */
	public boolean isShowTimeStamp() {
		return showTimeStamp;
	}

	/**
	 * @param showTimeStamp
	 *            The showTimeStamp to set.
	 */
	public void setShowTimeStamp(boolean showTimeStamp) {
		this.showTimeStamp = showTimeStamp;
	}
}
