package com.heaven7.plugin.idea.data_mediator;

public class Property {

    static final String TYPE_int = "int";
    static final String TYPE_long = "long";
    static final String TYPE_short = "short";
    static final String TYPE_byte = "byte";
    static final String TYPE_boolean = "boolean";
    static final String TYPE_float = "float";
    static final String TYPE_double = "double";
    static final String TYPE_char = "char";

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

    public String getRealTypeString() {
        String tempType = this.type;
        if(complexType == FieldFlags.COMPLEX_LIST || complexType == FieldFlags.COMPLEX_SPARSE_ARRAY) {
            switch (type) {
                case TYPE_int:
                    tempType = Integer.class.getName();
                    break;
                case TYPE_long:
                    tempType = Long.class.getName();
                    break;
                case TYPE_short:
                    tempType = Short.class.getName();
                    break;
                case TYPE_byte:
                    tempType = Byte.class.getName();
                    break;
                case TYPE_boolean:
                    tempType = Boolean.class.getName();
                    break;
                case TYPE_float:
                    tempType = Float.class.getName();
                    break;
                case TYPE_double:
                    tempType = Double.class.getName();
                    break;
                case TYPE_char:
                    tempType = Character.class.getName();
                    break;
            }
        }
        switch (complexType){
            case FieldFlags.COMPLEX_ARRAY:
                return tempType + "[]";
            case FieldFlags.COMPLEX_LIST:
                return "List<" + tempType + ">";
            case FieldFlags.COMPLEX_SPARSE_ARRAY:
                return "SparseArray<"+ tempType + ">";
        }
        return tempType;
    }

    /**
     * indicate the type of property is primitive or not.
     *
     * @return true if is primitive
     * @since 1.0.8
     */
    public boolean isPrimitive() {
        switch (complexType) {
            case FieldFlags.COMPLEX_ARRAY:
            case FieldFlags.COMPLEX_LIST:
            case FieldFlags.COMPLEX_SPARSE_ARRAY:
                return false;
        }
        switch (type) {
            case TYPE_int:
            case TYPE_long:
            case TYPE_short:
            case TYPE_byte:
            case TYPE_boolean:
            case TYPE_float:
            case TYPE_double:
            case TYPE_char:
                return true;
        }
        return false;
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
            return Class.forName(type);
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
}
