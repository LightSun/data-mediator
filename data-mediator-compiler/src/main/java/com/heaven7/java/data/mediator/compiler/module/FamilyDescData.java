package com.heaven7.java.data.mediator.compiler.module;

import com.heaven7.java.data.mediator.compiler.FieldData;

/**
 * the family description data
 *
 * @author heaven7
 * @since 1.4.5
 */
public class FamilyDescData {

    private byte type = 1;
    private String[] master;
    private String[] slave;
    private FieldData.TypeCompat connectClass;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String[] getMaster() {
        return master;
    }

    public void setMaster(String[] master) {
        this.master = master;
    }

    public String[] getSlave() {
        return slave;
    }

    public void setSlave(String[] slave) {
        this.slave = slave;
    }

    public FieldData.TypeCompat getConnectClass() {
        return connectClass;
    }

    public void setConnectClass(FieldData.TypeCompat connectClass) {
        this.connectClass = connectClass;
    }

    public boolean hasSlave() {
        return slave != null && slave.length > 0;
    }
}
