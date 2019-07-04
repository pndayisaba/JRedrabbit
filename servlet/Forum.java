import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.json.*;
import org.apache.commons.lang3.StringUtils;
//import com.redrabbit.common;
// Extend HttpServlet class
public class Forum extends HttpServlet {
  private String message;
  protected String email;
  protected String password;
  private String errorMessage;
  private String forumPostData = "[ ]";
  private ArrayList<String> required = new ArrayList<String>();
  private String lookupURI = "forum";
  private String contentFile = "forum.jsp";
  private ArrayList<Map<String, String>> uiResponse = new ArrayList<Map<String, String>>();

  public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException 
  {
    this.required.add("description");
  }

  public void setProps(HttpServletRequest request)
  {
    //set this.lookupURI;
    this.lookupURI = request.getRequestURI().toString().replaceAll("^\\/|\\/$","");
    // Set this.contentFile;
    switch(this.lookupURI)
    {
      case "forum/thankyou":
        this.contentFile = "thankyou-forum.jsp";
          break;
      case "forum/create":
        this.contentFile = "create-post.jsp";
        break;
      default:
        this.contentFile = "forum.jsp";
    		break;
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    response.reset();
    response.setContentType("application/json; charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setHeader("Expires", "0"); // Proxies.
    PrintWriter pw = response.getWriter();
    
    this.setProps(request);
    this.message = "";
    this.uiResponse.clear();
    this.required.clear();
    
    JsonObject inputData = new JsonParser().parse("{}").getAsJsonObject();
    String str, wholeStr = "";
    try
    {
      BufferedReader br = request.getReader();
      while ((str = br.readLine()) != null)
      {
        wholeStr += str;
      }
      GsonBuilder builderX = new GsonBuilder();
      builderX.setPrettyPrinting();      
      Gson gson = builderX.create();
     
      inputData = new JsonParser().parse(wholeStr).getAsJsonObject();
      
    } catch (Exception e) {
      //logger.error("", e);
    }
	   
    // Verify that the user is signed in;
    this.email = RedRabbit.getCookieByName(request, "email");
    if("".equals(this.email))
    {
      HashMap<String, String> error = new HashMap<String, String>();
      error.put("name", "unkown");
      error.put("success", "0");
      error.put("message", "Unknown user! Please make sure you are signed in.");
      this.uiResponse.add(this.uiResponse.size(),error);
      //error.clear();
    }
    
    if(this.uiResponse.size() == 0)
    {
      if("".equals(inputData.get("forum_id").toString()))
      {
        this.required.add("title");
      }
		   
      for(String s: this.required)
      {
        if(inputData.get(s) == null || inputData.get(s).toString().trim().length() == 0)
        {
          HashMap<String, String> error = new HashMap<String, String>();
          error.put("name", s);
          error.put("success", "0");
          error.put("message", "Required");

          this.uiResponse.add(this.uiResponse.size(), error);
        }
			}
    }
	   
    // If there are still no errors at this point, add the entries ino the database;
    if(this.uiResponse.size() ==0)
    {
       try
	     {
	       DatabaseConnection dbc = new DatabaseConnection("CALL forum_spi(?,?,?,?);");
         dbc.prepStatement.setString(1, inputData.get("title") !=null ? inputData.get("title").toString() : "");
         dbc.prepStatement.setString(2, inputData.get("description") !=null ? inputData.get("description").toString() : "");
         dbc.prepStatement.setString(3, this.email !=null ? this.email : "");
         dbc.prepStatement.setString(4, inputData.get("forum_id") !=null ? inputData.get("forum_id").toString() : "");
         
         dbc.run();
				 String S = "" +dbc.DataSet;
         this.message = this.message.concat(S);
				
         if(dbc.DataSet !=null && "1".equals(dbc.DataSet.get(0).get("success").toString()))
         {
           if(inputData.get("is_ajax") !=null && "1".equals(inputData.get("is_ajax").toString()))
           { 
             // Create success response;
             HashMap<String, String> resp = new HashMap<String, String>();
             resp.put("success","1");
             this.uiResponse.add(this.uiResponse.size(),resp);
           }
           else
           {
             this.contentFile = "thankyou-forum.jsp";
           }
         }
         else
         {
           HashMap<String, String> error = new HashMap<String, String>();
           error.put("name", "Uknown");
           error.put("success", "0");
           error.put("message", "Error: creating a record failed with code with DBFAILED." );
           this.uiResponse.add(this.uiResponse.size(), error);
         }
	     } 
	     catch (Exception ex)
	     {
	       System.out.println(ex);
	     }
     }
	
    // Send this.uiResponse as a well formatted JSON
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();      
    Gson gson = builder.create();
    String strJSON = gson.toJson(this.uiResponse);
    String inputDataJson = gson.toJson(inputData);
		if(inputData.get("is_ajax") !=null && "1".equals(inputData.get("is_ajax").toString()) )
		{	
			
			pw.print(strJSON);
		}
		else
		{
			if("thankyou-forum.jsp".equals((this.contentFile)))
			{
				response.setHeader("Location","/forum/thankyou");
				response.setStatus(HttpServletResponse.SC_FOUND); //SC_FOUND: 302 redirect;
			}
			else
			{
			  
				String jsSnippet = "<script type=\"text/javascript\">var uiResponse = "+strJSON+";</script>";
				request.setAttribute("contentFile",this.contentFile);
				request.setAttribute("jsSnippet",jsSnippet);
				RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
		    rd.forward(request,response);
			}
		}
  }
   
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    
    response.reset();
    response.setContentType("text/html");
    this.setProps(request);
    this.message = "";
    
    PrintWriter pw = response.getWriter();
    try 
    {
      this.email = RedRabbit.getCookieByName(request, "email");
      DatabaseConnection dbc = new DatabaseConnection("CALL forum_sps(?, ?)");
      dbc.prepStatement.setInt(1, 0);
      dbc.prepStatement.setString(2, "");
      dbc.run();
      GsonBuilder builder = new GsonBuilder();
      builder.setPrettyPrinting();     
      Gson gson = builder.serializeNulls().create();
      
      this.forumPostData = gson.toJson(dbc.DataSet);
  		File file = new File(request.getServletContext().getRealPath("/WEB-INF/components/forum-post.tpl.jsp"));
      
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = reader.readLine();
      // Main template;
      String template = "";
      while(line !=null)
      {
    	  template = template.concat(line);
    	  line = reader.readLine();
      }
	      
      // Controlls template;
      File file2 = new File(request.getServletContext().getRealPath("/WEB-INF/components/forum-post-controls.tpl.jsp"));
	      
      BufferedReader reader2 = new BufferedReader(new FileReader(file2));
      line = reader2.readLine();
      String controlsTemplate = "";
      while(line !=null)
      {
    	  controlsTemplate = controlsTemplate.concat(line);
    	  line = reader2.readLine();
      }
	      
      // Replies
      File file3 = new File(request.getServletContext().getRealPath("/WEB-INF/components/forum-post-child.tpl.jsp"));
      
      BufferedReader reader3 = new BufferedReader(new FileReader(file3));
      line = reader3.readLine();
      String replyTemplate = "";
      while(line !=null)
      {
    	  replyTemplate = replyTemplate.concat(line);
    	  line = reader3.readLine();
      }
      
      int j = 0;
		      
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
           
					   
			   String lineFeed = System.getProperty("line.separator");
			   for(String key: entry.keySet())
         {
           String value = "";
           if(entry.get(key) !=null && entry.get(key) !="")
        	   value = entry.get(key).toString();
           tpl = tpl.replace("[@"+key+"]", value.replace(lineFeed, "<br />"));
         }
					   
			   // BEGIN REPLIES;
			   String childReply = "";
			   for(Map<String, Object> entry2: dbc.DataSet)
         {
				   
			     if(entry2.get("parent_forum_id") !=null
			       && entry2.get("parent_forum_id") == entry.get("forum_id")
			     ) {   
					   childReply = childReply.concat(replyTemplate);
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
							   childReply = childReply.replace("[@"+key2+"]", entry2.get(key2).toString().replace(lineFeed, "<br />"));
					   }
				   }
         }
          tpl = tpl.replace("[@postReplyPlaceholder]", childReply);
          // END REPLIES;
          this.message = this.message.concat(tpl);
		    }    
      }
      request.setAttribute("jsSnippet",this.errorMessage);
      
      request.setAttribute("contentFile",this.contentFile);
      request.setAttribute("message",this.message);
      request.setAttribute("forumPostData",this.forumPostData);
      RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
      rd.forward(request,response);
	  }
	  catch(Exception ex)
	  {
	    System.out.println("Problem creating forum page.");
		  System.out.println(ex.getStackTrace());
	  }	
  }
   
   /**
  * Update post;
  * @param request
  * @param response
  * @return void;
  */
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    //response.setContentType("application/json");
    response.reset(); // don't included previous response;
    response.setContentType("text/html;charset=UTF-8;");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setHeader("Expires", "0"); // Proxies.
    PrintWriter pw = response.getWriter();
    // Parse PUT parameters
	  HashMap<String, String> inputParams = new HashMap<String, String>();
	  inputParams = RedRabbit.parseInputStreamParameters(request); 
    this.message = "";

    this.uiResponse.clear();
	  this.required.clear();
	  this.required.add("description");
	  this.required.add("forum_id");
	   
	  // Check that user is signed in;
	  this.email = RedRabbit.getCookieByName(request, "email");
	  
	  if("".equals(this.email))
	  {
	    HashMap<String, String> error = new HashMap<String, String>();
		  error.put("name", "email");
		  error.put("success","0");
		  error.put("message", "Required");
		  this.uiResponse.add(this.uiResponse.size(), error);
	  }
	   
   // If no errors so far, check for required fields;
   if(this.uiResponse.size() == 0)
   {
     for(String s: this.required)
     {
  	   if(inputParams.get(s) == null)
  	   {
    	   HashMap<String, String> error = new HashMap<String, String>();
  		   error.put("name", s);
  		   error.put("success","0");
  		   error.put("message", "Required");
  		   
  		   this.uiResponse.add(this.uiResponse.size(), error);
  	   }
     }
   }
	   
   try
   {
	   // If there are still no validadtion errors at this point, attempt
	   // updating the database;
	   if(this.uiResponse.size() == 0)
	   {
		   DatabaseConnection dbc = new DatabaseConnection("CALL forum_spu(?, ?, ?, ?);");
		   dbc.prepStatement.setString(1, inputParams.get("title"));
		   dbc.prepStatement.setString(2, inputParams.get("description"));
		   dbc.prepStatement.setString(3, this.email);
		   dbc.prepStatement.setString(4, inputParams.get("forum_id"));
		   dbc.run();
		   
		   if(dbc.DataSet !=null && "1".equals(dbc.DataSet.get(0).get("success").toString()))
		   {
			   HashMap<String, String> resp = new HashMap<String, String>();
			   resp.put("success", "1");
			   resp.put("message", "You have successfully updated your post.");
			   this.uiResponse.add(this.uiResponse.size(), resp);
		   }
		   else
		   {
			   HashMap<String, String> error = new HashMap<String, String>();
			   error.put("success","0");
			   error.put("message", "Error: unknown error occurred. Your changes did not save.");
			   this.uiResponse.add(this.uiResponse.size(), error);
		   }	   
	   }
   }
   catch(Exception ex)
   {
	   System.out.println(ex);
   }
	   
	   // Send this.uiResponse as a well formatted JSON
     GsonBuilder builder = new GsonBuilder();
     builder.setPrettyPrinting();      
     Gson gson = builder.create();
     String strJSON = gson.toJson(this.uiResponse);
     	 	
     
     pw.print(strJSON);
  }
   
   /**
  * Delete user post;
  * 
  * @param request
  * @param response
  * @throws ServletException
  * @throws IOException
  */
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("application/json");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setHeader("Expires", "0"); // Proxies.
	   
    response.reset(); // dont' include previous response;
    this.uiResponse.clear();
	   
    HashMap<String, String> inputParams = RedRabbit.parseInputStreamParameters(request);
 
    this.email = RedRabbit.getCookieByName(request, "email");
    if(this.email == null || "".equals(this.email))
    {
      HashMap<String, String> resp = new HashMap<String, String>();
  	   resp.put("name", "email");
  	   resp.put("success", "0");
  	   resp.put("message", "Unknown user! Are you signed in?");
  	   this.uiResponse.add(this.uiResponse.size(), resp);
    }
    else if(inputParams.get("forum_id") == null || "".equals(inputParams.get("forum_id")))
    {
  	   HashMap<String, String> resp = new HashMap<String, String>();
  	   resp.put("name", "forum_id");
  	   resp.put("success", "0");
  	   resp.put("message", "Trying to delete nothing. Parameter forum_id is required but was not supplied!");
  	   this.uiResponse.add(this.uiResponse.size(), resp);
    }
   
    try
    {
      if(this.uiResponse.size() == 0)
      {
         DatabaseConnection dbc = new DatabaseConnection("CALL delete_forum_spd(?, ?);");
         dbc.prepStatement.setString(1, inputParams.get("forum_id"));
         dbc.prepStatement.setString(2, this.email);
         dbc.run();
  	  
         if(dbc.DataSet !=null && "1".equals(dbc.DataSet.get(0).get("success").toString()))
         {
           HashMap<String, String> resp = new HashMap<String, String>();
           resp.put("success", "1");
           resp.put("message", "Post deleted!");
           this.uiResponse.add(this.uiResponse.size(), resp);
         }
         else
         {
  			   HashMap<String, String> resp = new HashMap<String, String>();
  			   resp.put("success", "0");
  			   resp.put("message", "There was a problem deleting your post!");
  			   this.uiResponse.add(this.uiResponse.size(), resp);
         }
      }
    }
    catch(Exception ex)
    {
      System.out.println(ex);
    }
    // Send this.uiResponse as a well formatted JSON
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();      
    Gson gson = builder.create();
    String strJSON = gson.toJson(this.uiResponse);
    	 	
    PrintWriter pw = response.getWriter();
    pw.print(strJSON);
  }
   
  public void destroy() 
  {
    // do nothing.
  } 
}


