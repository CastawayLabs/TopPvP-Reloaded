package org.CastawayDevelopment.TopPvP.Managers;

import java.util.LinkedHashSet;
import java.util.Map;
import org.CastawayDevelopment.TopPvP.TopPvP;
import org.bukkit.entity.Player;

/**
 *
 * @author DarkSeraphim
 */
public class PlayerClass extends AbstractPlayerClass
{
    
    public class Incrementable
    {
        private int value;
        
        Incrementable()
        {
            this(0);
        }
        
        Incrementable(int def)
        {
            this.value = def;
        }
        
        public int getValue()
        {
            return this.value;
        }
        
        public void increment()
        {
            this.value++;
        }
        
        public void setValue(int i)
        {
            this.value = i;
        }
    }
    
    private final int id;
    
    private Incrementable kills;
    
    private Incrementable deaths;
    
    private final LinkedHashSet<String> lastKills = new LinkedHashSet<String>();
    
    protected PlayerClass(Player player)
    {
        super(player);
        Map<String, Object> data = TopPvP.getDatabaseManager().getPlayerData(player);
        this.id = (Integer) data.get("id");
        this.kills = new Incrementable((Integer) data.get("kills"));
        this.deaths = new Incrementable((Integer) data.get("deaths"));
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public Incrementable getKills()
    {
        return this.kills;
    }
    
    public Incrementable getDeaths()
    {
        return this.deaths;
    }
    
    public LinkedHashSet<String> getLastKills()
    {
        return this.lastKills;
    }
    
    public void update()
    {
        TopPvP.getDatabaseManager().updatePlayer(this);
        TopPvP.getScoreboardManager().updatePlayer(this);
    }
    
}
