package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * define the data fields.
 *  for interface to describe bean class define.
 *  <h2>Here is a demo</h2>
 *  <code><pre>

 {@literal @}Fields({
 {@literal @}Field(propName = "name", seriaName = "heaven7",type = String.class),
 {@literal @}Field(propName = "test_object", seriaName = "test_object",
              flags = FLAG_EXPOSE_DEFAULT|FLAG_EXPOSE_SERIALIZE_FALSE,type = Object.class),
 {@literal @}Field(propName = "test_Format", seriaName = "test_Format",flags = 1,type = Double.class),
 {@literal @}Field(propName = "test_int", seriaName = "test_int",type = int.class,
              flags = FLAG_EXPOSE_DEFAULT | FLAG_COPY | FLAG_RESET),
 {@literal @}Field(propName = "test_list", seriaName = "test_list",type = long.class, complexType = COMPLEXT_LIST,
              flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP ),
 {@literal @}Field(propName = "test_array", seriaName = "test_array",type = String.class,
              complexType = COMPLEXT_ARRAY,flags =FLAG_RESET | FLAG_SHARE | FLAG_SNAP
      ),
 })
 public interface StudentBind extends ICopyable, IResetable, IShareable, ISnapable{
 }
   </pre></code>
 * @author heaven7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Fields {

	/**
	 * define the all fields for data-module
	 * @return the definition of all fields.
	 */
	Field[] value();

	/**
	 * enable generate code of the chain call style or not（normal java bean）. here is a chain call demo :
	 * <code><pre>
	 *     Person p = new Person().setName(xxx).setAge(xxx).setId(xxx);
	 * </pre></code>
	 *
	 * @return true if you like chain call.
	 * @since 1.0.2
	 */
	boolean enableChain() default true;
}
