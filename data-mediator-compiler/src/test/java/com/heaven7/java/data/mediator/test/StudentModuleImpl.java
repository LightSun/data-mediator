package com.heaven7.java.data.mediator.test;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.SparseArrayPropertyEditor;
import com.heaven7.java.data.mediator.internal.DataMediatorDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/13 0013.
 */
public class StudentModuleImpl implements IStudent {

    private int age;
    private String name;
    private String id;
    private List<String> mTags;
    private SparseArray<String> cityData;

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setTags(List<String> tags) {
        this.mTags = tags;
    }

    @Override
    public List<String> getTags() {
        return mTags;
    }

    @Override
    public void setCityData(SparseArray<String> sa) {
         this.cityData = sa;
    }

    @Override
    public SparseArray<String> getCityData() {
        return cityData;
    }

    @Override
    public SparseArrayPropertyEditor<IStudent, String> beginCityDataEditor() {
        if(cityData == null){
            cityData = new SparseArray<>();
        }
        return new SparseArrayPropertyEditor<IStudent, String>(this,
                DataMediatorDelegate.getDefault().getSparseArrayDelegate(cityData),
                null, null);
    }

    @Override
    public ListPropertyEditor<IStudent, String> beginTagsEditor() {
        if(mTags == null){
            mTags = new ArrayList<>();
        }
        return new ListPropertyEditor<IStudent, String>(this, mTags, null, null);
    }

    @Override
    public Object copy() {
        return null;
    }

    @Override
    public void copyTo(Object out) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void clearShare() {

    }

    @Override
    public void clearSnap() {

    }

    @Override
    public void setSelected(boolean selected) {

    }

    @Override
    public boolean isSelected() {
        return false;
    }
}
