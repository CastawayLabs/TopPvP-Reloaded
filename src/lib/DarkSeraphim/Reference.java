package lib.DarkSeraphim;

public class Reference 
{
    private final String table;
    
    private final String column;
    
    public Reference(String table, String column)
    {
        this.table = table;
        this.column = column;
    }
    
    public String getTable()
    {
        return this.table;
    }
    
    public String getColumn()
    {
        return this.column;
    }

}
