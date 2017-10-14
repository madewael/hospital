package be.howest.ti.frameworks.hospital.domain.attributes;

import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;

@JsonFormat(shape = OBJECT)
public enum Condition {

    KIDNEY("Kidney problems",     "Nephrologist"),
    EYE("Visual problems",     "Ophthalmologist"),
    HEART("Heart problems",      "Cardiologist"),
    RESPIRATORY("Respiratory issues",  "Pulmonologist"),
    GYN("Uterine discomfort",  "Gyneacologist"),
    TEETH("Dental issues",       "Dentist"),
    BLOOD("Sanguine worries",    "Hematologist");

    private final String symptom,doctor;

    Condition(String symptom, String doctor){
        this.symptom = symptom;
        this.doctor = doctor;
    }

    @JsonProperty
    public String getSymptom(){return symptom;}

    @JsonProperty
    public String getDoctor(){return doctor;}

    public static Condition parse(String conditionAsString) {
        try {
            return Condition.valueOf(conditionAsString);
        } catch (IllegalArgumentException ex){
            throw new HospitalException("Unknown Condition" + conditionAsString, ex);
        }
    }
}
