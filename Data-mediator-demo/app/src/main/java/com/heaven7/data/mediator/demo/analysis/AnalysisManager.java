package com.heaven7.data.mediator.demo.analysis;

import com.heaven7.core.util.Logger;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;

/**
 * 一个统计的例子。
 * Created by heaven7 on 2017/10/6.
 */
public class AnalysisManager{

    private DataMediator<AnalysisDataModule> mDM;

    private static class Creator {
        static final AnalysisManager INSTANCE = new AnalysisManager();
    }

    private AnalysisManager() {
        this.mDM = DataMediatorFactory.createDataMediator(AnalysisDataModule.class);
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
    public void handleAnalyseData(){
        //copy data
        final AnalysisDataModule temp = DataMediatorFactory.obtainData(AnalysisDataModule.class);
        mDM.getData().copyTo(temp);
        //TODO doSomething with  'temp'.比如发送数据到服务器 或者 保存到数据库
        Logger.i("AnalysisManager", "handleAnalyseData", "start send server , data = " + temp.toString());
        temp.recycle();
    }
}
