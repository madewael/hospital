"use strict";

var $interfaceMessage = $('.interfacemassage');

function convertCondition(k,i,o)
{
    return conditionKeys.find(c=>c[i]===k)[o];
}

function patient2ageCat(pat){
  var age = pat.age;

  var idx;
  for(idx=0;idx<(ageCategories.length-1)/2;idx++){
    var cat = ageCategories[(2*idx)];
    var maxAge = ageCategories[(2*idx)+1];
    if ( age<maxAge  ){
      return cat;
    }
  }

  return ageCategories[(2*idx)];
}

var dataModule = (function(data){
  var patients = [];
    var doctors = [];

    function isPatient(obj){
        return ("symptom" in obj) && ("age" in obj);
    }

    function isDoctor(obj){
        return ("specialty" in obj);
    }

    function json2patient(json){
        return new Patient(json.name, json.male,json.age,json.symptom);
    }

    function json2doctor(json){
        return new Doctor(json.name, json.male,json.specialty);
    }


    // How to deep copy
    function getPatients (){
        return data.filter(isPatient).map(json2patient);
    }

    function getDoctors (){
        return data.filter(isDoctor).map(json2doctor);
    }

    // find patients / doctors hier nodig

    function findPatient(name){
        return getPatients ().find((p)=>{return p.name === name;})
    }
    function findDoctor(name){
        return getDoctors.find((p)=>{return p.name === name;})
    }

    return {
        getPatients: getPatients,
        getDoctors: getDoctors,
        findPatient: findPatient,
        findDoctor: findDoctor
    }


})(personData);

function createPersonArticle(id, type, name, organkey, personKey, cap){
  var $pers = $("<article></article>");
  $pers.attr("draggable","true");
  $pers.attr("id",id);
   $pers.attr("data-type",type);

   var $h3 = $("<h3></h3>");
   $h3.append(name);
   $pers.append($h3);

   var $fig = $("<figure></figure>");
   $fig.append(organIconMap.create(organkey));
   $fig.append(personIconMap.create(personKey));
   $fig.append($("<figcaption>"+ cap +"</figcaption>"))

   $pers.append($fig);

   return $pers;
}

function patientRenderer() {
  var symIconkey = convertCondition(this.symptom,"name","icon");
  var patIconkey = patientIconKeys[(this.male)?"male":"female"][patient2ageCat(this)];
  var condition = convertCondition(this.symptom,"name","displayName");

  return createPersonArticle(
    this.id,
    "patient",
    this.toString(),
    symIconkey,
    patIconkey,
    condition
  );
}

Patient.prototype.render = patientRenderer;

function patientEntersHospital() {
  return this.toString() + " is admitted in the hospital with " + convertCondition(this.symptom,"name","displayName") + ".";
}
Patient.prototype.enterHospital = patientEntersHospital;


function doctorRenderer() {
  var symIconkey = convertCondition(this.specialty,"name","icon");
  var docIconkey = convertCondition(this.specialty,"name",this.male?"maleIcon":"femaleIcon");
  var docName    = convertCondition(this.specialty,"name","doctorDisplayName");

  return createPersonArticle(
    this.id,
    "doctor",
    this.toString(),
    symIconkey,
    docIconkey,
    docName
  );
}
Doctor.prototype.render = doctorRenderer;


function doctorEntersHospital() {
  return this.toString() + " starts working at the hospital as a " + convertCondition(this.specialty,"name","doctorDisplayName") + ".";
}
Doctor.prototype.enterHospital = doctorEntersHospital;


// Code voor drag and drop event handling
var listen = function(e){
    e.dataTransfer = e.originalEvent.dataTransfer;
    e.dataTransfer.setData("person", e.target.id); // Geef gegevens door op deze manier wanneer je de drag released

    $(this).addClass('dragged');
    // Neat little trick to preserve dragged element's style in Chrome even when it is a link
    if (e.dataTransfer.setDragImage)
        e.dataTransfer.setDragImage(e.target, 0, 0);
};

var releaseDrag = function(e){
    console.log('end drag');
    $(this).removeClass('dragged');
};

var showFeedback  = function(){
    $(this).toggleClass('droppable');
};

var allowDrop = function(e){
    e.preventDefault();
};

// Eigenlijke verwerking van gedropte element
var checkDropzone = function(e){
    e.dataTransfer = e.originalEvent.dataTransfer;

    var draggedPersonId = e.dataTransfer.getData('person'); // gegevens ophalen

    var draggedPerson = Person.dict[draggedPersonId];
    $(this).toggleClass('droppable');
    // Hier verdere logica implementeren
    $interfaceMessage.text(draggedPerson.enterHospital());

    return false; // Niet verwijderen!
};




$(function(){
    dataModule.getPatients().map(p=>p.render()).forEach(art=>$("header").append(art));
    dataModule.getDoctors() .map(p=>p.render()).forEach(art=>$("footer").append(art));

    $('header,footer').on('dragstart','article',listen).on('dragend','article',releaseDrag);

    $('main figure')
    .on('dragenter',"img",showFeedback)
    .on('dragleave',"img",showFeedback)
    .on('dragover',"img",allowDrop)
    .on('drop',"img",checkDropzone);
    
});





