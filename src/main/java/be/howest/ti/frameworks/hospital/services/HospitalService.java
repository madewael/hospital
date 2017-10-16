package be.howest.ti.frameworks.hospital.services;

import be.howest.ti.frameworks.hospital.data.*;
import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.attributes.Condition;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
import be.howest.ti.frameworks.hospital.domain.persons.Administrator;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import be.howest.ti.frameworks.hospital.domain.units.Department;
import be.howest.ti.frameworks.hospital.domain.units.Room;
import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class HospitalService {

    private final PatientRepository patients;
    private final UserServices users;
    private final AdminRepository admins;
    private final DoctorRepository doctors;
    private final DepartmentRepository departments;
    private final RoomRepository rooms;
    private final StayRepository stays;
    private final AppointmentServices appointments;

    @Autowired
    HospitalService(PatientRepository patients,
                       UserServices users,
                       DoctorRepository doctors,
                    AdminRepository admins,
                       DepartmentRepository departments,
                       RoomRepository rooms,
                       StayRepository stays,
                    AppointmentServices appointments) {
        this.patients = patients;
        this.users = users;
        this.doctors = doctors;
        this.admins = admins;
        this.departments = departments;
        this.rooms = rooms;
        this.stays = stays;
        this.appointments = appointments;

        //populateWithTestData();
    }

    private void populateWithTestData(){


        Patient a = createPatient("Alice Armageddon");
        createPatient("Bob Brusseels");
        createPatient("Carol Cardoen");
        createPatient("Bob Brusseels");

        Doctor house = createDoctor("House", Condition.BLOOD);
        createDoctor("Jos", Condition.BLOOD);


        for(Condition d : Condition.values()){
            Department dep = new Department(d);
            if (d==Condition.BLOOD){
                rooms.save(dep.addRoom(1));
            } else if (d==Condition.EYE){
                rooms.save(dep.addRoom(0));
            } else {

                rooms.save(dep.addRoom(1));
                rooms.save(dep.addRoom(1));

                rooms.save(dep.addRoom(2));
                rooms.save(dep.addRoom(2));
                rooms.save(dep.addRoom(2));
                rooms.save(dep.addRoom(2));

                rooms.save(dep.addRoom(4));
                rooms.save(dep.addRoom(4));


            }
            departments.save(dep);

            admins.save(createAdmin("root"));

        }

        Date now = new Date();
        for(int i=-5 ; i<5 ; i++){
            Date time = new Date( System.currentTimeMillis() + i*TimeUnit.HOURS.toMillis(1));
            makeAppointment(a,house,time);
        }



    }

    public Doctor createDoctor(String name, Condition specialty) {
        String username = users.createUsername(name);
        Doctor d = new Doctor(username,username+"123",name, specialty);
        return doctors.save(d);
    }

    public Administrator createAdmin(String name) {
        String username = users.createUsername(name);
        Administrator d = new Administrator(username,username+"123",name);
        return admins.save(d);
    }

    public Patient createPatient(String name) {
        String username = users.createUsername(name);
        Patient p = new Patient(username, username+"123",name, BloodType.UNKNOWN, SocialSecurity.A);
        return (Patient) users.save(p);
    }

    public Stay admitPatient(Patient p, Condition c, int roomSize){

        if (p.isAdmitted()){
            throw new HospitalException(p + " is already admitted.");
        }



        Department d = departments.findOne(c);

        List<Room> roomsWithFreeSpace = d.getRoomsWithFreeSpace();

        if (roomsWithFreeSpace.size()<=0){
            throw new HospitalException("No more free rooms in the department of " + d);
        }

        Set<Integer> capacitiesOfRoomWithFreeSpace = new HashSet<>();
        for(Room m : roomsWithFreeSpace){

            if (m.getCapacity()==roomSize){
                Stay s = new Stay(m);
                p.admit(s);
                stays.save(s);
                patients.save(p);
                return s;
            } else {
                capacitiesOfRoomWithFreeSpace.add(m.getCapacity());
            }

        }
        throw new HospitalException("No more rooms with the requested size. Available sizes are "+ capacitiesOfRoomWithFreeSpace);
    }

    public Iterable<Doctor> findAllDoctors(){
        return doctors.findAll();
    }

    public Iterable<Patient> findAllPatients(){
        return patients.findAll();
    }

    public Patient updateSocialSecurity(Patient p, SocialSecurity ss) {
        p.setSocialSecurity(ss);
        return patients.save(p);
    }

    public Patient updateBloodType(Patient p, BloodType bt) {
        p.setBloodType(bt);
        return patients.save(p);
    }

    public Appointment makeAppointment(Patient p, Doctor d, Date date) {
        Appointment app = new Appointment(p, d, date);
        return appointments.save(app);
    }

    public List<Appointment> getAppointmentsForDoctor(Doctor d) {
        return appointments.findRelevantAppointments(d);
    }

    public User findUser(String username) {
        return users.findOne(username);
    }

    public Doctor findDoctor(String userName) {
        return doctors.findOne(userName);
    }

    public Patient findPatient(String userName) {
        return patients.findOne(userName);
    }

    public Administrator findAdmin(String userName) {
        return admins.findOne(userName);
    }
}
