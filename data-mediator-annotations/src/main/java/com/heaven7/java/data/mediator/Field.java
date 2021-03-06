package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Fields(serializeName="", propertyNmae="",flags, type=A.class)

/**
 * this class used to describe the field of entity(like java bean).
 * Here is a simple definition.
 * <code><pre>
 *      {@literal @}Field(propName = "testItem_1", type = Student.class,  since = 1.2, until = 2.0),
 * </pre></code>
 * @author heaven7
 * @see Fields
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
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
	 * default is  {@linkplain FieldFlags#FLAGS_MAIN_SCOPES_3}.
	 * @return the flags.
	 */
	int flags() default FieldFlags.FLAGS_MAIN_SCOPES_3;

	/**
	 * define the {@literal @}Since annotation for gson.
	 * <p>Here is a demo :
	 * <code><pre>
	 //data module
	 public class Car3 {

		 {@literal @}Since(2.0)
		private String mark;

		 {@literal @}Since(2.1)
		private int model;

		 {@literal @}Until(1.9)
		private String type;

		 {@literal @}Until(2.1)
		private String maker;

		public String getMark() {
		return mark;
		}
		public void setMark(String mark) {
		this.mark = mark;
		}

		public int getModel() {
		return model;
		}
		public void setModel(int model) {
		this.model = model;
		}

		public String getType() {
		return type;
		}
		public void setType(String type) {
		this.type = type;
		}

		public String getMaker() {
		return maker;
		}
		public void setMaker(String maker) {
		this.maker = maker;
		}

		 {@literal @}Override
		public String toString() {
		return "Car3 [mark=" + mark + ", model=" + model + ", type=" + type
		+ ", maker=" + maker
		+ "]";
		}
	}
	 //calling demo
	 Gson gson = new GsonBuilder().setVersion(2.0).create();
	 Car3 car = new Car3();
	 car.setMark("AUDI");
	 car.setModel(2014); //2,1
	 car.setType("DIESEL");
	 car.setMaker("AUDI GERMANY");

	 // Serialize
	String jsonString = gson.toJson(car);
        System.out.println("Serialized jsonString : " + jsonString);

	// Deserialize
	String inputJson = "{\"mark\":\"AUDI\",\"model\":2014,\"type\":\"DIESEL\",\"maker\":\"AUDI Germany\"}";
	car = gson.fromJson(inputJson, Car3.class);
        System.out.println("Deserialized Car : " + car);
	 </pre></code>
	 * </p>
	 * @return the since version .must >= 1.0
	 * @see Field#until()
	 * @since 1.0.5
	 */
	double since() default 1.0;
	/**
	 * define the {@literal @}Until annotation for gson.
	 * <p>Here is a demo :
	 * <code><pre>
	 //data module
	 public class Car3 {

		 {@literal @}Since(2.0)
		 private String mark;

		 {@literal @}Since(2.1)
		 private int model;

		 {@literal @}Until(1.9)
		 private String type;

		 {@literal @}Until(2.1)
		 private String maker;

		 public String getMark() {
		 return mark;
		 }
		 public void setMark(String mark) {
		 this.mark = mark;
		 }

		 public int getModel() {
		 return model;
		 }
		 public void setModel(int model) {
		 this.model = model;
		 }

		 public String getType() {
		 return type;
		 }
		 public void setType(String type) {
		 this.type = type;
		 }

		 public String getMaker() {
		 return maker;
		 }
		 public void setMaker(String maker) {
		 this.maker = maker;
		 }

		 {@literal @}Override
		 public String toString() {
		 return "Car3 [mark=" + mark + ", model=" + model + ", type=" + type
		 + ", maker=" + maker
		 + "]";
		 }
	 }
	 //calling demo
	 Gson gson = new GsonBuilder().setVersion(2.0).create();
	 Car3 car = new Car3();
	 car.setMark("AUDI");
	 car.setModel(2014); //2,1
	 car.setType("DIESEL");
	 car.setMaker("AUDI GERMANY");

	 // Serialize
	 String jsonString = gson.toJson(car);
	 System.out.println("Serialized jsonString : " + jsonString);

	 // Deserialize
	 String inputJson = "{\"mark\":\"AUDI\",\"model\":2014,\"type\":\"DIESEL\",\"maker\":\"AUDI Germany\"}";
	 car = gson.fromJson(inputJson, Car3.class);
	 System.out.println("Deserialized Car : " + car);
	 </pre></code>
	 * </p>
	 * @return the until version .
	 * @see Field#since()
	 * @since 1.0.5
	 */
	double until() default Integer.MAX_VALUE;
}
