package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.domain.attributes.Condition;

import javax.persistence.Entity;

@Entity
public class Doctor extends Employee {

    private Condition specialty;

    private double honorarium;

    public double getHonorarium() {
        return honorarium;
    }

    public Condition getSpecialty() {
        return specialty;
    }

    public Doctor(){}

    public Doctor(String name, Condition specialty){
        super(name);
        this.specialty = specialty;
    }

}
