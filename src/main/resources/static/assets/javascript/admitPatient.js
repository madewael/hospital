var $output = $("#output");
function admitSuccess(data){
    $output.html("OK: "+ JSON.stringify(data));
}
function admitFail(fail){
    $output.html("NOK"+fail.message);
}


$(function(){
    var $addPatientForm = $("#admitPatientForm");
    var $selectPatient = $("#patient");
    var $selectCondition = $("#condition");

    function createPatientOption(username, name){
        return $("<option value='"+username+"'>"+ name+ " ("+ username+")</option>");
    }

    function addPatients(data){
        for(var username in data){
            $selectPatient.append(createPatientOption( username , data[username] ));
        }
    }

    $.ajax({
        url : "/API/patients",
        method: "GET",
        success : addPatients
    });

    function createaddConditionOption(id, name){
        return $("<option value='\"+id+\"'>"+ name+ "</option>");
    }

    function addConditions(data){
        for(var id in data){
            $selectCondition.append(createaddConditionOption( id , data[id] ));
        }
    }

    $.ajax({
        url : "/API/conditions",
        method: "GET",
        success : addConditions
    });



});