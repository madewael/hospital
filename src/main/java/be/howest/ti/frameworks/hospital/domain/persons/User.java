package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.domain.access.Admin;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User {

    @Admin
    @Id
    private String userName;

    @Admin
    private String password;

    @OneToOne private Patient patient;
    @OneToOne private Doctor doctor;
    @OneToOne private Administrator administrator;

    public User(){}

    private User(String userName, String password, Patient patient, Doctor doctor, Administrator administrator) {
        this.userName = userName;
        this.password = password;

        this.patient = patient;
        this.doctor = doctor;
        this.administrator = administrator;

    }

    public User(String userName, String password, Patient patient) {
        this(userName,password,patient,null,null);

    }

    public User(String userName, String password, Doctor doctor) {
        this(userName,password,null,doctor,null);
    }

    public User(String userName, String password, Administrator administrator) {
        this(userName,password,null,null,administrator);
    }


    public String getUserName(){
        return userName;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public boolean isPatient() {
        return patient!=null;
    }

    public boolean isDoctor() {
        return doctor!=null;
    }

    public boolean isAdministrator() {
        return administrator!=null;
    }

    public Person getPerson() {
        if (isPatient()) return patient;
        if(isDoctor()) return doctor;
        if (isAdministrator()) return administrator;
        return null;
    }
}
