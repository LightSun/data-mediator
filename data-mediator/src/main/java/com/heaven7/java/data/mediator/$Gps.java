package com.heaven7.java.data.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * group properties
 *
 * @author heaven7
 * @since 1.4.5
 */
public abstract class $Gps {

    private final List<GroupDataManager.GroupProperty> mGps;

    public $Gps() {
        this.mGps = new ArrayList<>(3);
    }

    public void addGroupProperty(GroupDataManager.GroupProperty gp) {
        this.mGps.add(gp);
    }

    public List<GroupDataManager.GroupProperty> getGroupProperties() {
        return mGps;
    }

    public static GroupDataManager.GroupProperty createGroupProperty(byte type, Property prop, long focusVal, long oppoVal){
        GroupDataManager.GroupProperty gp = new GroupDataManager.GroupProperty();
        gp.setProperty(prop);
        gp.setType(type);
        gp.setValue(focusVal);
        gp.setOppositeValue(oppoVal);
        return gp;
    }
    public static GroupDataManager.GroupProperty createGroupProperty(byte type, Property prop, long focusVal){
        GroupDataManager.GroupProperty gp = new GroupDataManager.GroupProperty();
        gp.setProperty(prop);
        gp.setType(type);
        gp.setValue(focusVal);
        gp.setAsFlags(true);
        return gp;
    }
    public static GroupDataManager.GroupProperty createGroupProperty(byte type, Property prop,
                                                                     long focusVal, long oppoVal, boolean asFlag){
        GroupDataManager.GroupProperty gp = new GroupDataManager.GroupProperty();
        gp.setProperty(prop);
        gp.setType(type);
        gp.setValue(focusVal);
        gp.setOppositeValue(oppoVal);
        gp.setAsFlags(asFlag);
        return gp;
    }
}
