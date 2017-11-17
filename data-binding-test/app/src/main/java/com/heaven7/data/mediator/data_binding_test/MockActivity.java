package com.heaven7.data.mediator.data_binding_test;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.heaven7.android.data.mediator.AndroidBinder;
import com.heaven7.data.mediator.data_binding_test.util.MockBinderFactory;
import com.heaven7.java.data.mediator.BindMethodSupplier;
import com.heaven7.java.data.mediator.bind.BindAny;
import com.heaven7.java.data.mediator.bind.BindBackground;
import com.heaven7.java.data.mediator.bind.BindBackgroundColor;
import com.heaven7.java.data.mediator.bind.BindBackgroundRes;
import com.heaven7.java.data.mediator.bind.BindCheckable;
import com.heaven7.java.data.mediator.bind.BindEnable;
import com.heaven7.java.data.mediator.bind.BindImageBitmap;
import com.heaven7.java.data.mediator.bind.BindImageDrawable;
import com.heaven7.java.data.mediator.bind.BindImageRes;
import com.heaven7.java.data.mediator.bind.BindImageUri;
import com.heaven7.java.data.mediator.bind.BindImageUrl;
import com.heaven7.java.data.mediator.bind.BindMethodSupplierClass;
import com.heaven7.java.data.mediator.bind.BindText;
import com.heaven7.java.data.mediator.bind.BindTextColor;
import com.heaven7.java.data.mediator.bind.BindTextColorRes;
import com.heaven7.java.data.mediator.bind.BindTextRes;
import com.heaven7.java.data.mediator.bind.BindTextSize;
import com.heaven7.java.data.mediator.bind.BindTextSizePx;
import com.heaven7.java.data.mediator.bind.BindTextSizeRes;
import com.heaven7.java.data.mediator.bind.BindVisibility;
import com.heaven7.java.data.mediator.bind.BinderClass;
import com.heaven7.java.data.mediator.bind.BinderFactoryClass;
import com.heaven7.java.data.mediator.bind.BindsAny;
import com.heaven7.java.data.mediator.bind.BindsTextView;
import com.heaven7.java.data.mediator.bind.BindsView;

/**
 * just test generate
 * Created by heaven7 on 2017/11/6 0006.
 */
@BinderFactoryClass(MockBinderFactory.class)
@BinderClass(AndroidBinder.class)
@BindMethodSupplierClass(BindMethodSupplier.DefaultBindMethodSupplier2.class)
public class MockActivity extends AppCompatActivity {

    @BindsView({"stu_backgroundRes", "stu_visibility", "stu_enable"})
    TextView mTv_name;

    @BindsView({"stu_backgroundRes2", "stu_visibility2"})
    TextView mTv_name2;

    @BindsTextView(value = {"textSizeRes", "textColorRes", "textRes"}, index = 2)
    TextView mTv_3;

    @BindsTextView(value = {"textSizeRes2", "textColorRes2"}, index = 3)
    TextView mTv_4;


    @BindCheckable("checkable")
    CheckBox mcb;

    @BindBackground("v_bg")
    @BindBackgroundColor("v_bgColor")
    @BindBackgroundRes("v_bgRes")
    @BindVisibility("v_visibility")
    @BindEnable("v_enable")
    View mV;

    @BindText("tv_text")
    @BindTextRes("tv_textRes")
    @BindTextSize("tv_textSize")
    @BindTextSizeRes("tv_textSizeRes")
    @BindTextSizePx("tv_textSizePx")
    @BindTextColor("tv_textColor")
    @BindTextColorRes("tv_textColorRes")
    TextView mTv;

    @BindImageUrl("iv_url")
    @BindImageUri("iv_uri")
    @BindImageBitmap("iv_bitmap")
    @BindImageDrawable("iv_drawable")
    @BindImageRes("iv_res")
    ImageView mIv;

    @BindAny(value = "prop", method = "bindAddText")
    @BindsAny(value = {"prop1", "prop2"}, methods = {"bindAddText1", "bindAddText2"})
    TextView mTv_supplier;
}
