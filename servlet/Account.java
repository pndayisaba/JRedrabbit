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
     
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
	 
	   //If the user is not logged in redirect to the login page;
	   this.email = RedRabbit.getUserEmailFromCookie(request); 
	      
      if(this.email.length() == 0)
      {
	      response.setContentType("text/html");
          response.setStatus(HttpServletResponse.SC_FOUND);
      	  response.setHeader("Location","/login");
     }
     
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


