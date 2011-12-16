package controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class a multipurpose control which has various drawing options 1)
 * Gradient to draw the text gradient 2) Wave to draw the text using waves 3)
 * Comic to draw the text using comic 4) FADED to draw the text using faded 5)
 * ROSE to draw the text using rose image Also it has two other options which
 * can be applied altogether to the above options 1) 3D text draw draws text in
 * 3D format 2) 3D border draw 3D border around text
 * 
 * @author Kuldeep Saxena
 * 
 */
public class MultiPurposeControl extends JPanel {
	/**
	 * @serial serial key
	 */
	private static final long serialVersionUID = 2126263962272894381L;

	/**
	 * text to be drawn
	 */
	private String STR_text = "";
	/**
	 * image icon to be drawn
	 */
	private ImageIcon icon = null;

	/**
	 * default font for the text to be drawn
	 */
	private Font font = new Font("Times New Roman", Font.PLAIN, 20);

	/**
	 * is 3D drawing is on
	 */
	private boolean threeD = false;
	/**
	 * is 3D border is needs to be drawn
	 */
	private boolean threeDBox = false;

	/**
	 * default font color
	 */
	private Color foreColor = Color.black;
	/**
	 * y position updated while drawing in progress
	 */
	private int y;
	/**
	 * font metrics object associated with font and component
	 */
	private FontMetrics fm;
	/**
	 * size of the parent conatiner to determine the wrapping
	 */
	private Dimension containerSize;
	/**
	 * component width
	 */
	private int totalWidth = 0;
	/**
	 * component height
	 */
	private int totalHeight = 0;
	/**
	 * no of rows
	 */
	private int rows = 1;
	/**
	 * chars per line
	 */
	private int charsPerLine;

	/**
	 * Gradient drawing
	 */
	public static final int GRADIENT = 1000;
	/**
	 * wave drawing
	 */
	public static final int WAVE = 1001;
	/**
	 * faded drawing
	 */
	public static final int FADED = 1002;
	/**
	 * comic drawing
	 */
	public static final int COMIC = 1003;
	/**
	 * rose drawing
	 */
	public static final int FLOWER = 1004;

	/**
	 * dancing draw
	 */
	public static final int DANCING = 1005;

	/**
	 * current value takes any of the gradient, wave, faded, comic and flower
	 */
	private int currentValue = -1;

	boolean firstTime = true;
	private boolean underLine = false;
	private boolean strikeThrough = false;

	/**
	 * constructor with no arg calls other constructor with empty string
	 * 
	 */
	public MultiPurposeControl() {
		this("");
	}

	/**
	 * Constructor takes string to be drawn as input
	 * 
	 * @param STR_text
	 *            text to be drawn
	 */
	public MultiPurposeControl(String STR_text) {
		this.STR_text = STR_text;
		setBorder(null);
		setOpaque(false);
	}

	/**
	 * sets font
	 * 
	 * @param font
	 *            font for drawing
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * returns current font
	 * 
	 * @return font
	 */
	public Font getFont() {
		return this.font;
	}

	/**
	 * sets drawing text
	 * 
	 * @param text
	 *            text to be drawn
	 */
	public void setText(String text) {
		this.STR_text = text;
	}

	/**
	 * sets icon to be drawn
	 * 
	 * @param icon
	 */
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public void setUnderLine(boolean underLine) {
		this.underLine = underLine;
	}

	/**
	 * overridden function to done the actual drawing
	 */
	public void paint(Graphics g) {
		// takes metrics related to the font
		fm = getFontMetrics(font);
		// updates render quality
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		int iconWidth = 0;
		int iconHeight = 0;

		if (icon != null) {
			iconWidth = icon.getIconWidth();
			iconHeight = icon.getIconHeight();
		}

		totalWidth = fm.stringWidth(STR_text) + iconWidth + 17;
		totalHeight = fm.getHeight() + iconHeight + 10;
		y = calculateDrawY(fm);
		Dimension size = calculatePreferredSize();
		setPreferredSize(size);
		setSize(getPreferredSize());
		setMinimumSize(size);
		setMaximumSize(size);
		charsPerLine = STR_text.length() / rows;
		if ((STR_text.length()) % 2 == 1)
			charsPerLine++;
		// System.out.println(charsPerLine + " " + STR_text.length() + " " +
		// rows);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(renderHints);
		// darws icon if any
		if (icon != null) {
			g2.drawImage(icon.getImage(), 5, 5, null);
		}

		g2.setFont(font);

		// when 3D border is selected
		// / darws a border first
		if (threeDBox) {
			g2.setColor(getBackground().darker());
			g2.fillRoundRect(10, 10, getWidth(), getHeight(), 8, 8);
			g2.drawRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 8, 8);
			g2.setColor(getBackground());
			g2.fillRoundRect(1, 1, getWidth() - 10, getHeight() - 10, 8, 8);
		}

		// if value is to draw comic text
		if (currentValue == COMIC) {
			drawComic(g2);
			return;
		}
		// if value if to draw wave text
		if (currentValue == WAVE) {
			drawWave(g2);
			return;
		}

		// for other options
		// first checks 3D value and draws it
		if (threeD) {
			drawShadow(g2);
		}

		if (currentValue == DANCING) {
			return;
		}
		// for other value it update the paitn mode and draws the text
		if (currentValue == FLOWER || currentValue == FADED
				|| currentValue == GRADIENT) {
			setPaintMode(g2);
			drawDefault(g2, 4);
			return;
		}

		g2.setColor(foreColor);
		drawDefault(g2, 4);
	}

	/**
	 * draws the text to the gievn x position and if chars exceeds the width it
	 * wraps the text to next line
	 * 
	 * @param g2
	 *            graphics object used for drawing
	 * @param x
	 *            x point to be draw the text
	 */
	private void drawDefault(Graphics2D g2, int x) {

		int startIndex = 0;
		// System.out.println(rows + "" +charsPerLine);
		for (int i = 0; i < rows; i++) {
			String toDrawn = "";
			if ((startIndex + charsPerLine) < STR_text.length()) {
				toDrawn = STR_text.substring(startIndex, startIndex
						+ charsPerLine);
			} else {
				toDrawn = STR_text.substring(startIndex);
			}
			startIndex += charsPerLine;
			g2.drawString(toDrawn, x, y);
			if (strikeThrough) {
				// System.out.println("Strike through");
				// g2.drawLine(x, (fm.getHeight () * i) +y, x +
				// fm.stringWidth(toDrawn), (fm.getHeight () * i) +y);
			}
			y += fm.getHeight();
			if (underLine) {
				g2.drawLine(x, fm.getHeight() * (i + 1),
						x + fm.stringWidth(toDrawn), fm.getHeight() * (i + 1));
			}

		}
		y = calculateDrawY(fm);

	}

	/**
	 * called to draw the comic text
	 * 
	 * @param g2
	 *            graphics object used for drawing text
	 */
	private void drawComic(Graphics2D g2) {
		g2.setColor(foreColor);
		boolean lower = true;
		int x = 4;
		int drawLength = 0;
		// it runs char by char
		for (int i = 0; i < STR_text.length(); i++) {
			char c = STR_text.charAt(i);
			drawLength++;
			if (lower) {
				g2.shear(0.069, 0.00001);
			} else {
				g2.shear(-0.069, -0.00001);
			}
			lower = !lower;
			// darw 3D shadow first then it draws the char
			if (threeD) {
				g2.setColor(Color.gray);
				g2.drawString("" + c, x - 4, y);
				g2.setColor(Color.gray.brighter());
				g2.drawString("" + c, x - 3, y);
				g2.setColor(Color.gray.brighter().brighter());
				g2.drawString("" + c, x - 2, y);
				g2.drawString("" + c, x - 1, y);
				g2.setColor(foreColor);
			}
			g2.setColor(foreColor);
			g2.drawString("" + c, x, y);
			if (drawLength >= charsPerLine) {
				x = 4;
				y += fm.getHeight();
				drawLength = 0;
			} else {
				x = x + fm.stringWidth("" + c);
			}
		}
		return;
	}

	/**
	 * called to draw wave text
	 * 
	 * @param g2
	 *            graphics object used to draw text
	 */
	private void drawWave(Graphics2D g2) {

		int prevpos = 0;
		int pos = -1;
		int x = 4;
		int drawLength = 0;
		// darws text char by char
		for (int i = 0; i < STR_text.length(); i++) {
			char c = STR_text.charAt(i);
			drawLength++;
			if (pos == -1) {
				g2.rotate(-0.0008);
			}
			if (pos == 0) {
				g2.rotate(0.0008);
			}
			if (pos == 1) {
				g2.rotate(-0.0008);
			}
			// draws shadow first
			if (threeD) {
				g2.setColor(Color.gray);
				g2.drawString("" + c, x - 4, y);
				g2.setColor(Color.gray.brighter());
				g2.drawString("" + c, x - 3, y);
				g2.setColor(Color.gray.brighter().brighter());
				g2.drawString("" + c, x - 2, y);
				g2.drawString("" + c, x - 1, y);
				g2.setColor(foreColor);
			}
			g2.setColor(foreColor);
			g2.drawString("" + c, x, y);
			if (pos == 0 && prevpos == -1) {
				prevpos = pos;
				pos = 1;
			}
			if (i % 2 == 0) {
				pos = 1;
			}

			if (i % 3 == 0) {
				pos = 0;
			}
			if (drawLength >= charsPerLine) {
				x = 4;
				y += fm.getHeight();
				drawLength = 0;
			} else {
				x = x + fm.stringWidth("" + c);
			}
		}
		return;
	}

	/**
	 * sets the paint according to the selection
	 * 
	 * @param g2
	 *            graphics object which is been updated
	 */
	private void setPaintMode(Graphics2D g2) {
		// if gradient creates the gradientpaitn object
		if (currentValue == GRADIENT) {
			Color secondColor = foreColor.darker().darker();

			if (foreColor.getRed() < 30 && foreColor.getBlue() < 30
					&& foreColor.getGreen() < 30) {
				secondColor = Color.yellow.darker();// foreColor.brighter().brighter().brighter().brighter();
			}
			GradientPaint gp = new GradientPaint(0, 0, secondColor,
					getWidth() / 10, getHeight() / 10, foreColor, true);
			g2.setPaint(gp);
			return;
		}
		// if faded value is selected creating gradient paint with fading at
		// boundary
		if (currentValue == FADED) {
			GradientPaint gp = new GradientPaint(0, 0, foreColor,
					getWidth() - 5, getHeight(), Color.white.darker(), true);
			g2.setPaint(gp);
			return;
		}
		// when flower filling is used using rose.jpg to fill the text
		if (currentValue == FLOWER) {
			Image image = new ImageIcon("resources/images/rose.jpg").getImage();

			BufferedImage bufferedImage = new BufferedImage(
					image.getWidth(this), image.getHeight(this),
					BufferedImage.TYPE_INT_RGB);

			Graphics2D temp = bufferedImage.createGraphics();
			temp.drawImage(image, null, null);
			Rectangle2D rect = new Rectangle2D.Double(0, 0,
					fm.stringWidth("W"), getHeight());
			TexturePaint tp = new TexturePaint(bufferedImage, rect);
			g2.setPaint(tp);
			return;
		}
	}

	/**
	 * calculates the y according to the fontmetrics
	 * 
	 * @param fm
	 *            fontmetrics
	 * @return y
	 */
	private int calculateDrawY(FontMetrics fm) {
		int height = fm.getHeight();
		return height - 2;
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
	 * @return Returns the threeDBox.
	 */
	public boolean isThreeDBox() {
		return threeDBox;
	}

	/**
	 * @param threeDBox
	 *            The threeDBox to set.
	 */
	public void setThreeDBox(boolean threeDBox) {
		this.threeDBox = threeDBox;
	}

	/**
	 * draws shadow for the text
	 * 
	 * @param g2
	 *            graphics object used to draw the text
	 */
	private void drawShadow(Graphics2D g2) {
		g2.setColor(Color.gray);
		drawDefault(g2, 0);
		g2.setColor(Color.gray.brighter());
		drawDefault(g2, 1);
		g2.setColor(Color.gray.brighter().brighter());
		drawDefault(g2, 2);
		drawDefault(g2, 3);
	}

	/**
	 * @return Returns the foreColor.
	 */
	public Color getForeColor() {
		return foreColor;
	}

	/**
	 * @param foreColor
	 *            The foreColor to set.
	 */
	public void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}

	public void resetAttributes() {
		currentValue = -1;
	}

	/**
	 * @return Returns the containerSize.
	 */
	public Dimension getContainerSize() {
		return containerSize;
	}

	/**
	 * @param containerSize
	 *            The containerSize to set.
	 */
	public void setContainerSize(Dimension containerSize) {
		this.containerSize = containerSize;
	}

	/**
	 * calculates the preffred size according to the container size and the text
	 * size
	 * 
	 * @return
	 */
	private Dimension calculatePreferredSize() {
		Dimension d = null;
		if (containerSize == null || containerSize.width < 1
				|| containerSize.height < 1) {
			containerSize = new Dimension(totalWidth, totalHeight);
		}
		int width = containerSize.width;
		rows = 1;

		int tempValue = 0;
		while ((tempValue = tempValue + width) < totalWidth) {
			// System.out.println("Value " + tempValue + " " +totalWidth);
			rows++;
		}
		if (rows > 1)
			d = new Dimension(width, rows * totalHeight);
		else
			d = new Dimension(totalWidth, rows * totalHeight);
		return d;
	}

	/**
	 * @return Returns the currentValue.
	 */
	public int getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue
	 *            The currentValue to set.
	 */
	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
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
	 * @return Returns the underLine.
	 */
	public boolean isUnderLine() {
		return underLine;
	}

}
