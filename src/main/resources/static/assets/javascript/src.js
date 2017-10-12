function IconMap(src,leftInit,leftStep,topInit,topStep, icons){
    this.src = src;
    this.leftInit = leftInit;
    this.leftStep = leftStep;
    this.topInit = topInit;
    this.topStep = topStep;
    this.icons = {};
    for(var r=0 ; r<icons.length ; r++){
        for(var c=0 ; c<icons[r].length ; c++){
            this.icons[icons[r][c]] = {r:r,c:c};
        }
    }
}

IconMap.prototype.create = function(key){
    if (!this.icons[key]) throw "unknown key" + key + " in "+this.src;

    var r = this.icons[key].r;
    var c = this.icons[key].c;

    var top = this.topInit+(r*this.topStep);
    var left = this.leftInit+(c*this.leftStep);
    
    var $element = $("<span></span>");
    $element.css("background-image","url("+this.src+")");
    $element.css("background-position","-"+left+"px -"+top+"px");
    $element.addClass('icon');

    return ($element);
};

var organIconMap = new IconMap("assets/media/icons-organs.png",
    20, 76, 10, 73, [
        ["body","veins","green","bloodDrop","bloodPlate","brokenBone","bone","brain","brainColor"],
        ["cells","cell1","cell2","cell2","deadBody","dna","ear","embryo","eye"],
        ["eyeball","foot","footSick","hand","handSick","heart","heartSick","bowls","teeth"],
        ["kidney","liver","liverSick","lungs","lungsSick","water","month","ukn1","neuron"],
        ["nose","skin","skull","skullBrain","skullCrack","sperm","stomach","tooth","toothSlice"],
        ["toothSliceHole","toothSliceHole2","uterus","ukn2","eggCell","eggCellSperm"]
    ]);

var personIconMap = new IconMap("assets/media/icons-persons.png",
    25,74,10,75,[
        ["noFoodFemale","noFoodMale","angel","armyDoctorFemale","armyDoctorMale","child","suitFemale","suitMale","heartDoctorFemale"],
        ["heartDoctorMale","kidPatientFemale","kidPatientMale","teethDoctorFemale","teethDoctorMale","doctorFemale","doctorMale","doctorNurse","doctorPatient"],
        ["doctorNoFoodPatient","motherBaby","patients","surgeonNurse","surgeonPatient","parentsChild","parentsBaby","family","doctorNursePatients"],
        ["uterusDoctorFemale","uterusDoctorMale","doctorSyringeFemale","doctorSyringeMale","doctorMouthProtectionFemale","doctorMouthProtection","studentFemale","student","baby"],
        ["nurseFemale","nurse","doctorAppleFemale","doctorApple","eyeDoctorFemale","eyeDoctorMale","doctorLampFemale","doctorLampMale","patientFemale"],
        ["patientMale","patientBandageFemale","patientBandageMale","childDoctorFemale","childDoctorMale","rxDoctorFemale","rxDoctorMale","woman","lungDoctor"],
        ["receptionist","oldPatientFemale","oldPatientMale","doctorMouthProtectionBlue","doctorMouthProtectionGreen","patient2Female","patient2Male","kidneyDoctor","animalDoctor"]
    ]);


/*
 * All persons under 3 years are "babies", all persons over 75 years are "elderly pleople".
 * We call people between 3 and 16 "childeren" and people between 16 and 75 are called "adults".
 * The following array represents this knwoledge:
 */
var ageCategories = ["baby",3,"child",16,"adult",75,"elderly"];

/*
 * A mapping between the sex of a patient, his/her age and the appropriate 'icon key' as it can be found in the 'personIconMap'.
 * example: patientIconKeys["male"]["child"] yields "kidPatientMale".
 */
var patientIconKeys = {
  male   : {
    baby    : "child",
    child   : "kidPatientMale",
    adult   : "patientMale",
    elderly : "oldPatientMale"
  },
  female : {
    baby    : "child",
    child   : "kidPatientFemale",
    adult   : "patientFemale",
    elderly : "oldPatientFemale"
  }
};


/*
 * {
 *  "name"              : the name/key of a condition as it appears in doctors specialties of patients symptoms.
 *  "icon"              : the 'icon key' of the condition as it can be found in the 'organIconMap'.
 *  "maleIcon"          : the 'icon key' of a male doctor that specializes in this condition as it can be found in the 'personIconMap'.
 *  "femaleIcon"        : the 'icon key' of a female doctor that specializes in this condition as it can be found in the 'personIconMap'.
 *  "displayName"       : the name of a illness/disease that relates to this condition.
 *  "doctorDisplayName" : the name of a doctor that specializes in this condition.
 * }
 */
const conditionKeys = [
{name:"kidney",      icon:"kidney",     maleIcon:"kidneyDoctor",      femaleIcon:"kidneyDoctor",        displayName:"Kidney problems",    doctorDisplayName:"Nephrologist"},
{name:"eye",         icon:"eye",        maleIcon:"eyeDoctorMale",     femaleIcon:"eyeDoctorFemale",     displayName:"Visual problems",    doctorDisplayName:"Ophthalmologist"},
{name:"heart",       icon:"heart",      maleIcon:"heartDoctorMale",   femaleIcon:"heartDoctorFemale",   displayName:"Heart problems",     doctorDisplayName:"Cardiologist"},
{name:"respiratory", icon:"lungs",      maleIcon:"lungDoctor",        femaleIcon:"lungDoctor",          displayName:"Respiratory issues", doctorDisplayName:"Pulmonologist"},
{name:"gyn",         icon:"uterus",     maleIcon:"uterusDoctorMale",  femaleIcon:"uterusDoctorFemale",  displayName:"Uterine discomfort", doctorDisplayName:"Gyneacologist"},
{name:"teeth",       icon:"teeth",      maleIcon:"teethDoctorMale",   femaleIcon:"teethDoctorFemale",   displayName:"Dental issues",      doctorDisplayName:"Dentist"},
{name:"blood",       icon:"bloodPlate", maleIcon:"doctorSyringeMale", femaleIcon:"doctorSyringeFemale", displayName:"Sanguine worries",   doctorDisplayName:"Hematologist"}
];

/*
 * {
 *  "name"      : the name of a doctor.
 *  "male"      : 'true' if it is a male doctor, 'false' if it is a female doctor.
 *  "specialty" : the name/key of a condition in which this doctor is specialized.
 * }
 *
 * or
 *
 * {
 *  "name"    : the name of a patient.
 *  "male"    : 'true' if it is a male patient, 'false' if it is a female patient.
 *  "age"     : the age of this patient.
 *  "symptom" : the name/key of a condition in which this patient has symptoms.
 * }
 */
var personData =  [
{name:"Gregory House",  male:true,specialty:"blood"},
{name:"Martin Manners", male:true,specialty:"teeth"},
{name:"Lindsey Case",   male:false,specialty:"gyn"},
{name:"Clare Hobbs",    male:false,specialty:"respiratory"},
{name:"Sam Uncle",      male:true,specialty:"eye"},
{name:"Damon Cringle",  male:true,specialty:"kidney"},
{name:"Ellie Skelly",   male:false,specialty:"gyn"},
{name:"Max Dear",       male:true,specialty:"heart"},
{name:"Pat Sang",       male:true,specialty:"blood"},

{name:"Alice",              male:false, age:20, symptom:"kidney"},
{name:"Jane Doe",           male:false, age:66, symptom:"gyn"},
{name:"John Doe",           male:true,  age:48, symptom:"blood"},
{name:"Jimmy Doe",          male:true,  age:8,  symptom:"teeth"},
{name:"Jacky Doe",          male:false, age:16, symptom:"respiratory"},
{name:"Lyanna Raine",       male:false, age:22, symptom:"respiratory"},
{name:"Egon Lint",          male:true,  age:75, symptom:"heart"},
{name:"Lester Cobbleworth", male:true,  age:89, symptom:"blood"},
{name:"Nolan Keith",        male:true,  age:4,  symptom:"teeth"},
{name:"Jenny Danes",        male:false,  age:4,  symptom:"teeth"}];