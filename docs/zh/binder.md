
### data-mediator 数据绑定详解（binder）
 * 原理： 通过编译层，生成代理。 然后用自动回调的方式通知数据变化。
 * 好处： 只需一次绑定，以后所有操作都可直接操作代理 从而自动操作view. 参考我之前写的数据绑定框架（xml配置）
 或者 google-data-binding.
 * binder对象的创建：
 ```java
  mBinder = DataMediatorFactory.createBinder(TextViewBindModule.class);
 ```
 * 使用方式, binder使用比较简单。因为我在android做了一层封装.比如我要绑定TextView的一些属性:
 ```java
 //关键代码
   @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        initResource(context);
        mBinder = DataMediatorFactory.createBinder(TextViewBindModule.class);
        //绑定一组属性到TextView
        mBinder.beginBatchTextViewBinder(mTv)
                .bindText(TextViewBindModule.PROP_text.getName())
                .bindTextRes("textRes")
                .bindTextColor("textColor")
                .bindTextColorRes("textColorRes")
                .bindTextSize("textSize")
                .bindTextSizeRes("textSizeRes")
                .end();
        mProxy = mBinder.getDataProxy();
    }

    @Override
    protected void onDestroy() {
        mBinder.unbindAll();
        super.onDestroy();
    }

    @OnClick(R.id.bt_text)
    public void onClickChangeText(View v){
        //改变文本
        mProxy.setText(getString(mTextRess[mRan.nextInt(5)]));
    }
    @OnClick(R.id.bt_text_res)
    public void onClickChangeTextRes(View v){
        //改变文本---通过资源id
        mProxy.setTextRes(mTextRess[mRan.nextInt(5)]);
    }


    @OnClick(R.id.bt_text_color)
    public void onClickChangeTextColor(View v){
        //改变文本颜色
        mProxy.setTextColor(getResources().getColor(mColorRess[mRan.nextInt(5)]));
    }
    @OnClick(R.id.bt_text_color_res)
    public void onClickChangeTextColorRes(View v){
        //改变文本颜色---通过资源id
        mProxy.setTextColorRes(mColorRess[mRan.nextInt(5)]);
    }


    @OnClick(R.id.bt_text_size)
    public void onClickChangeTextSize(View v){
        //改变文本大小
        mProxy.setTextSize(getResources().getDimensionPixelSize(mTextSizeRess[mRan.nextInt(5)]));
    }

    @OnClick(R.id.bt_text_size_res)
    public void onClickChangeTextSizeRes(View v){
        //改变文本大小---通过资源id
        mProxy.setTextSizeRes(mTextSizeRess[mRan.nextInt(5)]);
    }
 ```
 * 如何主动解绑 ? 
 ```java
  //通过binder对象的unbind方法，tag在android封装层是view对象。list/recyclerview则可能是adapter
  // 内部采用弱引用机制，如果tag对象被释放，也会自动解绑
  Binder<T> unbind(Object tag) 
 ```
 * 如何绑定属性bean的属性到任意对象呢？
 ```java
 //下面这个方法是绑定属性的通用方法。自定义控件即可使用此方法绑定。
 Binder<T> bind(String property, BinderCallback<? super T> callback)
 ```
 * 其他
   * 如果我想首次绑定之后马上应用到view上呢？
   ```java
    //绑定并 首次应用属性(绑定只需要1次)
    binder.bindBackground("background", mV_bg)
    .bindBackgroundRes("backgroundRes", mV_bg_res)
    .bindBackgroundColor("backgroundColor", mV_bg_color)
    .bindEnable("enable", mV_enable)
    .applyProperties(PropertyInterceptor.NULL);//应用.
    
    //ps: PropertyInterceptor 这个对象可用于应用属性时候的拦截，避免不必要的问题。比如我在xml配置了view的text.但是首次不想受null影响.
    
   ```
   * 属性应用到view拦截器.
   ```java
    PropertyInterceptor 这个对象可用于应用属性时候的拦截，避免不必要的问题。比如我在xml配置了view的text.但是首次不想受null影响.
   ```



    