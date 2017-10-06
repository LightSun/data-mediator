package com.heaven7.java.data.mediator.test;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.internal.DataMediatorDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * generate proxy step:
 *    1, generate MediatorSharedProperties.
 *    2, generate filed .like 'PROP_AGE' and etc.
 *    3, override method for interface (property or others)
 * Created by heaven7 on 2017/9/13 0013.
 */
public class StudentModule_Proxy extends BaseMediator<IStudent> implements IStudent {

    private static final Property PROP_AGE   = MediatorSharedProperties.get("int", "age", 0);
    private static final Property PROP_NAME  = MediatorSharedProperties.get("java.lang.String", "name", 0);
    private static final Property PROP_ID    = MediatorSharedProperties.get("java.lang.String", "id", 0);
    private static final Property PROP_TAGS  = MediatorSharedProperties.get("java.lang.String", "tag", FieldData.COMPLEXT_LIST);
    private static final Property PROP_cityData  = MediatorSharedProperties.get("java.lang.String", "cityData", FieldData.COMPLEXT_SPARSE_ARRAY);

    public StudentModule_Proxy(IStudent student){
       super(student);
    }

    @Override
    public void applyProperties(PropertyInterceptor interceptor) {
        Throwables.checkNull(interceptor);
        startBatchApply(interceptor)
                .addProperty(PROP_AGE, getAge())
                .addProperty(PROP_NAME, getName())
                .addProperty(PROP_ID, getId())
                .addProperty(PROP_TAGS, getTags())
                .apply();
    }

    @Override
    public int getAge() {
        return _getTarget().getAge();
    }

    @Override
    public void setAge(int age) {
        IStudent target = _getTarget();
        int oldValue = target.getAge();
        if(_getEqualsComparator().isEquals(oldValue, age)){
            return;
        }
        target.setAge(age);
        dispatchValueChanged(PROP_AGE, oldValue, age);
    }

    @Override
    public String getName() {
        return _getTarget().getName();
    }

    @Override
    public void setName(String name) {
        IStudent target = _getTarget();
        String oldValue = target.getName();
        if(_getEqualsComparator().isEquals(oldValue, name)){
            return;
        }
        target.setName(name);
        dispatchValueChanged(PROP_NAME, oldValue, name);
    }

    @Override
    public String getId() {
        return _getTarget().getId();
    }

    @Override
    public void setId(String id) {
        IStudent target = _getTarget();
        String oldValue = target.getName();
        if(_getEqualsComparator().isEquals(oldValue, id)){
            return;
        }
        target.setId(id);
        dispatchValueChanged(PROP_ID, oldValue, id);
    }

    @Override
    public void setTags(List<String> tags) {
        _getTarget().setTags(tags);
    }

    @Override
    public List<String> getTags() {
        return _getTarget().getTags();
    }

    @Override
    public void setCityData(SparseArray<String> sa) {
        _getTarget().setCityData(sa);//callback
    }
    @Override
    public SparseArray<String> getCityData() {
        return null;
    }

    @Override
    public SparseArrayPropertyEditor<IStudent, String> beginCityDataEditor() {
        IStudent target = _getTarget();
        SparseArray<String> cityData = target.getCityData();
        if(cityData == null){
            cityData = new SparseArray<>();
            target.setCityData(cityData);
        }
        return new SparseArrayPropertyEditor<IStudent, String>(this,
                DataMediatorDelegate.getDefault().getSparseArrayDelegate(cityData),
                PROP_cityData, this);
    }

    @Override
    public ListPropertyEditor<IStudent, String> beginTagsEditor() {
        IStudent target = _getTarget();
        List<String> tags = target.getTags();
        if(tags == null){
            tags = new ArrayList<>();
            target.setTags(tags);
        }
        return new ListPropertyEditor<IStudent, String>(target, tags, PROP_TAGS, this);
    }

    @Override
    public IStudent copy() {
        return (IStudent) _getTarget().copy();
    }

    @Override
    public void copyTo(Object out) {
        _getTarget().copyTo(out);
    }

    @Override
    public void reset() {
        _getTarget().reset();
    }

    @Override
    public void clearShare() {
        _getTarget().clearShare();
    }

    @Override
    public void clearSnap() {
        _getTarget().clearSnap();
    }

    @Override
    public String toString() {
        return _getTarget().toString();
    }

    @Override
    public void setSelected(boolean selected) {
        IStudent target = _getTarget();
        boolean oldValue = target.isSelected();
        if(_getEqualsComparator().isEquals(oldValue, selected)){
            return;
        }
        target.setSelected(selected);
        dispatchValueChanged(PROP_ID, oldValue, selected);
    }

    @Override
    public boolean isSelected() {
        return _getTarget().isSelected();
    }
}
