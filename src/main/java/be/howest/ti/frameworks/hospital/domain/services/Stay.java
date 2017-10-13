package be.howest.ti.frameworks.hospital.domain.services;

import be.howest.ti.frameworks.hospital.domain.bills.Billable;
import be.howest.ti.frameworks.hospital.domain.units.Room;

import javax.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
public class Stay implements Billable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private Date admissionDate;
    private Date fireDate;

    @ManyToOne
    private Room room;

    public Stay(){}

    public Stay(Room room){
        admissionDate = new Date();
        this.room = room;
        fireDate = null;
    }


    public int duration() {
        long duration = fireDate.getTime()-admissionDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(duration);
    }

    @Override
    public double cost() {
        return duration()*room.getCostPerDay();
    }

    public Room getRoom(){
        return room;
    }

    public Date getAdmissionDate(){
        return admissionDate;
    }

    public Date getFireDate(){
        return fireDate;
    }
}
