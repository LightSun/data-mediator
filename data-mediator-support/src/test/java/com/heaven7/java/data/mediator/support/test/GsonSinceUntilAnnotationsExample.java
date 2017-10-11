package com.heaven7.java.data.mediator.support.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heaven7.java.base.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import static com.heaven7.java.data.mediator.support.test.GsonTest.log;

public class GsonSinceUntilAnnotationsExample {
 
    public static void main(String args[]) {
        //  log(void.class.isPrimitive()); // true
        // test2();
        // testList();
        testExtractAdapter();
    }

    private static void testExtractAdapter() {
        Gson gson = new Gson();
        String json = gson.toJson(newTestGsonBean());
        log(json);
    }

    //test List and SparseArray
    private static void testList() {
        TestGsonBean tl = newTestGsonBean();

        Gson gson = new Gson();
        String json = gson.toJson(tl);
        log(json);

        TestGsonBean testList = gson.fromJson(json, TestGsonBean.class);
        log(testList);
        //========================= above ok ========================
    }

    private static TestGsonBean newTestGsonBean() {
        List<Car3> list = new ArrayList<>();
        SparseArray<Car3> map = new SparseArray<>();
        Car3[] arr = new Car3[3];
        for(int i = 0 ; i < 3 ; i++){
            Car3 car = new Car3();
            car.setMark("AUDI");
            car.setModel(2014); //2,1
            car.setType("DIESEL");
            car.setMaker("AUDI GERMANY");
            car.setCost(i * 100);

            car.getColors().add("GREY");
            car.getColors().add("BLACK");
            car.getColors().add("WHITE");
            list.add(car);
            map.put(i, car);
            arr[i] = car;
        }
        TestGsonBean tl = new TestGsonBean();
        tl.setList(list);
        tl.setCarMap(map); //null时，map不参与序列化输出
        tl.setCarsArr(arr);
        tl.setCar(arr[0]);
        return tl;
    }

    private static void test2() {
        //gson 还可以根据版本去选择是否序列化/反序列化
        Gson gson = new GsonBuilder().setVersion(2.0).create();
        Car3 car = new Car3();
        car.setMark("AUDI");
        car.setModel(2014); //2,1
        car.setType("DIESEL");
        car.setMaker("AUDI GERMANY");
        car.setCost(55000);

        car.getColors().add("GREY");
        car.getColors().add("BLACK");
        car.getColors().add("WHITE");

        /* Serialize */
        String jsonString = gson.toJson(car);
        System.out.println("Serialized jsonString : " + jsonString);

        /* Deserialize */
        String inputJson = "{\"mark\":\"AUDI\",\"model\":2014,\"type\":\"DIESEL\",\"maker\":\"AUDI Germany\",\"cost\":55000,\"colors\":[\"GRAY\",\"BLACK\",\"WHITE\"]}";
        car = gson.fromJson(inputJson, Car3.class);
        System.out.println("Deserialized Car : " + car);
    }

}