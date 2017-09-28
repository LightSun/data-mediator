
### API 说明

 * 注解 @Fields 成员说明
  ```java
  public @interface Fields {

	//声明所有的字段(field)
	Field[] value();

	/**
	 * 是否启用链式编程。默认true
	 * @since 1.0.2
	 */
	boolean enableChain() default true;

	/**
	 * 指定最大缓存的对象个数
	 * @since 1.0.3
	 */
	int maxPoolCount() default 0;
}

  ```
 * 注解 @Field类成员说明.
    ```java
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Field {

    //指定属性名称， 主要用于set,get方法。 和 字段的名称.比如 我们属性为name的。那么get.set方法为getName, setName
        String propName();

     // 指定gson @SerializedName 注解的名称。默认空字符串表示不添加这个注解。 用途请见[gson](https://github.com/google/gson)
        String seriaName() default "";

     //指定字段的基本类型
        Class<?> type() default String.class;

     //指定字段的复合类型. 目前支持list和array. 其他暂不支持。
     // array为：FieldFlags.COMPLEX_ARRAY  
     // List:    FieldFlags.COMPLEX_LIST
        int complexType() default 0;

     //字段支持的标志，有：
     //    FLAG_TRANSIENT， 用于不被序列化(Serializable).
     //    FLAG_VOLATILE:   用于普通多线程。(线程少的情况)
     //    FLAG_SNAP:       字段的域标志。 表示当实现ISnapable 接口后， 该字段会支持这个接口。
     //    FLAG_SHARE:      字段的域标志。 表示当实现IShareable接口后， 该字段会支持这个接口。
     //    FLAG_COPY:       字段的域标志。 表示当实现ICopyable 接口后， 该字段会支持这个接口。
     //    FLAG_RESET:　　　字段的域标志。 表示当实现IResetable 接口后， 该字段会支持这个接口。　
     //    FLAG_TO_STRING:  字段的域标志。表示会参与toString.
     //    FLAG_PARCELABLE: 字段的域标志。表示会参与parcelable (android)
     //    FLAG_EXPOSE_DEFAULT:             表示将会生成默认的gson注解@Expose. 默认  serialize = true, deserialize = true
     //    FLAG_EXPOSE_SERIALIZE_FALSE:     表示@Expose注解的 serialize = false.
     //    FLAG_EXPOSE_DESERIALIZE_FALSE：  表示@Expose注解的 deserialize = false.
     //ps : 所有这个标志均在 FieldFlags类中、
        int flags() default 0; //1.0.12后默认flags = FLAG_COPY | FLAG_TO_STRING | FLAG_PARCELABLE. 编译层支持

    }
    ```
  *  DataMediatorFactory 成员说明.
   ```java
    /**
     根据module的类型，获取module的实体对象（非代理）。
     * @since 1.1.0
     */
    public static <T> T obtainData(Class<T> clazz)
    /**
       根据module类型 创建一个实体对象（非代理）
     */
    public static <T> T createData(Class<T> clazz);

    /**
      根据module类型创建数据中介者 。（可操作数据和代理）
     */
    public static <T> DataMediator<T> createDataMediator(Class<T> clazz)

    /**
      根据实体对象，包装成数据中介者 。
     */
    public static <T> DataMediator<T> wrapToDataMediator(T t);

    /**
     根据 数据中介者  创建binder
     * @since 1.0.8
     */
    public static <T> Binder<T> createBinder(DataMediator<T> mediator)
    /** 
      根据数据module类型创建binder.
     * @since 1.0.8
     */
    public static <T> Binder<T> createBinder(Class<T> moduleClass)
   ```

* Binder 成员说明.
  ```java
   
   //获取数据中介者
    public DataMediator<T> getDataMediator();

    //获取 真正的数据对象
    public T getData()
    
    //获取数据代理（自动操作数据）
    public T getDataProxy()

   //应用属性到 所有的view上（已绑定的）
    public void applyProperties(){
        getDataMediator().applyProperties();
    }

   //应用属性到 所有的view上（已绑定的）, 并指定属性拦截器
    public void applyProperties(PropertyInterceptor interceptor){
        getDataMediator().applyProperties(interceptor);
    }

    //绑定数据的property属性 到指定的回调上。（通过此可操作任意view）
    public Binder<T> bind(String property, BinderCallback<? super T> callback);

   //解绑 
    public Binder<T> unbind(Object tag)
   //解绑所有
    public void unbindAll(){
        mMap.clear();
        mMediator.removeDataMediatorCallbacks();
    }

    //============================ batch binder =================================================

   // 开启view的一组binder
    public BatchViewBinder<T> beginBatchViewBinder(Object view){
        return new BatchViewBinder<T>(this, view);
    }
    //开启TextView的一组binder
    public BatchTextViewBinder<T> beginBatchTextViewBinder(Object view){
        return new BatchTextViewBinder<T>(this, view);
    }

    //============================== special =========================================//

    //绑定 属性到 checkable控件上
    public abstract Binder<T> bindCheckable(String property, Object checkableView);

   //绑定属性 到 view的可见性上（默认属性类型为int类型。View.GONE and etc.）
    public abstract Binder<T> bindVisibility(String property, Object view, boolean forceAsBoolean);

   //绑定 属性到 view的可见性上。 属性类型boolean,
    public Binder<T> bindVisibility(String property, Object view){
        return bindVisibility(property, view, true);
    }

    //绑定属性到 view的enable上
    public abstract Binder<T> bindEnable(String property, Object view);
    
    //绑定属性到 view的背景资源上。int类型
    public abstract Binder<T> bindBackgroundRes(String property, Object view);
    
   //绑定属性到 view的背景上。 Drawable类型
    public abstract Binder<T> bindBackground(String property, Object view);
    
    //绑定属性到 view的背景颜色上。 int类型
    public abstract Binder<T> bindBackgroundColor(String property, Object view);
    
   //绑定属性到 textView的文本上
    public abstract Binder<T> bindText(String property, Object textView);
    
    //绑定属性到文本资源上
    public abstract Binder<T> bindTextRes(String property, Object textView);
    
    //绑定属性到 文本颜色上
    public abstract Binder<T> bindTextColor(String property, Object textView);
    
    //绑定属性到 文本颜色资源上
    public abstract Binder<T> bindTextColorRes(String property, Object textView);
    /
    //绑定属性到 文本大小资源上
    public abstract Binder<T> bindTextSizeRes(String property,Object textView);
   
   //绑定属性到 文本大小上  int类型
    public abstract Binder<T> bindTextSize(String property, Object textView);
   
   //绑定属性到 文本大小dp值上。。float类型
    public abstract Binder<T> bindTextSizeDp(String property, Object textView);
    
    //绑定属性到 图片地址上
    public abstract Binder<T> bindImageUrl(String property, Object imageView, Object imgLoader);
   
   //绑定属性到 图片 uri上
    public abstract Binder<T> bindImageUri(String property, Object imageView);
    
    //绑定属性到 图片资源上 .int
    public abstract Binder<T> bindImageRes(String property, Object imageView);
   
   //绑定属性到 图片drawable上
    public abstract Binder<T> bindImageDrawable(String property, Object imageView);
    
    //绑定属性到 图片Bitmap上
    public abstract Binder<T> bindImageBitmap(String property,  Object imageView);

   //绑定属性到 recyclerView的adapter上。 list类型
    public abstract Binder<T> bindRecyclerList(String property, Object recyclerView);
    
    //绑定属性到 ListView的adapter上。 list类型
    public abstract Binder<T> bindList(String property, Object listView);
  ```