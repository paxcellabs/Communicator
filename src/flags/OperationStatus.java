package flags;

/**
 * this interface contains various operation related status flags
 * 
 * @author Kuldeep Saxena
 */

public interface OperationStatus {
	/**
	 * Operation sucessful status
	 */
	public static final int OPERATION_SUCESSFUL = 1001;
	/**
	 * Operation failure status
	 */
	public static final int OPERATION_FAILURE = 1002;
	/**
	 * Invalid password status
	 */
	public static final int INVALID_PASSWORD = 1003;
	/**
	 * invalid id status
	 */
	public static final int INVALID_ID = 1004;
	/**
	 * user already exists status
	 */
	public static final int USER_ALREADY_EXISTS = 1005;
	/**
	 * login sucessful status
	 */
	public static final int SUCESSFUL_LOGIN = 1006;
	/**
	 * registration sucessful status
	 */
	public static final int USER_REGISTERED_SUCESSFULLY = 1007;
	/**
	 * user profile updated sucessful status
	 */
	public static final int USER_UPDATED_SUCESSFULLY = 1008;
	/**
	 * server not avialable status
	 */
	public static final int SERVER_NOT_AVAILABLE = 1009;
}
