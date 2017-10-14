package be.howest.ti.frameworks.hospital.services;

import be.howest.ti.frameworks.hospital.data.AppointmentRepository;
import be.howest.ti.frameworks.hospital.data.UserRepository;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Person;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Component
public class UserServices {

    private final UserRepository users;

    @Autowired
    UserServices(UserRepository users) {
        this.users = users;

    }

    User save(User u) {
        return users.save(u);
    }

    User findOne(String userName) {
        return users.findOne(userName);
    }

    public User findUserForPerson(Person p){
        for( User u : users.findAll()){
            if( u.getPerson()==p ){
                return u;
            }
        }
        return null;
    }

    List<User> findAll(Predicate<User> keep){
        List<User> lst = new ArrayList<>();
        for( User u : users.findAll()){
            if (keep.test(u)){
                lst.add(u);
            }
        }
        return lst;
    }

    String createUsername(String name){
        String usernameBase = name.toLowerCase().replaceAll("\\s+","");
        String username = usernameBase;

        int i = 2;
        while (users.exists(username)) {
            username = usernameBase + i;
            i++;
        }


        return username;
    }


}
