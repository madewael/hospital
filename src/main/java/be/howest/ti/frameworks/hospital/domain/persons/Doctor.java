package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.domain.attributes.Condition;

import javax.persistence.Entity;

@Entity
public class Doctor extends Person {

    private Condition specialty;

    private double honorarium;

    public double getHonorarium() {
        return honorarium;
    }

    public Condition getSpecialty() {
        return specialty;
    }

    public Doctor(){}

    public Doctor(String userName, String password, String name, Condition specialty){
        super(userName,password,name);
        this.specialty = specialty;
    }

}
