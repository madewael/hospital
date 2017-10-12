package be.howest.ti.frameworks.hospital.domain.bills;

import be.howest.ti.frameworks.hospital.domain.access.Admin;
import be.howest.ti.frameworks.hospital.domain.services.Consult;
import be.howest.ti.frameworks.hospital.domain.services.Stay;
import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;
import be.howest.ti.frameworks.hospital.domain.persons.Patient;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


public class Bill {

    @Id
    private long id;

    private Patient patient;

    private boolean closed;

    @Admin
    private double payed;

    private Stay stay;
    private List<Consult> consults;

    public double cost(){
        double consultCosts = consults
                .stream()
                .map(Billable::cost)
                .reduce(0.0, Double::sum);
        return stay.cost() + consultCosts;
    }

    public void add(Consult consult){
        if (isOpen()){
            consults.add(consult);
        } else {
            throw new HospitalException("Can't add consult to closed bill");
        }
    }




    //@Admin
    public double dueByPatient() {
        return (cost()-payed)*patient.getSocialSecurity().factor;
    }

    public double pay(double amount){
        if ( isOpen() ){
            return amount;
        } else {
            double cost = cost();
            payed+=amount;

            if (payed>cost) {
                double refund = cost-payed;
                payed = cost;
                return refund;
            } else {
                return 0.0;
            }
        }
    }


    @Admin
    public void close(){
        closed = true;
    }

    public boolean isOpen() {
        return isClosed();
    }

    public boolean isClosed() {
        return closed;
    }
}
