package com.heaven7.java.data.mediator.test;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
public interface IStudent extends ICopyable, IResetable, IShareable, ISnapable, ISelectable{
    int getAge();

    void setAge(int age);

    String getName();

    void setName(String name);

    String getId();

    void setId(String id);

    void setTags(List<String> tags);
    List<String> getTags();
    ListPropertyEditor<IStudent,String> beginTagsEditor();
    //SparseArray;

    void setCityData(SparseArray<String> sa);
    SparseArray<String> getCityData();
    SparseArrayPropertyEditor<IStudent, String> beginCityDataEditor();
}
