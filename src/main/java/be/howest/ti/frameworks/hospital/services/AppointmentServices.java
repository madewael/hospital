package be.howest.ti.frameworks.hospital.services;

import be.howest.ti.frameworks.hospital.data.AppointmentRepository;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.persons.Person;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Component
public class AppointmentServices {

    private final AppointmentRepository appointments;

    @Autowired
    AppointmentServices(AppointmentRepository appointments) {
        this.appointments = appointments;

    }

    Appointment save(Appointment app) {
        return appointments.save(app);
    }

    List<Appointment> findRelevantAppointments(Doctor d){
        Date oneHourAgo = new Date( System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        return appointments.findAppointmentByDoctorAndDateAfter(d, oneHourAgo);
    }

    List<Appointment> findRelevantAppointments(Patient p){
        Date oneHourAgo = new Date( System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        return appointments.findAppointmentByPatientAndDateAfter(p, oneHourAgo);
    }




}
