import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Map;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.redrabbit.common;
// Extend HttpServlet class
public class CreateForumPost extends HttpServlet {
 
   private String message;
   protected String email;
   protected String password;
   private String errorMessage;
   private String forumPostData = "[ ]";

   public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
      // Do required initialization
      message = "Forum HERE...";
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
     
	this.message = "CREATE FORUM POST...";
    // Set response content type
    response.setContentType("text/html");
	request.setAttribute("contentFile","create-post.jsp");
	request.setAttribute("message",this.message);
	RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
      rd.forward(request,response);
	
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException 
   {
	   response.setContentType("text/html");
	      
	   try 
	   {
	      	 this.email = RedRabbit.getUserEmailFromCookie(request);
	          
		      DatabaseConnection dbc = new DatabaseConnection("CALL forum_sps(?)");
		      dbc.prepStatement.setInt(1, 0);
		      //dbc.prepStatement.setString(2, password);
		      dbc.run();
			
		      GsonBuilder builder = new GsonBuilder();
		      builder.setPrettyPrinting();     
		      Gson gson = builder.create();
		      
		      this.forumPostData = gson.toJson(dbc.DataSet);
		      
		      
		      
		      File file = new File(request.getServletContext().getRealPath("/WEB-INF/components/forum-post.tpl.jsp"));
		      
		      BufferedReader reader = new BufferedReader(new FileReader(file));
		      String line = reader.readLine();
		      // Main template;
		      String template = "";
		      while(line !=null)
		      {
		    	  template += line;
		    	  line = reader.readLine();
		      }
		      
		      // Controlls template;
		      File file2 = new File(request.getServletContext().getRealPath("/WEB-INF/components/forum-post-controls.tpl.jsp"));
		      
		      BufferedReader reader2 = new BufferedReader(new FileReader(file2));
		      line = reader2.readLine();
		      String controlsTemplate = "";
		      while(line !=null)
		      {
		    	  controlsTemplate += line;
		    	  line = reader2.readLine();
		      }
		      
		      // Replies
		      File file3 = new File(request.getServletContext().getRealPath("/WEB-INF/components/forum-post-child.tpl.jsp"));
		      
		      BufferedReader reader3 = new BufferedReader(new FileReader(file3));
		      line = reader3.readLine();
		      String replyTemplate = "";
		      while(line !=null)
		      {
		    	  replyTemplate += line;
		    	  line = reader3.readLine();
		      }
		      
		      
		      int j = 0;
		      this.message = ""; 
		      ArrayList<String> columns = new ArrayList<String>();
			  for(Map<String, Object> entry: dbc.DataSet)
			  {	  
				   if (entry.get("parent_forum_id") == null) //Main post
				   {

					   String tpl = template;
					   String replaceWith = "";
					   if(this.email !=null && !this.email.isEmpty() && entry.get("email") !=null && entry.get("email") !="" && this.email.equals(entry.get("email").toString()))
						   replaceWith = controlsTemplate;
						   
					   tpl = tpl.replace("[@postControlsPlaceholder]", replaceWith);
					     
					   
					   for(String key: entry.keySet())
					   {
						   String value = "";
						   if(entry.get(key) !=null && entry.get(key) !="")
							   value = entry.get(key).toString();
						   tpl = tpl.replace("[@"+key+"]", value);
					   }
					   
					   // BEGIN REPLIES;
					   String childReply = "";
					   for(Map<String, Object> entry2: dbc.DataSet)
					   {
						   
						   if(entry2.get("parent_forum_id") !=null && entry.get("parent_forum_id") !="" && entry2.get("parent_forum_id")==entry.get("forum_id") )
						   {   
							   childReply += replyTemplate;
							   // Custom controls
							   String childReplaceWith = "";
							   if(this.email !=null && !this.email.isEmpty() && this.email.equals(entry2.get("email").toString()))
							   {
								   childReplaceWith = controlsTemplate;
							   }
							   childReply = childReply.replace("[@postControlsPlaceholder]", childReplaceWith);
							   // Add dynamic content
							   for(String key2: entry2.keySet())
							   {
								   if(entry2.get(key2) !=null && entry2.get(key2) !="")
									   childReply = childReply.replace("[@"+key2+"]", entry2.get(key2).toString());
							   }
						   }
					   }
					   tpl = tpl.replace("[@postReplyPlaceholder]", childReply);
					   // END REPLIES;
					   this.message +=tpl;
				   }    
			 }
			 request.setAttribute("jsSnippet",this.errorMessage);
	      
	    request.setAttribute("contentFile","forum.jsp");
		request.setAttribute("message",this.message);
		request.setAttribute("forumPostData",this.forumPostData);
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


