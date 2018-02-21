/**
 * 
 */
package com.spservices.friendmanager;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class holds user data and implements functions for adding friends or followers or blocks a user.
 * @author Alok Goyal
 *
 */
public class UserProfile {
	private final String userEmail;	
	private Hashtable<String, Timestamp> friends;
	private Hashtable<String, Timestamp> followers;
	private Hashtable<String, Timestamp> blocked;
	
	/**
	 * This is constructor to build user profile.
	 * @param email user email id to identify user uniquely 
	 */
	public UserProfile(String email) {
		userEmail = email;
		friends = new Hashtable<String, Timestamp>();
		followers = new Hashtable<String, Timestamp>();
		blocked = new Hashtable<String, Timestamp>();
	}
	/**
	 * This constructor initializes list of friends, followers and blocked emails. This is handy constructor to be used for creating in memory
	 * data after reading it from persistant storage.
	 * @param email email id of user
	 * @param friendsList Hashmap containing all friends and time when they were added last
	 * @param followerList Hashmap containing all followers and time when they were added last
	 * @param blockedList Hashmap containing all blocked contacts and time when they were added last
	 */
	public UserProfile(String email, Hashtable<String, Timestamp> friendsList,
			Hashtable<String, Timestamp> followerList,
			Hashtable<String, Timestamp> blockedList) {
		userEmail = email;
		friends = friendsList;
		followers = followerList;
		blocked = blockedList;
	}
	/**
	 * This function returns member Hashmap containing blocked contacts
	 * @return Hashmap of blocked contacts
	 */
	public Hashtable<String, Timestamp> getBlockedHash(){
		return blocked;
	}
	/**
	 * This function returns member Hashmap containing friends
	 * @return Hashmap of friends
	 */
	public Hashtable<String, Timestamp> getfriendsHash(){
		return friends;
	}
	/**
	 * This function returns member Hashmap containing followers
	 * @return Hashmap of followers
	 */
	public Hashtable<String, Timestamp> getfollowersHash(){
		return followers;
	}
	/**
	 * This function adds a user as friend
	 * @param email email id of user to be added
	 * @throws FriendManagerException
	 */
	public void addFriend(String email) throws FriendManagerException {
		if(blocked.containsKey(email))
			throw new FriendManagerException();
		//TODO Create running log for crash recovery
		//Timestamp will be used to trace logs to update user profiles in case of crash
		friends.put(email, new Timestamp(System.currentTimeMillis()));
		followers.put(email, new Timestamp(System.currentTimeMillis()));
	}

	public Set<String> getFriends() {
		return friends.keySet();
	}
	/**
	 * This function adds a user as follower
	 * @param email email id of user who will get update
	 * @throws FriendManagerException
	 */

	public void addFollower(String email) {
		//TODO Create running log for crash recovery
		//Timestamp will be used to trace logs to update user profiles in case of crash
		followers.put(email, new Timestamp(System.currentTimeMillis()));
	}
	/**
	 * Test if user can be added as friend to this user
	 * @param email user to be tested
	 * @return true if user can be added to this user
	 */
	public boolean canAdd(String email) {
		return !blocked.containsKey(email) ;
	}
	/**
	 * Test if user is blocekd by this user
	 * @param email user to be tested
	 * @return true if user is blocked by this user.
	 */
	public boolean isBlocked(String email) {		
		return blocked.containsKey(email);
	}
	/**
	 * Test is given user is friend to this user or not.
	 * @param email email id of user to be tested
	 * @return true is user is friend to this user.
	 */
	public boolean isFriend(String email) {		
		return friends.containsKey(email);
	}
	/**
	 * This funtion removes a user from getting update from  s user
	 * @param email email id of user to be remvoed from followers list
	 */
	public void unfollow(String email) {
		//TODO Create running log for crash recovery
		//Timestamp will be used to trace logs to update user profiles in case of crash
		if(followers.containsKey(email))
			followers.remove(email);
		
	}
	/**
	 * This function blocks user for future add requests
	 * @param email email id of user to be blocked
	 * @throws FriendManagerException
	 */
	public void block(String email) {
		//TODO Create running log for crash recovery
		//Timestamp will be used to trace logs to update user profiles in case of crash
		blocked.put(email, new Timestamp(System.currentTimeMillis()));		
	}
	/**
	 * This function returns set of all contacts who will get updates from this user
	 * @return Set of emails who will get update from this user
	 */
	public Set<String> getSubscribers() {
		return followers.keySet();
	}

}
