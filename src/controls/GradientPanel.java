package controls;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * 
 * This class overrides JPanel's paintComponent method to give gradient filling
 * 
 * @author kuldeep
 * 
 */
public class GradientPanel extends JPanel {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 3330613788248197169L;
	/**
	 * gradient filling color1
	 */
	private Color C_c1;
	/**
	 * gradient filling color2
	 */
	private Color C_c2;

	/**
	 * Gradient paint object
	 */
	private GradientPaint gp = null;

	/**
	 * Default constructor
	 * 
	 * @param C_c1
	 *            first color value
	 * @param C_c2
	 *            second color value
	 */
	public GradientPanel(Color C_c1, Color C_c2) {
		this.C_c1 = C_c1;
		this.C_c2 = C_c2;
	}

	/**
	 * sets colors value
	 * 
	 * @param c1
	 *            color1
	 * @param c2
	 *            color2
	 */
	public void setColors(Color c1, Color c2) {
		this.C_c1 = c1;
		this.C_c2 = c2;
	}

	/**
	 * paints components
	 */
	public void paintComponent(Graphics g) {

		Graphics2D g2D = (Graphics2D) g;
		if (gp == null) {
			gp = new GradientPaint(0, 0, C_c1, (float) (getWidth() / 5),
					getHeight() / 5, C_c2, true);
		}

		g2D.setPaint(gp);
		g2D.fillRect(0, 0, getWidth(), getHeight());
		paintChildren(g2D);
	}
}
