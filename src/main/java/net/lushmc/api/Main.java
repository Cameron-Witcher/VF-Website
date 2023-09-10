package net.lushmc.api;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.json2.JSONException;
import org.json2.JSONObject;

import net.lushmc.api.utils.sql.IDatabase;
import net.lushmc.api.utils.sql.SQLDriver;
import net.lushmc.api.utils.sql.SQLUtils;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Main {

	public static void main(String[] args) throws JSONException, SQLException {

		System.out.println("Server started.");

		BufferedReader br1;
		Properties prop = new Properties();
		try {

			br1 = new BufferedReader(new FileReader(new File("config.properties")));
			String st;
			while ((st = br1.readLine()) != null) {
				prop.setProperty(st.split("=")[0], st.split("=")[1]);
			}
			br1.close();

			System.out.println("Testing property loading. user: " + prop.getProperty("username"));

			// get the property value and print it out
//			String user = prop.getProperty("user");
//			String company1 = prop.getProperty("company1");
//			String company2 = prop.getProperty("company2");
//			String company3 = prop.getProperty("company3");

//			result = "Company List = " + company1 + ", " + company2 + ", " + company3;
//			System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getLocalizedMessage());
			prop.setProperty("username", "api");
			prop.setProperty("password", "DHRL?eGiab6X!Qi@4MpR3rF#D?Q#aY");
			prop.setProperty("host", "sql.mysticcloud.net");
		}

//		SQLUtils.createDatabase("minecraft", new IDatabase(SQLDriver.MYSQL, prop.getProperty("host"), "minecraft", 3306,
//				prop.getProperty("username"), prop.getProperty("password")));
//		SQLUtils.createDatabase("forums", new IDatabase(SQLDriver.MYSQL, prop.getProperty("host"), "forums", 3306,
//				prop.getProperty("username"), prop.getProperty("password")));
		SQLUtils.createDatabase("plugins", new IDatabase(SQLDriver.MYSQL, prop.getProperty("host"), "s9_lush_core",
				3306, prop.getProperty("username"), prop.getProperty("password")));

//		Utils.init();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
//				Utils.update();
			}
		}, 0, TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
		mainAPI();
		System.out.println("Server ready to take commands....");
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("test")) {
				try {
//					Utils.update();
					System.out.println("Testing properties for user 60191757-427b-421e-bee0-399465d7e852");
					System.out.println(getPlayerJson("60191757-427b-421e-bee0-399465d7e852").toString());
				} catch (NullPointerException e) {
					System.out.println("System is empty. No user found.");
				}
				System.out.println("System exiting");
				System.exit(0);
			}
		}
		while (true) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String name;
			try {
				name = reader.readLine();
				if (name == null)
					continue;
				if (name.equalsIgnoreCase("stop"))
					System.exit(0);
//				if (name.equalsIgnoreCase("update"))
//					Utils.update();
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void mainAPI() {

		Spark.port(8000);

		Spark.before("*/favicon.ico", (q, a) -> {
			File f = new File("icon.png");
			BufferedImage bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", baos);
			byte[] bytes = baos.toByteArray();
			a.type("image/png");

			a.raw().getOutputStream().write(bytes);
			a.raw().getOutputStream().flush();
			a.raw().getOutputStream().close();

		});

		Spark.path("/gui", () -> {
			Spark.get("/player/:name", (q, a) -> {
				JSONObject json = getPlayerJson(q.params(":name"));
				return new VelocityTemplateEngine().render(new ModelAndView(json.toMap(), "player.vm"));
			});
		});

		Spark.path("/api", () -> {
			Spark.path("/license", () -> {
				Spark.get("/guis/:key", (q, a) -> {
					a.type("application/json");
					JSONObject json = new JSONObject("{}");
					ResultSet rs = SQLUtils.getDatabase("plugins")
							.query("SELECT * FROM gui_license WHERE license='" + q.params(":key") + "';");
					if (rs != null) {
						while (rs.next()) {

							json.put("license", q.params(":key"));
							json.put("json", new JSONObject(rs.getString("json")));

						}
					}
					if (!json.has("license")) {
						json.put("error", "Could not find license");
					}
					return json.toString();
				});
			});
//			Spark.get("/infringements/:name", (q, a) -> {
//				a.type("application/json");
//				return getPlayerInfringementJson(q.params(":name")).toString();
//			});
			Spark.get("/infringement/:id", (q, a) -> {
				a.type("media/gif");
				return getPlayerJson(q.params(":name")).toString();
			});
			Spark.get("/player/:name", (q, a) -> {
				a.type("application/json");
				return getPlayerJson(q.params(":name")).toString();

			});
		});

		Spark.path("/resource", () -> {
			Spark.path("/image", () -> {
				Spark.before("/:name", (req, res) -> {
					File f = new File(req.params(":name"));
					if (f.exists()) {
						BufferedImage bi = ImageIO.read(f);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(bi, "png", baos);
						byte[] bytes = baos.toByteArray();
						res.type("image/png");

						res.raw().getOutputStream().write(bytes);
						res.raw().getOutputStream().flush();
						res.raw().getOutputStream().close();
					} else {
						res.type("html");
						res.body("<h1>Sorry, " + req.params(":name") + " doesn't exist.</h1>");
					}

				});

			});
			Spark.path("/download", () -> {
				Spark.before("/:name", (req, res) -> {
					File f = new File(req.params(":name"));
					if (f.exists()) {
						BufferedImage bi = ImageIO.read(f);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(bi, "png", baos);
						byte[] bytes = baos.toByteArray();
						res.type("application/force-download");

						res.raw().getOutputStream().write(bytes);
						res.raw().getOutputStream().flush();
						res.raw().getOutputStream().close();
					} else {
						res.type("html");
						res.body("<h1>Sorry, " + req.params(":name") + " doesn't exist.</h1>");
					}

				});

			});

		});

		Spark.get("/image/:name", (req, res) -> {
			res.redirect("/resource/" + req.pathInfo());
			return req.pathInfo();
		});

	}

	public static JSONObject getInfringementJson(String player) throws JSONException, SQLException {

		JSONObject json = new JSONObject("{}");
		try {
			json.put("uuid", UUID.fromString(player).toString());
//			json.put("name", Utils.getUsername(UUID.fromString(player)));
		} catch (Exception ex) {
			json.put("name", player);
//			json.put("uuid", Utils.findUUID(player).toString());
		}

		return json;
	}

	public static JSONObject getPlayerJson(String player) throws JSONException, SQLException {
		ResultSet rs = SQLUtils.getDatabase("plugins").query("SELECT * FROM player_data");
		while (rs.next()) {
			JSONObject data = new JSONObject(rs.getString("data"));
			if (rs.getString("uuid").equals(player))
				return data;
			if (data.getString("lastUsername").equals(player))
				return data;
			List<String> names = new ArrayList<>();
			for (Object o : data.getJSONArray("usernames"))
				names.add((String) o);
			if (names.contains(player))
				return data;
		}
		// TODO
		return new JSONObject("{\"error\":\"Player not found in database.\"}");
	}

}
