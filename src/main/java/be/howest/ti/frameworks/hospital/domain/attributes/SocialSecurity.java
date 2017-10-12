package be.howest.ti.frameworks.hospital.domain.attributes;

import be.howest.ti.frameworks.hospital.domain.utils.HospitalException;

public enum SocialSecurity {

    A(1.0),B(0.5),C(0.0);

    public final double factor;

    SocialSecurity(double factor){
        this.factor = factor;
    }

    public static SocialSecurity parse(String social) {
        try {
            return SocialSecurity.valueOf(social);
        } catch (IllegalArgumentException ex){
            throw new HospitalException("Unknown social state" + social, ex);
        }
    }
}
