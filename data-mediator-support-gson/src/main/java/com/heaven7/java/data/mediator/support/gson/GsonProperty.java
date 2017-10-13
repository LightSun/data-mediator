/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator.support.gson;

import com.heaven7.java.data.mediator.Property;

/**
 * the gson property which is an extend class of {@linkplain Property}
 * @author heaven7
 */
public class GsonProperty extends Property {

    private String seriaName;
    private double since = 1.0;
    private double until = Integer.MAX_VALUE;
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
    private GsonProperty(String type, String name, int complexType) {
        super(type, name, complexType);
    }
    public String getRealSerializeName() {
        return seriaName == null || seriaName.length()==0 ? getName() : seriaName;
    }
    public void setSerializeName(String serializableName) {
        this.seriaName = serializableName;
    }
    public String getSerializeName() {
        return seriaName;
    }
    public double getUntil() {
        return until;
    }
    public void setUntil(double until) {
        this.until = until;
    }

    public double getSince() {
        return since;
    }
    public void setSince(double since) {
        this.since = since;
    }

    /**
     * create gson property from target parameters.
     * @param prop the base property
     * @param serializeName the serialize name of gson
     * @param since the since of gson
     * @param util the until of gson
     * @return the gson property
     */
    public static GsonProperty of(Property prop, String serializeName, double since, double util){
        GsonProperty gp = new GsonProperty(prop.getTypeString(), prop.getName(),prop.getComplexType());
        gp.setSerializeName(serializeName);
        gp.setSince(since);
        gp.setUntil(util);
        return gp;
    }
    /**
     * wrap the normal property to {@linkplain GsonProperty}
     * @param prop the normal property
     * @param serializeName the serialize name
     * @return the gson property.
     */
    public static GsonProperty of(Property prop, String serializeName){
        GsonProperty gp = new GsonProperty(prop.getTypeString(), prop.getName(),prop.getComplexType());
        gp.setSerializeName(serializeName);
        return gp;
    }
    /**
     * wrap the normal property to {@linkplain GsonProperty}
     * @param prop the normal property
     * @return the gson property.
     */
    public static GsonProperty of(Property prop){
        return of(prop, "");
    }
}
