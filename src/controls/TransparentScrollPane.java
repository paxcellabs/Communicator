package controls;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 * This class provides transparent scrollpane (scrollpane without border
 * 
 * @author kuldeep
 * 
 */
public class TransparentScrollPane extends JScrollPane {
	/**
	 * @serial serail key
	 */
	private static final long serialVersionUID = -8815161318623597329L;

	/**
	 * consttructor
	 * 
	 * @param P_data
	 *            adds the component to the scroll pane
	 */
	public TransparentScrollPane(JComponent P_data) {
		super(P_data);
		setViewportOpaque(false);
	}

	/**
	 * constructor
	 * 
	 */
	public TransparentScrollPane() {
		super();

	}

	/**
	 * sets the opaque value of the viewport
	 * 
	 * @param opa
	 *            opaque or not
	 */
	private void setViewportOpaque(boolean opa) {
		getViewport().setOpaque(opa);
	}

	/**
	 * constructor
	 * 
	 * @param P_data
	 *            component to be added
	 * @param vertical
	 *            vertical scroll bar policy
	 * @param horizontal
	 *            horizontal scroll bar policy
	 */
	public TransparentScrollPane(JComponent P_data, int vertical, int horizontal) {
		super(P_data, vertical, horizontal);
		setViewportOpaque(false);
	}

	/**
	 * overridden always returns false to make it transparent component
	 * 
	 * @return false
	 */
	public boolean isOpaque() {
		return false;
	}
}
