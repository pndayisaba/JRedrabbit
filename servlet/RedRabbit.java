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
		if(request.getParameter("email") !=null)
			return request.getParameter("email");
		
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
		   
		   String[] data = params.split("&");
		   HashMap<String, String>  inputParams = new HashMap<String, String>();
		   for(String s: data)
		   {
			   String[] Q = s.split("=");
			   if(Q.length == 2)
			   {
				   inputParams.put(Q[0], Q[1]);
			   }
		   }
		   
		   return inputParams;
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
