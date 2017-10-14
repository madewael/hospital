package be.howest.ti.frameworks.hospital.domain.services;

import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private Date date;


    public Appointment(){}
    public Appointment(Patient patient, Doctor doctor, Date date) {
        this.patient = patient;
        this.doctor = doctor;
        this.date =  (Date)date.clone();
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public Date getDate() {
        return (Date) date.clone();
    }
}
