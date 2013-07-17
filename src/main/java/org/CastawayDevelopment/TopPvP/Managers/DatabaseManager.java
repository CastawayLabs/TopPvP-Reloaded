/*
 * This file is part of TopPvP, PvP leader stats plugin!.
 * TopPvP is licensed under GNU General Public License v3.
 * Copyright (C) 2013 The Castaway Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.CastawayDevelopment.TopPvP.Managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.CastawayDevelopment.TopPvP.TopPvP;
import org.bukkit.entity.Player;

public class DatabaseManager {
	private TopPvP plugin;

	//private DatabaseHandler databaseHandler;

	public static String tableName = "toppvp";

	private static String createTableQuery = "CREATE TABLE IF NOT EXISTS "+tableName+" (" +
			"id int(255) NOT NULL AUTO_INCREMENT," +
			"username varchar(32) NOT NULL," +
			"kills int(255) NOT NULL DEFAULT '0'," +
			"deaths int(255) NOT NULL DEFAULT '0'," +
			"UNIQUE KEY `username` (`username`)," +
			"PRIMARY KEY `id` (`id`))" +
			"ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	public ResultSet getPlayer (Player player) {
		ResultSet resultSet = null;//databaseHandler.query("SELECT * FROM "+tableName+" WHERE username='"+player.getName()+"'");

		try {
			if (resultSet.first()) {
				plugin.getLogger().info("Got username "+resultSet.getString("username"));
			} else {
				// No player by the username exists
			}
		} catch (SQLException exception) {

		}

		return null;
	}

	public void createPlayer (Player player) {
		updateQuery("INSERT INTO "+tableName+" (username, kills, deaths) VALUES ('"+player.getName()+"', 0, 0)");
	}

	public void updateQuery (String query) {
		//databaseHandler.query(query);
	}

	public void connect() {
		/*if (!databaseHandler.checkConnection()) {
			databaseHandler.open();
		}

		boolean exists = databaseHandler.checkTable(tableName);
		if (!exists) {
			// Create it.
			databaseHandler.createTable(createTableQuery);
		} else {
			plugin.getLogger().info("Table exists.");
		}*/
	}

	public DatabaseManager (TopPvP plugin) {
		/*this.plugin = plugin;

		// TODO database type
		// TODO database connection details..
		String type = "mysql"; // mysql | sqlite | h2
		if (type.equals("mysql")) {
			databaseHandler = new MySQL(plugin.getLogger(), "", "localhost", "3306", "minecraft", "root", "password");
		} else if (type.equals("sqlite")) {
			databaseHandler = new SQLite(plugin.getLogger(), "", "minecraft", plugin.getDataFolder().getAbsolutePath());
		} else if (type.equals("h2")) {
			// init for h2
			throw new RuntimeException("Unsupported database type "+type);
		} else {
			throw new RuntimeException("Unknown database type "+type);
		}

		// if no exception thrown, announce what db type plugin is using
		plugin.getLogger().info("Using "+type);

		TopPvP.log("Connecting to DB");
		this.connect();*/
	}
}
