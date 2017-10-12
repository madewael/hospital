package be.howest.ti.frameworks.hospital.domain.attributes;

import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;

public enum BloodType {

    UNKNOWN, A_plus, A_minus, O_plus, O_minus;

    public static BloodType parse(String bloodTypeAsString) {
        try {
            return BloodType.valueOf(bloodTypeAsString);
        } catch (IllegalArgumentException ex){
            throw new HospitalException("Unknown bloodtype" + bloodTypeAsString, ex);
        }
    }
}
