package com.milind.terabytesizequeue;

import com.milind.terabytesizequeue.model.Emp;
import com.milind.terabytesizequeue.util.EmpCreator;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.wire.DocumentContext;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  Main Class to write one million objects
 */
public class Main {

    public static void main (String args[]){
        final Emp emp = new Emp();
        final ChronicleQueue q = ChronicleQueue.single("emp-data");
        final ExcerptAppender appender = q.acquireAppender();
        // Creating 10 ^7 ( 10 Million Objects and writing the same.
        long start = System.currentTimeMillis();
        System.out.println("Started Writing the Objects");
        Map<String, Long> indexMap = new HashMap<>();
        Emp temp = null;
        long indexPosToSearch = ThreadLocalRandom.current().nextLong(10000000);
        for (long i = 0; i < 1e7; i++) {
            try (final DocumentContext document =
                         appender.writingDocument()) {
                document
                        .wire()
                        .bytes()
                        .writeObject(Emp.class,
                                EmpCreator.recreateEmp(emp));

                if(i== indexPosToSearch){
                    temp= new Emp();
                    temp.setName(emp.getName());
                    temp.setEmpId(emp.getEmpId());
                    indexMap.put(emp.getName(), document.index());
                }
            }

        }
        System.out.println("Time taken to write 10 million object (MilliSec) : "+ (System.currentTimeMillis()- start));
        readData(q);
        getParticularEmp(indexMap,temp,q);
    }

    /**
     * Reading top 100
     * @param queue
     */
    static void readData(ChronicleQueue queue){
        final ExcerptTailer tailer = queue.createTailer();

        long start = System.currentTimeMillis();
        for (long i = 0; i < 1e2; i++) {
            try (final DocumentContext document =
                         tailer.readingDocument()) {
               Emp emp = document
                        .wire()
                        .bytes()
                        .readObject(Emp.class);
            }
        }
        System.out.println("Time taken to read first 100" +
                " objects (MilliSec) : "+ (System.currentTimeMillis()- start));
    }

    /**
     * This will get the particualr emp
     * @param indexMap
     * @param temp
     */
    static void getParticularEmp(Map<String, Long> indexMap, Emp temp,ChronicleQueue queue){
        long start = System.currentTimeMillis();
        final ExcerptTailer tailer = queue.createTailer();
        tailer.moveToIndex(indexMap.get(temp.getName()));
        Emp emp = tailer.readingDocument()
                .wire()
                .bytes()
                .readObject(Emp.class);
        System.out.println("Time taken to fetch particular Emp" +
                "object (MilliSec) : "+ (System.currentTimeMillis()- start));
        System.out.println("Assert: "+ temp.getName().equalsIgnoreCase(emp.getName()));
    }
}
