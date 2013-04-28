package com.CastawayDevelopment.TopPvP.Listeners;

import com.CastawayDevelopment.TopPvP.TopPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author matejkramny
 */
public class TopPVPPlayerJoinListener implements Listener {
	private Player player;
	private TopPvP plugin;

	public TopPVPPlayerJoinListener (TopPvP plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event){
		player = event.getPlayer();

		plugin.getScoreboardManager().update();
		//plugin.getScoreboardManager().addPlayer(player);
	}
}
