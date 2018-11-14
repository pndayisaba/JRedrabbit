import java.io.*;
import java.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestX{
	
	
   public static void main(String args[])
   {
	System.out.println("Hello Toronto");
	//Need call proper stored procedure to get user by email and password;
	DatabaseConnection dbc = new DatabaseConnection("CALL user_login_sps(?, ?)"); 
	
	try
	{
		dbc.prepStatement.setString(1, "sallen@redrabbit.com");
		dbc.prepStatement.setString(2, "we"); 
		
		dbc.run();
		
		System.out.println("TEST 1: "+dbc.DataSet);
		int j = 0;
		for(Object row: dbc.DataSet)
		{	
		   System.out.println("email: "+dbc.DataSet.get(j).get("email")+", first_name: "+dbc.DataSet.get(j).get("first_name")+", last_name: " +dbc.DataSet.get(j).get("last_name"));
	        j++;
	            
		}
	}
	catch(Exception ex)
	{
		System.out.println(ex);
	}
	
   }
}
