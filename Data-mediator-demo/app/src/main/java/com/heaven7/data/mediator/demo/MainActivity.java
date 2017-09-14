package com.heaven7.data.mediator.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.testpackage.StudentModule;
import com.heaven7.data.mediator.demo.util.DataMediators;
import com.heaven7.java.data.mediator.BaseMediator;
import com.heaven7.java.data.mediator.DataMediatorCallback;
import com.heaven7.java.data.mediator.Property;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StudentModule module = DataMediators.createModule(StudentModule.class);
        BaseMediator<StudentModule> proxy = DataMediators.createProxy(StudentModule.class);
        proxy.addCallback(new DataMediatorCallback<StudentModule>() {
            @Override
            public void onPropertyValueChanged(StudentModule data, Property prop, Object oldValue, Object newValue) {
                Logger.w("MainActivity","onPropertyValueChanged","oldValue = "
                        + oldValue + ", newValue = " + newValue );
            }
        });
        StudentModule st = (StudentModule) proxy;
        st.setName("heaven7");
        st.setName("heaven7_2");
    }
}
