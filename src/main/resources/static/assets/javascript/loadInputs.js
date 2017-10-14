Date.prototype.toDateInputValue = (function() {
    const local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});

function ownProperties(obj){
    const res = [];
    for(let k in obj){
        if (obj.hasOwnProperty(k)){
            res.push(k);
        }
    }
    return k;
}

function initSelect($select, url, convertDataToValueAndText){
    $.ajax({
        url : url,
        method: "GET",
        success : function(data){
            ownProperties(data)
                .map(id => convertDataToValueAndText(id,data[id]))
                .map(kv => $("<option value='"+kv.value+"'>"+ kv.txt + "</option>"))
                .forEach( $option => $select.append( option ) );
        }
    });
}

function initForm($form, inputs, url, method, success, fail){
    const $submit = $form.find(":submit");

    $form.submit(function(evt){
        evt.preventDefault();

        const inputKeys = ownProperties(inputs);

        const data = {};
        for(let key in inputKeys){
            let $input = inputs[key];
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
            for(let key in inputKeys){
                inputs[key].attr("disabled", false);
            }
            $submit.attr("disabled", false);
        });


    });

}