/**
 * 
 * @param {formSelector, buttonSelector} config; 
 */

function LoanAmortization() {
  const config = {formSelector: "#loan-amort-schedule", buttonSelector: "#btn-loan"};
  
  LoanAmortization.prototype.config = config;
  document.querySelector(config.formSelector).addEventListener("click", LoanAmortization.prototype.postForm);
    
}
LoanAmortization.prototype.postForm = () => {
  const config = LoanAmortization.prototype.config;
  if (config.formSelector)
  {
    const elems = document.querySelector(config.formSelector).elements;
    const data = { };
    for (let j in elems)
    {
      data[elems[j].name] = elems[j].value;
    }
    console.log("DATA: ", data);
    const URL = window.location.pathname;
    console.log("URL: ", URL);
    superagent
      .post(URL)
      .send(data)
      .set('Accept', 'Application/json')
      .then(LoanAmortization.prototype.processResponse);
    
  }
}

LoanAmortization.prototype.processResponse = (response) => {
  console.log("RESPONSE: ", response);
}



