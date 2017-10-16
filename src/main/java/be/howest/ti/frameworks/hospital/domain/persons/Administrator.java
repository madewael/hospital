package be.howest.ti.frameworks.hospital.domain.persons;

import javax.persistence.Entity;

@Entity
public class Administrator extends Person {

    public Administrator(){}
    public Administrator(String username, String password, String name){
        super(name,password,name);
    }

}
