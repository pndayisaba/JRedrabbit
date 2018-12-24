import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


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
	 * Get user email from the request cookie;
	 * @param request - HttpServletRequest object;
	 * @return String;
	 */
	public static String getUserEmailFromCookie(HttpServletRequest request)
	{
		Cookie[] cookies =  request.getCookies();
  	  
	     if(cookies !=null)
	     {
	     	for(Cookie cookie: cookies)
	     	{
	     	 if(cookie.getName().equals("email"))
	     		 return cookie.getValue().toString(); 
	     	}
	      }
	     return "";
	}
	
	/*public static void redirectUser(HttpServletRequest request, HttpServletResponse response)
	{
		String email = "";
		//If the user is not logged in redirect to the homepage;
	   Cookie[] cookies = request.getCookies();
	   if(cookies !=null)
	   {
	      for(Cookie cookie: cookies)
	      {
		      if(cookie.getName().equals("email") && cookie.getValue() !="")
			      email = cookie.getValue();
		  }
	   }   
      
      if(email =="")
      {
	      response.setContentType("text/html");
          response.setStatus(HttpServletResponse.SC_FOUND);
      	  response.setHeader("Location","/");
     }
	}*/
}
