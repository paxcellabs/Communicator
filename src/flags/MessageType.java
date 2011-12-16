package flags;

/**
 * This interface contains various message related flags
 * 
 * @author Kuldeep Saxena
 */

public interface MessageType {
	/**
	 * Display message in alert dialog box
	 */
	public static final int DIS_ALERT_MESSAGE = 3001;
	/**
	 * display message in information dialog box
	 */
	public static final int DIS_INFO_MESSAGE = 3002;
	/**
	 * display message in warning dialog box
	 */
	public static final int DIS_WARNING_MESSAGE = 3003;
	/**
	 * display message in message box flag
	 */
	public static final int DIS_TEXTBOX_MESSAGE = 3004;
	/**
	 * text message flag
	 */
	public static final int TEXT_MESSAGE = 3005;
	/**
	 * multimedia message flag
	 */
	public static final int MULTIMEDIA_MESSAGE = 3006;

	/**
	 * text option dim
	 */
	public static final int DIM = 3007;
	/**
	 * text option wave
	 */
	public static final int WAVE = 3008;
	/**
	 * text option comic
	 */
	public static final int COMIC = 3009;
	/**
	 * text option gradient
	 */
	public static final int GRADIENT = 3010;
	/**
	 * text option flower/rose
	 */
	public static final int FLOWER = 3011;

	/**
	 * text option flower/rose
	 */
	public static final int DANCING = 3012;

	/**
	 * text option with none selected
	 */
	public static final int NONE = 3013;
	/**
	 * message type local
	 */
	public final int LOCAL = 3014;
	/**
	 * message type remote
	 */
	public final int REMOTE = 3015;

}
