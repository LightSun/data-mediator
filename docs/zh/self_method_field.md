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