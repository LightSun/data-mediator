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
    }

}
