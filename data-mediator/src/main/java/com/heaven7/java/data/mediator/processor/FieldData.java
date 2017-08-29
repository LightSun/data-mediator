package com.heaven7.java.data.mediator.processor;

public class FieldData {
    public static final int COMPLEXT_ARRAY = 1;
    public static final int COMPLEXT_LIST  = 2;

    public static final String STR_PROP_NAME           = "propName";
    public static final String STR_SERIA_NAME          = "seriaName";
    public static final String STR_FLAGS               = "flags";
    public static final String STR_TYPE                = "type";
    public static final String STR_COMPLEXT_TYPE       = "complexType";

    public static final int FLAG_TRANSIENT      = 0x0001;
    public static final int FLAG_EXPOSE         = 0x0002;

    public static final int FLAG_SNAP           = 0x0004;
    public static final int FLAG_SHARE_SHAP     = 0x0008;
    public static final int FLAG_CACHE          = 0x0010;
    public static final int FLAG_RESET          = 0x0020;

    public static final int FLAG_SERIALIZABLE   = 0x0040;
    public static final int FLAG_PARCEABLE      = 0x0080;
    public static final int FLAG_VOLATILE       = 0x0100;

    private String propertyName;
    private String serializeName;
    private int flags;
    private Class<?> type;
    private int complexType;

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
    public int getComplexType() {
        return complexType;
    }
    public void setComplexType(int complexType) {
        this.complexType = complexType;
    }
}
