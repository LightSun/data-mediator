# 自定义方法，字段
 ```java
@Fields({
        @Field(propName = "test_self1"),
        @Field(propName = "test_self2", type = int.class)
})
@ImplClass(TestUtil.class) //定义自定义方法的来源类
public interface TestSelfMethod1 extends DataPools.Poolable {

  //定义一个常量字段， 加@Keep注解表示不被生成代码插件所影响
    @Keep
    int STATE_OK = 1;

    Property PROP_test_self1 = SharedProperties.get("java.lang.String", "test_self1", 0);
    Property PROP_test_self2 = SharedProperties.get("int", "test_self2", 0);

    String getTest_self1();
    TestSelfMethod1 setTest_self1(String test_self11);

    int getTest_self2();
    TestSelfMethod1 setTest_self2(int test_self21);

    //自定义的方法， 来源类对应方法的名称是： getStudentId
    //所以就是 TestUtil.getStudentId(...)
    @ImplMethod("getStudentId")
    int getId(Student stu, int key);

    //没有指定方法名称，即用相同的名称： parseStudent
    @ImplMethod
    void parseStudent(Student stu, int key);
}

public class TestUtil {
    //相比  'int getId(Student stu, int key)', 只是添加了第一个参数，Module
    public static int getStudentId(TestSelfMethod1 tsf, Student stu, int key){
        //do anything in here you can
        return  0;
    }
    public static void parseStudent(TestSelfMethod1 tsf, Student stu, int key){
       //do anything in here you can
    }
}
 ```
 
 # Android 平台例子。
 ```java
 //模型定义，实现自定义接口
 @Fields({
        @Field(propName = "text")
})
public interface TestSelfMethod extends TestSelfMethodWithImplInterface.TextDelegate, DataPools.Poolable {

    Property PROP_text = SharedProperties.get("java.lang.String", "text", 0);

    @Keep
    @ImplMethod(from = HelpUtil.class)
    void changeText(String text);

    TestSelfMethod setText(String text1);

    String getText();/*
================== start methods from super properties ===============
======================================================================= */

    class HelpUtil {
        //compare to  ' void changeText(String text);' , just add a module param at the first.
        public static void changeText(TestSelfMethod module, String text) {
            //just mock text change.
            //module can be real data or data proxy, if is proxy it will auto dispatch text change event.
            module.setText(text);
        }

    }
}

// 示例activity
public class TestSelfMethodWithImplInterface extends BaseActivity {

    @BindView(R.id.textView)
    TextView mTv;

    private TestSelfMethod mProxy;
    @Override
    protected int getLayoutId() {
        return R.layout.ac_self_methods;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        Binder<TestSelfMethod> binder = DataMediatorFactory.createBinder(TestSelfMethod.class);
        //bind property to textView
        binder.bindText(TestSelfMethod.PROP_text, mTv);
        //get proxy
        mProxy = binder.getDataProxy();
    }

    @OnClick(R.id.button)
    public void onClickCallSelf(View view){
        //call self method
        mProxy.changeText("text changed: " + System.currentTimeMillis());
    }

    public interface TextDelegate{
        void changeText(String text);
    }

}

```
