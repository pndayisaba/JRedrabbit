import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class BaseService {
	
	public void redirectUser(HttpServletRequest request, HttpServletResponse response)
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
	}
}
