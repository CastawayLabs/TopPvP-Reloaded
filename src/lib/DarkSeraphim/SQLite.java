package org.sensationcraft.login.sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;

public class SQLite extends Database
{

	private enum Type
	{
		INSERT("INSERT"),
		UPDATE("UPDATE"),
		DELETE("DELETE"),
		SELECT("SELECT"),
		CREATE("CREATE"),
		NONE("");

		private final String sql;

		private Type(String sql)
		{
			this.sql = sql;
		}

		public String getSQL()
		{
			return this.sql;
		}

		public static Type getType(String keyword)
		{
			for(Type t : values())
			{
				if(t.getSQL().equalsIgnoreCase(keyword)) return t;
			}
			return Type.NONE;
		}
	}

	private File dbfile;

	public SQLite(Logger log, File dbfile)
	{
		super(log);
		this.dbfile = dbfile;
		if(!this.dbfile.exists())
		{
			try
			{
				if(!this.dbfile.createNewFile())
					throw new IOException("Failed to create file");
			}
			catch(IOException ex)
			{
				this.log("Failed to find (and create) file at %s", this.dbfile.getPath());
				this.dbfile = null;
			}
		}
	}

	@Override
	public boolean initialize()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return this.dbfile != null && this.dbfile.exists();
		}
		catch(ClassNotFoundException ex)
		{
			this.log("SQLite library not found!");
			return false;
		}
	}

	@Override
	public boolean connect()
	{
		if(this.initialize())
		{
			try
			{
				this.con = DriverManager.getConnection("jdbc:sqlite:" + this.dbfile.getAbsolutePath());
			}
			catch (SQLException ex)
			{
				this.log("Failed to establish a SQLite connection, SQLException: ", ex.getMessage());
			}
		}
		return this.con != null;
	}

	@Override
	public boolean checkTable(String name)
	{
		if(!this.isReady()) return false;
		try
		{
			DatabaseMetaData meta = this.con.getMetaData();
			ResultSet result = meta.getTables(null, null, name, null);
			boolean ret = result.next();
                        return ret;
		}
		catch(SQLException ex)
		{
			// Swallow the exception, as it is a conditional
			//log("Table %s does not exist", name);
		}
		// Check
		return false;
	}

	@Override
	public void createTable(TableBuilder builder)
	{
		if(!this.isReady()) return;
                
		StringBuilder table = new StringBuilder("CREATE TABLE `").append(builder.getTableName()).append("`(");
		for(Map.Entry<String, PropertyList> property : builder.getColumns().entrySet())
		{
			table.append(property.getKey()).append(" ").append(property.getValue().getProperties()).append(",");
		}
                
                String pkey = builder.getPrimaryKey();
                if(pkey != null)
                    table.append(String.format("PRIMARY KEY(%s),", pkey));

                for(Map.Entry<String, Reference> reference : builder.getReferences().entrySet())
                {
                    if(reference.getValue() != null)
                    table.append(String.format("FOREIGN KEY %s REFERENCES `%s`(%s)", reference.getKey(), reference.getValue().getTable(), reference.getValue().getColumn()));
                }
                
		// Delete the last comma
		if(builder.getColumns().size() > 0) {
			table.deleteCharAt(table.length() - 1);
		}
		String query = table.append(");").toString();
                
		this.executeQuery(query);
        }

	@Override
	public ResultSet executeQuery(String query)
	{
		if(!this.isReady()) return null;

		ResultSet result = null;

		try
		{
			Statement stmt = this.con.createStatement();
			switch(this.getQueryType(query))
			{
			case INSERT:
			case UPDATE:
			case DELETE:
			case CREATE:
                                stmt.executeUpdate(query);
				break;
			default:
				result = stmt.executeQuery(query);
				break;
			}
		}
		catch(SQLException ex)
		{
			this.log("An exception has occurred while executing query '%s': %s", query, ex.getMessage());
		}

		return result;
	}

	@Override
	public PreparedStatement prepare(String query)
	{
		if(!this.isReady()) return null;
		PreparedStatement stmt = null;
		try
		{
			stmt = this.con.prepareStatement(query);
		}
		catch(SQLException ex)
		{
                        ex.printStackTrace();
			this.log("An exception has occurred while preparing query '%s': %s", query, ex.getMessage());
		}
		return stmt;
	}

	private SQLite.Type getQueryType(String query)
	{
		String typename = query.split(" ")[0];
		return Type.getType(typename);
	}

}
