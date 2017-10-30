package com.heaven7.java.data.mediator.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/16.
 */
public class NormalTest {

    public static void main(String[] args){
        List<? super StudentModuleImpl> list = new ArrayList<>();
        list.add(new StudentModuleImpl());

        List<IStudent> list3 = (List<IStudent>) list;
        list3.get(0).setName("455556456");

        //list.removeAll(list3);
       // list.addAll(StudentModuleImpl);
        //省，市, 区，学校，年级，班级。

        // 对象:  外教，学生。 班级？
        // 房间： 房间号， 开始时间-预计结束时间-实际结束时间。 课程类型，状态(进行中.未开始，已结束)
        // 同一个老师, 助教，学生不能在多个房间。(修改--)

        log(Math.E);
        log(Math.exp(1));

        Object f = 0f;
        log(f.getClass().getName());
        f = 0d;
        log(f.getClass().getName());

        String fullName = "com.heaven7.Outter.Inner";
        String packageName = "com.heaven7";
        final String interfaceName = fullName.substring(packageName.length() + 1);
        log(interfaceName);
    }

    private static void  log (Object obj){
        System.out.println(obj);
    }

}
