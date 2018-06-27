package com.heaven7.java.data.mediator.compiler;

public class GroupProperty {

    private String prop;
    private byte type;
    private long focusVal;
    private long oppositeVal;
    private boolean asFlags;

    private FieldData mFieldData;

    public FieldData getFieldData() {
        return mFieldData;
    }
    public void setFieldData(FieldData mFieldData) {
        this.mFieldData = mFieldData;
    }

    public String getProp() {
        return prop;
    }
    public void setProp(String prop) {
        this.prop = prop;
    }

    public byte getType() {
        return type;
    }
    public void setType(byte type) {
        this.type = type;
    }

    public long getFocusVal() {
        return focusVal;
    }
    public void setFocusVal(long focusVal) {
        this.focusVal = focusVal;
    }

    public long getOppositeVal() {
        return oppositeVal;
    }
    public void setOppositeVal(long oppositeVal) {
        this.oppositeVal = oppositeVal;
    }

    public boolean isAsFlags() {
        return asFlags;
    }
    public void setAsFlags(boolean asFlags) {
        this.asFlags = asFlags;
    }
}
