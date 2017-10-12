package be.howest.ti.frameworks.hospital;

import be.howest.ti.frameworks.hospital.data.*;
import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.attributes.Condition;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.persons.Person;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import be.howest.ti.frameworks.hospital.domain.units.Department;
import be.howest.ti.frameworks.hospital.domain.units.Room;
import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;
import javassist.bytecode.DeprecatedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/API")
public class HospitalController {

    private final PatientRepository patients;
    private final UserServices users;
    private final DoctorRepository doctors;
    private final DepartmentRepository departments;
    private final RoomRepository rooms;

    @Autowired
    HospitalController(
            PatientRepository patients,
            UserServices users,
            DoctorRepository doctors,
            DepartmentRepository departments,
            RoomRepository rooms) {
        this.patients = patients;
        this.users = users;
        this.doctors = doctors;
        this.departments = departments;
        this.rooms = rooms;

        createPatient("Alice Armageddon");
        createPatient("Bob Brusseels");
        createPatient("Carol Cardoen");
        createPatient("Bob Brusseels");

        createDoctor("House", "BLOOD");
        createDoctor("Jos", "BLOOD");


        for(Condition d : Condition.values()){
            Department dep = new Department(d);
            rooms.save(dep.addRoom(1));
            rooms.save(dep.addRoom(1));

            rooms.save(dep.addRoom(2));
            rooms.save(dep.addRoom(2));
            rooms.save(dep.addRoom(2));
            rooms.save(dep.addRoom(2));

            rooms.save(dep.addRoom(4));
            rooms.save(dep.addRoom(4));

            departments.save(dep);
        }

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

    @PostMapping("/patients/admit")
    public Stay admitPatient(
            @RequestParam final String userName,
            @RequestParam final String condition
    ) {
        Patient p = findPatient(userName);
        Condition c = Condition.values()[Integer.parseInt(condition)];
        Department d = departments.findOne(c);

        Stay s = new Stay();

        return null;
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
            throw new HospitalException("Could not find the needed patient");
        Patient p = u.getPatient();
        return p;
    }

    @GetMapping("/doctors/{userName}")
    public Doctor findDoctor(@PathVariable final String userName) {
        User u = users.findOne(userName);
        if (u == null || !u.isDoctor())
            throw new HospitalException("Could not find the needed doctor");
        Doctor d = u.getDoctor();
        return d;
    }

}
