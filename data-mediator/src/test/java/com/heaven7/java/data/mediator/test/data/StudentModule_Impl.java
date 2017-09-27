package com.heaven7.java.data.mediator.test.data;

import com.heaven7.java.base.util.Objects;
import com.heaven7.java.data.mediator.DataPools;

import java.io.Serializable;

public class StudentModule_Impl implements StudentModule, Serializable{
  private static final long serialVersionUID =  1L;

  private boolean selected;

  private int age;

  private String name;

  private long id;

  public StudentModule_Impl() {
  }

  @Override
  public String toString() {
    Objects.ToStringHelper helper = Objects.toStringHelper(this)
        .add("age", String.valueOf(this.age))
        .add("name", String.valueOf(this.name))
        .add("id", String.valueOf(this.id));
    return helper.toString();
  }

  public int getAge() {
    return age;
  }

  public StudentModule setAge(int age1) {
    this.age = age1;
    return this;
  }

  public String getName() {
    return name;
  }

  public StudentModule setName(String name1) {
    this.name = name1;
    return this;
  }

  public long getId() {
    return id;
  }

  public StudentModule setId(long id1) {
    this.id = id1;
    return this;
  }

  @Override
  public void clearProperties() {
    this.name = null;
    this.age = 0;
    this.id = 0;
  }

  @Override
  public void recycle() {
    DataPools.recycle(this);
  }
}
