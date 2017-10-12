package be.howest.ti.frameworks.hospital.data;

import be.howest.ti.frameworks.hospital.domain.persons.Person;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserServices {


    private final UserRepository users;

    @Autowired
    UserServices(UserRepository users) {
        this.users = users;

    }

    public User save(User u) {
        return users.save(u);
    }

    public User findOne(String userName) {
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

    public List<User> findAll(Predicate<User> keep){
        List<User> lst = new ArrayList();
        for( User u : users.findAll()){
            if (keep.test(u)){
                lst.add(u);
            }
        }
        return lst;
    }

    public String createUsername(String name){
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
