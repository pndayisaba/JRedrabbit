import java.io.*;
import java.util.*;
import java.time.*;

import javax.sql.RowSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.*;

import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


//import com.redrabbit.common.*;

public class TestX{
	
  public static void readJSON() throws Exception
  {
    
    
  }
  
  public static void main(String args[])
  {
    try
    {
      HashMap<String, String> config = RedRabbit.loadDatabaseCredentials();
      
      System.out.println("CONFIG: " + config);
    } catch(Exception ex)
    {
      System.out.println("PROBLEM: ");
      System.out.println(ex);
    }
    
  }
}
