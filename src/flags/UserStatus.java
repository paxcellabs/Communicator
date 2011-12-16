package flags;

/**
 * This interface contains user's status related flags
 */

public interface UserStatus {

	/**
	 * user online status
	 */
	public final static int USER_ONLINE = 100;
	/**
	 * user offline status
	 */
	public final static int USER_OFFLINE = 101;
	/**
	 * user busy status
	 */
	public final static int USER_BUSY = 102;
	/**
	 * unknow status
	 */
	public final static int UNKNOWN = -1;
}
