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

    $admitPatientForm.submit(function(evt){
        evt.preventDefault();

        var username = $selectPatient.val();
        var condition = $selectCondition.val();
        var roomSize = $selectRoomSize.val();

        $admitPatientForm.find("select").attr("disabled", true);
        var data = { username : username , condition : condition , roomSize:roomSize};
        console.log(data);

        $.ajax({
            url : "/API/patient/admit",
            method: "POST",
            data : data,
            success : admitSuccess
        }).fail(function( $xhr ) {
            admitFail( $xhr.responseJSON );
            $admitPatientForm.find("select").attr("disabled", false);
        });
        console.log("loaded",$admitPatientForm)
    });

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

    function createAddConditionOption(id, name){
        return $("<option value='"+id+"'>"+ name+ "</option>");
    }

    function addConditions(data){
        for(var id in data){
            $selectCondition.append(createAddConditionOption( id , data[id] ));
        }
    }

    $.ajax({
        url : "/API/conditions",
        method: "GET",
        success : addConditions
    });



});