/**
 * 
 */
package com.spservices.friendmanager;

import java.util.ArrayList;

/**
 * This class contain list of machines on which user profiles are stored. Users are distributed on different machines 
 * based on hash value of user email id. Current implementation is done only for a single machine. 
 * @author goyala 
 * 
 */

public class MachineContainer {

	// Keeping ArrayList so that hash can be distributed in different buckets
	// This implementation can be extended to keeping user list in multiple
	// machines for load balancing. For now we will keep only one element
	
	private ArrayList<Machine> userMachine;
	private final int bucketSize = 1;
	/**
	 * This is default constructor and should be initializing machines which contain user profiles.
	 */
	public MachineContainer() {
		userMachine = new ArrayList<Machine>(bucketSize);
		int i = 0;
		while(i < bucketSize){
			userMachine.add(new Machine(i++));
		}
	}
	
	private long calculateHash(String email){
		long ret = 0;
		for(int i = 0; i < email.length(); i++){
			ret += email.charAt(i);
		}
		return ret;
	}
	private int calculateListBucket(String email){
		
		return (int) calculateHash(email)%bucketSize ;
	}
	/**
	 * This function returns machine on which use profile can be found.
	 * @param email User email id for which any operation is requested 
	 * @return
	 * will be stored.
	 * Object of machine which have user profile and which will modify user profile. If use is a new user it will return machine on which user data
	 */
	public Machine getUserMachine(String email){
		int index = calculateListBucket(email);
		
		while(index < (userMachine.size()-1)){
			userMachine.add(new Machine(userMachine.size()));
		}
		return userMachine.get(index);
	}
	
	/**
	 * 
	 */
	public static void initializeMachineContainer() throws FriendManagerException{
		
	}

}
