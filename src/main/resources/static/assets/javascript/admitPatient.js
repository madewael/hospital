var $output = $("#output");
function admitSuccess(data){
    $output.html("OK: "+ JSON.stringify(data));
}
function admitFail(fail){
    $output.html("NOK"+fail.message);
}


$(function(){
    const $admitPatientForm = $("#admitPatientForm");
    const $selectPatient = $("#patient");
    const $selectCondition = $("#condition");
    const $selectRoomSize = $("#roomSize");

    initForm($admitPatientForm,{
        username : $selectPatient,
        condition : $selectCondition,
        roomSize : $selectRoomSize
    },"/API/patient/admit", "POST", admitSuccess, admitFail);



    initSelect($selectPatient, "/API/patients", function(username, name){
        return {value : username , txt : name + " ("+username+")"};
    });

    initSelect($selectCondition, "/API/conditions", function(id, condition){
        return {value : id , txt : condition.symptom};
    });


});