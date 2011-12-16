package controls;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import javax.swing.JPopupMenu;

/**
 * 
 * This class provides enhance popup menu, which is rounded in shape and has
 * gradient filling
 * 
 * @author kuldeep
 * 
 */
public class RoundPopupMenu extends JPopupMenu {

	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 6951662897022432978L;
	/**
	 * color used for making paint object
	 */
	private Color c = null;
	/**
	 * gradient paint object
	 */
	private GradientPaint gp = null;

	/**
	 * Constructor with the color value
	 * 
	 * @param c
	 *            gradient paint color
	 */
	public RoundPopupMenu(Color c) {
		setOpaque(false);
		setBorder(null);
		this.c = c;
	}

	/**
	 * default constructor sets default value of color
	 * 
	 */
	public RoundPopupMenu() {
		this(new Color(238, 228, 218));
	}

	/**
	 * sets it as transparent
	 */
	public boolean isOpaque() {
		return false;
	}

	/**
	 * overridden to draw the component
	 * 
	 * @param g
	 *            graphics object
	 */
	public void paintComponent(java.awt.Graphics g) {
		gp = new GradientPaint(20, 20, c.brighter(), getWidth() / 2,
				getHeight() / 2, c.darker(), true);
		Graphics2D g2 = (Graphics2D) g;
		if (gp != null) {
			g2.setColor(c);
			g2.setPaint(gp);
			g2.drawRoundRect(0, 0, getWidth() + 1, getHeight() - 2, 8, 8);
			g2.setColor(c.darker());
			g2.drawRoundRect(0, 1, getWidth() + 2, getHeight() - 3, 7, 7);
			g2.setColor(c.darker().darker());
			g2.drawRoundRect(0, 2, getWidth() + 4, getHeight() - 4, 6, 6);
			g2.setPaint(gp);
			g2.fillRoundRect(0, 3, getWidth() + 6, getHeight() - 6, 5, 5);
		}
		paintChildren(g);
	}

	/**
	 * used to make the popup visible/hidden
	 * 
	 * @param visible
	 *            make it visible/hidde
	 */
	public void setForseVisible(boolean visible) {
		super.setVisible(visible);
	}

	/**
	 * overridden to make the popup visible all the time
	 * 
	 * @param visible
	 *            of no use here overridden always calls the super function with
	 *            true
	 */
	public void setVisible(boolean visible) {
		super.setVisible(true);
	}
}
