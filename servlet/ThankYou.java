import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Extend HttpServlet class
public class ThankYou extends HttpServlet {
 
   private String message;

   public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
      // Do required initialization
      
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      // Set response content type
      response.setContentType("text/html");
	  request.setAttribute("contentFile","thankyou.jsp");
	  RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
      rd.forward(request,response);
	
   }
   

   public void destroy() {
      // do nothing.
   }
}


