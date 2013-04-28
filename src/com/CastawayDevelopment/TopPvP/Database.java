package com.CastawayDevelopment.TopPvP;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author matejkramny
 */
public class Database {
	private TopPvP plugin;
	public static String tableName = "toppvp";
	private static String username = "root";
	private static String password = "";
	private static String url = "jdbc:mysql://localhost:3306/minecraft";

	private static String createTableQuery = "CREATE TABLE IF NOT EXISTS "+tableName+" (" +
			"id int(255) NOT NULL AUTO_INCREMENT," +
			"username varchar(32) NOT NULL," +
			"kills int(255) NOT NULL DEFAULT '0'," +
			"deaths int(255) NOT NULL DEFAULT '0'," +
			"UNIQUE KEY `username` (`username`)," +
			"PRIMARY KEY `id` (`id`))" +
			"ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	public ResultSetHandler<Boolean> returnBoolean = new ResultSetHandler<Boolean>() {
		public Boolean handle(ResultSet rs) {
			try {
				rs.next();
			} catch (SQLException ex) {
				return false;
			}

			return true;
		}
	};

	public Connection getConnection() throws SQLException {
		// Connection is handled by bukkit! We just need to retrieve it..
		return DriverManager.getConnection(url, username, password);
	}

	public Map getPlayer (Player player) {
		QueryRunner runner = new QueryRunner();
		try {
			Connection connection = getConnection();

			List mapList = (List) runner.query(connection, "SELECT * FROM "+tableName+" WHERE username='"+player.getName()+"'", new MapListHandler());
			if (mapList.size() > 0) {
				return (Map)mapList.get(0);
			}
		} catch (SQLException exc) {
			TopPvP.log("SQL Exception "+exc);
		}

		return null;
	}

	public void createPlayer (Player player) {
		updateQuery("INSERT INTO "+tableName+" (username, kills, deaths) VALUES ('"+player.getName()+"', 0, 0)");
	}

	public void updateQuery (String query) {
		QueryRunner runner = new QueryRunner();
		try {
			Connection connection = getConnection();

			runner.update(connection, query);
		} catch (SQLException exc) {
			TopPvP.log("SQL Exception "+exc);
		}
	}

	public void connect() {
		boolean exists = tableExists();
		if (!exists) {
			// Create it.
			QueryRunner runner = new QueryRunner();

			try {
				Connection connection = getConnection();
				runner.update(connection, createTableQuery);

				DbUtils.close(connection);
			} catch (SQLException exc) {
				System.out.println("[TopPvP] Error creating table" + exc);
			}
		} else {
			plugin.getLogger().info("Table exists.");
		}
	}

	public boolean tableExists() {
		boolean exists = false;
		try {
			QueryRunner runner = new QueryRunner();
			Connection connection = getConnection();

			try {
				exists = runner.query(connection, "SELECT id FROM "+tableName, returnBoolean);
			} catch (SQLException exc) {
				exists = false;
			}

			DbUtils.close(connection);
		} catch (Exception exc) {
			plugin.getLogger().warning("Cannot connect to database server");
			exists = false;
		} finally {
			return exists;
		}
	}

	public Database (TopPvP plugin) {
		this.plugin = plugin;
		TopPvP.log("Connecting to DB");
		this.connect();
	}
}
