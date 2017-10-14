package be.howest.ti.frameworks.hospital.services;

import be.howest.ti.frameworks.hospital.data.*;
import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.attributes.Condition;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
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
    private final DoctorRepository doctors;
    private final DepartmentRepository departments;
    private final RoomRepository rooms;
    private final StayRepository stays;
    private final AppointmentServices appointments;

    @Autowired
    HospitalService(PatientRepository patients,
                       UserServices users,
                       DoctorRepository doctors,
                       DepartmentRepository departments,
                       RoomRepository rooms,
                       StayRepository stays,
                    AppointmentServices appointments) {
        this.patients = patients;
        this.users = users;
        this.doctors = doctors;
        this.departments = departments;
        this.rooms = rooms;
        this.stays = stays;
        this.appointments = appointments;

        populateWithTestData();
    }

    private void populateWithTestData(){
        User a = createPatient("Alice Armageddon");
        createPatient("Bob Brusseels");
        createPatient("Carol Cardoen");
        createPatient("Bob Brusseels");

        User house = createDoctor("House", Condition.BLOOD);
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
        }

        Date now = new Date();
        for(int i=-5 ; i<5 ; i++){
            Date time = new Date( System.currentTimeMillis() + i*TimeUnit.HOURS.toMillis(1));
            makeAppointment(a.getPatient(),house.getDoctor(),time);
        }



    }

    public User createDoctor(String name, Condition specialty) {
        Doctor d = new Doctor(name, specialty);
        User u = new User(name, name + "123", d);

        doctors.save(d);
        return users.save(u);
    }

    public User createPatient(String name) {
        Patient p = new Patient(name, BloodType.UNKNOWN, SocialSecurity.A);
        String username = users.createUsername(name);
        User u = new User(username, username + "123", p);

        patients.save(p);
        return users.save(u);
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


    public User findUser(String username){
        return users.findOne(username);
    }

    public List<User> findAllDoctors(){
        return users.findAll(User::isDoctor);
    }

    public List<User> findAllPatients(){
        return users.findAll(User::isPatient);
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
}
