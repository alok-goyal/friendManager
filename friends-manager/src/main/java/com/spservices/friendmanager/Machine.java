package com.spservices.friendmanager;


import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class implements business logic on a machine. Call to this machine will be made if user data
 * resides on this machine.
 * @author Alok Goyal
 *
 */
public class Machine {

	private final int machineId;
	private Hashtable<String, UserProfile> users;
	private boolean dirty;
	private String fileName;

	/**
	 * Default constructor for machine. It also reads pre-stored data to initialize content.
	 * @param id an interger value to define machine.
	 */
	public Machine(int id) {
		machineId = id;
		dirty = false;
		fileName = FriendManagerConstants.filePath;
		fileName += Integer.toString(id);
		fileName += FriendManagerConstants.fileext;
		users = new Hashtable<String, UserProfile>();
		// TODO read file or if not able to read then start from blank
		try {
			readFromFile();
		} catch (Exception e) {
			// Ignore and start with what ever we got
			return;
		}

	}

	private void readFromFile() throws IOException {
		Store.readData(fileName, users);
	}



	// TODO this function should be called in a background thread
	// Right now calling it in each call as current log is not implemented
	private void storeDate() {

		try {
			if (dirty) {
				Store.saveData(fileName, users);
				dirty = false;
			}
		} catch (Exception e) {
			// Failed to write avoiding for now
			return;
		}
	}
	/**
	 * This function returns uer profile for a given user. If user does not exists it creates user profile
	 * @param user user email id
	 * @return Object of type Userprofile containing data for user
	 */
	private UserProfile fetch(String user) {
		if (!users.containsKey(user)) {
			UserProfile up = new UserProfile(user);
			users.put(user, up);
			dirty = true;
		}
		return users.get(user);
	}
	
	/**
	 * This function checks if a user1 can add user2 or not. To add user2, user2 should not be in blocked list of user1. 
	 * @param user1 requesting user
	 * @param user2 target user
	 * @return returns true is user1 can add user2 otherwise return false.
	 */
	public boolean canAdd(String user1, String user2) {
		UserProfile up = users.get(user1);
		return (up == null || up.canAdd(user2));
	}
	/**
	 * This function adds user2 as friend to user1. As friend will also be subscribed for receiving notifications from friend.
	 * @param user1 requesting user
	 * @param user2 target user
	 * @throws FriendManagerException
	 */
	public void add(String user1, String user2) throws FriendManagerException {
		UserProfile up = fetch(user1);
		up.addFriend(user2);
		dirty = true;
		storeDate();
	}
	/**
	 * This function returns set of all friends for user.
	 * @param user user for which list of friends is requested
	 * @return a set view of list of friends.
	 */
	public Set<String> getFriends(String user) {
		UserProfile up = users.get(user);
		if (up == null)
			return new HashSet<String>(0);
		else
			return up.getFriends();
	}
	/**
	 * This function checks if target is blocked from requester or not.
	 * @param requester check for this user.
	 * @param target check if this user is blocked by requester.
	 * @return true is requester had blocked target.
	 */
	public boolean isBlocked(String requester, String target) {
		UserProfile up = users.get(requester);
		return (up != null && up.isBlocked(target));

	}
	/**
	 * This function adds follower for a given user.
	 * @param target add follower for this email
	 * @param requester requesting update from target email
	 */
	public void follow(String target, String requester) {
		UserProfile up = fetch(target);
		up.addFollower(requester);
		dirty = true;
		storeDate();
	}
	/**
	 * This function checks if user2 is friend of user1 or not.
	 * @param user1 user whos list is checked
	 * @param user2 user who is checked
	 * @return true if user2 is friend with user1.
	 */
	public boolean isFriend(String user1, String user2) {
		UserProfile up = users.get(user1);
		return (up != null && up.isFriend(user2));
	}
	/**
	 * Unsubscribe user2 from follow list of user1.
	 * @param user1 remove from following list of this user
	 * @param user2 remove this user from following list of user1
	 */
	public void unfollow(String user1, String user2) {
		UserProfile up = users.get(user1);
		up.unfollow(user2);
		dirty = true;
		storeDate();
	}
	/**
	 * Block user2 for user1. User2 can not be added as friend to user1 in future.
	 * @param user1 requesting user
	 * @param user2 blocked user for requesting user
	 */
	public void block(String user1, String user2) {
		UserProfile up = fetch(user1);
		up.block(user2);
		dirty = true;
		storeDate();
	}
	/**
	 * Get Set of all users which will get update for user represneted by user.
	 * @param user user email id for which list of followers is requested
	 * @return a set of all email ids which will receive update for given user.
	 */
	public Set<String> getSubscribers(String user) {
		UserProfile up = users.get(user);
		if (up == null)
			return new HashSet<String>(0);
		else
			return up.getSubscribers();
	}

}
