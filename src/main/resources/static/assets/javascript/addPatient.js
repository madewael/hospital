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

    initForm($addPatientForm,{
        name : $name
    },"/API/patient", "POST", registerSuccess, registerFail);


});