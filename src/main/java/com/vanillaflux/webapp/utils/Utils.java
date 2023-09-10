package com.vanillaflux.webapp.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json2.JSONArray;
import org.json2.JSONObject;

import com.vanillaflux.webapp.utils.sql.SQLUtils;

public class Utils {

	private static Map<UUID, String> usernames = new HashMap<>();
	private static Map<UUID, Integer> forums = new HashMap<>();
	private static List<FriendRequest> requests = new ArrayList<>();

//	public static void init() {
//		update();
//	}

//	public static void update() {
//		updateUsernames();
//		updateForumsNames();
//		updateFriends();
//	}

//	public static String getUsername(UUID uid) {
//		return usernames.containsKey(uid) ? usernames.get(uid) : null;
//	}

//	public static JSONArray getFriends(UUID uid) {
//		JSONArray array = new JSONArray();
//		int fid = forums.containsKey(uid) ? forums.get(uid) : -1;
//		for (FriendRequest req : requests) {
//			if (req.getFriend() == fid || req.getRequester() == fid) {
//				JSONObject fob = new JSONObject("{}");
//				fob.put("request_id", req.getID());
//				fob.put("requester_id", req.getRequester());
//				fob.put("friend_id", req.getFriend());
//				fob.put("accepted", req.isAccepted());
//				array.put(fob);
//			}
//
//		}
//
//		return array;
//	}

//	private static void updateForumsNames() {
//		System.out.println("Updating Forums info");
//		forums.clear();
//		ResultSet rs = SQLUtils.getDatabase("minecraft")
//				.query("SELECT * FROM mystic_players WHERE forums_name IS NOT NULL;");
//
//		try {
//			while (rs.next())
//				forums.put(UUID.fromString(rs.getString("uuid")), Integer.parseInt(rs.getString("forums_name")));
//
//			rs.close();
//		} catch (NullPointerException | SQLException e) {
//			e.printStackTrace();
//		}
//	}

//	private static void updateFriends() {
//		System.out.println("Updating Friendships");
//		requests.clear();
//		ResultSet rs = SQLUtils.getDatabase("forums").query("SELECT * FROM nl2_friends;");
//		try {
//			while (rs.next())
//				requests.add(new FriendRequest(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("friend_id"),
//						rs.getInt("accepted") == 1 ? true : false));
//			rs.close();
//		} catch (NullPointerException | SQLException e) {
//			e.printStackTrace();
//		}
//	}

//	private static void updateUsernames() {
//		System.out.println("Updating Usernames");
//		usernames.clear();
//		ResultSet rs = SQLUtils.getDatabase("minecraft").query("SELECT * FROM mystic_players;");
//		try {
//			while (rs.next()) {
//				usernames.put(UUID.fromString(rs.getString("uuid")), rs.getString("name"));
//			}
//
//			rs.close();
//		} catch (NullPointerException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public static UUID findUUID(String name) {
//		for (Entry<UUID, String> e : usernames.entrySet()) {
//			if (name.equalsIgnoreCase(e.getValue()))
//				return e.getKey();
//		}
//		return null;
//	}

//	public static File mergeFiles(String... files) {
//		try {
//			OutputStream outputStream = new FileOutputStream("merge.html");
//			for (String file : files) {
//
//				InputStream inputStream = main.getClass().getClassLoader().getResourceAsStream(file);
//
//				byte[] buffer = new byte[2048];
//
//				int length = 0;
//
//				while ((length = inputStream.read(buffer)) != -1) {
//					System.out.println("Buffer Read of length: " + length);
//					outputStream.write(buffer, 0, length);
//				}
//
//				inputStream.close();
//
//			}
//			outputStream.close();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		return new File("merge.html");
//		
//
//	}

}
