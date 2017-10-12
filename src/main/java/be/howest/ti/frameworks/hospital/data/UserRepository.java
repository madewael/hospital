package be.howest.ti.frameworks.hospital.data;

import be.howest.ti.frameworks.hospital.domain.persons.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {

}