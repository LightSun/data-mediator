package com.heaven7.plugin.data.mediator.convertor;

import java.util.ArrayList;
import java.util.List;

import static com.heaven7.plugin.data.mediator.convertor.Util.checkArgument;

public class Property {

    private static final String TYPE_STRING = "java.lang.String";

    private static final String TYPE_int = "int";
    private static final String TYPE_long = "long";
    private static final String TYPE_short = "short";
    private static final String TYPE_byte = "byte";
    private static final String TYPE_boolean = "boolean";
    private static final String TYPE_float = "float";
    private static final String TYPE_double = "double";
    private static final String TYPE_char = "char";

    private final String type;
    private final String name;
    private final int complexType;

    /**
     * create a property instance by type , name  and complex type
     *
     * @param type the type string. eg: 'int', 'java.lang.Integer'
     * @param name the property name
     */
    public Property(String type, String name, int complexType) {
        if (type == null || name == null) {
            throw new NullPointerException();
        }
        this.type = type;
        this.name = name;
        this.complexType = complexType;
    }

    /**
     * get the type as String.
     *
     * @return the type as String.
     * @since 1.2.0
     */
    public String getTypeString() {
        return type;
    }

    /**
     * get base the property type.
     *
     * @return the base type.
     * @see #getComplexType()
     */
    public Class<?> getType() {
        switch (type) {
            case TYPE_int:
                return int.class;
            case TYPE_long:
                return long.class;
            case TYPE_short:
                return short.class;
            case TYPE_byte:
                return byte.class;
            case TYPE_boolean:
                return boolean.class;
            case TYPE_float:
                return float.class;
            case TYPE_double:
                return double.class;
            case TYPE_char:
                return char.class;
        }
        try {
            return bestGuessClass(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get the property name.
     *
     * @return the property name
     */
    public String getName() {
        return name;
    }

    /**
     * get the complex type
     *
     * @return the complex type
     */
    public int getComplexType() {
        return complexType;
    }

    /**
     * {@inheritDoc}
     *
     * @param o the object
     * @return true if equals.
     * @see Object#equals(Object)
     * @since 1.0.3
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        if (complexType != property.complexType) return false;
        if (!type.equals(property.type)) return false;
        return name.equals(property.name);
    }

    /**
     * {@inheritDoc}
     *
     * @return the hash code.
     * @see Object#hashCode()
     * @since 1.0.3
     */
    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + complexType;
        return result;
    }

    @Override
    public String toString() {
        return "Property{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", complexType=" + complexType +
                '}';
    }

    //resolve inner class
    private static Class<?> bestGuessClass(String classNameString) throws ClassNotFoundException{
        List<String> names = new ArrayList<>();

        // Add the package name, like "java.util.concurrent", or "" for no package.
        int p = 0;
        while (p < classNameString.length() && Character.isLowerCase(classNameString.codePointAt(p))) {
            p = classNameString.indexOf('.', p) + 1;
            checkArgument(p != 0, "couldn't make a guess for %s", classNameString);
        }
        names.add(p != 0 ? classNameString.substring(0, p - 1) : "");

        // Add the class names, like "Map" and "Entry".
        for (String part : classNameString.substring(p).split("\\.", -1)) {
            checkArgument(!part.isEmpty() && Character.isUpperCase(part.codePointAt(0)),
                    "couldn't make a guess for %s", classNameString);
            names.add(part);
        }

        final int size = names.size();
        checkArgument(size >= 2, "couldn't make a guess for %s", classNameString);
        //System.out.println(names);
        StringBuilder sb = new StringBuilder();
        sb.append(names.get(0))
                .append(".")
                .append(names.get(1));
        if(size > 2){
            for(int i = 2 ; i < size; i ++){
                sb.append("$")
                        .append(names.get(i));
            }
        }
        return Class.forName(sb.toString());
    }

    public boolean isString() {
        return TYPE_STRING.equals(type);
    }
}
