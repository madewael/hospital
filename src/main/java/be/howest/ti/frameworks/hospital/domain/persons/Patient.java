package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.domain.access.Admin;
import be.howest.ti.frameworks.hospital.domain.access.Involved;
import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.access.Medical;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
import be.howest.ti.frameworks.hospital.domain.bills.Bill;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Patient extends Person {

    private BloodType bloodType;

    private SocialSecurity socialSecurity;

    //@OneToMany private List<Bill> bills;

    @OneToMany
    private List<Appointment> appointments;

    @Medical
    public BloodType getBloodType(){
        return bloodType;
    }

    @Admin
    public SocialSecurity getSocialSecurity(){
        return socialSecurity;
    }

    @OneToOne
    private Stay currentStay;

    public Patient(){}

    public Patient(String userName, String password, String name, BloodType bt, SocialSecurity ss){
        super(userName,password,name);
        this.bloodType = bt;
        this.socialSecurity = ss;
        appointments = new ArrayList<>();
    }

    @Involved
    public List<Appointment> getAppointments(){
        return Collections.unmodifiableList(appointments);
    }

    @Medical
    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    @Admin
    public void setSocialSecurity(SocialSecurity socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public boolean isAdmitted(){
        return currentStay!=null;
    }

    public void admit(Stay stay){
        if ( isAdmitted() ) {
            throw new HospitalException(this + " is already admitted.");
        } else {
            this.currentStay = stay;
        }
    }

    public String toString(){
        return name + "(patient)";
    }

}
