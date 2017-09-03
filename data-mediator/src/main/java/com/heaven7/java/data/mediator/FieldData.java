package com.heaven7.java.data.mediator;

public class FieldData {

    public static final int COMPLEXT_ARRAY = 1;
    public static final int COMPLEXT_LIST  = 2;

    public static final int FLAG_TRANSIENT      = 0x00000001;
    public static final int FLAG_VOLATILE       = 0x00000002;

    public static final int FLAG_SNAP           = 0x00000004;
    public static final int FLAG_SHARE          = 0x00000008;
    public static final int FLAG_COPY           = 0x00000010;//16
    public static final int FLAG_RESET          = 0x00000020;//32

    public static final int FLAG_SERIALIZABLE   = 0x00000040;//64
    public static final int FLAG_PARCEABLE      = 0x00000080;//128

    /**
     * @ Expose : serialize, deserialize, default is true
     */
    public static final int FLAG_EXPOSE_DEFAULT           = 0x00000100;//256
    /**
     * @ Expose : serialize = false
     */
    public static final int FLAG_EXPOSE_SERIALIZE_FALSE   = 0x00000200;//512
    /**
     * @ Expose : deserialize = false
     */
    public static final int FLAG_EXPOSE_DESERIALIZE_FALSE = 0x00000400;//1024

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

    public boolean isList(){
        return complexType == COMPLEXT_LIST;
    }
    public boolean isArray(){
        return complexType == COMPLEXT_ARRAY;
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "propertyName='" + propertyName + '\'' +
                '}';
    }
}
