package be.howest.ti.frameworks.hospital.domain.utils;

public class HospitalException extends RuntimeException {
    public HospitalException(String msg) {
        super(msg);
    }
    public HospitalException(String msg,Throwable cause) {
        super(msg,cause);
    }
}
