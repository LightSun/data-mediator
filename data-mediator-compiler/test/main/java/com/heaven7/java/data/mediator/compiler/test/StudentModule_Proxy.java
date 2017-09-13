package com.heaven7.java.data.mediator.compiler.test;

import com.heaven7.java.data.mediator.BaseMediator;
import com.heaven7.java.data.mediator.Property;

/**
 * Created by heaven7 on 2017/9/13 0013.
 */
public class StudentModule_Proxy extends BaseMediator<IStudent> implements IStudent{

    private static final Property PROP_AGE   = new Property("int", "age", 0);
    private static final Property PROP_NAME  = new Property("java.lang.String", "name", 0);
    private static final Property PROP_ID    = new Property("java.lang.String", "id", 0);
    private EqualsComparator mEqualsComparator = DefaultEqualsComparator.getInstance();

    public StudentModule_Proxy(IStudent student){
       super(student);
    }

    public EqualsComparator getEqualsComparator() {
        return mEqualsComparator;
    }
    public void setEqualsComparator(EqualsComparator mEqualsComparator) {
        this.mEqualsComparator = mEqualsComparator;
    }

    @Override
    public int getAge() {
        return getTarget().getAge();
    }

    @Override
    public void setAge(int age) {
        IStudent target = getTarget();
        int oldValue = target.getAge();
        if(oldValue == age){
            return;
        }
        target.setAge(age);
        dispatchCallbacks(PROP_AGE, oldValue, age);
    }

    @Override
    public String getName() {
        return getTarget().getName();
    }

    @Override
    public void setName(String name) {
        IStudent target = getTarget();
        String oldValue = target.getName();
        if(name != null ){
            if(name.equals(oldValue)){
                return;
            }
        }else {
            if(oldValue == null){
                return;
            }
        }
        target.setName(name);
        dispatchCallbacks(PROP_NAME, oldValue, name);
    }

    @Override
    public String getId() {
        return getTarget().getId();
    }

    @Override
    public void setId(String id) {
        IStudent target = getTarget();
        String oldValue = target.getName();
        if(id != null ){
            if(id.equals(oldValue)){
                return;
            }
        }else {
            if(oldValue == null){
                return;
            }
        }
        target.setId(id);
        dispatchCallbacks(PROP_ID, oldValue, id);
    }


}
