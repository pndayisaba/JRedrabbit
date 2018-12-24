<div>
${uiResponse}
</div>
<form name="signup-form" id="signup-form" method="POST" action="/signup">

    <div>
    <label for="first_name">First name</label><input type="text" name="first_name" id="first_name" />
    <div id="first-name-error" class="input-error">Required</div>
    </div> 
    <div>
    <label for="last_name">Last name</label><input type="text" name="last_name" id="last_name" />
    <div id="last-name-error" class="input-error">Required</div>
    </div> 
    <div>
        <label for="email">Email</label><input type="email" name="email" id="email" />
        <div id="email-error" class="input-error">Required</div>
    </div>
    <div>
        <label for="password">Password</label><input type="password" name="password" id="password" />
        <div id="password-error" class="input-error">Required</div>
    </div>
    <div>
        <label for="email">Re-type password</label><input type="password" name="password2" id="password2" />
        <div id="password2-error" class="input-error">Required</div>
    </div>
    <div><button type="submit" id="btn-submit">Submit</button></div>    
</form>

<script type="text/javascript" src="/js/redrabbit-form.js"></script>
<script>
var RedRabbitForm = new RedRabbitForm();

/*var RedRabbitForm = new RedRabbitForm({
    errors: <?=(!empty($signupResponseData) ? json_encode($signupResponseData) :'{ }')?>,
    data:<?=(!empty($_POST) ? json_encode($_POST) : '[{ }]')?>
    });
RedRabbitForm.showHideErrors();
RedRabbitForm.ShowFormValues(); 
*/
</script>