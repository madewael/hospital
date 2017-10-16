package be.howest.ti.frameworks.hospital.domain.persons;

import javax.persistence.*;

@MappedSuperclass
public abstract class Person extends User {

    protected String name;

    Person(){}

    Person(String userName, String password, String name){
        super(userName,password);
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
