package com.heaven7.java.data.mediator.support.test;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.support.gson.Car3;

import java.util.Arrays;
import java.util.List;

/**
 * test gson
 */
@JsonAdapter(TestGsonBeanTypeAdapter.class)
public class TestGsonBean {

    public static final Property PROP_car = new Property("com.heaven7.java.data.mediator.support.gson.Car3",
            "car", 0);
    public static final Property PROP_mList = new Property("com.heaven7.java.data.mediator.support.gson.Car3",
            "list", FieldFlags.COMPLEX_LIST);
    public static final Property PROP_carsArr = new Property("com.heaven7.java.data.mediator.support.gson.Car3",
            "carsArr", FieldFlags.COMPLEX_ARRAY);
    public static final Property PROP_carMap = new Property("com.heaven7.java.data.mediator.support.gson.Car3",
            "carMap", FieldFlags.COMPLEX_SPARSE_ARRAY);

    public static final Property PROP_f_val = new Property("float",
            "f_val", 0);
    public static final Property PROP_doubles = new Property("double",
            "doubles", FieldFlags.COMPLEX_ARRAY);
    public static final Property PROP_doubleList = new Property("double",
            "doubleList", FieldFlags.COMPLEX_LIST);
    public static final Property PROP_doubelSparse = new Property("java.lang.Double",
            "doubelSparse", FieldFlags.COMPLEX_SPARSE_ARRAY);

    private Car3 car;

    private float f_val = 1.1f;
    private double[] doubles = new double[]{1.1d};
    private List<Double> doubleList = Arrays.asList(1.5, 1.6);
    private SparseArray<Double> doubelSparse;

    //@JsonAdapter(ListCar3TypeAdapter.class) // this is optional
    @SerializedName("cars")
    private List<Car3> list;

    @SerializedName("carsArr")
    private Car3[] carsArr; // no need JsonAdapter.

   // @JsonAdapter(Car3SparseArrayTypeAdapter.class)
    private SparseArray<Car3> carMap;

    private int age;

    public TestGsonBean() {
        doubelSparse = new SparseArray<>();
        doubelSparse.put(1, 1.6);
        doubelSparse.put(2, 2.6);
    }

    public Car3 getCar() {
        return car;
    }
    public void setCar(Car3 car) {
        this.car = car;
    }

    public List<Car3> getList() {
        return list;
    }
    public void setList(List<Car3> mList) {
        this.list = mList;
    }

    public SparseArray<Car3> getCarMap() {
        return carMap;
    }
    public void setCarMap(SparseArray<Car3> carMap) {
        this.carMap = carMap;
    }

    public Car3[] getCarsArr() {
        return carsArr;
    }

    public void setCarsArr(Car3[] carsArr) {
        this.carsArr = carsArr;
    }

    public float getF_val() {
        return f_val;
    }
    public void setF_val(float f_val) {
        this.f_val = f_val;
    }
    public double[] getDoubles() {
        return doubles;
    }
    public void setDoubles(double[] doubles) {
        this.doubles = doubles;
    }
    public List<Double> getDoubleList() {
        return doubleList;
    }
    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }

    public SparseArray<Double> getDoubelSparse() {
        return doubelSparse;
    }
    public void setDoubelSparse(SparseArray<Double> doubelSparse) {
        this.doubelSparse = doubelSparse;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestGsonBean{" +
                "car=" + car +
                ",\n f_val=" + f_val +
                ",\n doubles=" + Arrays.toString(doubles) +
                ",\n doubleList=" + doubleList +
                ",\n doubelSparse=" + doubelSparse +
                ",\n list=" + list +
                ",\n carsArr=" + Arrays.toString(carsArr) +
                ",\n carMap=" + carMap +
                ",\n age=" + age +
                '}';
    }
}
