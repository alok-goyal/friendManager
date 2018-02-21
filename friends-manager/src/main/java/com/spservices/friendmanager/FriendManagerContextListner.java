/**
 * 
 */
package com.spservices.friendmanager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Alok Goyal
 *
 */
public class FriendManagerContextListner implements ServletContextListener{


	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{

		try {
			MachineContainerWrapper.initInstance();
		} catch (FriendManagerException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{	
		MachineContainerWrapper.storeAndExit();
		  
	}

}
