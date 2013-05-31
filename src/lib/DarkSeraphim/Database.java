package org.sensationcraft.login.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sensationcraft.login.SCLogin;

public abstract class Database
{

	protected Connection con;

	private final Logger log;

	protected Database(Logger log)
	{
		this.log = log;
	}

	public abstract boolean initialize();

	public abstract boolean connect();
        
        public void close()
        {
            try
            {
                this.con.close();
                this.con = null;
            }
            catch(SQLException ex)
            {
                log("An exception occurred while closing the connection: %s", ex.getMessage());
            }
        }

	public boolean isReady()
	{
		if(this.con == null)
		{
			this.log("Tried to execute a query or to prepare a statement while the connection was not ready.");
			return false;
		}
		return true;
	}

	public abstract boolean checkTable(String name);

	public abstract void createTable(TableBuilder builder);

	public abstract ResultSet executeQuery(String query);

	public abstract PreparedStatement prepare(String query);
        
        public static void synchronizedExecuteUpdate(final PreparedStatement stmt, final Object lock, final Object...params)
        {
            synchronized(lock)
            {
                try
                {
                    for(int i = 1; i <= params.length; i++)
                    {
                        stmt.setObject(i, params[i-1]);
                    }
                    stmt.executeUpdate();
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        
        public static ResultSet synchronizedExecuteQuery(final PreparedStatement stmt, final Object lock, final Object...params)
        {
            synchronized(lock)
            {
                try
                {
                    for(int i = 1; i <= params.length; i++)
                    {
                        stmt.setObject(i, params[i-1]);
                    }
                    return stmt.executeQuery();
                }
                catch(SQLException ex)
                {
                    ex.printStackTrace();
                }
                return null;
            }
        }

	protected void log(String msg, Object...o)
	{
                if(SCLogin.debug)
                {
                    StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                    for(StackTraceElement t : trace)
                    {
                        System.out.println(String.format("Called by %s, in %s on %d", t.getMethodName(), t.getFileName(), t.getLineNumber()));
                    }
                }
		this.log.log(Level.SEVERE, String.format(msg, o));
	}
}
