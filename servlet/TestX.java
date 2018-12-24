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
	//Need call proper stored procedure to get user by email and password;
	//DatabaseConnection dbc = new DatabaseConnection("CALL user_sps(?)"); 
	//RedRabbit redrabbit = new RedRabbit();
	ArrayList<Map<String, Object>> userInfo = RedRabbit.getUserByEmail("obbb4545444mackenzie@redrabbit.com");
	System.out.println("userInfo: "+userInfo);
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
