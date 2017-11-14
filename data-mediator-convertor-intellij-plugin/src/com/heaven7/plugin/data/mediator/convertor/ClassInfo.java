package com.heaven7.plugin.data.mediator.convertor;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

    private final List<PropertyInfo> propertyInfos = new ArrayList<>();
    private List<String> interfaces;

    public List<PropertyInfo> getPropertyInfos() {
        return propertyInfos;
    }
    public List<String> getInterfaces() {
        return interfaces;
    }
    public void addPropertyInfo(PropertyInfo info) {
        this.propertyInfos.add(info);
    }

    public void addInterface(String interfaceName){
        if(interfaceName == null){
            return;
        }
        if(interfaces == null){
            interfaces = new ArrayList<>();
        }
        interfaces.add(interfaceName);
    }
}
