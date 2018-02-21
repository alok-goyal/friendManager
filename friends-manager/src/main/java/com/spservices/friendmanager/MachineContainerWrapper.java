/**
 * 
 */
package com.spservices.friendmanager;

/**
 * @author Alok Goyal
 *
 */
public class MachineContainerWrapper {


	private static MachineContainer machineContainerInstance = new MachineContainer();
	private static boolean initCalled = false;
	private MachineContainerWrapper() {
		// TODO Auto-generated constructor stub
		
	}
	
	public static void initInstance() throws FriendManagerException {
		//cacheManagerInstance = new CacheManager();
		try {
			if(!initCalled){
				MachineContainer.initializeMachineContainer();
				initCalled = true;
			}
		} catch (FriendManagerException e) {
			e.printStackTrace();			
		}		
	}
	
	public static MachineContainer getInstance() {
		//Return instance already created
		return machineContainerInstance;
	}

	public static void storeAndExit() {
		// TODO call store for all machines
		
	}
}
