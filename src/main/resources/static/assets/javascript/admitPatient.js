var $output = $("#output");
function admitSuccess(data){
    $output.html("OK: "+ JSON.stringify(data));
}
function admitFail(fail){
    $output.html("NOK"+fail.message);
}


$(function(){
    var $admitPatientForm = $("#admitPatientForm");
    var $selectPatient = $("#patient");
    var $selectCondition = $("#condition");
    var $selectRoomSize = $("#roomSize");

    initForm($admitPatientForm,{
        username : $selectPatient,
        condition : $selectCondition,
        roomSize : $("#roomSize")
    },"/API/patient/admit", "POST", admitSuccess, admitFail);



    initSelect($selectPatient, "/API/patients", function(username, name){
        return {value : username , txt : name + " ("+username+")"};
    });

    initSelect($selectCondition, "/API/conditions", function(id, name){
        return {value : id , txt : name};
    });


});