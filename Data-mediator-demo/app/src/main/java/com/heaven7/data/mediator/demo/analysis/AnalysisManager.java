package com.heaven7.data.mediator.demo.analysis;

import com.heaven7.core.util.Logger;
import com.heaven7.java.data.mediator.DataConsumer;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;

/**
 * 一个统计的例子。包含缓存和数据统计相关逻辑。
 * Created by heaven7 on 2017/10/6.
 */
public class AnalysisManager implements DataConsumer<AnalysisDataModule>{

    private DataMediator<AnalysisDataModule> mDM;

    private static class Creator {
        static final AnalysisManager INSTANCE = new AnalysisManager();
    }

    private AnalysisManager() {
        this.mDM = DataMediatorFactory.createDataMediator(AnalysisDataModule.class);
        mDM.setDataConsumer(this);
        //just mock init data（模拟初始化数据）
        mDM.getData().setApiVersion("18")
                .setNet("4g"); // in product code this often set by network change.
    }
    public static AnalysisManager getInstance(){
        return Creator.INSTANCE;
    }
    public DataMediator<AnalysisDataModule> getDataMediator(){
        return mDM;
    }
    public AnalysisDataModule getAnalyseData(){
        return mDM.getData();
    }

    @Override
    public void accept(AnalysisDataModule data) {
        //copy data
        final AnalysisDataModule temp = DataMediatorFactory.obtainData(AnalysisDataModule.class);
        data.copyTo(temp);
        //TODO doSomething with  'temp'.比如发送数据到服务器 或者 保存到数据库
        Logger.i("AnalysisManager", "handleAnalyseData", "start send server , data = " + temp.toString());
        temp.recycle();
    }

}
