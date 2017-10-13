function initSelect($select, url, convertDataToValueAndText){
    $.ajax({
        url : url,
        method: "GET",
        success : function(data){
            for(var id in data){
                var kv = convertDataToValueAndText(id,data[id]);
                $select.append( $("<option value='"+kv.value+"'>"+ kv.txt + "</option>") );
            }
        }
    });
}

function initForm($form, inputs, url, method, success, fail){
    var $submit = $form.find(":submit");

    $form.submit(function(evt){
        evt.preventDefault();

        var data = {};
        for(var key in inputs){
            var $input = inputs[key];
            $input.attr("disabled", true);
            data[key] = $input.val();
        }
        $submit.attr("disabled", true);

        $.ajax({
            url : url,
            method: method,
            data : data,
            success : success
        }).fail(function( $xhr ) {
            fail( $xhr.responseJSON );
            for(var key in inputs){
                inputs[key].attr("disabled", false);
            }
            $submit.attr("disabled", false);
        });


    });

}