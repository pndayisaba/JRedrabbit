import java.io.*;
import java.util.*;

import javax.sql.RowSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.redrabbit.common.*;

public class TestX{
	
	
   public static void main(String args[])
   {
	System.out.println("Hello Toronto");
	String s = "is_ajax=1&forum_id=25&description=December 17, 2018 - DESCRIPTION [4] 000&=&=";
	String[] required = s.split("&");
	
	HashMap<String, String> M = new HashMap<String, String>();
	M.put("is_ajax", "1");
	M.put("forum_id", "25");
	
	System.out.println("M.: "+M.get("is_ajax_Q"));
	try
	{
		//dbc.prepStatement.setInt(1,"obbb4545444mackenzie@redrabbit.com");
		//dbc.prepStatement.setString(2, "we"); 
		//dbc.run();
		
		//BaseService bs = new BaseService();
		
		//System.out.println("BS: "+bs);
	}
	catch(Exception ex)
	{
		System.out.println(ex);
	}
	
   }
}
