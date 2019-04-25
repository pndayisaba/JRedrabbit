import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.net.URLDecoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;
import java.util.regex.Pattern;

import org.json.JSONObject;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileSystems;

public class RedRabbit {
	
  /**
	 * Get user information from the database based on the parameter email;
	 * @param email: user email for lookup
	 * @return ArrayList;
	 */
	public static ArrayList<Map<String, Object>> getUserInfoByEmail(String email)
	{
		try {
		  DatabaseConnection dbc = new DatabaseConnection("CALL user_sps(?)");
			dbc.prepStatement.setString(1,email);
			dbc.run();
			//System.out.println("FROM getUserByEmail()! Return: "+dbc.DataSet);
			return dbc.DataSet;
		}
		catch (Exception ex)
		{
			System.out.println(ex.getStackTrace());
		}
		
		return new ArrayList<Map<String, Object>>();
	}
	/**
	 * Get cookie value given cookieName parameter;
	 * @param request: HttpServletRequest object;
	 * @param cookieName:   
	 * @return String;
	 */
	public static String getCookieByName(HttpServletRequest request, String cookieName)
	{
		if(cookieName !=null && !"".equals(cookieName.trim()))
		{	
			Cookie[] cookies =  request.getCookies();
	  	  
		     if(cookies !=null)
		     {
		     	for(Cookie cookie: cookies)
		     	{
		     	 if(cookieName.equals(cookie.getName().toString()) && cookie.getValue() !=null && !"".equals(cookie.getValue()))
		     		 return cookie.getValue().toString(); 
		     	}
		      }
		}
	     return "";
	}
	
	/**
	 * Parse input stream parameters. Useful for request method like PUT, DELETE...
	 * 
	 * @param request
	 * @return HashMap
	 */
	public static HashMap<String, String> parseInputStreamParameters(HttpServletRequest request)  throws ServletException, IOException
	{
	  BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	   String params = br.readLine();
	   params = java.net.URLDecoder.decode(params, "UTF-8");
	   String[] data = params.split("&");
	   HashMap<String, String>  inputParams = new HashMap<String, String>();
	   
	   for(int j=0; j < data.length; ++j)
	   {
		   String[] Q = data[j].split("=");
		   if(Q.length == 2)
		   {
			   inputParams.put(Q[0], Q[1]);
		   }
	   }
	   return inputParams;
	}
	
  /**
   * @param obj: Object such as ArrayList, HashMap ... to use for building a json;
   * @return String: JSON string;
   */
  public static String createJson(Object obj)
  {
	  GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();      
    Gson gson = builder.create();
    String strJSON = gson.toJson(obj);
    return strJSON;
  }
	
	/**
	 * Load database connection props from json file;
	 * @return HashMap;
	 */
  public static HashMap<String, String> loadDatabaseCredentials()
	{
	  try
	  {
      HashMap<String, String> config = new HashMap<String, String>();
      String webRoot = RedRabbit.getWebRoot();
      File jsonFile = new File(webRoot + "WEB-INF/config/database.json");
      JsonReader reader  = new JsonReader(new FileReader(jsonFile));
  	  config = new Gson().fromJson(reader, HashMap.class);
  	  return config;	
	  } catch(Exception ex)
	  {
	    System.out.println("RedRabbit() Creating database config failed!");
	    System.out.println("Error Message: " + ex.getMessage());
	  }
	  return new HashMap<String, String>();
	}
	
	/**
	 * Get site document root from system canonical path;
	 * @return String;
	 */
	public static String getWebRoot()
	{
	  try
	  {
	    String catalinaBase = System.getProperty("catalina.base");
      if (catalinaBase.isEmpty())
        System.out.println("Loading CatalinaBase Failed!");
      else 
        return catalinaBase +"/webapps/JRedrabbit/";
	  } catch(Exception ex)
	  {
	    System.out.println("RedRabbit.getSiteRoot() failed with message: " + ex.getMessage());
	  } 
    return "";
	}
	
// -- END --	
}
