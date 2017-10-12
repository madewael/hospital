package be.howest.ti.frameworks.hospital.domain.units;

import be.howest.ti.frameworks.hospital.domain.persons.Patient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Room {


    private static double costPerDay = 121.0;

    @Id private String id;
    private int capacity;

    @OneToMany
    private List<Patient> patients;

    private double roomFactor;

    public Room(){}

    public Room(String id, int capacity){
        this.id = id;
        this.capacity = capacity;

        roomFactor = Room.getRoomFactor(capacity);
    }


    public int getPatientCount(){
        return patients.size();
    }

    public static double getCostPerDay() {
        return costPerDay;
    }

    public double getRoomFactor() {
        return Room.getRoomFactor(capacity);
    }

    private static double getRoomFactor(int capacity) {
        switch (capacity){
            case 1: return 2.5;
            case 2: return 1.5;
            default: return 1.0;
        }
    }

    public int getCapacity() {
        return capacity;
    }
}
