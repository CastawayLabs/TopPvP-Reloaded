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
package org.CastawayDevelopment.TopPvP;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.CastawayDevelopment.TopPvP.Commands.*;
import org.CastawayDevelopment.TopPvP.Listeners.*;
import org.CastawayDevelopment.TopPvP.Managers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TopPvP extends JavaPlugin
{

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
    private static DatabaseManager databaseManager;
    private static ScoreboardManager scoreboardManager;
    private static PlayerManager playerManager;
    
    /*
     * Commands
     */
    private BountyCommand bountyCmd;
    private SetCommand setCmd;
    private ResetCommand resetCmd;

    /**
     * Get this central plugin name.
     *
     * @return Name.
     */
    public static String getPluginName()
    {
        return TopPvP.active.getDescription().getName();
    }

    /**
     * Log using the TopPvP logger. (Prefixed with
     * <code>[TopPvP]</code>; to add on component, add prefix to message.)
     *
     * @param msg Message to log with level <code>INFO</code>.
     */
    public static void log(String msg)
    {
        log(Level.INFO, msg);
    }

    /**
     * Log using the TopPvP logger. (Prefixed with
     * <code>[TopPvP]</code>; to add on component, add prefix to message.)
     *
     * @param level Level to log with.
     * @param msg Message to log.
     */
    public static void log(Level level, String msg)
    {
        Logger.getLogger("TopPvP").log(level, "[TopPvP] " + msg);
    }

    public static DatabaseManager getDatabaseManager()
    {
        return databaseManager;
    }

    public static ScoreboardManager getScoreboardManager()
    {
        return scoreboardManager;
    }
    
    public static PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    /**
     *
     *
     */
    @Override
    public void onEnable()
    {
        // Vault Hook
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            getServer().getLogger().severe("================= TopPvP- Reloaded ==================");
            getServer().getLogger().severe("Vault is required for TopPvP- Reloaded to operate!");
            getServer().getLogger().severe("Please install Vault first!");
            getServer().getLogger().severe("You can find the latest version here:");
            getServer().getLogger().severe("http://dev.bukkit.org/server-mods/vault/");
            getServer().getLogger().severe("==============================================");
            setEnabled(false);
            return;
        }
        // TODO check if scoreboard plugin exists!

        // Plugin folder
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdirs();
        }

        TopPvP.active = this;

        scoreboardManager = new ScoreboardManager(this);

        // Hooks
        EntityListener deathListener = new EntityListener(this);
        PlayerListener joinListener = new PlayerListener(this);
        
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(joinListener, this);
        pm.registerEvents(deathListener, this);

        // Database
        databaseManager = new DatabaseManager(this);
        
        // Players
        playerManager = new PlayerManager(this);
        
        initCommands();
    }
    
    private void initCommands()
    {
        this.bountyCmd = new BountyCommand(this);
        this.setCmd = new SetCommand(this);
        this.resetCmd = new ResetCommand(this);
    }

    /**
     *
     *
     */
    @Override
    public void onDisable()
    {
        TopPvP.active = null;
        TopPvP.databaseManager = null;
        TopPvP.scoreboardManager = null;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args)
    {
        Sub sub;
        String cmdName;
        boolean alias = true;
        if(!label.equalsIgnoreCase("toppvp"))
            cmdName = label;
        else
            if(args.length < 1)
                cmdName = "help";
            else
            {
                alias = false;
                cmdName = args[0];
                args = Arrays.copyOfRange(args, 1, args.length);
            }
            
        try
        {
            sub = Sub.valueOf(cmdName);
        }
        catch(IllegalArgumentException ex)
        {
            return false;
        }
        
        TopPvPCommand cmd;
        
        switch(sub)
        {
            case BOUNTY:
                cmd = this.bountyCmd;
                break;
            case SET:
                cmd = this.setCmd;
                break;
            case RESET:
                cmd = this.resetCmd;
                break;
            default:
                sender.sendMessage(ChatColor.DARK_RED+"Command not found");
                return true;
        }
        
        if(cmd.needsPlayer() && sender instanceof Player == false)
        {
            sender.sendMessage(ChatColor.DARK_RED+"This command is only for ingame players");
        }
        else
        {
            cmd.execute(sender, args, alias);
        }
        
        return true;
    }
    
    private enum Sub
    {
        TOPPVP,
        BOUNTY,
        SET,
        RESET
    }
}
