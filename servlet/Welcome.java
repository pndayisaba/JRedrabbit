import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class Welcome extends HttpServlet {
 
   private String message;

   public void init() throws ServletException {
      // Do required initialization
      message = "LOGIN FROM HERE...";
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      
      // Set response content type
      response.setContentType("text/html");
	request.setAttribute("contentFile","welcome.jsp");
	RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
      rd.forward(request,response);
	
	// Actual logic goes here.
      //PrintWriter out = response.getWriter();
      //out.println("<h1>" + message + "</h1>");
   }

   public void destroy() {
      // do nothing.
   }
}


