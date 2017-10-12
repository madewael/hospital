package be.howest.ti.frameworks.hospital.domain.units;

import be.howest.ti.frameworks.hospital.domain.attributes.Condition;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Department {

    @Id
    private Condition specialty;

    @OneToMany
    private List<Room> rooms;


    public Department(){}

    public Department(Condition specialty){
        this.specialty = specialty;
        rooms = new ArrayList<>();
    }

    public Room addRoom(int capacity){
        Room r = new Room(specialty.doctor+"_"+rooms.size(),capacity);
        rooms.add(r);
        return r;
    }

    public int getPatientCount(){
        return getRooms()
                .stream()
                .map(Room::getPatientCount)
                .reduce(0, Integer::sum);
    }

    public int getCapacity(){
        return getRooms()
                .stream()
                .map(Room::getCapacity)
                .reduce(0, Integer::sum);
    }


    public Condition getSpecialty() {
        return specialty;
    }


    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

}
