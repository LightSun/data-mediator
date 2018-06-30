package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.internal.ReflectUtils;
import com.heaven7.java.data.mediator.util.ExpreEvaluator;

import java.util.*;

/**
 * the family manager. help we manage the relationship of family.
 *
 * @author heaven7
 * @since 1.4.5
 */
public final class FamilyManager<T> extends DataMediatorCallback<T>{

    /**
     * the connector connect the double properties.
     * @author heaven7
     */
    public interface Connector {
        /**
         * connect the double properties by actually expression
         * @param mediator the base mediator often is the module proxy
         * @param main the main property which is the producer
         * @param slave the main property which is the consumer
         * @return the expression to connect the double properties.
         */
        String connect(BaseMediator<?> mediator, Property main, Property slave);
    }

    public static final byte TYPE_MASTER_SLAVE = 1;
    public static final byte TYPE_BROTHER = 2;
    /** forbid inspector */
    private final ForbidInspector mForbid = new ForbidInspectorImpl();
    private final SparseArray<List<FamilyGroup>> mGroups = new SparseArray<>(3);
    private final BaseMediator<T> mMediator;
    private ExpreEvaluator mEvaluator; //TODO assign

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

    @Override
    @SuppressWarnings("unchecked")
    public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
        if (data != mMediator._getTarget()) {
            return;
        }
        final List<FamilyGroup> mss = mGroups.get(TYPE_MASTER_SLAVE);
        final List<FamilyGroup> brothers = mGroups.get(TYPE_BROTHER);
        if (Predicates.isEmpty(mss) && Predicates.isEmpty(brothers)) {
            return;
        }
        if (!Predicates.isEmpty(mss)) {
            for (FamilyGroup fg : mss) {
                if (fg.master.contains(prop)) {
                    fg.callbackByMasterSlave(prop, newValue, mMediator, mEvaluator);
                }
            }
        }
        if (!Predicates.isEmpty(brothers)) {
            for (FamilyGroup fg : brothers) {
                if (fg.master.contains(prop)) {
                    fg.callbackByBrother(prop, newValue, mMediator, mEvaluator, mForbid);
                }
            }
        }
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

    /**
     * the family groups
     * @author heaven7
     */
    public static abstract class Fgs{

        private final List<FamilyGroup> list = new ArrayList<>(3);

        public void addFamilyGroup(FamilyGroup fg){
            list.add(fg);
        }
        public List<FamilyGroup> getFamilyGroups(){
            return list;
        }
    }

    /**
     * the family group
     * @author heaven7
     */
    public static class FamilyGroup {
        byte type;
        List<Property> master;
        List<Property> slave;
        Connector connector;

        void callbackByMasterSlave(Property main, Object newValue, Object proxy, ExpreEvaluator eval) {
            for (Property prop : slave) {
                applyProperty(main, prop, newValue, proxy, eval);
            }
        }

        void callbackByBrother(Property main, Object newValue, Object proxy, ExpreEvaluator eval, ForbidInspector notifier) {
            for (Property prop : master) {
                if (prop == main) {
                    continue;
                }
                //for forbid. we need avoid call each other.
                if(notifier.shouldForbid(main, prop)){
                    continue;
                }
                notifier.onPreCallbackBrother(main, prop);
                applyProperty(main, prop, newValue, proxy, eval);
                notifier.onPostCallbackBrother(main, prop);
            }
        }

        private void applyProperty(Property main, Property prop, Object newValue, Object proxy, ExpreEvaluator eval) {
            final String expre = connector.connect((BaseMediator<?>) proxy, main, prop);
            ExpreEvaluator.ExpreContext context = DataMediatorFactory.createExpreContext(proxy.getClass());
            if(context == null){
                 throw new ExpreEvaluator.ExpreEvaluatorException("create ExpreContext failed .has your module declare @ImportDesc ?");
            }
            //add newValue.
            context.addVariable(main.getName(), newValue);
            Object value = eval.evaluate(context, expre);
            ReflectUtils.setValue(prop, proxy, value);
        }
    }
    private interface ForbidInspector {
        void onPreCallbackBrother(Property main, Property receiver);
        void onPostCallbackBrother(Property main, Property receiver);
        boolean shouldForbid(Property main, Property receiver);
    }
    private static class ForbidInspectorImpl implements ForbidInspector {
        final Set<String> forbidStrs = new HashSet<>(4);
        @Override
        public void onPreCallbackBrother(Property main, Property receiver) {
            forbidStrs.add(generateKey(main, receiver));
        }
        @Override
        public void onPostCallbackBrother(Property main, Property receiver) {
            forbidStrs.remove(generateKey(main, receiver));
        }
        @Override
        public boolean shouldForbid(Property main, Property receiver) {
            return forbidStrs.contains(generateKey(receiver, main));
        }
        //forbid: main-receiver -> receiver->main
        static String generateKey(Property main, Property receiver){
            return receiver.getName() +"_" + main.getName();
        }

    }
}
