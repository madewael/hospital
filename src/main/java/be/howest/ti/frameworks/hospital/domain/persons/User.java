package be.howest.ti.frameworks.hospital.domain.persons;

import be.howest.ti.frameworks.hospital.domain.access.Admin;

import javax.persistence.*;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name = "USER_TYPE")
public class User {

    @Admin
    @Id
    private String userName;

    @Admin
    private String password;


    public User(){}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;

    }



    public String getUserName() {
        return userName;
    }

    @Admin
    public String getPassword() {
        return password;
    }
}
