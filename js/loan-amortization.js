/**
 * 
 * Loan Amortization schedule handler;
 */

function LoanAmortization() { }

LoanAmortization.prototype.columns = [
  {name: 'paymentNumber', title: '#', className: 'payment-number'},
  {name: 'date', title: 'Date', className: 'data'},
  {name: 'monthPayment', title: 'Payment', className: 'month-payment'},
  {name: 'principalPaid', title: 'Principal<br />Paid', className: 'principal-paid'},
  {name: 'interestPaid', title: 'Interest<br />Paid', className: 'interest-paid'},
  {name: 'totalInterestPaid', title: 'Total<br />Interest', className: 'total-interest'},
  {name: 'balance', title: 'Balance', className: 'balance'}
];

LoanAmortization.prototype.postForm = () => {
  const config = {formSelector: "#loan-amort-schedule", buttonSelector: "#btn-loan"};
  
  if (config.formSelector)
  {
    const elems = document.querySelector(config.formSelector).elements;
    const data = { };
    for (let j in elems)
    {
      data[elems[j].name] = elems[j].value;
    }
    superagent
      .post(window.location.pathname)
      .send(data)
      .set('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8')
      .then(LoanAmortization.prototype.processResponse);
  }
}

LoanAmortization.prototype.processResponse = (data) => {
	if (data.body.paymentSchedule.length) 
	{
		const columns = LoanAmortization.prototype.columns;
		let htmlBody = '';
		columns.forEach((obj) => {
		  htmlBody +='<div class="' + obj.className + ' col">' + obj.title + '</div>';
		});
		htmlBody = '<div class="schedule-row heading">' + htmlBody + '</div>';
		
		data.body.paymentSchedule.forEach((obj, index) => {
		  htmlBody +='<div class="schedule-row ' + (index === 0 || index % 2 === 0 ? 'odd' : 'even') + '">';
		  for(let col of columns)
	    {
		    htmlBody +='<div class="' + col.className + ' col">' + obj[col.name] + '</div>';
	    }
		  htmlBody +='</div> <!-- .schedule-row -->';
		});
		document.querySelector('.schedule-wrap').innerHTML = htmlBody;
	}
}



