package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * define the impl class of module's 'self-method', this can only used to interface in 'data-mediator'
 * <code><pre>
     {@literal @}Fields({
         {@literal @}Field(propName = "testItem100", type = FlowItem.class)
     })
    {@literal @}ImplClass(TestUtil.class)
     public interface TestItem100 {
         //not assigned method name of ImplClass. so use the same name.
         //this impl method will from TestUtil.class
         {@literal @}ImplMethod
         void parseStudent(Student stu, int key);
         //......
     }

     //here is the TestUtil.java
     public class TestUtil {
         public static void parseStudent(TestSelfMethod1 tsf, Student stu, int key){
              //do thing. you want
         }
     }
 * </pre></code>
 * Created by heaven7 on 2017/10/24.
 * @since 1.1.0
 * @see ImplMethod
 * @see Keep
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ImplClass {
    /**
     * indicate the main class which hold the impl of 'self-method'.
     * @return the main class which hold the impl of 'self-method'.
     */
    Class<?> value();
}
