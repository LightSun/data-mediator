package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Fields(serializeName="", propertyNmae="",flags, type=A.class)

/**
 * this class used to describe the field of entity(like java bean).
 * @author heaven7
 * @see Fields
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Field {

	/**
	 * define the property name of the field.
	 * @return the property name
	 */
	String propName();

	/**
	 * define the serialize name of 'google-gson'. if not empty , it will generate the @SerializedName annotation.
	 * eg: <pre>
	 *      {@literal}@SerializedName("heaven7")
	        private String name;
	 * </pre>
	 * @return the serialize name
	 */
	String seriaName() default "";

	/**
	 * define the base type of the field. which can be another class which is annotated by {@linkplain Fields}.
	 * eg:
	 * <pre>
	 {@literal}@Fields({
	 {@literal}@Field(propName = "name", seriaName = "heaven7", type = String.class),
	 {@literal}@Field(propName = "data", seriaName = "result", type = ResultData.class),
	 })
	 public interface TestBind {
	 }

	 // another data-module will be extends by ClassBind.
	 {@literal}@Fields({
	 {@literal}@Field(propName = "name2", seriaName = "xxx2", type = Integer.class, complexType = COMPLEXT_ARRAY),
	 {@literal}@Field(propName = "name3", seriaName = "xxx3", type = Integer.class, complexType = COMPLEXT_LIST),
	 {@literal}@Field(propName = "data", seriaName = "result", type = ResultData.class, flags = FLAG_PARCEABLE),
	 })
	 public interface TestBind2 extends Parcelable {
	 }

	 // below the data-module  depend TestBind. and super class is TestBind2
	 {@literal}@Fields({
	 {@literal}@Field(propName = "student", seriaName = "class_1", type = TestBind.class),
	 })
	 public interface ClassBind extends TestBind2{  //here can't extends multi data-module or else cause error.

	 }
	 * </pre>
	 * @return the class type. default is String.class
	 */
	Class<?> type() default String.class;

	/**
	 * define the complex type of the field. such as: if type() = String.class, complexType() = COMPLEXT_LIST,
	 * then the field will be List{@literal <}String>.
	 * Here is the demo:
	 * <pre>
	 {@literal}@Fields({
	 {@literal}@Field(propName = "name2", seriaName = "xxx2", type = Integer.class, complexType = COMPLEXT_ARRAY),
	 {@literal}@Field(propName = "name3", seriaName = "xxx3", type = Integer.class, complexType = COMPLEXT_LIST),
	 })
	 public interface TestBind2 extends Parcelable {
	 }

	 // The generate class is something like this.
	 public class TestBind2Module_Impl implements TestBind2Module, Parcelable{
		 ......
		 {@literal}@SerializedName("xxx2")
		 private Integer[] name2;

		 {@literal}@SerializedName("xxx3")
		 private List<Integer> name3;
		 ......
	 }
	 * </pre>
	 * @return the complex type, default is 0 means simple.
	 */
	int complexType() default 0;

	/**
	 * define the flags for the field. more to see in wiki or demo.
	 * @return the flags.
	 */
	int flags() default 0;
}
