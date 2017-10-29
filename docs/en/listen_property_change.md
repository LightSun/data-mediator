# Listen proerty change

* [simple demo](#1)
* [listen list change](#2)
* [listen sparse array change](#3)

# <h2 id="1">simple demo</h2>
```java
public class TestPropertyChangeActivity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView mTv_desc;

    @BindView(R.id.bt_set_text_on_TextView)
    Button mBt_changeProperty;
    @BindView(R.id.bt_set_text_on_mediator)
    Button mBt_temp;

    DataMediator<Student> mMediator;


    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_double_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mBt_changeProperty.setText("click this to change property");
        mBt_temp.setVisibility(View.GONE);

        //create data mediator
        mMediator = DataMediatorFactory.createDataMediator(Student.class);
        //add property callback
        mMediator.addDataMediatorCallback(new DataMediatorCallback<Student>() {
            @Override
            public void onPropertyValueChanged(Student data, Property prop, Object oldValue, Object newValue) {
                Logger.w("TestPropertyChangeActivity","onPropertyValueChanged","prop = "
                        + prop.getName() + " ,oldValue = " + oldValue + " ,newValue = " + newValue);
                mTv_desc.setText(String.valueOf(newValue));
            }
        });
        mMediator.getDataProxy().setName("heaven7");
    }

    @OnClick(R.id.bt_set_text_on_TextView)
    public void onClickSetTextOnTextView(View v){
        mMediator.getDataProxy().setName("time: " + System.currentTimeMillis());
    }
}

```

# <h2 id="2">listen list change</h2>
public class TestListPropertyChange {

    private DataMediator<FlowItem> mDm_flowitem;

    public TestListPropertyChange(){
        mDm_flowitem = DataMediatorFactory.createDataMediator(FlowItem.class);
        mDm_flowitem.addDataMediatorCallback(DataMediatorCallback.create(
                FlowItem.PROP_desc.getName(), new ListPropertyCallbackImpl()));
    }

    @Test
    public void testListPropertyChange() {
        mDm_flowitem.getDataProxy().beginDescEditor()
                .add("heaven7")
                .add(1, "google")
                .set(0, "facebook")
                .remove(1)
                .end();
    }
    private static class ListPropertyCallbackImpl implements ListPropertyCallback<FlowItem>{

        @Override
        public void onAddPropertyValues(FlowItem data, Property prop, Object newValue, Object addedValue) {
             //called on add
            //all value is List type
        }
        @Override
        public void onAddPropertyValuesWithIndex(FlowItem data, Property prop,
                                                 Object newValue, Object addedValue, int index) {
             //called on add with index
            //all value is List type
        }

        @Override
        public void onRemovePropertyValues(FlowItem data, Property prop,
                                           Object newValue, Object removeValue) {
             //called on remove
            //all value is List type
        }

        @Override
        public void onPropertyItemChanged(FlowItem data, Property prop,
                                          Object oldItem, Object newItem, int index) {
            //called on remove
            //oldItem and new item is a  element of list
        }

        @Override
        public void onPropertyValueChanged(FlowItem data, Property prop,
                                           Object oldValue, Object newValue) {
            //all value is List type, may be null
        }

        @Override
        public void onPropertyApplied(FlowItem data, Property prop, Object value) {
            //all value is List type, may be null
        }
    }

}

# <h2 id="3">listen SparseArray change</h2>
```java
public class TestSparseArrayActivity extends BaseActivity {

    private static final String TAG = "TestSparseArray";
    @BindView(R.id.tv_sa)
    TextView mTv_sa;

    private DataMediator<TestBind> mDm;
    private Set<Integer> mIndexes = new HashSet<>();

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_sparse_array;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mDm = DataMediatorFactory.createDataMediator(TestBind.class);
        // 这里直接用属性回调。也可以用binder.bind(String property, SparseArrayPropertyCallback<? super T> callback)方法
        mDm.addDataMediatorCallback(DataMediatorCallback.createForSparse(
                TestBind.PROP_cityData2.getName(), new CallbackImpl()));

    }

    // put 操作
    @OnClick(R.id.bt_put)
    public void onClickPut(View v){
        final Student stu = createStu(-1);
        mDm.getDataProxy().beginCityData2Editor()
                .put((int)stu.getId(), stu)
                .end();
    }

    // 移除操作(通过key)
    @OnClick(R.id.bt_remove_key)
    public void onClickRemoveByKey(View v){
        if(!mIndexes.isEmpty()){
            final Integer index = mIndexes.iterator().next();
            mDm.getDataProxy().beginCityData2Editor()
                    .remove(index);
            mIndexes.remove(index);
        }else{
            mTv_sa.setText("");
            Logger.w(TAG , "onClickRemoveByKey", "already empty");
        }
    }

    // 移除操作(通过value)
    @OnClick(R.id.bt_remove_value)
    public void onClickRemoveByValue(View v){
        if(!mIndexes.isEmpty()){
            final Integer index = mIndexes.iterator().next();
            mDm.getDataProxy().beginCityData2Editor()
                    .removeByValue(createStu(index));
            mIndexes.remove(index);
        }else{
            mTv_sa.setText("");
            Logger.w(TAG , "onClickRemoveByValue", "already empty");
        }
    }

    //清空操作
    @OnClick(R.id.bt_clear)
    public void onClickClear(View v){
        if(!mIndexes.isEmpty()){
            mDm.getDataProxy().beginCityData2Editor().clear();
            mIndexes.clear();
        }else{
            mTv_sa.setText("");
            Logger.w(TAG , "onClickClear", "already empty");
        }
    }
    private Student createStu(int index) {
        if(index < 0){
            index = new Random().nextInt(5);
        }
        mIndexes.add(index);
        return DataMediatorFactory.createData(Student.class)
                .setId(index).setName("google_" + index).setAge(index);
    }

    private void setLogText(String method, String msg){
        mTv_sa.setText(method + ": \n  " + msg + "\n\n now is: \n"
                + mDm.getData().getCityData2().toString());
    }

    private class CallbackImpl implements SparseArrayPropertyCallback<TestBind>{

        @Override
        public void onEntryValueChanged(TestBind data, Property prop, Integer key,
                                        Object oldValue, Object newValue) {
            final String msg = "oldValue = " + oldValue + " ,newValue = " + newValue;
            Logger.i(TAG , "onEntryValueChanged", msg);
            setLogText("onEntryValueChanged", msg);
        }
        @Override
        public void onAddEntry(TestBind data, Property prop, Integer key, Object value) {
            final String msg = "key = " + key + " ,value = " + value;
            Logger.i(TAG , "onAddEntry", msg);
            setLogText("onAddEntry", msg);
        }
        @Override
        public void onRemoveEntry(TestBind data, Property prop, Integer key, Object value) {
            final String msg = "key = " + key + " ,value = " + value;
            Logger.i(TAG , "onRemoveEntry", msg);
            setLogText("onRemoveEntry", msg);
        }
        @Override
        public void onClearEntries(TestBind data, Property prop, Object entries) {
            final String msg = entries.toString(); //here entries is SparseArray
            Logger.i(TAG , "onClearEntries", msg);
            setLogText("onClearEntries", msg);
        }
        @Override
        public void onPropertyValueChanged(TestBind data, Property prop,
                                           Object oldValue, Object newValue) {
            final String msg = "oldValue = " + oldValue + " ,newValue = " + newValue;
            Logger.i(TAG , "onPropertyValueChanged", msg);
            setLogText("onPropertyValueChanged", msg);
        }
        @Override
        public void onPropertyApplied(TestBind data, Property prop, Object value) {
            final String msg = "value = " + value;
            Logger.i(TAG , "onPropertyApplied", msg);
            setLogText("onPropertyApplied", msg);
        }
    }
}

```




