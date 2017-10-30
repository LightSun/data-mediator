package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_ARRAY;
import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_LIST;
import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;

/**
 * Created by heaven7 on 2017/8/29 0029.
 */
@Fields({
        @Field(propName = "student", seriaName = "class_1", type = TestBind.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "student2", seriaName = "class_2", type = TestBind.class,
                complexType = COMPLEXT_LIST, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "student3", seriaName = "class_3", type = TestBind.class,
                complexType = COMPLEXT_ARRAY, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "student4", seriaName = "class_4", type = TestBind.class, flags = FLAGS_ALL_SCOPES)
})
public interface TestInterface2 extends StudentBind, Parcelable {

    Property PROP_student = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student", 0);
    Property PROP_student2 = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student2", 2);
    Property PROP_student3 = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student3", 1);
    Property PROP_student4 = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student4", 0);

    TestInterface2 setStudent(TestBind student1);

    TestBind getStudent();

    TestInterface2 setStudent2(List<TestBind> student21);

    List<TestBind> getStudent2();

    ListPropertyEditor<? extends TestInterface2, TestBind> beginStudent2Editor();

    TestInterface2 setStudent3(TestBind[] student31);

    TestBind[] getStudent3();

    TestInterface2 setStudent4(TestBind student41);

    TestBind getStudent4();/*
================== start methods from super properties ===============
======================================================================= */

    TestInterface2 setName(String name1);

    TestInterface2 setTest_object(Object test_object1);

    TestInterface2 setTest_Format(Double test_Format1);

    TestInterface2 setTest_int(int test_int1);

    TestInterface2 setTest_list(List<Long> test_list1);

    ListPropertyEditor<? extends TestInterface2, Long> beginTest_listEditor();

    TestInterface2 setTest_array(String[] test_array1);
}
