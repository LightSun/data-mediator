package com.heaven7.java.data.mediator.test;

import com.heaven7.java.data.mediator.ListPropertyEditor;

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
    public ListPropertyEditor<IStudent, String> newTagsEditor() {
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
