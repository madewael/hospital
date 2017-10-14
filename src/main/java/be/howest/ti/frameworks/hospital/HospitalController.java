package be.howest.ti.frameworks.hospital;

import be.howest.ti.frameworks.hospital.data.*;
import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.attributes.Condition;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.persons.Person;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import be.howest.ti.frameworks.hospital.domain.units.Department;
import be.howest.ti.frameworks.hospital.domain.units.Room;
import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;
import javassist.bytecode.DeprecatedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/API")
public class HospitalController {

    private final PatientRepository patients;
    private final UserServices users;
    private final DoctorRepository doctors;
    private final DepartmentRepository departments;
    private final RoomRepository rooms;
    private final StayRepository stays;

    @Autowired
    HospitalController(
            PatientRepository patients,
            UserServices users,
            DoctorRepository doctors,
            DepartmentRepository departments,
            RoomRepository rooms,
            StayRepository stays) {
        this.patients = patients;
        this.users = users;
        this.doctors = doctors;
        this.departments = departments;
        this.rooms = rooms;
        this.stays = stays;

        createPatient("Alice Armageddon");
        createPatient("Bob Brusseels");
        createPatient("Carol Cardoen");
        createPatient("Bob Brusseels");

        createDoctor("House", "BLOOD");
        createDoctor("Jos", "BLOOD");


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

    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(       Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }


    @GetMapping("/tests")
    public List<String> listTestStrings() {
        String extra = "nope";
        return Arrays.asList("test1", "test2", extra);
    }

    @GetMapping("/currentUser")
    public Person getCurrentUser(Principal p) {
        if (p!=null && p.getName() != null) {
            User u = users.findOne(p.getName());
            return u.getPerson();
        } else {
            return null;
        }
    }

    @GetMapping("/patients")
    public Map<String,String> listPatients() {
        Map<String,String> res = new HashMap<>();
        for(User u : users.findAll(User::isPatient) ) {
            res.put(u.getUserName(),u.getPerson().getName());
        }
        return res;
    }

    @GetMapping("/doctors")
    public Map<String,Doctor> listDoctors() {
        Map<String,Doctor> res = new HashMap<>();
        for(User u : users.findAll(User::isDoctor) ) {
            Doctor d = u.getDoctor();
            res.put(u.getUserName(), d);
        }
        return res;
    }

    @GetMapping("/conditions")
    public Map<Integer,String> listConditions() {
        Map<Integer,String> res = new HashMap<>();
        for(Condition d : Condition.values() ) {
            res.put( d.ordinal() , d.symptom );
        }
        return res;
    }

    @PostMapping("/patient")
    public User createPatient(@RequestParam String name) {
        if (name==null || name.length()<1){
            throw new HospitalException("Cannot create patient with an empty name");
        }

        Patient p = new Patient(name, BloodType.UNKNOWN, SocialSecurity.A);
        String username = users.createUsername(name);
        User u = new User(username, username + "123", p);

        patients.save(p);

        return users.save(u);
    }

    @PostMapping("/doctor")
    public User createDoctor(@RequestParam String name,@RequestParam String specialty) {
        Condition c = Condition.parse(specialty);
        Doctor d = new Doctor(name, c);
        User u = new User(name, name + "123", d);

        doctors.save(d);
        return users.save(u);
    }

    @PatchMapping("/patients/{userName}/blood/{bloodType}")
    public Patient updateBloodType(
            @PathVariable final String userName,
            @PathVariable final String bloodType
    ) {
        Patient p = findPatient(userName);
        BloodType bt = BloodType.parse(bloodType);
        p.setBloodType(bt);
        return patients.save(p);
    }

    @PostMapping("/patient/admit")
    public Stay admitPatient(
            @RequestParam final String username,
            @RequestParam final int condition,
            @RequestParam final int roomSize
    ) {
        Patient p = findPatient(username);

        if (p.isAdmitted()){
            throw new HospitalException(p + " is already admitted.");
        }


        Condition c = Condition.values()[condition];
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

    @PostMapping("/patient/appointment")
    public Appointment makeAppointment(
            @RequestParam final String patient,
            @RequestParam final String doctor,
            @RequestParam final Date date

    ) {
        Patient p = findPatient(patient);
        Doctor d = findDoctor(doctor);
        return new Appointment(p, d, date);
    }

    @PatchMapping("/patients/{userName}/social/{social}")
    public Patient updateSocialSecurity(
            @PathVariable final String userName,
            @PathVariable final String social
    ) {
        Patient p = findPatient(userName);
        SocialSecurity ss = SocialSecurity.parse(social);
        p.setSocialSecurity(ss);
        return patients.save(p);
    }

    @GetMapping("/patients/{userName}")
    public Patient findPatient(@PathVariable final String userName) {
        User u = users.findOne(userName);
        if (u == null || !u.isPatient())
            throw new HospitalException("Could not find the needed patient:"+userName);
        return u.getPatient();
    }

    @GetMapping("/doctors/{userName}")
    public Doctor findDoctor(@PathVariable final String userName) {
        User u = users.findOne(userName);
        if (u == null || !u.isDoctor())
            throw new HospitalException("Could not find the needed doctor");
        return u.getDoctor();
    }

}
