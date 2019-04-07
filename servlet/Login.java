import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Extend HttpServlet class
public class Login extends HttpServlet {
 
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
	     {
		response.setContentType("text/html");
		response.setHeader("Location","/account");
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY); //SC_FOUND: 302 redirect;
	     }
	  }
      }
	
      message = "LOGIN FROM HERE...";

     this.errorMessage = "<script type=\"text/javascript\">"+
     "var responseData = [{\"message\":\"Could not sign you in. Incorrect credentials!\",\"success\":0}];"+
     "</script>";
    
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
     
	this.message = "Info Coming Soon...";
      // Set response content type
      response.setContentType("text/html");
	request.setAttribute("contentFile","login.jsp");
	request.setAttribute("message",this.message);
	RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
      rd.forward(request,response);
	
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
	 
	   try 
	   {
	     this.message = "Info Coming Soon...";
	      // Set response content type
	      response.setContentType("text/html");
	      if(request.getParameterMap().containsKey("email") && request.getParameterMap().containsKey("password"))
	      {
		      String email = request.getParameter("email");
		      String password = request.getParameter("password");
		      
		      DatabaseConnection dbc = new DatabaseConnection("CALL user_login_sps(?,?)");
		      dbc.prepStatement.setString(1, email);
		      dbc.prepStatement.setString(2, password);
		      dbc.run();
			
		      GsonBuilder builder = new GsonBuilder();
		      builder.setPrettyPrinting();      
		      
		      Gson gson = builder.create();
		      
		      if(dbc.DataSet.get(0).get("email").toString() !="")
		      {
		    	  Cookie cookie = new Cookie("email",dbc.DataSet.get(0).get("email").toString());
		    	  cookie.setMaxAge(3600*5);
		    	  cookie.setPath("/");
		    	  cookie.setDomain(request.getServerName());
		    	  
		    	  response.addCookie(cookie);
			
		      	  String jsonStr = gson.toJson(dbc.DataSet);
		      	  jsonStr = "<script>var responseData = "+jsonStr+"</script>";
			  request.setAttribute("jsSnippet",jsonStr);

		      	  this.message = "Congratulations!<br />You are now Signed into your account!";

			                response.setContentType("text/html");
                response.setHeader("Location","/account");
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY); //SC_FOUND: 302 redirect;
		      }


		      else
		      {

			 request.setAttribute("jsSnippet",this.errorMessage);
		      }
	      }
	     else
	    {
		this.errorMessage = "<script type=\"text/javascript\">var responseData = "+
		"[{message:\"Your email and password are required to sign you into your account! Try again to continue.\",success:0}];"+
		"</script>";
	    }
	      
	     PrintWriter out = response.getWriter();
	     out.print("GREETINGS FROM PRINTWRITER");
		request.setAttribute("contentFile","login.jsp");
		request.setAttribute("message",this.message);
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
	      rd.forward(request,response);
	   }
	   catch(Exception ex)
	   {
		   System.out.println(ex);
	   }
			
   }
   
   

   public void destroy() {
      // do nothing.
   }
}


