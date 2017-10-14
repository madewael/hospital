package be.howest.ti.frameworks.hospital.controllers;

import be.howest.ti.frameworks.hospital.domain.persons.Person;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class HospitalSession {

    private Person user;

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

}
