package be.howest.ti.frameworks.hospital.domain.services;

import be.howest.ti.frameworks.hospital.domain.bills.Billable;
import be.howest.ti.frameworks.hospital.domain.units.Room;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Stay implements Billable {

    private Date admissionDate;
    private Date fireDate;
    private Room room;

    public Stay(){}

    public Stay(Room room){
        admissionDate = new Date();
        this.room = room;
        fireDate = null;
    }


    public int duration(){
        long duration = fireDate.getTime()-admissionDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(duration);
    }

    @Override
    public double cost() {
        return duration()*room.getCostPerDay();
    }

}
