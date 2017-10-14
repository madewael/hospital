package be.howest.ti.frameworks.hospital.data;

import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment,Integer> {

    List<Appointment> findAppointmentByDoctorAndDateAfter(Doctor d, Date after);
    List<Appointment> findAppointmentByPatientAndDateAfter(Patient p, Date after);
}