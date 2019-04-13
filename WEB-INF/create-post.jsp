
<h2>Welcome Forum Discussions</div>
<p>Ask Questions, or help other people by answering their questions</p>
<form name="forum-form" id="forum-form" method="POST" action="/forum/create">
    <div id="unkown-error" class="input-error"></div>
    <div>
        <label for="title">Title</label><input type="text" name="title" id="title" />
        <div id="title-error" class="input-error"></div>
    </div>
    <div>
        <label for="description">Description</label><textarea name="description" id="description"></textarea>
        <div id="description-error" class="input-error"></div>
    </div>
    <div><button type="submit" id="btn-forum-submit">Submit</button></div>
</form>

<script type="text/javascript" src="/js/redrabbit-form.js"></script>
<script type="text/javascript">
    /*var RRF = new RedRabbitForm({
        errors: <?=(!empty($errors) ? json_encode($errors):'[{ }]')?>,
        data: <?=(!empty($inputData) ? json_encode($inputData) : 'new Object()')?>
    });
    RRF.showHideErrors();
    RRF.displayFormValues();*/
    
</script>
