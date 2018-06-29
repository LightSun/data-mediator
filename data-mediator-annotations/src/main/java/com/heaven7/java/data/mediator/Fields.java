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

 {@literal @}Fields(value = {
 {@literal @}Field(propName = "name"),
 {@literal @}Field(propName = "id", seriaName = "_id"),
 {@literal @}Field(propName = "age", type = int.class, flags = FieldFlags.FLAGS_MAIN_SCOPES_2 | FieldFlags.FLAG_EXPOSE_DEFAULT),
 {@literal @}Field(propName = "grade", type = int.class, since = 1.3),
 {@literal @}Field(propName = "nickName", since = 1.2, until = 2.6)
 }, generateJsonAdapter = false)
 public interface IStudent {
 }
   </pre></code>
 * @author heaven7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Fields {

	/**
	 * define the all fields for data-module
	 * @return the definition of all fields.
	 */
	Field[] value();

	/**
	 * define the group properties.
	 * @return the group fields
	 * @since 1.2.3
	 */
	GroupDesc[] groups() default {};

	FamilyDesc[] families() default {};

	ImportDesc importDesc() default @ImportDesc(names = {""}, classes = {Void.class});

	/**
	 * enable generate code of the chain call style or not(normal java bean). here is a chain call demo :
	 * <code><pre>
	 *     Person p = new Person().setName(xxx).setAge(xxx).setId(xxx);
	 * </pre></code>
	 *
	 * @return true if you like chain call.
	 * @since 1.0.2
	 */
	boolean enableChain() default true;

	/**
	 * the max pool count of current module. default 0.
	 * @return the max pool count.
	 * @since 1.0.3
	 */
	int maxPoolCount() default 0;

	/**
	 * use {@literal @}JsonAdapter or not. that means is this is enabled .
	 * it will auto generate TypeAdapter and generate {@literal @}JsonAdapter.
	 * <p>Note: if global gson config {@linkplain GsonConfig#generateJsonAdapter()} = false,
	 * no matter this value is true or false, it will not generate . That means only
	 * {@linkplain GsonConfig#generateJsonAdapter()} = true, and this return true. will generate
	 * Json adapter for current module.</p>
	 * @return true if enable json adapter or not. you can see more in 'Google/Gson'.
	 * @see GsonConfig#generateJsonAdapter()
	 * @since 1.1.4
	 */
	boolean generateJsonAdapter() default true;
}
