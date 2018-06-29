package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.internal.ReflectUtils;
import com.heaven7.java.data.mediator.util.ExpreEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the family manager. help we manage the relationship of family.
 *
 * @author heaven7
 */
public final class FamilyManager<T> extends DataMediatorCallback<T> {

    public interface Connector {
        String connect(Property main, Property slave);
    }

    public static final byte TYPE_MASTER_SLAVE = 1;
    public static final byte TYPE_BROTHER = 2;

    private final SparseArray<List<FamilyGroup>> mGroups = new SparseArray<>(3);
    private final BaseMediator<T> mMediator;
    private ExpreEvaluator mEvaluator;

    public FamilyManager(BaseMediator<T> mediator, List<FamilyGroup> groups) {
        Throwables.checkEmpty(groups);
        Throwables.checkNull(mediator);
        this.mMediator = mediator;
        //prepare
        for (FamilyGroup fg : groups) {
            List<FamilyGroup> list = mGroups.get(fg.type);
            if (list == null) {
                list = new ArrayList<>(4);
                mGroups.put(fg.type, list);
            }
            list.add(fg);
        }
    }

    public void attach() {
        mMediator.addCallback(this);
    }
    public void detach() {
        mMediator.removeCallback(this);
    }

    @Override @SuppressWarnings("unchecked")
    public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
        if (data != mMediator._getTarget()) {
            return;
        }
        final List<FamilyGroup> mss = mGroups.get(TYPE_MASTER_SLAVE);
        final List<FamilyGroup> brothers = mGroups.get(TYPE_BROTHER);
        if(Predicates.isEmpty(mss) && Predicates.isEmpty(brothers)){
            return;
        }
        //remove callback temporary
        mMediator.removeCallback(this);
        if (!Predicates.isEmpty(mss)) {
            for (FamilyGroup fg : mss) {
                if (fg.master.contains(prop)) {
                    fg.callbackByMasterSlave(prop, mMediator, mEvaluator);
                }
            }
        }
        if (!Predicates.isEmpty(brothers)) {
            for (FamilyGroup fg : brothers) {
                if (fg.master.contains(prop)) {
                    fg.callbackByBrother(prop, mMediator, mEvaluator);
                }
            }
        }
        //restore
        mMediator.addCallback(this);
    }

    public static FamilyGroup createFamilyGroup(byte type, Property[] master, Property[] slave,
                                                Connector connector) {
        FamilyGroup fg = new FamilyGroup();
        fg.type = type;
        fg.master = Arrays.asList(master);
        fg.slave = Arrays.asList(slave);
        fg.connector = connector;
        return fg;
    }

    public static class FamilyGroup {
        byte type;
        List<Property> master;
        List<Property> slave;
        Connector connector;

        void callbackByMasterSlave(Property main, Object proxy, ExpreEvaluator eval) {
            for (Property prop : slave) {
                applyProperty(main, prop, proxy, eval);
            }
        }

        void callbackByBrother(Property main, Object proxy, ExpreEvaluator eval) {
            for(Property prop: master){
                if(prop == main){
                    continue;
                }
                applyProperty(main, prop, proxy, eval);
            }
        }
        private void applyProperty(Property main, Property prop, Object proxy, ExpreEvaluator eval) {
            final String expre = connector.connect(main, prop);
            //TODO evaluate expre (expre, proxy)
            Object value = null; //eval.evaluate(expre, );
            ReflectUtils.setValue(prop, proxy, value);
        }
    }
}
