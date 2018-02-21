package com.spservices.friendmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Set;

import org.json.JSONObject;

/**
 * This class implements functions to store and retrieve data from persistant storage
 * @author Alok Goyal
 *
 */
public class Store {


	
	public Store() {}
	/**
	 * This function stores users profile to file in form of JSON.
	 * @param fileName - file to which data will be stored
	 * @param users - hash of users
	 * @throws IOException 
	 */
	public static void saveData(String fileName, Hashtable<String, UserProfile> users) throws IOException{
		JSONObject root = new JSONObject();
		for (String user : users.keySet()) {
			UserProfile up = users.get(user);
			JSONObject obj = new JSONObject();
			obj.put(FriendManagerConstants.blockedKey, up.getBlockedHash());
			obj.put(FriendManagerConstants.friendsKey, up.getfriendsHash());
			obj.put(FriendManagerConstants.followersKey, up.getfollowersHash());
			root.put(user, obj);
		}

		FileWriter file = new FileWriter(fileName);
		root.write(file, 2, 0);
		file.flush();
	}
	
	private static Hashtable<String, Timestamp> readHashFromJsonObj(JSONObject obj) {
		Hashtable<String, Timestamp> ret = new Hashtable<String, Timestamp>();
		
		Set<String> keys = obj.keySet();
		for (String key : keys) {
			Timestamp timeStamp = Timestamp.valueOf(obj.getString(key));
			ret.put(key, timeStamp);
		}
		return ret;
	}
	
	/**
	 * This function read file from storage and returns a hash of user profiles. This hash is in memory copy of user data.
	 * @param fileName - file name with path from where data needs to be read
	 * @param users - once file is read, data is stored and in this Hashmap
	 * @throws IOException
	 */
	public static void readData(String fileName, Hashtable<String, UserProfile> users) throws IOException{
		File inFile = new File(fileName);
		if (inFile.exists()) {
			String content = new String(Files.readAllBytes(Paths.get(fileName)));
			JSONObject jsonObj = new JSONObject(content);
			Set<String> keys = jsonObj.keySet();
			for (String user : keys) {
				JSONObject userData = jsonObj.getJSONObject(user);
				
				JSONObject blocked = userData.getJSONObject(FriendManagerConstants.blockedKey);
				Hashtable<String, Timestamp> blockedList = readHashFromJsonObj(blocked);
				
				JSONObject friends = userData.getJSONObject(FriendManagerConstants.friendsKey);
				Hashtable<String, Timestamp> friendsList = readHashFromJsonObj(friends);
				
				JSONObject followers = userData.getJSONObject(FriendManagerConstants.followersKey);
				Hashtable<String, Timestamp> followersList = readHashFromJsonObj(followers);
				
				UserProfile up = new UserProfile(user, friendsList, followersList, blockedList);
				users.put(user, up);
			}
		}
	}

}
