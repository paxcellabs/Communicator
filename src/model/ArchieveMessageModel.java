/**
 * 
 */
package model;

import java.io.File;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * provides a table model for offline message
 * 
 * @author Kuldeep Saxena
 */

public class ArchieveMessageModel extends DefaultTableModel {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = -2082514784344050918L;

	/**
	 * date value
	 */
	Vector<File> messages = null;

	/**
	 * default column count 3
	 */
	public int getColumnCount() {
		return 1;
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
		messages.set(row, (File) value);
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
		String fileName = messages.get(row).getName();
		return fileName.substring(6, 8) + " - " + fileName.substring(4, 6)
				+ " - " + fileName.substring(0, 4);
	}

	/**
	 * adds a new row to the model
	 * 
	 * @param date
	 *            date to be added
	 */
	public void addRow(File f) {
		messages.add(f);
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
	public ArchieveMessageModel(Object[] columns, int numrows) {
		super(columns, numrows);
		messages = new Vector<File>();
	}

	/**
	 * returns date value specified index
	 * 
	 * @param index
	 *            index location
	 * @return messages
	 */
	public File getMessageAt(int index) {
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
