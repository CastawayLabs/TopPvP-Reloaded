package com.CastawayDevelopment.TopPvP.Listeners;

import com.CastawayDevelopment.TopPvP.Database;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.CastawayDevelopment.TopPvP.TopPvP;

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
					Map dbKiller = plugin.getMysqlDatabase().getPlayer((Player)killer);
					Map dbVictim = plugin.getMysqlDatabase().getPlayer((Player)victim);
					int kills = (Integer)dbKiller.get("kills");
					int deaths = (Integer)dbVictim.get("deaths");
					kills++;
					deaths++;
					// update db
					Database db = plugin.getMysqlDatabase();
					db.updateQuery("UPDATE "+Database.tableName+" SET kills='"+kills+"' WHERE username='"+((Player) killer).getName()+"'");
					db.updateQuery("UPDATE "+Database.tableName+" SET deaths='"+deaths+"' WHERE username='"+((Player) victim).getName()+"'");
					plugin.getScoreboardManager().update();
				} else if (killer instanceof Monster) {
					// Player killed by monster
				}
			}
		} else if (victim instanceof Monster) {
			// Monster was killed
		}
	}
}
