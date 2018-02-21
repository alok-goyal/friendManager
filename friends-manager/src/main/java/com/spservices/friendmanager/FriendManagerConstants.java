/**
 * 
 */
package com.spservices.friendmanager;

/**
 * @author Alok Goyal
 *
 */
public class FriendManagerConstants {

	//TODO In real implementation these variables can be part of property files. Hence in case of any changes in JSON we can
	// change property file without changing much of code
	final static protected String okResponse = "{ \"success\": true}";
	final static protected String failResponse = "{ \"success\": false}";
	final static protected String failBadInput = "{ \"success\": false , \"reason\": \"Bad input\"}";
	final static protected String failToAdd = "{ \"success\": false , \"reason\": \"Either one is blocked by other\"}";
	final static protected String failedtoFollow = "{ \"success\": false , \"reason\": \"Requester had blocked updates from target\"}";
	final static protected String filePath = "/home/ec2-user/data/friendmanager";
	//final static protected String filePath = "D:\\sp_test\\data\\friendmanager";
	final static protected String fileext = ".json";
	final static protected String friendsKey = "friends";
	final static protected String emailKey = "email";
	final static protected String freindsCount = "count";
	final static protected String requester = "requestor";
	final static protected String target = "target";
	final static protected String sender = "sender";
	final static protected String recipeints = "recipients";
	final static protected String blockedKey = "blocked";
	final static protected String followersKey = "followers";

	
	public FriendManagerConstants() {}

}
