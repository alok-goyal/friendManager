package com.spservices.friendmanager;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This clas implements REST API for friend manager server
 * @author Alok Goyal
 *
 */
@Path("/friends")
public class FriendsManagerAPI {
	
	/**
	 * This function is exposing REST API to connect 2 users
	 * @param payload
	 * @return
	 */
	@POST
	@Path("/connect")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response createConnection(InputStream payload) {

		try {
			FriendManagerController fm = new FriendManagerController(payload);
			String ret = fm.createConnection();
			return Response.ok(ret).build();
		} catch (Exception e) {
			return Response.ok(e).build();
		}
		
	}
	/**
	 * This function is exposing REST API to get list of all friends for user
	 * @param payload
	 * @return JSON response
	 * 
	 */
	@POST
	@Path("/getfriends")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response getFriends(InputStream payload) {

		try {
			FriendManagerController fm = new FriendManagerController(payload);
			String ret = fm.getFriends();
			return Response.ok(ret).build();
		} catch (Exception e) {
			return Response.ok(e).build();
		}
		
	}
	/**
	 * This function is exposing REST API to get commong friend between 2 users
	 * @param payload
	 * @return JSON response
	 * 
	 */
	@POST
	@Path("/getcommon")
	@Produces(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.TEXT_PLAIN)
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response getCommonFriends(InputStream payload) throws IOException {

		try {
			FriendManagerController fm = new FriendManagerController(payload);
			String ret = fm.getCommonFriends();
			return Response.ok(ret).build();
		} catch (Exception e) {
			return Response.ok(e).build();
		}
		
	}
	/**
	 * This function is exposing REST API to follow a target user from requesting user
	 * @param payload
	 * @return JSON response
	 * 
	 */
	@POST
	@Path("/follow")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response follow(InputStream payload) {

		try {
			FriendManagerController fm = new FriendManagerController(payload);
			String ret = fm.followUser();
			return Response.ok(ret).build();
		} catch (Exception e) {
			return Response.ok(e).build();
		}
		
	}
	/**
	 * This function is exposing REST API to block update from a particular user
	 * @param payload
	 * @return JSON response
	 * 
	 */
	@POST
	@Path("/block")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response block(InputStream payload) {

		try {
			FriendManagerController fm = new FriendManagerController(payload);
			String ret = fm.blockUser();
			return Response.ok(ret).build();
		} catch (Exception e) {
			
			return Response.ok(e).build();
		}
	}
	/**
	 * This function is exposing REST API to get list of all followers for a user
	 * @param payload
	 * @return JSON response
	 * 
	 */
	@POST
	@Path("/getsubscribers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
	public Response getListSubscribers(InputStream payload)  {

		try {
			FriendManagerController fm = new FriendManagerController(payload);
			String ret = fm.getSubscribers();
			return Response.ok(ret).build();
		} catch (Exception e) {
			return Response.ok(e).build();
		}
		
	}
}
