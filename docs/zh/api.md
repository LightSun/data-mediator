
# API说明

 * [FieldFlags类常量](#FieldFlags类常量说明)
 * [注解@Fields成员](#注解@Fields成员说明)
 * [注解@Field类成员](#注解@Field类成员说明)
 * [Binder成员](#Binder成员说明)
 * [BinderCallback](#BinderCallback说明)
 * [DataMediatorFactory成员](#DataMediatorFactory成员说明)
 
 # FieldFlags类常量说明
 ```java
 
    // 字段复合类型-数组结构。@Field注解 complexType()可指定。默认普通类型
    public static final int COMPLEXT_ARRAY = 1;
    // 字段复合类型-list结构。 @Field注解 complexType()可指定。默认普通类型
    public static final int COMPLEXT_LIST = 2;

    // transient标志。用于标识字段是否是 transient,  @Field注解 flags()可指定
    public static final int FLAG_TRANSIENT = 0x00000001;

   // volatile标志。用于标识字段是否是 volatile,  @Field注解 flags()可指定
    public static final int FLAG_VOLATILE = 0x00000002;

   // 快照标志。 表示这个字段会参加ISnapable接口的  clearSnap()方法
    public static final int FLAG_SNAP     = 0x00000004;
   
   //共享标志。 表示这个字段会参加IShareable接口的  clearShare()方法
    public static final int FLAG_SHARE    = 0x00000008;
   //拷贝标志。 表示这个字段会参加 ICopyable接口的所有方法
    public static final int FLAG_COPY     = 0x00000010;//16
    //重置标志。 表示这个字段会参加 IResetable 接口的reset方法。
    public static final int FLAG_RESET    = 0x00000020;//32

    //toString 标志。表示这个字段会参加toString方法
    public static final int FLAG_TO_STRING = 0x00000040; //64

    //parcelable标志。 表示这个字段会参加android的序列化到内存 （自动有CREATOR）。
    public static final int FLAG_PARCELABLE = 0x00000080; //128

    /**
     gson 的注解@Expose标志。表示会在字段上生成gson - @expose注解(serialize = true,
     deserialize = true). eg:
      <pre>@Expose(
     serialize = true,
     deserialize = true
     )</pre>
     */
    public static final int FLAG_EXPOSE_DEFAULT = 0x00000100;//256
    /**
     gson 的注解@Expose标志。表示会在字段上生成gson - @expose注解.并且 serialize 强制为false,
     * <pre>@Expose(
     serialize = false,
     deserialize = true  //indicate by FLAG_EXPOSE_DESERIALIZE_FALSE
     )</pre>
     */
    public static final int FLAG_EXPOSE_SERIALIZE_FALSE = 0x00000200;//512
    /**
     gson 的注解@Expose标志。表示会在字段上生成gson - @expose注解.并且 deserialize 强制为false,
     该标志和 FLAG_EXPOSE_SERIALIZE_FALSE互不影响
     * <pre>@Expose(
     serialize = true,
     deserialize = false
     )</pre>    
     */
    public static final int FLAG_EXPOSE_DESERIALIZE_FALSE = 0x00000400;//1024


    /**
     * hashCode和Equals标志。 表示字段会参加hashCode和Equals方法。
     * @since 1.1.1
     */
    public static final int FLAG_HASH_EQUALS              = 0x00000800; //2048

    /**
     * 复合标志。.
     * @see #FLAG_SNAP 快照
     * @see #FLAG_RESET 重置
     * @see #FLAG_SHARE 共享
     * @see #FLAG_COPY  拷贝
     * @see #FLAG_PARCELABLE android序列化到内存
     * @see #FLAG_TO_STRING   toString
     * @see #FLAG_HASH_EQUALS   hashCode和equals
     */
    public static final int FLAGS_ALL_SCOPES = FLAG_SNAP | FLAG_RESET | FLAG_SHARE
            | FLAG_COPY | FLAG_PARCELABLE | FLAG_TO_STRING | FLAG_HASH_EQUALS;

    /**
     * 复合标志。
     * @see #FLAG_COPY         拷贝
     * @see #FLAG_PARCELABLE     android序列化到内存
     * @see #FLAG_TO_STRING      toString标志
     * @since 1.0.7
     */
    public static final int FLAGS_MAIN_SCOPES = FLAG_COPY | FLAG_PARCELABLE | FLAG_TO_STRING ;
    /**
      复合标志. 表示gson注解的@Expose .serialize = false并且deserialize = false.  
     * @see #FLAG_EXPOSE_DEFAULT
     * @see #FLAG_EXPOSE_SERIALIZE_FALSE
     * @see #FLAG_EXPOSE_DESERIALIZE_FALSE
     * @since 1.0.7
     */
    public static final int FLAGS_NO_EXPOSE = FLAG_EXPOSE_DEFAULT | FLAG_EXPOSE_SERIALIZE_FALSE | FLAG_EXPOSE_DESERIALIZE_FALSE;

    /**
      复合标志， 表示在 FLAGS_MAIN_SCOPES 上添加 hashCode和equals标志
     * @see #FLAG_COPY       拷贝
     * @see #FLAG_PARCELABLE   android序列化到内存
     * @see #FLAG_TO_STRING    toString标志
     * @see #FLAG_HASH_EQUALS   hash和equals标志
     * @since 1.1.1
     */
    public static final int FLAGS_MAIN_SCOPES_2 = FLAGS_MAIN_SCOPES | FLAG_HASH_EQUALS;
 
 ``` 

# 注解@Fields成员说明
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
# 注解@Field类成员说明
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
# DataMediatorFactory成员说明
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

# Binder成员说明
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
# BinderCallback说明
   ```java
   public static class SimpleBinderCallback<T> implements BinderCallback<T>{

       //打 tag 用于主动解绑。可以为null
	@Override
	public Object getTag() {
	    return null;
	}
	// 在添加list 结构的values时被回调。(属性类型List)
	@Override
	public void onAddPropertyValues(T data, Property prop, Object newValue, Object addedValue) {

	}
	// 在添加list 结构的values时被回调 (携带起始的index)。(属性类型List)
	@Override
	public void onAddPropertyValuesWithIndex(T data, Property prop, Object newValue,
						 Object addedValue, int index) {

	}
	//在 移除 list结构的 Values 时被调用 (属性类型List)
	@Override
	public void onRemovePropertyValues(T data, Property prop, Object newValue, Object removeValue) {

	}
	// 在属性值改变时调用。
	@Override
	public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {

	}
       // 在属性应用时调用。对应DataMediator/Binder的applyProperties方法
	@Override
	public void onPropertyApplied(T data, Property prop, Object value) {
	    onPropertyValueChanged(data, prop, null, value);
	}
    }

   ```
