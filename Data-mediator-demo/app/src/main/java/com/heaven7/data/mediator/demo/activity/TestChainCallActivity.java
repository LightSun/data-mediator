package com.heaven7.data.mediator.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.testpackage.Student;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;

/**
 * 链式调用 测试
 * Created by heaven7 on 2017/9/18 0018.
 */

public class TestChainCallActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_double_bind);

        DataMediator<Student> mediator = DataMediatorFactory.createDataMediator(Student.class);
        //数据代理层
        mediator.getDataProxy()
                .setName(null)
                .setAge(0)
                .setId(0);

        //数据真正的模型实现
        mediator.getData().setName(null)
                .setAge(0)
                .setId(0);
    }
}
