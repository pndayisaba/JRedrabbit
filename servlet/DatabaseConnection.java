import java.io.*;
import com.mysql.cj.jdbc.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;
import org.json.JSONObject;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

/**
 * @author pndayisaba;
 * Connect to database and return a record set;
 */
public class DatabaseConnection {
  private MysqlDataSource ds = null;
  public ArrayList<Map<String, Object>> DataSet = new ArrayList<Map<String, Object>>(); 
  public Connection conn = null;
  public PreparedStatement prepStatement = null;
  
  public DatabaseConnection(String query) // throws SQLException
  {
    
  	try
    {
  	  //query = "CALL forum_sps(0)";
      this.ds = new MysqlDataSource();
      this.ds.setServerTimezone("UTC");
      HashMap<String, String> config = RedRabbit.loadDatabaseCredentials();
      this.ds.setUser(config.get("userName"));
      this.ds.setDatabaseName(config.get("dbName"));
      this.ds.setServerName(config.get("serverName"));
      this.ds.setPassword(config.get("password"));
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


