package be.howest.ti.frameworks.hospital.domain.services;

import be.howest.ti.frameworks.hospital.domain.access.Medical;
import be.howest.ti.frameworks.hospital.domain.bills.Billable;
import be.howest.ti.frameworks.hospital.domain.units.Room;

import java.util.concurrent.TimeUnit;

public class Consult extends Appointment implements Billable {

    private long duration;

    private Room room;

    @Medical
    private String log;

    @Override
    public double cost() {
        int hours = (int) TimeUnit.MILLISECONDS.toHours(duration);
        return hours*getDoctor().getHonorarium()*room.getRoomFactor();
    }
}
