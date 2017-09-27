package com.heaven7.java.data.mediator.test.data;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.test.Parcel;
import com.heaven7.java.data.mediator.test.Parcelable;

import java.io.Serializable;

public interface StudentModule extends DataPools.Poolable{

  String toString();

  int getAge();

  StudentModule setAge(int age1);

  String getName();

  StudentModule setName(String name1);

  long getId();

  StudentModule setId(long id1);
}
