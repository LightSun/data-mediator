package com.heaven7.java.data.mediator.processor;

/*public*/ class FieldData {
    public static final String STR_PROP_NAME  = "propName";
    public static final String STR_SERIA_NAME = "seriaName";
    public static final String STR_FLAGS      = "flags";
    public static final String STR_TYPE       = "type";

    private String propertyName;
    private String serializeName;
    private int flags;
    private Class<?> type;

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

    public Class<?> getType() {
        return type;
    }
    public void setType(Class<?> type) {
        this.type = type;
    }
}
