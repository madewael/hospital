package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.data.UserRepository;
import be.howest.ti.frameworks.hospital.data.UserServices;
import be.howest.ti.frameworks.hospital.domain.access.Admin;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@MappedSuperclass
public abstract class Person {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    protected String name;

    Person(){}

    Person(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
