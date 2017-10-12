package be.howest.ti.frameworks.hospital.domain.services;

import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Appointment {

    @Id
    private long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private Date date;

    public Doctor getDoctor(){
        // TODO: 10/10/17 replace by field
        return null;
    }

    public Patient getPatient() {
        return patient;
    }

    public Date getDate() {
        return (Date) date.clone();
    }
}
