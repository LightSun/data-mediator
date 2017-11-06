package com.heaven7.data.mediator.data_binding_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.heaven7.java.data.mediator.bind.BindImageUrl;
import com.heaven7.java.data.mediator.bind.BindVisibility;
import com.heaven7.java.data.mediator.bind.BindsView;

public class MainActivity extends AppCompatActivity {

    @BindVisibility(value = "visibility", forceAsBoolean = false)
    @BindImageUrl(value = "imageUrl", index = 1)
    @BindsView("text")
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
    }
}
