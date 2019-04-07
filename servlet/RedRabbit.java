import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.net.URLDecoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class RedRabbit {
	
	/**
	 * Get user information from the database based on the parameter email;
	 * @param email: user email for lookup
	 * @return ArrayList;
	 */
	public static ArrayList<Map<String, Object>> getUserInfoByEmail(String email)
	{
		DatabaseConnection dbc = new DatabaseConnection("CALL user_sps(?)");
		try {
			dbc.prepStatement.setString(1,email);
			dbc.run();
			//System.out.println("FROM getUserByEmail()! Return: "+dbc.DataSet);
			return dbc.DataSet;
		}
		catch (Exception ex)
		{
			//return ex.toString();
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
		// Parse PUT parameters
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
}
