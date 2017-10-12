"use strict";

function Person(name, male) {
    this.name = name;
    this.male = male;
    this.id = Person.createId(this);
}

Person.nextId = 0;
Person.dict = {};

Person.createId = function(newPerson){
	var id = "person-" + (Person.nextId++);
	Person.dict[id] = newPerson;
	return id;
}

Person.prototype.toString = function() {
    return this.getTitle() + " " + this.name;
};

Person.prototype.getTitle = function(){
    return (this.male)?"mr.":'ms.';
};

function Patient(name,male,age, symptom){
    Person.call(this, name,male);
    this.age = age;
    this.symptom = symptom;
}
Patient.prototype = Object.create(Person.prototype);
Patient.prototype.constructor = Patient;


function Doctor(name,male,specialty){
    Person.call(this, name,male);
    this.specialty = specialty;
}

Doctor.prototype = Object.create(Person.prototype);
Doctor.prototype.constructor = Doctor;


Doctor.prototype.getTitle = function(){ return "dr."; };