# Self Method and field
 ```java
@Fields({
        @Field(propName = "test_self1"),
        @Field(propName = "test_self2", type = int.class)
})
@ImplClass(TestUtil.class) //define the source impl class of self method 
public interface TestSelfMethod1 extends DataPools.Poolable {

  //define a constant field. add annotation @Keep for not effect by idea-plugin(Data-mediator generator)
    @Keep
    int STATE_OK = 1;

    Property PROP_test_self1 = SharedProperties.get("java.lang.String", "test_self1", 0);
    Property PROP_test_self2 = SharedProperties.get("int", "test_self2", 0);

    String getTest_self1();
    TestSelfMethod1 setTest_self1(String test_self11);

    int getTest_self2();
    TestSelfMethod1 setTest_self2(int test_self21);

    @ImplMethod("getStudentId")
    int getId(Student stu, int key);

    //not assigned method name of ImplClass. so use the same name.
    @ImplMethod
    void parseStudent(Student stu, int key);
}

public class TestUtil {
    //compare to  'int getId(Student stu, int key)', only add a first param of  module.
    public static int getStudentId(TestSelfMethod1 tsf, Student stu, int key){
        //do something you want.
        return  0;
    }
    public static void parseStudent(TestSelfMethod1 tsf, Student stu, int key){
       //do something you want.
    }
}
 ```