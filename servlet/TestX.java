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
import java.math.*;

import com.redrabbit.common.*;

public class TestX{
	
	private BigDecimal principal = new BigDecimal(165000);
	
	
   public static void main(String args[])
   {
	  TestX test = new TestX();
	BigDecimal bd = new BigDecimal(0.0098734).setScale(6, RoundingMode.HALF_EVEN);  //new BigDecimal((4.5 /12) /100).setScale(5, RoundingMode.HALF_UP);
	System.out.println("Hello Toronto");
	System.out.println("principal: ");
	System.out.println(test.principal);
	System.out.println(new BigDecimal(3 * 6));
	System.out.println("bd: " + bd.toString());
	
   }
}
