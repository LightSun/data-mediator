package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the family desc of data internal.
 * @author heaven7
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface FamilyDesc {

    /**
     * the relationship type. like...
     * @return the relationship type
     */
    byte type();

    /**
     * the master properties
     * <li>if type is 'master-slave' the all master properties will effect {@linkplain #slave()} properties.
     * <li>if type is 'brother' the all properties will effect each other.
     * @return the master properties.
     */
    String[] master();

    /**
     * the slave properties
     * <li>if type is 'master-slave' the {@linkplain #master()} properties will effect this all slave properties.
     * <li>if type is 'brother' , here often be empty.
     * @return the master properties.
     */
    String[] slave() default {};

    /**
     * the connector class which use expression to connect.
     * <li>if type is 'master-slave' , the class will connect master with slave </li>
     * <li>if type is 'brother', the connect class will connect all {@linkplain #master()} properties with each other.</li>
     * @return the connect class. which use expression to connect.
     */
    Class<?> connect();

   // Class<?>[] imports();
}
