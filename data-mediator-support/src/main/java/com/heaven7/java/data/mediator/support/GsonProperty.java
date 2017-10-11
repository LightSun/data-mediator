package com.heaven7.java.data.mediator.support;

import com.heaven7.java.data.mediator.Property;

public class GsonProperty extends Property {

    private String seriaName;
    /**
     * create a property instance by type , name  and complex type
     *
     * @param type        the type string. eg: 'int', 'java.lang.Integer'
     * @param name        the property name
     * @param complexType the complex type.
     *                    see {@linkplain com.heaven7.java.data.mediator.FieldFlags#COMPLEX_ARRAY},
     *                     {@linkplain com.heaven7.java.data.mediator.FieldFlags#COMPLEXT_LIST},
     *                     {@linkplain com.heaven7.java.data.mediator.FieldFlags#COMPLEX_SPARSE_ARRAY},
     */
    public GsonProperty(String type, String name, int complexType) {
        super(type, name, complexType);
    }

    public String getSerializeName() {
        return seriaName == null || seriaName.length()==0 ? getName() : seriaName;
    }
    public void setSerializeName(String serializableName) {
        this.seriaName = serializableName;
    }

    /**
     * wrap the normal property to {@linkplain GsonProperty}
     * @param prop the normal property
     * @param serializeName the serialize name
     * @return the gson property.
     */
    public static GsonProperty wrap(Property prop, String serializeName){
        GsonProperty gp = new GsonProperty(prop.getType().getName(), prop.getName(),prop.getComplexType());
        gp.setSerializeName(serializeName);
        return gp;
    }
    /**
     * wrap the normal property to {@linkplain GsonProperty}
     * @param prop the normal property
     * @return the gson property.
     */
    public static GsonProperty wrap(Property prop){
        return wrap(prop, "");
    }
}
