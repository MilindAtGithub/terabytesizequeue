package com.milind.terabytesizequeue.util;

import com.milind.terabytesizequeue.model.Emp;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.ThreadLocalRandom.*;

/**
 * This is the employee creator class
 */
public class EmpCreator {

    /**
     * This is the recreator Object which will use the exsisting object ony and will
     * not create any new object.
     * @param emp
     * @return
     */
    public static Emp recreateEmp( Emp emp){
        emp.setEmpId(current().nextInt(1000)+System.currentTimeMillis());
        emp.setName("Test-"+emp.getEmpId());
        return emp;
    }

}
