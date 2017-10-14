var $output = $("#output");
function makeAppointmentSuccess(data){
    $output.html("OK: "+ JSON.stringify(data));
}
function makeAppointmentFail(fail){
    $output.html("NOK"+fail.message);
}


$(function(){
    var $makeAppointmentForm = $("#makeAppointmentForm");
    var $selectPatient = $("#patient");
    var $selectDoctor = $("#doctor");
    var $date = $("#date");

    $date.val(new Date().toDateInputValue());

    initForm($makeAppointmentForm,{
        patient : $selectPatient,
        doctor : $selectDoctor,
        date : $date
    },"/API/patient/appointment", "POST", makeAppointmentSuccess, makeAppointmentFail);



    initSelect($selectPatient, "/API/patients", function(username, name){
        return {value : username , txt : name + " ("+username+")"};
    });

    initSelect($selectDoctor, "/API/doctors", function(username, doctor){
        console.log(doctor);
        var txt = doctor.name + " ( "+ doctor.specialty +" )";
        return {value : username , txt : txt};
    });


});