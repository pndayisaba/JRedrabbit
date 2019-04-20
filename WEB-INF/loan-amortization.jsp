<form name="loan-amort-schedule" id="loan-amort-schedule" method="POST" action="/loan-amortization">
	<h1>Loan Amortization Schedule</h1>
	<div class="row">
   <label for="principal">Amount:</label>
   <input type="text" name="principal" id="principal" />
  </div>
	<div class="row">
		<label for="termType">Term type:</label>
		<select name="termType" id="termType">
		  <option value="years">Years</option>
		  <option value="months">Months</option>
		</select>
	</div>
	<div class="row">
	 <label for="term">Term:</label>
	 <input type="text" name="term" id="term" />
	</div>
	<div class="row">
   <label for="interest">Yearly interest %:</label>
   <input type="text" name="interest" id="term" />
  </div>
  <button type="button" name="btn-loan" id="btn-loan" onclick="window.LA.postForm();">Show Schedule</button>
  <!-- <button type="submit" name="btn-loan" id="btn-loan">Show Schedule</button> -->
</form>
<div class="schedule-wrap">
</div>
<script type="text/javascript">
var LA = new LoanAmortization({formSelector: "#loan-amort-schedule", buttonSelector: "#btn-loan"});
</script>


