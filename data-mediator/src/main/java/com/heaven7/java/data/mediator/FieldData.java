package com.heaven7.java.data.mediator;

public class FieldData {

    private String propertyName;
    private String serializeName;
    private int flags;
    private int type;

    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSerializeName() {
        return serializeName;
    }
    public void setSerializeName(String serializeName) {
        this.serializeName = serializeName;
    }

    public int getFlags() {
        return flags;
    }
    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
