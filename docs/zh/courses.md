
# 所有教程
 * [gson注解生成](#1)
 * [binder简单应用](#2)
 * [属性回调](#3)
 * [List属性编辑器](#4)
 
# <h2 id="1">gson注解生成</h2>
 * 最开始的时候想到的就是和gson搭配。sample:
 主要就是生成gson注解。请看下面一个数据模型定义。
 ```java
 @Fields({
        @Field(propName = "name", seriaName = "heaven7", type = String.class, flags = FLAGS_NO_EXPOSE),       
})
public interface StudentBind extends IDataMediator, ISelectable{
}
    
 ```
 propName = "name"  表示属性的名称 是 name , 会影响生成的get/set方法名称
 <br>type = String.class   表示属性类型是String类型  
  <br> 下面是2个与gson相关的属性:
  <br>seriaName = "heaven7"  表示了要生成gson注解的 
  ```java
  @SerializedName("heaven7")
  ```
  <br>flags = FLAGS_NO_EXPOSE 表示了要生成gson注解的
  ```java
  @Expose(
      serialize = false,
      deserialize = false
  )
  ```
  生成结果
  ```java
  @SerializedName("heaven7")
  @Expose(
      serialize = false,
      deserialize = false
  )
  private String name;
  ```
  
# <h2 id="2">binder 简单运用</h2>

 * 假设我想绑定一个bean的属性到view的背景上（即setBackground）。那么很简单
 ```java
 View mV_bg = ...;
 //创建binder
 Binder<ViewBindModule>  binder = DataMediatorFactory.createBinder(ViewBindModule.class);
 //绑定属性
 binder.bindBackground("background", mV_bg) //这里的"background" 指的是绑定到数据实体对象上的属性。不是view的属性
                              //bindBackground 才是表示绑定到view的background上. 而且是drawable类型
                              
 //改变背景（drawable）
 binder.getDataProxy().setBackground(mUserDrawable1 ? mDrawable2 : mDrawable1);                              
 
 ```

# <h2 id="3">属性回调</h2>
 * 上面的binder对象实际上是基于属性回调的。

 * 实际场景： 
   * 在工作中，有的时候我们想监听某些数据/ui 的变化。以完成统计功能， 以前在项目中遇到过。<br>
   比如新闻类的ui, 顶部有很多tab, 下面是数据流列表。 这时候有这样一个需求: <br>
   要求用户点击了哪个item，属于哪个tab. 什么类型, 在列表中的索引, 这个时候我发现，搞统计还是挺麻烦的。<br>
   即使最后我设计了一套通用的框架, 还是感觉不完美。 
   * 所以后面我想到了数据中介者data-mediator这个框架. 后面会支持这些更加复杂的功能。
 * 属性回调，不一定非要绑定到view的属性上（比如setText, setTextColor, setBackground等等）。 它也可以是事件点击的属性, 后面也会支持。 
  <br>换句话说， 以后它可以做很多后台的一些操作。
  
 * 回归正题, 如何使用属性回调？
 <br>在demo中有这样一个简单的例子。
 ```java
 public class TestPropertyChangeActivity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView mTv_desc;

    @BindView(R.id.bt_set_text_on_TextView)
    Button mBt_changeProperty;
    @BindView(R.id.bt_set_text_on_mediator)
    Button mBt_temp;

    DataMediator<StudentModule> mMediator;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_double_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mBt_changeProperty.setText("click this to change property");
        mBt_temp.setVisibility(View.GONE);

        //为数据模型创建  中介者。
        mMediator = DataMediatorFactory.createDataMediator(StudentModule.class);
        //添加属性callback
        mMediator.addDataMediatorCallback(new DataMediatorCallback<StudentModule>() {
            @Override
            public void onPropertyValueChanged(StudentModule data, Property prop, Object oldValue, Object newValue) {
                Logger.w("TestPropertyChangeActivity","onPropertyValueChanged","prop = "
                        + prop.getName() + " ,oldValue = " + oldValue + " ,newValue = " + newValue);
               //改变文本         
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
 仅仅，属性改变的时候改变一下文本。很简单吧。  
 
# <h2 id="4">List属性编辑器</h2>
 * 一般用于绑定列表控件，比如android RecyclerView. 下面是一个demo:
  ```java
  public class TestRecyclerListBindActivity extends BaseActivity {

    private static final Random sRan = new Random();

    @BindView(R.id.rv)
    RecyclerView mRv;

    private Binder<RecyclerListBindModule> mBinder;
    private TestRecyclerListAdapter<StudentModule> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_recycler_list_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
    //初始化adapter
        initAdapter();
    //创建binder
        mBinder = DataMediatorFactory.createBinder(RecyclerListBindModule.class);
    //绑定列表
        onBindListItems(mBinder);
    }

    //添加一个item
    @OnClick(R.id.bt_add)
    public void onClickAddItem(View v){
        mBinder.getDataProxy().beginStudentsEditor()
                .add(0, createItem());
    }

    //添加一组items
    @OnClick(R.id.bt_add_all)
    public void onClickAddItems(View v){
        List<StudentModule> list = createItems();
        mBinder.getDataProxy().beginStudentsEditor()
                .addAll(list);
    }

    //删除一个 item
    @OnClick(R.id.bt_remove)
    public void onClickRemoveItem(View v){
        mBinder.getDataProxy().beginStudentsEditor()
                .remove(0);
    }

    //替换所有items
    @OnClick(R.id.bt_replace)
    public void onClickReplaceItem(View v){
        mBinder.getDataProxy().setStudents(createItems());
    }

    protected void onBindListItems(Binder<RecyclerListBindModule> mBinder) {
        //通用的绑定方法. 这里用于绑定列表
        mBinder.bind(RecyclerListBindModule.PROP_students.getName(),
                new ListBinderCallback<>(mAdapter));
    }

    protected void initAdapter() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter = new TestRecyclerListAdapter<StudentModule>(
                R.layout.item_test_recycler_list, null) {
            @Override
            protected void onBindData(Context context, int position,
                                      StudentModule item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_age, ""+item.getAge());
            }
        });
    }

    private static StudentModule createItem(){
        StudentModule data = DataMediatorFactory.createData(StudentModule.class);
        data.setAge(sRan.nextInt(10001));
        data.setName("google__" + sRan.nextInt(100));
        return data;
    }
    @NonNull
    private static List<StudentModule> createItems() {
        List<StudentModule> list = new ArrayList<>();
        //just mock data
        final int count = sRan.nextInt(10) + 1;
        for (int i =0 ; i< count ; i++){
            list.add(createItem());
        }
        return list;
    }

    private static abstract class TestRecyclerListAdapter<T extends ISelectable>
             extends QuickRecycleViewAdapter<T> implements
             ListBinderCallback.IItemManager<T>{

        public TestRecyclerListAdapter(int layoutId, List<T> mDatas) {
            super(layoutId, mDatas);
        }

         //===================== sub is IItemManager's methods ===============
         @Override
        public void addItems(List<T> items) {
            getAdapterManager().addItems(items);
        }

        @Override
        public void addItems(int index, List<T> items) {
            getAdapterManager().addItems(index, items);
        }

        @Override
        public void removeItems(List<T> items) {
            getAdapterManager().removeItems(items);
        }

        @Override
        public void replaceItems(List<T> items) {
            getAdapterManager().replaceAllItems(items);
        }
    }
}

  ```
  复杂么？ 不复杂，第一步绑定了列表。然后改变数据的时候回调到了ListBinderCallback.IItemManager
  
 
 
 
 
 
 
 
 
 
