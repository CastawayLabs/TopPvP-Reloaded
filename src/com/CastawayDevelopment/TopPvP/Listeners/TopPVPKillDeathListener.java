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

//Test
package com.CastawayDevelopment.TopPvP.Listeners;

import com.CastawayDevelopment.TopPvP.DatabaseManager;
import com.avaje.ebean.annotation.Sql;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.CastawayDevelopment.TopPvP.TopPvP;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TopPVPKillDeathListener implements Listener {
	private TopPvP plugin;

	public TopPVPKillDeathListener (TopPvP plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Player Death Listener
	 *
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity victim = event.getEntity();

		if(victim instanceof Player){
			// Player was killed

			if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) victim.getLastDamageCause();
				Entity killer = entityDamageByEntityEvent.getDamager();

				if (killer instanceof Player) {
					// Player killed by player
					// log it.
					DatabaseManager manager = plugin.getDatabaseManager();
					ResultSet dbKiller = manager.getPlayer((Player) killer);
					ResultSet dbVictim = manager.getPlayer((Player) victim);

					try {
						int kills = dbKiller.getInt("kills");
						int deaths = dbVictim.getInt("deaths");

						dbKiller.updateInt("kills", kills++);
						dbVictim.updateInt("deaths", deaths++);

						// Update records (could be more efficient using a single SQL statement..)
						dbKiller.updateRow();
						dbVictim.updateRow();

						plugin.getScoreboardManager().update();
					} catch (SQLException exception) {
						plugin.getLogger().warning("Could not get/update players (death)");
					}
				} else if (killer instanceof Monster) {
					// Player killed by monster
				}
			}
		} else if (victim instanceof Monster) {
			// Monster was killed
		}
	}
}
