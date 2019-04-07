import java.io.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import org.apache.commons.lang3.math.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Extend HttpServlet class
public class LoanAmortization extends HttpServlet {
 
	//principal loan amount
	private BigDecimal principal = new BigDecimal(0);
	private double yearInterest = 0;
	private BigDecimal monthInterest = new BigDecimal(0);
	private int termYears = 0;
	private int termMonths = 0;
	private BigDecimal monthPayment = new BigDecimal(0);
	private BigDecimal totalInterestPaid = new BigDecimal(0);
	private String termType = "";
	private HashMap<String, String> data = new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> paymentSchedule = new ArrayList<HashMap<String, String>>();
	private final int DECIMALS = 8;
	private final int DECIMALS_RATE = 5;
  private String contentFile = "loan-amortization.jsp";
   

   public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
     // Do required initialization
	  
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Set response content type
    response.setContentType("text/html");
	  request.setAttribute("contentFile",this.contentFile);
	  RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
    rd.forward(request,response);
	
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
	   
		// Set response content type
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		try
		{
		
			HashMap<String, String> postData = RedRabbit.parseInputStreamParameters(request);
			//out.println("postData: ");
			//out.println(postData);
			this.termType = postData.get("termType");
			for(Map.Entry<String, String> entry: postData.entrySet())
			{
			  //out.println("ENTRYKEY :"+entry.getKey());
			  if (NumberUtils.isCreatable(entry.getValue()) == true)
				{
					switch(entry.getKey()) {
						case "principal": 
							this.principal = new BigDecimal(entry.getValue());
							break;
						case "interest":
							this.yearInterest = Double.parseDouble(entry.getValue());
							this.monthInterest = new BigDecimal( (this.yearInterest / 12) /100 ).setScale(this.DECIMALS_RATE, RoundingMode.HALF_UP);
							break;
						case "term":
						  if( this.termType.equals( "years") )
						  {
						    int years = Integer.parseInt(postData.get("term"));
						    if (years > 0)
						    {
						      this.termYears = years;
						      this.termMonths = this.termYears * 12;
						    }
						  } 
						  else if (this.termType.equals("months"))
						  {
						    int months = Integer.parseInt(postData.get("term"));
						    if ( months > 0)
						    {
						      this.termMonths = months;
						      this.termYears = this.termMonths / 12;
						    }
						  }
							break;
					}
				}
			}
			this.setMonthPayment(out);
		  this.setAmortizationSchedule();
		} catch(Exception ex) {
			  out.println("Exception thrown: ");
			  out.println(ex);
			  //return ex;
		}
		//out.println("paymentSchedule BB: ");
		out.println(this.getPaymentScheduleJSON());
   }
   
   public void setMonthPayment(PrintWriter out) throws IOException{
	   try
	   {
		   //Formulae: M=P[r(1+r)^n/((1+r)^n)]
		   if(this.isValid() == true)
		   {
			   
			  BigDecimal one = new BigDecimal(1);
			  BigDecimal a = this.monthInterest.multiply(  one.add(this.monthInterest).pow(this.termMonths)  );
			  BigDecimal b = one.add(this.monthInterest).pow(this.termMonths).subtract(one);
			  this.monthPayment = this.principal.multiply(a.divide(b, this.DECIMALS, RoundingMode.HALF_UP));
		   }
	   } catch(Exception ex) {
		     out.println("[EX] ERROR: ");
		     out.println(ex);
	   }
   }
   
   /**
    * Create loan payment amortization schedule based the principal amount, 
    * interest, and term;
    * @throws IOException
    */
   public void setAmortizationSchedule() throws IOException {
	   try
	   {
		   if (this.isValid() == true)
		   {
			   BigDecimal balance = this.principal;
			   this.totalInterestPaid = new BigDecimal(0);
			   
			   Boolean doneEarly = false;
			   int j = 0;
			   while(j < this.termMonths && !doneEarly)
			   {
			     HashMap<String, String> newRow = new HashMap<String, String>();
				   
				   BigDecimal interestPaid = balance.multiply(this.monthInterest).setScale(this.DECIMALS, RoundingMode.HALF_UP);
				   BigDecimal principalPaid = this.monthPayment.subtract(interestPaid).setScale(this.DECIMALS, RoundingMode.HALF_UP);
				   this.totalInterestPaid = this.totalInterestPaid.add(interestPaid).setScale(this.DECIMALS, RoundingMode.HALF_UP);
				   if ( balance.compareTo(principalPaid) > -1 )
					   balance = balance.subtract(principalPaid).setScale(this.DECIMALS, RoundingMode.HALF_UP);
				   else
				   {
					   balance = principalPaid;	
					   doneEarly = true;
				   }
				   
				   newRow.put("principalPaid", principalPaid.toString() );
				   newRow.put("interestPaid", interestPaid.toString() );
				   newRow.put("totalInterestPaid", this.totalInterestPaid.toString() );
				   newRow.put("balance", balance.toString() );
				   newRow.put("monthInterest", this.monthInterest.toString() );
				   newRow.put("monthPayment", this.monthPayment.toString() );
				   newRow.put("payment number", Integer.toString(j+1) );
				   this.paymentSchedule.add(newRow);
				   
				   j++;
			   }
		   }
	   } catch(Exception ex) {
		     System.out.println("Exception thrown: ");
		     System.out.println(ex);
	   }
   }
   
   public boolean isValid()
   {
	   return this.principal.compareTo(new BigDecimal(0)) == 1 && this.monthInterest.compareTo(new BigDecimal(0)) == 1 && this.termMonths > 0;
   }
   
   public String getPaymentScheduleJSON() {
	   if (this.paymentSchedule.size() > 0)
		   return RedRabbit.createJson(this.paymentSchedule);
	   else 
		   return "{ }";
   }

   public void destroy() {
      // do nothing.
   }
}
	


