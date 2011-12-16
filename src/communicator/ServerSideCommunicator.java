package communicator;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import utils.Logger;
import beans.Message;
import client.MessageReceiver;
import dboperations.DBoperations;
import flags.UserStatus;

/**
 * This class acts as a helper class for the server, it is the responsibility of
 * this class is to send message to user as well as update user on various
 * events. Its a singleton class associated with the server object.
 * 
 * @author Kuldeep Saxena
 */

public class ServerSideCommunicator extends Thread {

	/*
	 * Variable declaration section
	 */

	/**
	 * Default object of the class
	 */
	private static ServerSideCommunicator sscommunicator = null;

	/**
	 * thread to update users when new user sign in
	 */
	private UserOnlineNotifier uonNotifier = null;

	/**
	 * thread to update users when user sign out
	 */
	private UserOfflineNotifier uoffNotifier = null;

	/**
	 * thread to update users when new user signs up
	 */
	private NewUserNotifier newUserNotifier = null;

	/**
	 * thread to update user status
	 */
	private OnlineStatusNotifier onlineStatusNotifier = null;

	/**
	 * thread to update user status
	 */
	private OfflineStatusNotifier offlineStatusNotifier = null;

	/**
	 * thread to update users on message received
	 */
	private MessageDispatcher[] mdispatcher = null;

	/**
	 * object to record logs
	 */
	private Logger logger;

	/**
	 * block gets executed when class loads creates the object of the class
	 */
	static {
		sscommunicator = new ServerSideCommunicator();
	}

	/**
	 * Default constructor initializes logger starts the thread
	 */
	private ServerSideCommunicator() {
		logger = Logger.getDefaultInstance();
		start();
	}

	/**
	 * starts various threads to handle different tasks
	 */
	public void run() {
		// starting online status updater thread
		uonNotifier = new UserOnlineNotifier();
		uonNotifier.start();
		// starting offline status updater thread
		uoffNotifier = new UserOfflineNotifier();
		uoffNotifier.start();
		// starting new user status updater thread
		newUserNotifier = new NewUserNotifier();
		newUserNotifier.start();

		onlineStatusNotifier = new OnlineStatusNotifier();
		onlineStatusNotifier.start();

		offlineStatusNotifier = new OfflineStatusNotifier();
		offlineStatusNotifier.start();
		// starting message dispatcher theads
		mdispatcher = new MessageDispatcher[10];
		for (int i = 0; i < mdispatcher.length; i++) {
			mdispatcher[i] = new MessageDispatcher();
			mdispatcher[i].start();
		}
		while (true) {
			try {
				// keep it running
				Thread.sleep(60000);
			} catch (Exception e) {

			}
		}
	}

	/**
	 * only access method
	 * 
	 * @return object of the current class
	 */
	public static ServerSideCommunicator getDefaultInstance() {
		return sscommunicator;
	}

	/**
	 * this function notifies message dispatcher to send message
	 * 
	 */
	public void updateNewMessageReceived() {
		Thread t = new Thread() {
			public void run() {
				for (int i = 0; i < mdispatcher.length; i++) {
					// System.out.println(mdispatcher);
					synchronized (mdispatcher[i]) {
						mdispatcher[i].notifyAll();
					}
				}
			}
		};
		t.start();
	}

	/**
	 * notifies user offline status updater class
	 * 
	 */
	public void notifySignOutUserStatus() {
		Thread t = new Thread() {
			public void run() {
				synchronized (uoffNotifier) {
					uoffNotifier.notifyAll();
				}
			}
		};
		t.start();
	}

	/**
	 * notifies users that a new user been added
	 * 
	 */
	public void notifyNewUserStatus() {
		Thread t = new Thread() {
			public void run() {
				synchronized (newUserNotifier) {
					newUserNotifier.notifyAll();
				}
			}
		};
		t.start();
	}

	/**
	 * notifies user online status updater class
	 * 
	 */
	public void notifySignInUserStatus() {
		Thread t = new Thread() {
			public void run() {
				synchronized (uonNotifier) {
					uonNotifier.notifyAll();
				}
			}
		};
		t.start();
	}

	/**
	 * notifies online status update to user
	 * 
	 */
	public void notifyOnlineStatus() {
		Thread t = new Thread() {
			public void run() {
				synchronized (onlineStatusNotifier) {
					onlineStatusNotifier.notifyAll();
				}
			}
		};
		t.start();

	}

	/**
	 * notifies offline status
	 */

	public void notifyOfflineStatus() {
		Thread t = new Thread() {
			public void run() {
				synchronized (offlineStatusNotifier) {
					offlineStatusNotifier.notifyAll();
				}
			}
		};
		t.start();

	}

	/**
	 * Inner class this class update other users when any user goes offline
	 * 
	 * @author Kuldeep Saxena
	 * 
	 */
	private class UserOfflineNotifier extends Thread {
		UserOfflineNotifier() {

		}

		/**
		 * entry point for the thread
		 */
		public void run() {
			while (true) {
				try {
					while (DBoperations.getDefaultInstance().getSignOutUsers()
							.size() < 1) {
						synchronized (this) {
							wait();
						}
					}
					Enumeration<String> users = DBoperations
							.getDefaultInstance().getActiveUsers().keys();
					while (users.hasMoreElements()) {
						try {
							String user = users.nextElement();
							for (int i = 0; i < DBoperations
									.getDefaultInstance().getSignOutUsers()
									.size(); i++) {
								if (!DBoperations.getDefaultInstance()
										.getSignOutUsers().get(i)
										.equalsIgnoreCase(user)) {
									DBoperations
											.getDefaultInstance()
											.getActiveUsers()
											.get(user)
											.updateUserStatus(
													DBoperations
															.getDefaultInstance()
															.getSignOutUsers()
															.get(i),
													UserStatus.USER_OFFLINE);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					DBoperations.getDefaultInstance().getSignOutUsers().clear();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Inner class this class is used to update other user when any user goes
	 * online
	 * 
	 * @author Kuldeep Saxena
	 * 
	 */
	private class UserOnlineNotifier extends Thread {
		UserOnlineNotifier() {
		}

		/**
		 * continuously running thread
		 */
		public void run() {
			while (true) {
				try {
					while (DBoperations.getDefaultInstance().getSignInUsers()
							.size() < 1) {
						synchronized (this) {
							wait();
						}
					}
					Enumeration<String> users = DBoperations
							.getDefaultInstance().getActiveUsers().keys();
					while (users.hasMoreElements()) {
						try {
							String user = users.nextElement();
							for (int i = 0; i < DBoperations
									.getDefaultInstance().getSignInUsers()
									.size(); i++) {
								if (!DBoperations.getDefaultInstance()
										.getSignInUsers().get(i)
										.equalsIgnoreCase(user)) {
									DBoperations
											.getDefaultInstance()
											.getActiveUsers()
											.get(user)
											.updateUserStatus(
													DBoperations
															.getDefaultInstance()
															.getSignInUsers()
															.get(i),
													UserStatus.USER_ONLINE);
								}
							}
							// newUsers.remove(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					DBoperations.getDefaultInstance().getSignInUsers().clear();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Inner class this class is used to update other user when any user goes
	 * online
	 * 
	 * @author Kuldeep Saxena
	 * 
	 */
	private class NewUserNotifier extends Thread {
		NewUserNotifier() {
		}

		/**
		 * continuously running thread
		 */
		public void run() {
			while (true) {
				try {
					while (DBoperations.getDefaultInstance().getNewUsers()
							.size() < 1) {
						synchronized (this) {
							wait();
						}
					}
					Enumeration<String> users = DBoperations
							.getDefaultInstance().getActiveUsers().keys();
					while (users.hasMoreElements()) {
						try {
							String user = users.nextElement();
							for (int i = 0; i < DBoperations
									.getDefaultInstance().getNewUsers().size(); i++) {
								if (!DBoperations.getDefaultInstance()
										.getNewUsers().get(i)
										.equalsIgnoreCase(user)) {
									DBoperations
											.getDefaultInstance()
											.getActiveUsers()
											.get(user)
											.newUserAdded(
													DBoperations
															.getDefaultInstance()
															.getAllUsers()
															.get(DBoperations
																	.getDefaultInstance()
																	.getNewUsers()
																	.get(i)));
								}
							}
							// newUsers.remove(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					DBoperations.getDefaultInstance().getNewUsers().clear();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Inner class used to dispatch message to the user
	 * 
	 * @author Kuldeep Saxena
	 * 
	 */
	private class MessageDispatcher extends Thread {
		private boolean isBusy = false;

		/**
		 * continuously running thread
		 */
		public void run() {
			while (true) {
				try {
					synchronized (this) {
						/*
						 * if there are no messages in the queue let the system
						 * to wait
						 */
						while (DBoperations.getDefaultInstance().getMessages()
								.size() < 1) {
							isBusy = false;
							wait();
						}
						isBusy = true;
						/*
						 * checked again because other sending thread may
						 * already sended that message
						 * 
						 * @bug it should be in the synchronized block, as these
						 * vector can happen in free manner
						 */
						if (DBoperations.getDefaultInstance().getMessages()
								.size() > 0) {
							final Message message = DBoperations
									.getDefaultInstance().getMessages()
									.remove(0);
							String toUsers[] = message.getToUsers();
							String textMsg = "";
							try {
								textMsg = message.getTextMessage()
										.getTextMessage();// (0,
															// message.getTextMessage().getLength());
							} catch (Exception e) {
							}
							for (int j = 0; j < toUsers.length; j++) {
								final MessageReceiver mr = DBoperations
										.getDefaultInstance().getActiveUsers()
										.get(toUsers[j]);
								final String currentUser = toUsers[j];
								logger.writeLog("Sending message to "
										+ currentUser);
								logger.writeLog("Message contents : ["
										+ message.getFromUser() + "] -> "
										+ textMsg);
								if (mr == null) {
									logger.writeLog(currentUser
											+ " is offline writing message to system");
									DBoperations.getDefaultInstance()
											.writeOffLineMessage(toUsers[j],
													message);
								} else {
									Thread t = new Thread() {
										public void run() {
											try {
												mr.receiveMessage(message);
												logger.writeLog("Message sent to "
														+ currentUser);
											} catch (Exception e) {
												// e.printStackTrace();
												/**
												 * if failed to send message,
												 * the message is wriiten to the
												 * secondary storage and treated
												 * as offline message
												 */
												logger.writeLog("Failed to sent message to the client, assuming client as offline, writing message to media");
												DBoperations
														.getDefaultInstance()
														.getActiveUsers()
														.remove(currentUser);
												DBoperations
														.getDefaultInstance()
														.getSignOutUsers()
														.add(currentUser);
												notifySignOutUserStatus();
												DBoperations
														.getDefaultInstance()
														.writeOffLineMessage(
																currentUser,
																message);
											}
										}
									};
									t.start();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private class OnlineStatusNotifier extends Thread {

		/**
		 * entry point for the thread
		 */
		public void run() {
			while (true) {
				try {
					while (DBoperations.getDefaultInstance()
							.getOnlineStatusList().size() < 1) {
						synchronized (this) {
							wait();
						}
					}
					Enumeration<String> users = DBoperations
							.getDefaultInstance().getActiveUsers().keys();
					while (users.hasMoreElements()) {
						try {
							String user = users.nextElement();
							Set<String> keys = DBoperations
									.getDefaultInstance().getOnlineStatusList()
									.keySet();
							Iterator<String> i = keys.iterator();
							while (i.hasNext()) {
								String stUser = i.next();
								if (!stUser.equalsIgnoreCase(user)) {
									String msg = DBoperations
											.getDefaultInstance()
											.getOnlineStatusList().get(stUser);
									DBoperations
											.getDefaultInstance()
											.getActiveUsers()
											.get(user)
											.updateNewStatusMessage(stUser,
													msg, true);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					DBoperations.getDefaultInstance().getOnlineStatusList()
							.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class OfflineStatusNotifier extends Thread {
		/**
		 * entry point for the thread
		 */
		public void run() {
			while (true) {
				try {
					while (DBoperations.getDefaultInstance()
							.getOfflineStatusList().size() < 1) {
						synchronized (this) {
							wait();
						}
					}
					Enumeration<String> users = DBoperations
							.getDefaultInstance().getActiveUsers().keys();
					while (users.hasMoreElements()) {
						try {
							String user = users.nextElement();
							Set<String> keys = DBoperations
									.getDefaultInstance()
									.getOfflineStatusList().keySet();
							Iterator<String> i = keys.iterator();
							while (i.hasNext()) {
								String stUser = i.next();
								if (!stUser.equalsIgnoreCase(user)) {
									String msg = DBoperations
											.getDefaultInstance()
											.getOfflineStatusList().get(stUser);
									DBoperations
											.getDefaultInstance()
											.getActiveUsers()
											.get(user)
											.updateNewStatusMessage(stUser,
													msg, false);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					DBoperations.getDefaultInstance().getOfflineStatusList()
							.clear();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}