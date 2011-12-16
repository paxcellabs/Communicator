package model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import beans.Message;
import beans.TextMessage;

/**
 * provides a table model for offline message
 * 
 * @author Kuldeep Saxena
 */

public class MessageTableModel extends DefaultTableModel {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -2082514784344050918L;

	/**
	 * vector containing messages
	 */
	Vector<Message> messages = null;

	/**
	 * default column count 3
	 */
	public int getColumnCount() {
		return 3;
	}

	/**
	 * returns row count equals to vector <message> size
	 */
	public int getRowCount() {
		if (messages == null)
			return 0;
		return messages.size();
	}

	/**
	 * sets value at specified row and column
	 * 
	 * @param value
	 *            new value
	 * @param row
	 *            row value
	 * @param col
	 *            column value
	 * 
	 */
	public void setValueAt(Object value, int row, int col) {
		// rowData[row][col] = value;
		messages.set(row, (Message) value);
		fireTableCellUpdated(row, col);
	}

	/**
	 * 
	 * returns value at specified row and column
	 * 
	 * @param row
	 *            specified row
	 * @param col
	 *            specified column
	 * @return object at row and column
	 */
	public Object getValueAt(int row, int col) {
		if (col == 0)
			return messages.get(row).getFromUser();
		if (col == 1)
			return messages.get(row).getSentTime();
		if (col == 2) {
			TextMessage tm = messages.get(row).getTextMessage();
			return tm.getTextMessage();
		}
		return null;
	}

	/**
	 * adds a new row to the model
	 * 
	 * @param message
	 *            message to be added
	 */
	public void addRow(Message message) {
		messages.add(message);
		super.setRowCount(messages.size());
	}

	/**
	 * Constructor
	 * 
	 * @param columns
	 *            column headers
	 * @param numrows
	 *            no of rows
	 */
	public MessageTableModel(Object[] columns, int numrows) {
		super(columns, numrows);
		messages = new Vector<Message>();
	}

	/**
	 * returns message at specified index
	 * 
	 * @param index
	 *            index location
	 * @return messages
	 */
	public Message getMessageAt(int index) {
		return messages.get(index);
	}

	/**
	 * always returns false to make non editable table
	 * 
	 * @param row
	 *            row index
	 * @param col
	 *            column index
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * removes all elements from model
	 * 
	 */
	public void clear() {
		messages.clear();
	}
}
