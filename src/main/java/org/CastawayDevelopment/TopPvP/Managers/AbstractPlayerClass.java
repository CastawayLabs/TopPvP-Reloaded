package org.CastawayDevelopment.TopPvP.Managers;

import org.bukkit.entity.Player;

/**
 * Do not use any of the Player methods when the PlayerQuitEvent has been processed
 * 
 * @author DarkSeraphim
 */
public abstract class AbstractPlayerClass
{
    
    private final Player player;
    
    private boolean valid = true;
    
    protected AbstractPlayerClass(Player player)
    {
        this.player = player;
    }
    
    protected void quit()
    {
        this.valid = false;
    }
    
    public boolean isValid()
    {
        return this.valid;
    }
    
    public String getName()
    {
        return this.player.getName();
    }
    
}
