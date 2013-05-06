package com.CastawayDevelopment.TopPvP.Managers;

import com.CastawayDevelopment.TopPvP.TopPvP;
import org.bukkit.entity.Player;
import codebukkit.scoreboardapi.Scoreboard;
import codebukkit.scoreboardapi.ScoreboardAPI;

import java.util.Map;

/**
 * @author matejkramny
 */
public class ScoreboardManager {
	private TopPvP plugin;
	private ScoreboardAPI api;
	private Scoreboard kills;
	//private Scoreboard deaths;

	public ScoreboardManager (TopPvP plugin) {
		this.plugin = plugin;
		createScoreboards();
	}

	public void createScoreboards() {
		api = ScoreboardAPI.getInstance();
		kills = api.createScoreboard("toppvp_kills", 0);
		kills.setType(Scoreboard.Type.SIDEBAR);
		kills.setScoreboardName("Kills");

		//deaths = api.createScoreboard("toppvp_deaths", 1);
		//deaths.setType(Scoreboard.Type.SIDEBAR);
		//deaths.setScoreboardName("Deaths");
	}

	public void update() {
		TopPvP.log("Hello");

		Player players[] = plugin.getServer().getOnlinePlayers();
		for (Player player : players) {
			Map dbPlayer = plugin.getMysqlDatabase().getPlayer(player);
			if (dbPlayer == null) {
				// Not found?
				TopPvP.log("NULL");
				continue;
			}

			int killsInt = (Integer)dbPlayer.get("kills");
			int deathsInt = (Integer)dbPlayer.get("deaths");

			kills.setItem(player.getName(), killsInt);
			//deaths.setItem(player.getName(), deathsInt);
			if (!kills.hasPlayerAdded(player)) {
				kills.showToPlayer(player, true);
			}
			//if (!deaths.hasPlayerAdded(player)) {
			//	deaths.showToPlayer(player, true);
			//}
		}
	}

	public void addPlayer (Player player) {
		Map dbPlayer = plugin.getMysqlDatabase().getPlayer(player);
		kills.setItem(player.getName(), (Integer)dbPlayer.get("kills"));
		//deaths.setItem(player.getName(), (Integer)dbPlayer.get("deaths"));
	}

}
