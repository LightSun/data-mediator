package com.heaven7.java.data.mediator.test.gdm;

import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.GroupDataManager;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupDataManagerTest extends TestCase {

    public static final int FOCUS_VALUE = 3;
    public static final int OPPISITE_VALUE = -1;
    private List<GroupDataManager.MediatorDelegate<TestState>> mList;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<TestState> mDatas = createDatas();
        List<GroupDataManager.MediatorDelegate<TestState>> list = new ArrayList<>();
        this.mList = list;
        for (final TestState data : mDatas) {
            list.add(new GroupDataManager.MediatorDelegate<TestState>() {
                final DataMediator<TestState> mediator = DataMediatorFactory.createDataMediator(data);
                @Override
                public DataMediator<TestState> getDataMediator() {
                    return mediator;
                }
            });
        }

        GroupDataManager.GroupProperty gp = new GroupDataManager.GroupProperty();
        gp.setType(GroupDataManager.TYPE_MUTEX);
        gp.setProperty(TestState.PROP_state);
        gp.setValue(FOCUS_VALUE);
        gp.setOppositeValue(OPPISITE_VALUE);
        GroupDataManager.of(list, Arrays.asList(gp)).attachAll();
    }

    private List<TestState> createDatas() {
        List<TestState> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TestState_$Impl data = new TestState_$Impl();
            if (i == 0) {
                data.setState(FOCUS_VALUE);
            }
            list.add(data);
        }
        return list;
    }

    public void test1() {
        TestState state = mList.get(1).getDataMediator().getDataProxy();
        state.setState(FOCUS_VALUE);
        System.out.println(mList.get(0).getDataMediator().getData().getState());
        assert mList.get(0).getDataMediator().getData().getState() == OPPISITE_VALUE;

        mList.get(2).getDataMediator().getData().setState(FOCUS_VALUE);
        state = mList.get(0).getDataMediator().getDataProxy();
        state.setState(FOCUS_VALUE);

        assert mList.get(1).getDataMediator().getData().getState() == OPPISITE_VALUE;
        assert mList.get(2).getDataMediator().getData().getState() == OPPISITE_VALUE;
    }
}
