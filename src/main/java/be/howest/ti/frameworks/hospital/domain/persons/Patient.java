package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.domain.access.Admin;
import be.howest.ti.frameworks.hospital.domain.access.Involved;
import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.access.Medical;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
import be.howest.ti.frameworks.hospital.domain.bills.Bill;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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

    public Patient(){}

    public Patient(String name, BloodType bt, SocialSecurity ss){
        super(name);
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
}
