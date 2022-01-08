package com.milind.terabytesizequeue.model;


import net.openhft.chronicle.wire.SelfDescribingMarshallable;

// Take a note of the base class.
public  class Emp extends SelfDescribingMarshallable {

    long empId;
    String name;

    public long getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
