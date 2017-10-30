package com.heaven7.data.mediator.demo.testpackage;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IDataMediator;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

import static com.heaven7.java.data.mediator.FieldFlags.COMPLEX_ARRAY;
import static com.heaven7.java.data.mediator.FieldFlags.COMPLEX_LIST;
import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;

/**
 * Created by heaven7 on 2017/9/5 0005.
 */

//only ClassBind extends ICopyable.  FLAG_COPY can used.

@Fields(value = {
      @Field(propName = "student", seriaName = "class_1", type = TestBind.class, flags = FLAGS_ALL_SCOPES),
      @Field(propName = "student2", seriaName = "class_2", type = TestBind.class,
              complexType = COMPLEX_LIST, flags = FLAGS_ALL_SCOPES),
      @Field(propName = "student3", seriaName = "class_3", type = TestBind.class,
              complexType = COMPLEX_ARRAY, flags = FLAGS_ALL_SCOPES),
      @Field(propName = "student4", seriaName = "class_4", type = TestBind.class, flags = FLAGS_ALL_SCOPES),
})
public interface ClassBind extends TestBind2 ,IDataMediator, DataPools.Poolable {

    Property PROP_student = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student", 0);
    Property PROP_student2 = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student2", 2);
    Property PROP_student3 = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student3", 1);
    Property PROP_student4 = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student4", 0);

    ClassBind setStudent(TestBind student1);

    TestBind getStudent();

    ClassBind setStudent2(List<TestBind> student21);

    List<TestBind> getStudent2();

    ListPropertyEditor<? extends ClassBind, TestBind> beginStudent2Editor();

    ClassBind setStudent3(TestBind[] student31);

    TestBind[] getStudent3();

    ClassBind setStudent4(TestBind student41);

    TestBind getStudent4();/*
================== start methods from super properties ===============
======================================================================= */

    ClassBind setName(Integer name1);

    ClassBind setName2(Integer[] name21);

    ClassBind setName3(List<Integer> name31);

    ListPropertyEditor<? extends ClassBind, Integer> beginName3Editor();

    ClassBind setData(ResultData data1);
}

