package com.heaven7.java.data.mediator.test.relationship;

import com.heaven7.java.data.mediator.test.copy.Student;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class HashMapTest {

    @Test
    public void testReflectFieldFromSuper(){
        //Student student = new GoodStudent();
        Student student = new Student();
        try {
            Field field = student.getClass().getField("PROP_name");
            System.out.println(field);

            Method me = student.getClass().getMethod("getName");
            System.out.println(me);
            //Field publicField = student.getClass().getField("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHashEquals(){
        Student student = new Student();
        student.setName("1");

        HashMap map = new HashMap();
        map.put(student, 1);
        student.setName("2");

        //when hash, equals depend name. after change name, we can't find it.
        //when hash, hash depend name(equals not). after change name, we can't find it.
        //when equals depend name(hash not). after change name, we can find it.
        assert map.get(student)==null;
    }
}
