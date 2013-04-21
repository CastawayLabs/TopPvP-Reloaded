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
package com.CastawayDevelopment.TopPvP;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TopPvP extends JavaPlugin {
	
	/**
	 * Access Permissions via Vault
	 */
	public static Permission permission = null;
	
	/**
	 * Access Economy via Vault
	 */
	public static Economy economy = null;

	/**
	 * Access private instance of this plugin.
	 */
	private static TopPvP active = null;
	
	/**
	 * Get this central plugin name.
	 * 
	 * @return Name.
	 */
	public static String getPluginName() {
		return TopPvP.active.getDescription().getName();
	}
	
	/**
	 * Log using the TopPvP logger.
	 * (Prefixed with <code>[TopPvP]</code>; to add on component, add prefix
	 * to message.)
	 * 
	 * @param msg
	 *            Message to log with level <code>INFO</code>.
	 */
	public static void log(String msg) {
		log(Level.INFO, msg);
	}

	/**
	 * Log using the TopPvP logger.
	 * (Prefixed with <code>[TopPvP]</code>; to add on component, add prefix
	 * to message.)
	 * 
	 * @param level
	 *            Level to log with.
	 * @param msg
	 *            Message to log.
	 */
	public static void log(Level level, String msg) {
		Logger.getLogger("TopPvP").log(level, "[TopPvP] " + msg);
	}
	
	/**
	 * 
	 * 
	 */

	public void onEnable() {
		
		//Vault Hook
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			getServer().getLogger().severe("================= TopPvP- Reloaded ==================");
			getServer().getLogger().severe("Vault is required for TopPvP- Reloaded to operate!");
			getServer().getLogger().severe("Please install Vault first!");
			getServer().getLogger().severe("You can find the latest version here:");
			getServer().getLogger().severe("http://dev.bukkit.org/server-mods/vault/");
			getServer().getLogger().severe("==============================================");
			setEnabled(false);
			return;
		}
		
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		
		TopPvP.active = this;
		
	}
	
	/**
	 * 
	 * 
	 */
	@Override
	public void onDisable() {
		
		TopPvP.active = null;
	}
	
	
}