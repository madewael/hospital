package be.howest.ti.frameworks.hospital.aspects;

import be.howest.ti.frameworks.hospital.domain.access.Admin;
import be.howest.ti.frameworks.hospital.domain.access.Involved;
import be.howest.ti.frameworks.hospital.domain.access.Medical;
import be.howest.ti.frameworks.hospital.domain.persons.Administrator;
import be.howest.ti.frameworks.hospital.domain.persons.Doctor;
import be.howest.ti.frameworks.hospital.domain.persons.User;
import be.howest.ti.frameworks.hospital.services.HospitalService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class JsonViewFilterAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private static HospitalService _hospital;

    @Autowired
    public JsonViewFilterAspect(HospitalService hospital){
        _hospital = hospital;
        log.info("Created component:"+hospital);
    }

    public JsonViewFilterAspect(){
    }

    @Around("execution(* be.howest.ti.frameworks.hospital.domain..get*())")
    public Object protectDomainGetterAccess(ProceedingJoinPoint thisJoinPoint) {
        final HospitalService hospital = _hospital;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean access = true;
        if (auth != null) {
            String name = auth.getName(); //get logged in username
            MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
            Method targetMethod = methodSignature.getMethod();

            String str = "";


            boolean med = targetMethod.getAnnotation(Medical.class) != null;
            boolean inv = targetMethod.getAnnotation(Involved.class) != null;
            boolean adm = targetMethod.getAnnotation(Admin.class) != null;
            access = !(med||inv||adm);



            if (med) str += "M";
            if (inv) str += "I";
            if (adm) str += "A";

            if (hospital == null) {
                log.error("No hospital bean");
            }




            if ( targetMethod.getAnnotation(Medical.class)!=null ) {
                Doctor d = hospital.findDoctor(name);
                if (d!=null) access = true;
            }

            if ( targetMethod.getAnnotation(Admin.class)!=null ) {
                Administrator d = hospital.findAdmin(name);
                if (d!=null) access = true;
            }

            if (targetMethod.getAnnotation(Involved.class)!=null) {
                User u = hospital.findUser(name);
                if(thisJoinPoint.getTarget() == u){
                    access = true;
                }
            }

            log.info(name + " requests "+ thisJoinPoint.getSignature().toShortString() + " ("+ str +", "+access+")");
        } else {
            log.info("!!! requests " + thisJoinPoint.getSignature().toShortString());
        }

        Object res = null;
        try {
            res = thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (access)
            return res;
        else
            return null;

    }


}
