import java.io.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.time.*;
import java.math.*;
import org.apache.commons.lang3.math.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Extend HttpServlet class
public class LoanAmortization extends HttpServlet {
 
	//principal loan amount
  private double interest = 0;
  private int termYears = 0;
  private int termMonths = 0;
  private BigDecimal principal = new BigDecimal(0);
	private BigDecimal monthInterest = new BigDecimal(0);
	private BigDecimal monthPayment = new BigDecimal(0);
	private BigDecimal totalInterestPaid = new BigDecimal(0);
	private String termType = "";
	private HashMap<String, String> postData = new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> paymentSchedule = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> error = new ArrayList<HashMap<String, String>>();
	private List<String> requiredFields = new ArrayList<String>();
	private LinkedHashMap<String, Object> responseData = new LinkedHashMap<String, Object>();
	
	
	private final int DECIMALS = 8;
	private final int DECIMALS_RATE = 5;
  private String contentFile = "loan-amortization.jsp";
   

  public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException 
  {
    // Do required initialization
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    // Set response content type
    response.setContentType("text/html");
    request.setAttribute("contentFile",this.contentFile);
    RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
    rd.forward(request,response);
  }
   
  public void doPost(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException 
  {
    response.reset();
  	response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
  	response.setHeader("Pragma", "no-cache");
  	response.setHeader("Expires", "-1");
  	response.addHeader("Cache-Control", "no-cache");
  	response.setContentType("Application/json");
  	
  	PrintWriter out = response.getWriter();
  	try
  	{
      this.postData = RedRabbit.parseInputStreamParameters(request);
  	  this.run();	
  	} catch(Exception ex) {
  	  System.out.println("Exception thrown from doPost(): ");
  	  System.out.println(ex);
  	  System.out.println("Message: " + ex.getMessage());
      
    }
  	out.println(this.getResponseDataJSON());
  }
   
  public void setMonthPayment() throws IOException
  {
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
      System.out.println("[EX] ERROR: ");
      System.out.println(ex);
    }
  }
   
  /**
  * Create loan payment amortization schedule based the principal amount, 
  * interest, and term;
  * @throws IOException
  */
  public void setAmortizationSchedule() throws IOException 
  {
    try
    {
      if (this.isValid())
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
          
          if( balance.compareTo(principalPaid) > -1 )
            balance = balance.subtract(principalPaid).setScale(this.DECIMALS, RoundingMode.HALF_UP);
          else
          {
            balance = principalPaid;	
            doneEarly = true;
          }
          newRow.put("monthPayment", this.monthPayment.setScale(2, RoundingMode.HALF_DOWN).toString());
          newRow.put("principalPaid", principalPaid.setScale(2, RoundingMode.HALF_DOWN).toString() );
          newRow.put("interestPaid", interestPaid.setScale(2, RoundingMode.HALF_DOWN).toString() );
          newRow.put("totalInterestPaid", this.totalInterestPaid.setScale(2, RoundingMode.HALF_DOWN).toString() );
          newRow.put("balance", balance.setScale(2, RoundingMode.HALF_DOWN).toString() );
          newRow.put("paymentNumber", Integer.toString(j+1) );
          newRow.put("date", LocalDate.now().plusMonths(j).toString());
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

  public String getResponseDataJSON() 
  {
    return RedRabbit.createJson(this.responseData);
  }
   
  public void validate()
  {
    this.requiredFields.clear();
    this.error.clear();
    
    this.requiredFields.add("principal");
    this.requiredFields.add("interest");
    this.requiredFields.add("term");
    this.requiredFields.add("termType");
    
    for(String fieldName: this.requiredFields )
    {
      if(this.postData.get(fieldName) == null)
      {
        HashMap<String, String> err = new HashMap<String, String>();
        err.put("name", fieldName); 
        err.put("message", "Required");
        err.put("success", "0");
        this.error.add(err);
      }
      else if(fieldName.equals("principal") || fieldName.equals("interest") || fieldName.equals("term"))
      {
        if(NumberUtils.isCreatable(this.postData.get(fieldName)) == false)
        {
          HashMap<String, String> err = new HashMap<String, String>();
          err.put("name", fieldName);
          err.put("message", fieldName + " must be a number greater than zero.");
          err.put("success", "0");
          this.error.add(err);
        }
      }
      else if(fieldName.equals("termType") && !this.postData.get("termType").equals("years") && !this.postData.get("termType").equals("months"))
      {
        HashMap<String, String> err = new HashMap<String, String>();
        err.put("name", fieldName);
        err.put("message", "Term type must be one of [years, months], but found [" + this.postData.get("termType") + "]");
        err.put("success", "0");
        this.error.add(err);
      }
    }
  }
  
  /**
   * Set props | fields required for calculation;
   */
  public void setProps()
  {
    if(this.error.size() == 0)
    {
      for(Map.Entry<String, String> entry: this.postData.entrySet())
      {
    		if(entry.getValue() !=null)
    		{
    		  switch(entry.getKey())
    		  {
            case "principal":
              this.principal = new BigDecimal(entry.getValue());
              break;
            case "term":
              if(this.postData.get("termType").equals("years"))
                this.termMonths = Integer.parseInt(entry.getValue()) * 12;
            else if(this.postData.get("termType").equals("months"))
                this.termMonths = Integer.parseInt(entry.getValue());
                this.termYears = this.termMonths / 12;
              break;
            case "termType":
              this.termType = entry.getValue();
              break;
            case "interest":
              this.interest = Double.parseDouble(entry.getValue());
              this.monthInterest = new BigDecimal( (this.interest /12) /100 ).setScale(this.DECIMALS_RATE, RoundingMode.HALF_UP);
              break;
    		  }
    		}
      }
    }
  }
  
  /**
   * Create a response object;
   * Note some data are only included for the purpose for investigative purposes if need be;
   * @throws IOException
   */
  public void setResponseData() throws IOException
  {
    try
    {
      this.responseData.clear();
      this.responseData.put("principal", this.principal);
      this.responseData.put("interest", this.interest);
      this.responseData.put("monthInterest", this.monthInterest);
      this.responseData.put("totalInterestPaid", this.totalInterestPaid);
      this.responseData.put("monthPayment", this.monthPayment);
      this.responseData.put("termYears", this.termYears);
      this.responseData.put("termMonths", this.termMonths);
      this.responseData.put("termType", this.termType);
      this.responseData.put("postData", this.postData);
      this.responseData.put("error", this.error);
      if(this.error.size() == 0)
        this.responseData.put("paymentSchedule", this.paymentSchedule);
      else
        this.responseData.put("paymentSchedule", new ArrayList<String>());
    } catch(Exception ex)
    {
      
    }
  }

  /**
   * Execute methods in the order they must run;
   * @throws IOException
   */
  public void run() throws IOException
  {
    this.interest = 0;
    this.termYears = 0;
    this.termMonths = 0;
    this.principal = new BigDecimal(0);
    this.monthInterest = new BigDecimal(0);
    this.monthPayment = new BigDecimal(0);
    this.totalInterestPaid = new BigDecimal(0);
    this.termType = "";
    this.paymentSchedule.clear();
    this.error.clear();
    this.requiredFields.clear();
    this.responseData.clear();
    
    try
    {
      this.validate();
      this.setProps();
      this.setMonthPayment();
      this.setAmortizationSchedule();
      this.setResponseData();
    } catch(Exception ex)
    {
      
    }
  }
}
	


