import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Extend HttpServlet class
public class Account extends HttpServlet {
 
   private String message;
   protected String email;
   protected String password;
   private String errorMessage;

   public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
      // Do required initialization
      Cookie[] cookies = request.getCookies();
      if(cookies !=null)
      {
	for(Cookie cookie: cookies)
        {
	   if(cookie.getName().equals("email") && cookie.getValue() !="")
		this.email = cookie.getValue();
	}
      }   
      
      //if(this.email =="")
      //{
	  response.setContentType("text/html");
          response.setStatus(HttpServletResponse.SC_FOUND);
      	  response.setHeader("Location","http://localhost");
     // }
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
     
	this.message = "My Profile....";
      // Set response content type
      response.setContentType("text/html");
	request.setAttribute("contentFile","account.jsp");
	request.setAttribute("message",this.message);
	RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
      rd.forward(request,response);
	
   }
   
   
   

   public void destroy() {
      // do nothing.
   }
}


