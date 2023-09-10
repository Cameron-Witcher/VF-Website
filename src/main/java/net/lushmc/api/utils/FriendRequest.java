package net.lushmc.api.utils;

public class FriendRequest {

	private int id;
	private int user;
	private int friend;
	private boolean accepted;

	public FriendRequest(int id, int user, int friend, boolean accepted) {
		this.id = id;
		this.user = user;
		this.friend = friend;
		this.accepted = accepted;
	}

	public int getID() {
		return id;
	}

	public int getRequester() {
		return user;
	}

	public int getFriend() {
		return friend;
	}

	public boolean isAccepted() {
		return accepted;
	}
}
