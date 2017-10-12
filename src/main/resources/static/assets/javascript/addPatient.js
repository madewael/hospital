var $output = $("#output");
function registerSuccess(newUser){
    var username = newUser.userName;
    var name = newUser.patient.name;
    $output.html("OK: Registered "+name+" with '"+username+"' as username. The password is currently set to '"+
        username+"123'." +
        " Change this as soon as possible!");
}
function registerFail(fail){
    $output.html("NOK"+fail.message);
}


$(function(){
    var $addPatientForm = $("#addPatientForm");
    var $name = $("#name");

    console.log($addPatientForm);

    $addPatientForm.submit( function(evt){
        evt.preventDefault();

        var name = $name.val();
        $name.val("");

        var $theForm = $(this);
        $theForm.find("input").attr("disabled", true);

        $output.html("registering : "+name);

        $.ajax({
            url:"/API/patient",
            method : "POST",
            data : { "name" : name },
            success : registerSuccess
        }).fail(function( $xhr ) {
            registerFail( $xhr.responseJSON );
            $theForm.find("input").attr("disabled", false);
        });

    });

    console.log("loaded...");

});