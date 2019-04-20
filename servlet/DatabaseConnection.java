import com.mysql.cj.jdbc.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author pndayisaba;
 * Connect to database and return a record set;
 */

public class DatabaseConnection {
  private String serverName ="localhost";
  private String dbName ="redrabbit";
  private String userName ="webuser";
  private String password="!@webuser100";
  private MysqlDataSource ds = null;
  // A collection of ResultSet
  public ArrayList<Map<String, Object>> DataSet = new ArrayList<Map<String, Object>>(); 
  public Connection conn = null;
  public PreparedStatement prepStatement = null;
  
  public DatabaseConnection(String query)
  {
  	this.ds = new MysqlDataSource();
    this.ds.setUser(this.userName);
    this.ds.setDatabaseName(dbName);
    this.ds.setServerName(this.serverName);
    this.ds.setPassword(this.password);
    
    try
    {
    	this.conn = this.ds.getConnection();
      this.prepStatement = this.conn.prepareStatement(query);   
    }
    catch(Exception ex)
    {
        ex.printStackTrace();	
    }
  }
    
  public void run()
  {
  	try
    {
      ResultSet rs = this.prepStatement.executeQuery();
      ResultSetMetaData md =  rs.getMetaData();
      int columnCount = md.getColumnCount();
      while(rs.next())
      {
        HashMap<String,Object> row = new HashMap<String, Object>(); 
        for(int q=1; q <= columnCount; q++)
        {
          row.put(md.getColumnName(q),rs.getObject(q));
        }
        this.DataSet.add(row);
      }

      rs.close();
      this.prepStatement.close();
      this.conn.close();	   
    } catch(Exception ex)
    {
      ex.printStackTrace();	
    }
  }
}


