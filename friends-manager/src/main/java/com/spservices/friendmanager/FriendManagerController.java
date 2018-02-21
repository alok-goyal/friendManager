/**
 * 
 */
package com.spservices.friendmanager;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class has implemented business logic for friend management service.
 * 
 * @author Alok Goyal
 *
 */
public class FriendManagerController {

	private JSONObject packetData;
	private InputStream inputStream;

	/**
	 * This function parse input stream and load it to JSON object
	 * 
	 * @throws FriendManagerException
	 */
	private void parseInputStream() throws FriendManagerException {
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder requestStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				requestStrBuilder.append(inputStr);
			packetData = new JSONObject(requestStrBuilder.toString());

		} catch (Exception e) {
			// TODO Log this error
			throw (new FriendManagerException(FriendManagerConstants.failBadInput));
		}

	}

	private boolean isValidEmailId(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/**
	 * Default constructor for controller class
	 * 
	 * @param payload
	 */
	public FriendManagerController(InputStream payload) {
		inputStream = payload;
	}

	/**
	 * This function create connection between two emails passed in to JSON
	 * packet.
	 * 
	 * @return Returns a JSON string which can be returned to the caller.
	 */
	public String createConnection() {

		try {
			parseInputStream();

			JSONArray friendsArray = packetData.getJSONArray(FriendManagerConstants.friendsKey);
			if (friendsArray.length() != 2)
				return FriendManagerConstants.failBadInput;

			String emailUser1 = friendsArray.getString(0).toLowerCase();
			String emailUser2 = friendsArray.getString(1).toLowerCase();
			if (!(isValidEmailId(emailUser1) && isValidEmailId(emailUser2)))
				return FriendManagerConstants.failBadInput;
			Machine machine1 = MachineContainerWrapper.getInstance().getUserMachine(emailUser1);
			Machine machine2 = MachineContainerWrapper.getInstance().getUserMachine(emailUser2);
			// TODO need to consider if they are already friends should we
			// return true or false
			if (!(machine1.canAdd(emailUser1, emailUser2) && machine2.canAdd(emailUser2, emailUser1))) {
				return FriendManagerConstants.failToAdd;
			} else {
				machine1.add(emailUser1, emailUser2);
				machine2.add(emailUser2, emailUser1);
				return FriendManagerConstants.okResponse;
			}
		} catch (FriendManagerException e) {
			return FriendManagerConstants.failBadInput;
		}
	}

	/**
	 * This function collect list of all friends for an email passed in JSON
	 * packet.
	 * 
	 * @return Returns a JSON string which can be returned to the caller. This
	 *         will contain list of all friends for a given email.
	 */
	public String getFriends() {
		try {
			parseInputStream();

			String emailUser = packetData.getString(FriendManagerConstants.emailKey).toLowerCase();
			if (!isValidEmailId(emailUser))
				return FriendManagerConstants.failBadInput;

			Machine m = MachineContainerWrapper.getInstance().getUserMachine(emailUser);
			Set<String> emails = m.getFriends(emailUser);
			JSONObject ret = new JSONObject(FriendManagerConstants.okResponse);

			for (String str : emails) {
				ret.accumulate(FriendManagerConstants.friendsKey, str);
			}
			ret.put(FriendManagerConstants.freindsCount, emails.size());

			return ret.toString();
		} catch (FriendManagerException e) {
			return FriendManagerConstants.failBadInput;
		}
	}

	/**
	 * This function gets list of common friends between 2 email ids passed in
	 * JSON data.
	 * 
	 * @return Returns a JSON string which can be returned to the caller.
	 */
	public String getCommonFriends() {
		try {
			parseInputStream();

			JSONArray friendsArray = packetData.getJSONArray(FriendManagerConstants.friendsKey);
			if (friendsArray.length() != 2)
				return FriendManagerConstants.failBadInput;

			String emailUser1 = friendsArray.getString(0).toLowerCase();
			String emailUser2 = friendsArray.getString(1).toLowerCase();
			if (!(isValidEmailId(emailUser1) && isValidEmailId(emailUser2)))
				return FriendManagerConstants.failBadInput;
			Machine machine1 = MachineContainerWrapper.getInstance().getUserMachine(emailUser1);
			Machine machine2 = MachineContainerWrapper.getInstance().getUserMachine(emailUser2);
			JSONObject ret = new JSONObject(FriendManagerConstants.okResponse);
			Set<String> commonFriends = machine1.getFriends(emailUser1);
			Set<String> friendsUser2 = machine2.getFriends(emailUser2);

			commonFriends.retainAll(friendsUser2);
			for (String str : commonFriends) {
				ret.accumulate(FriendManagerConstants.friendsKey, str);
			}
			ret.put(FriendManagerConstants.freindsCount, commonFriends.size());

			return ret.toString();
		} catch (FriendManagerException e) {
			return FriendManagerConstants.failBadInput;
		}
	}

	/**
	 * This function subscribe a requester email to get update from any email.
	 * 
	 * @return Returns a JSON string which can be returned to the caller.
	 */
	public String followUser() {
		try {
			parseInputStream();

			String requester = packetData.getString(FriendManagerConstants.requester).toLowerCase();
			String target = packetData.getString(FriendManagerConstants.target).toLowerCase();
			if (!(isValidEmailId(requester) && isValidEmailId(target)))
				return FriendManagerConstants.failBadInput;

			Machine reqMachine = MachineContainerWrapper.getInstance().getUserMachine(requester);
			Machine targetMachine = MachineContainerWrapper.getInstance().getUserMachine(target);

			if (reqMachine.isBlocked(requester, target)) {
				return FriendManagerConstants.failedtoFollow;
			} else {
				targetMachine.follow(target, requester);
				return FriendManagerConstants.okResponse;
			}
		} catch (FriendManagerException e) {
			return FriendManagerConstants.failBadInput;
		}
	}

	/**
	 * This function blocks update from a user for a requesting user. It does
	 * not remove friendship in case two uses are friends. However if two users
	 * are not friends then they can not be friends in future.
	 * 
	 * @return Returns a JSON string which can be returned to the caller.
	 */
	public String blockUser() {
		try {
			parseInputStream();

			String requester = packetData.getString(FriendManagerConstants.requester).toLowerCase();
			String target = packetData.getString(FriendManagerConstants.target).toLowerCase();
			if (!(isValidEmailId(requester) && isValidEmailId(target)))
				return FriendManagerConstants.failBadInput;

			// Machine reqMachine =
			// MachineContainerWrapper.getInstance().getUserMachine(requester);
			Machine targetMachine = MachineContainerWrapper.getInstance().getUserMachine(target);

			if (targetMachine.isFriend(target, requester)) {
				targetMachine.unfollow(target, requester);
			} else {
				targetMachine.block(target, requester);
				targetMachine.unfollow(target, requester);
			}
			return FriendManagerConstants.okResponse;
		} catch (FriendManagerException e) {
			return FriendManagerConstants.failBadInput;
		}
	}

	/**
	 * This function collect and returns list of users which will get update
	 * from a given email.
	 * 
	 * @return Returns a JSON string which can be returned to the caller.
	 */
	public String getSubscribers() {
		try {
			parseInputStream();

			String emailUser = packetData.getString(FriendManagerConstants.sender).toLowerCase();
			if (!this.isValidEmailId(emailUser))
				return FriendManagerConstants.failBadInput;

			Machine m = MachineContainerWrapper.getInstance().getUserMachine(emailUser);
			Set<String> emails = m.getSubscribers(emailUser);
			JSONObject ret = new JSONObject(FriendManagerConstants.okResponse);

			for (String str : emails) {
				ret.accumulate(FriendManagerConstants.recipeints, str);
			}

			// ret.append(FriendManagerConstants.freindsCount, emails.size());
			return ret.toString();
		} catch (FriendManagerException e) {
			return FriendManagerConstants.failBadInput;
		}
	}

}
