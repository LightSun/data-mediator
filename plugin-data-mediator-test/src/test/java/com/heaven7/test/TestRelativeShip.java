package com.heaven7.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.plugin.idea.data_mediator.test.Student;
import com.heaven7.plugin.idea.data_mediator.test.TestItem;
import org.junit.Test;

public class TestRelativeShip {

    @Test
    public void test1(){
        //TestItem.PROP_testItem_1
        testSimple();
        testList1();
        testList2();
    }

    private void testList2() {
        //@BindXXX("stu.tags")
        DataMediator<TestItem> dm = DataMediatorFactory.createDataMediator(TestItem.class);
        DataMediator<Student> dm_stu = DataMediatorFactory.createDataMediator(dm.getData().getTestItem_1());

        dm_stu.addDataMediatorCallback(DataMediatorCallback.create("tags", new ListPropertyCallback<Student>() {
            @Override
            public void onPropertyValueChanged(Student data, Property prop, Object oldValue, Object newValue) {

            }

            @Override
            public void onPropertyApplied(Student data, Property prop, Object value) {

            }

            @Override
            public void onAddPropertyValues(Student data, Property prop, Object newValue, Object addedValue) {

            }

            @Override
            public void onAddPropertyValuesWithIndex(Student data, Property prop, Object newValue, Object addedValue, int index) {

            }

            @Override
            public void onRemovePropertyValues(Student data, Property prop, Object newValue, Object removeValue) {

            }

            @Override
            public void onPropertyItemChanged(Student data, Property prop, Object oldItem, Object newItem, int index) {

            }
        }));
    }

    private void testList1() {
        //@BindXXX("stus[0].name")
        DataMediator<TestItem> dm = DataMediatorFactory.createDataMediator(TestItem.class);
        Student student = dm.getData().getTestItem_3().get(0);

        DataMediator<Student> dm_stu = DataMediatorFactory.createDataMediator(student);
        String stuPropName = "name";
        dm_stu.addDataMediatorCallback(new DataMediatorCallback<Student>() {
            @Override
            public void onPropertyValueChanged(Student data, Property prop, Object oldValue, Object newValue) {
                if(prop.getName().equals(stuPropName)){
                    //virtual property
                    dm.getBaseMediator().dispatchValueChanged(prop, oldValue, newValue);
                }
            }
        });
    }

    private void testSimple() {
        //@BindXXX("stu.name")
        DataMediator<TestItem> dm = DataMediatorFactory.createDataMediator(TestItem.class);

        DataMediator<Student> dm_stu = DataMediatorFactory.createDataMediator(dm.getData().getTestItem_1());
        String stuPropName = "name";
        dm_stu.addDataMediatorCallback(new DataMediatorCallback<Student>() {
            @Override
            public void onPropertyValueChanged(Student data, Property prop, Object oldValue, Object newValue) {
                if(prop.getName().equals(stuPropName)){
                    //virtual property
                    dm.getBaseMediator().dispatchValueChanged(prop, oldValue, newValue);
                }
            }
        });
    }

}
