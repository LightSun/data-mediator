package com.heaven7.java.data.mediator.test.bind;

import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.test.copy.IStudent;

/**
 * Created by heaven7 on 2017/9/21 0021.
 */
@Define({
        @DataDefine(value = IStudent.class, id = 1),
        @DataDefine(value = IStudent.class, id = 2)
})
public class MockActivity  {

    @BindSingle(value = "name",id = 1)
    int mockTextView;

    @BindSingle(value = "age" ,id = 2)
    int mockTextView2;

    @BindList(value = "list.options", id = 1)
    int mRv;

    /**
     * 根据id匹配不同的对象？
     */

    //mock
    void onCreate(){
        //IStudent should be a module like :IStudentModule
        DataMediator<IStudent> mMediator = DataMediatorFactory.createDataMediator(IStudent.class);
        DataMediatorKnife.bind(this, mMediator, 1);
    }
}
