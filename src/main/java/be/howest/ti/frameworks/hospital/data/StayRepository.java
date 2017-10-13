package be.howest.ti.frameworks.hospital.data;

import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import org.springframework.data.repository.CrudRepository;

public interface StayRepository extends CrudRepository<Stay,Integer> {

}