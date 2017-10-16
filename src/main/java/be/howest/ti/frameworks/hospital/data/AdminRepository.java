package be.howest.ti.frameworks.hospital.data;

import be.howest.ti.frameworks.hospital.domain.persons.Administrator;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AdminRepository extends CrudRepository<Administrator,String> {

}