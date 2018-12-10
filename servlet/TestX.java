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

public class TestX{
	
	
   public static void main(String args[])
   {
	System.out.println("Hello Toronto");
	//Need call proper stored procedure to get user by email and password;
	DatabaseConnection dbc = new DatabaseConnection("CALL forum_sps(?)"); 
	
	try
	{
		dbc.prepStatement.setInt(1,0);
		//dbc.prepStatement.setString(2, "we"); 
		
		dbc.run();
		
		ArrayList<String> columns = new ArrayList<String>();
		
		System.out.println("Size: "+columns.size());
		int j = 0;
		for(Map<String, Object> entry: dbc.DataSet)
		{	
			
			
			//System.out.println("entrySet: "+entry.entrySet());
			for(String key: entry.keySet())
			{
				//if(key.toString().equals("title"))
					
				System.out.println("key: "+key);
			}
	        j++;
	            
		}
	}
	catch(Exception ex)
	{
		System.out.println(ex);
	}
	
   }
}
