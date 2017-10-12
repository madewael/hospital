package be.howest.ti.frameworks.hospital.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public aspect LoggerAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    pointcut personGetters()  : execution(* be.howest.ti.frameworks.hospital.domain..*.get*());


    private Object clean(Object obj){
        if ( obj instanceof String  ){
            return "(cleaned)";
        } if( obj instanceof Collection) {
            List res = new ArrayList();
            for(Object i : (Collection)obj){
                res.add( clean(i) );
            }
            return res;
        }else {
            return null;
        }
    }

    int i =0;
    Object around(): personGetters() {
        log.info("A getter was called:"+ thisJoinPoint.getSignature());
        Object res = proceed();

        i++;
        if (false) return clean(res);
        else return res;

    }


}
