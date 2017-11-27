package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.SparseArrayPropertyEditor;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Fields(value = {
        @Field(propName = "name", seriaName = "heaven7", type = String.class),
        @Field(propName = "data", seriaName = "result", type = ResultData.class),
        @Field(propName = "cityData", type = ResultData.class, complexType = FieldFlags.COMPLEX_SPARSE_ARRAY),
        @Field(propName = "cityData2", type = Student.class, complexType = FieldFlags.COMPLEX_SPARSE_ARRAY),
}, maxPoolCount = 100)
public interface TestBind extends Parcelable, DataPools.Poolable {

    Property PROP_name = SharedProperties.get(String.class.getName(), "name", 0);
    Property PROP_data = SharedProperties.get(ResultData.class.getName(), "data", 0);
    Property PROP_cityData = SharedProperties.get(ResultData.class.getName(), "cityData", 3);
    Property PROP_cityData2 = SharedProperties.get(Student.class.getName(), "cityData2", 3);

    TestBind setName(String name1);

    String getName();

    TestBind setData(ResultData data1);

    ResultData getData();

    TestBind setCityData(SparseArray<ResultData> cityData1);

    SparseArray<ResultData> getCityData();

    SparseArrayPropertyEditor<? extends TestBind, ResultData> beginCityDataEditor();

    TestBind setCityData2(SparseArray<Student> cityData21);

    SparseArray<Student> getCityData2();

    SparseArrayPropertyEditor<? extends TestBind, Student> beginCityData2Editor();
}
