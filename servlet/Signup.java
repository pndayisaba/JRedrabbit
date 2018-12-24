import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Extend HttpServlet class
public class Signup extends HttpServlet {
 
   private String message;
   protected String email;
   protected String password;
   private String errorMessage;
   private String[] requiredFields = {"email", "password", "password2", "first_name", "last_name"};
   private ArrayList<Map<String, String>> uiResponse = new ArrayList<Map<String, String>>();

   public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
      // Do required initialization
      
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
     
	this.message = "SIGNUP Info Coming Soon...";
      // Set response content type
      response.setContentType("text/html");
	  request.setAttribute("contentFile","signup.jsp");
	  request.setAttribute("message",this.message);
	  RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
      rd.forward(request,response);
	
   }
   
   /**
    * Register user;
    */
   public void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
	   
	    // Set response content type
	    response.setContentType("text/html");
	 
	   try 
	   {
		 //ArrayList<String> required = new ArrayList<String>(); // {"email", "password", "first_name", "last_name"};
		 
		 
	     this.message = "Info Coming Soon...";
	     this.uiResponse.clear();
	     HashMap<String, String> error = new HashMap<String, String>();
	      
	      if(request.getParameterMap().containsKey("email") && request.getParameterMap().containsKey("password"))
	      {
	    	  for(String s: this.requiredFields)
	    	  {
	    		  //this.message +=";; S: "+s;
	    		  
	    		  if(!request.getParameterMap().containsKey(s) || "".equals(request.getParameter(s)))
	    		  {
	    			  
	    			  error.put("name", s);
	    			  error.put("success", "0");
	    			  error.put("message", "Required!");
	    			  this.uiResponse.add(this.uiResponse.size(), error);
	    		  }
	    	  }
	    	  
	    	  if(this.uiResponse.size() ==0)
	    	  {
	    		  // Check that the user doesn't exists;
	    		 this.email = request.getParameter("email"); 
	    		  //RedRabbit redrabbit = new RedRabbit();
	    		  ArrayList<Map<String, Object>> userInfo = RedRabbit.getUserInfoByEmail(this.email);
	    		  this.message +="[dbc UserInfo: "+userInfo+"] ";
	    		  if(!userInfo.isEmpty())
	    		  {
	    			  error.clear();
	    			  error.put("name", "email");
	    			  error.put("success", "0");
	    			  error.put("message", "Email ["+request.getParameter("email")+"] is already registered!");
	    			  this.uiResponse.add(this.uiResponse.size(),error);
	    		  }
	    		  
	    		  // Check that password matches
	    		  if(this.uiResponse.size()==0 && !request.getParameter("password").equals(request.getParameter("password2")))
	    		  {
	    			  error.clear();
	    			  error.put("name", "password");
	    			  error.put("success", "0");
	    			  error.put("message", "Password does not match. Please try again!");
	    			  this.uiResponse.add(this.uiResponse.size(), error);
	    			  
	    			  // Set the same message for password2;
	    			  error.clear();
	    			  error.put("name", "password2");
	    			  error.put("success", "0");
	    			  error.put("message", "Password does not match. Please try again!");
	    			  this.uiResponse.add(this.uiResponse.size(), error);
	    		  }
	    		  
	    		  // If there are no errors, at this point, register user;
	    		  if(this.uiResponse.size() == 0)
	    		  {
	    			  DatabaseConnection dbc = new DatabaseConnection("CALL add_user_spi(?, ?, ?, ?);");
	    			  dbc.prepStatement.setString(1, request.getParameter("first_name"));
	    			  dbc.prepStatement.setString(2, request.getParameter("last_name"));
	    			  dbc.prepStatement.setString(3, request.getParameter("email"));
	    			  dbc.prepStatement.setString(4, request.getParameter("password"));
	    			  dbc.run();
	    			  this.message +="(DATASET: "+dbc.DataSet+") ";
	    			  if(!dbc.DataSet.isEmpty() && "1".equals(dbc.DataSet.get(0).get("success").toString()) )
	    			  {
	    				this.message +="THANK YOU FOR SIGNING UP! ...";
						response.setHeader("Location","/signup/thankyou");
						request.setAttribute("contentFile","thankyou.jsp");
						response.setStatus(HttpServletResponse.SC_FOUND); //SC_FOUND: 302 redirect;
						//RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
					    //rd.forward(request,response);
	    			  }
	    			  else if(dbc.DataSet.isEmpty())
	    			  {
	    				  error.clear();
	    				  error.put("name", "unknown");
	    				  error.put("success", "0");
	    				  error.put("message", "Sorry, an unknown error has occurred! You may refresh the page, or contact support for help.");
	    				  this.uiResponse.add(this.uiResponse.size(),error);
	    			  }
	    			  else 
	    			  {
	    				  error.clear();
	    				  error.put("name", "unknown");
	    				  error.put("success", dbc.DataSet.get(0).get("success").toString());
	    				  error.put("message", dbc.DataSet.get(0).get("message").toString());
	    				  this.uiResponse.add(this.uiResponse.size(), error);
	    				  error.clear();
	    			  }
	    		  }
	    	  }
	    	  
		      /*String email = request.getParameter("email");
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
		      }*/
	    	  

				/*response.setContentType("text/html");
				response.setHeader("Location","/thankyou");
				request.setAttribute("contentFile","thankyou.jsp");
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY); //SC_FOUND: 302 redirect;
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");*/
	      }
	     else
	    {
		this.errorMessage = "<script type=\"text/javascript\">var responseData = "+
		"[{message:\"Your email and password are required to sign you into your account! Try again to continue.\",success:0}];"+
		"</script>";
	    }
	      
	     PrintWriter out = response.getWriter();
	     out.print("GREETINGS FROM PRINTWRITER");
		request.setAttribute("contentFile","signup.jsp");
		request.setAttribute("uiResponse",this.message+this.uiResponse);
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


