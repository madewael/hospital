package be.howest.ti.frameworks.hospital.domain.persons;

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
