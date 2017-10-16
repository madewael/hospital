package be.howest.ti.frameworks.hospital.controllers;

import be.howest.ti.frameworks.hospital.domain.attributes.BloodType;
import be.howest.ti.frameworks.hospital.domain.attributes.Condition;
import be.howest.ti.frameworks.hospital.domain.attributes.SocialSecurity;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;
import be.howest.ti.frameworks.hospital.domain.persons.Person;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.domain.services.Appointment;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;
import be.howest.ti.frameworks.hospital.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/API")
public class HospitalController {

    private final HospitalService hospital;

    @Autowired
    HospitalController(HospitalService hospital) {
        this.hospital = hospital;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(       Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));

    }

    @GetMapping("/currentUser")
    public User getCurrentUser(Principal p) {
        if (p!=null && p.getName() != null) {
            User u = hospital.findUser(p.getName());
            return u;
        } else {
            return null;
        }
    }

    @GetMapping("/patients")
    public Map<String,String> listPatients() {
        Map<String,String> res = new HashMap<>();
        for(Patient p : hospital.findAllPatients() ) {
            res.put(p.getUserName(),p.getName());
        }
        return res;
    }

    @GetMapping("/doctors")
    public Map<String,Object> listDoctors() {
        Map<String,Object> res = new HashMap<>();
        for(Doctor d : hospital.findAllDoctors() ) {
            res.put(d.getUserName(), new Object(){
                public String getName(){ return d.getName(); }
                public String getSpecialty() { return d.getSpecialty().getDoctor(); }
            });
        }
        return res;
    }


    @GetMapping("/conditions")
    public Map<Integer,Condition> listConditions() {
        Map<Integer,Condition> res = new HashMap<>();
        for(Condition d : Condition.values() ) {
            res.put( d.ordinal() , d );
        }
        return res;
    }

    @PostMapping("/patient")
    public Patient createPatient(@RequestParam String name) {
        if (name==null || name.length()<1){
            throw new HospitalException("Cannot create patient with an empty name");
        }
        return hospital.createPatient(name);
    }

    @PostMapping("/doctor")
    public Doctor createDoctor(@RequestParam String name, @RequestParam String specialty) {
        if (name==null || name.length()<1){
            throw new HospitalException("Cannot create doctor with an empty name");
        }
        Condition c = Condition.parse(specialty);

        return hospital.createDoctor(name, c);

    }

    @PatchMapping("/patients/{userName}/blood/{bloodType}")
    public Patient updateBloodType(
            @PathVariable final String userName,
            @PathVariable final String bloodType
    ) {
        Patient p = findPatient(userName);
        BloodType bt = BloodType.parse(bloodType);
        return hospital.updateBloodType(p,bt);
    }

    @PostMapping("/patient/admit")
    public Stay admitPatient(
            @RequestParam final String username,
            @RequestParam final int condition,
            @RequestParam final int roomSize
    ) {
        Patient p = findPatient(username);
        Condition c = Condition.values()[condition];
        return hospital.admitPatient(p,c,roomSize);
    }

    @PostMapping("/patient/appointment")
    public Appointment makeAppointment(
            @RequestParam final String patient,
            @RequestParam final String doctor,
            @RequestParam final Date date

    ) {
        Patient p = findPatient(patient);
        Doctor d = findDoctor(doctor);
        return hospital.makeAppointment(p,d,date);
    }

    @PatchMapping("/patients/{userName}/social/{social}")
    public Patient updateSocialSecurity(
            @PathVariable final String userName,
            @PathVariable final String social
    ) {
        Patient p = findPatient(userName);
        SocialSecurity ss = SocialSecurity.parse(social);
        return hospital.updateSocialSecurity(p,ss);
    }

    @GetMapping("/patients/{userName}")
    public Patient findPatient(@PathVariable final String userName) {
        Patient d = hospital.findPatient(userName);
        if (d == null )
            throw new HospitalException("Could not find the needed Patient:"+userName);
        return d;
    }

    @GetMapping("/doctors/{userName}")
    public Doctor findDoctor(@PathVariable final String userName) {
        Doctor d = hospital.findDoctor(userName);
        if (d == null )
            throw new HospitalException("Could not find the needed doctor:"+userName);
        return d;
    }


    @GetMapping("/doctors/{userName}/appointments")
    public List<Appointment> getAppointmentsForDoctor(
            @PathVariable final String userName
    ) {
        Doctor d = findDoctor(userName);

        return hospital.getAppointmentsForDoctor(d);
    }

}
