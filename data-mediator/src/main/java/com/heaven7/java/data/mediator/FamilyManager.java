package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.Throwables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the family manager. help we manage the relationship of family.
 * @author heaven7
 */
public class FamilyManager {

    public interface Connector{
        String connect(Property main, Property slave);
    }
    public static final byte TYPE_MASTER_SLAVE = 1;
    public static final byte TYPE_BROTHER      = 2;

    private final List<FamilyGroup> mGroups;
    private final Object mOwner;

    public FamilyManager(Object mOwner, List<FamilyGroup> mGroups) {
        Throwables.checkEmpty(mGroups);
        Throwables.checkNull(mOwner);
        this.mOwner = mOwner;
        this.mGroups = mGroups;
    }

    public void attach(){

    }

    public void detach(){

    }

    public static FamilyGroup createFamilyGroup(byte type, String[] master, String[] slave, Class<? extends Connector> clazz){
        FamilyGroup fg = new FamilyGroup();
        fg.type = type;
        fg.master = Arrays.asList(master);
        fg.slave = Arrays.asList(slave);
        fg.clazz = clazz;
        return fg;
    }

    public static class FamilyGroup{
        byte type;
        List<String> master;
        List<String> slave;
        Class<? extends Connector> clazz;

    }
}
