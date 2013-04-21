package com.CastawayDevelopment.TopPvP.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.CastawayDevelopment.TopPvP.TopPvP;

public class TopPVPKillDeathListener implements Listener{
	private static TopPvP plugin;
	private Player killedplayer;
	private Player murderousplayer;
	
	public TopPVPKillDeathListener () {
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * Player Death Listener
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){ 
        	
            if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent){ 
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
                
                if(entityDamageByEntityEvent.getDamager() instanceof Player){ 
                	
                    murderousplayer = (Player)entityDamageByEntityEvent.getDamager();
                    killedplayer = (Player) entity;
                }
            }   
        }        
	}           
                    
                    
                    
                    
                    
                    
                    
	}
