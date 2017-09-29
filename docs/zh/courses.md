
### 和Gson搭配.
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

### binder 简单运用.

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

### 属性回调
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
 
 
 
 
 
