data-mediator
=======================================

- 一个数据层的框架。利用编译时注解技术， 在java和android平台自动生成 数据实体及相关的代码。
  方便数据层的使用。支持属性回调，支持gson. 通过操作binder和代理 实现绝大部分开发属性设置。
  亦可快速实现状态切换，监听。方便数据统计.
 <br>支持java和android平台。
[demo下载](https://github.com/LightSun/data-mediator/tree/master/output/app-debug.apk)

# 导航
 * [simple魅力](#基本魅力)
 * [设计思想](#设计思想)
 * [特点](#特点)
 * [安装](#安装)
 * [快速入门](#快速入门)
 * [简易教程](https://github.com/LightSun/data-mediator/blob/master/docs/zh/courses.md)
 * [data-mediator-demo说明](https://github.com/LightSun/data-mediator/blob/master/docs/zh/demo_readme.md)
 * [进阶指南](#进阶指南)
 * [混淆配置](#混淆配置)
 * [Gson注解详细说明](https://juejin.im/post/59e5663f51882546b15b92f0)

# 基本魅力
 - 假设我想定义的数据实体是下面这个。
```java
@JsonAdapter($FlowItemModule$TypeAdapter.class)
public class FlowItemModule_Impl implement Parcelable {
  public static final Parcelable.Creator<FlowItemModule_Impl> CREATOR = new Parcelable.Creator<FlowItemModule_Impl>() {
    @Override
    public FlowItemModule_Impl createFromParcel(Parcel in) {
      return new FlowItemModule_Impl(in);
    }

    @Override
    public FlowItemModule_Impl[] newArray(int size) {
      return new FlowItemModule_Impl[size];
    }
  };

  private boolean selected;

  @Since(1.2)
  @Until(2.0)
  private int id;

  @SerializedName("stu_name")
  private String name;

  @Expose(
      serialize = false,
      deserialize = false
  )
  private String desc;

  protected FlowItemModule_Impl(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.desc = in.readString();
  }

  public FlowItemModule_Impl() {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeString(this.desc);
  }

  @Override
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  @Override
  public boolean isSelected() {
    return this.selected;
  }

  @Override
  public String toString() {
    Objects.ToStringHelper helper = Objects.toStringHelper(this)
        .add("id", String.valueOf(this.id))
        .add("name", String.valueOf(this.name))
        .add("desc", String.valueOf(this.desc));
    return helper.toString();
  }

  public int getId() {
    return id;
  }

  public FlowItemModule setId(int id1) {
    this.id = id1;
    return this;
  }

  public String getName() {
    return name;
  }

  public FlowItemModule setName(String name1) {
    this.name = name1;
    return this;
  }

  public String getDesc() {
    return desc;
  }

  public FlowItemModule setDesc(String desc1) {
    this.desc = desc1;
    return this;
  }

  @Override
  public int hashCode() {
    int result = 0;
    result = 31 * result + getId();
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getDesc() != null ? getDesc().hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FlowItemModule_Impl)) {
      return false;
    }
     FlowItemModule_Impl that = (FlowItemModule_Impl) o;
    if (getId() != that.getId()) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getDesc() != null ? !getDesc().equals(that.getDesc()) : that.getDesc() != null) {
      return false;
    }
    return true;
  }
}

```
- 那么只要下面简短几行代码即可搞定.
````java
@Fields({
        @Field(propName = "id", type = int.class,  since = 1.2, until = 2.0),
        @Field(propName = "name" ,seriaName = "stu_name"),
        @Field(propName = "desc" , flags = FLAGS_MAIN_SCOPES_2 | FLAGS_NO_EXPOSE),
})
public interface FlowItem extends Parcelable{
}
`````
框架这么简单？ no, 更多惊喜等你来！

# 设计思想
- 整个设计分3层： 模型层，代理层，调用层

- 模型层：代表的是数据模型接口and实现
- 代理层：数据实体的代理
- 调用层：操作模型和代理的

# 特点
- 自动生成数据的接口和实现类.可自动实现Serializable和 Parcelable(android)接口。
  <br>自动生成get/is , set , toString方法.
  <br>自动生成代理层 以便监听数据变化。
- 字段:
  * 1, 支持多种类型 , 8大基本类型(int,long,short,byte,float,double,boolean ,char)及其包装类型， String类型, 
   <br> 和其他类型 .数组和list/SparseArray结构同样支持。（map暂不支持parcelable）.<br>
  * 2, 支持所有的gson注解 for 'Google-Gson'.
  * 3, 支持多域， 比如： 重置(IResetable接口), 拷贝（ICopyable接口), 共享（Shareable), 快照（ISnapable)接口。toString.
  <br>     作用: 比如重置： 很多时候我们调用了数据的一些方法，改变了一些属性。然后想重置以便重新使用。
  <br>     比如 toString. 可选择某些字段参加或者不参加toString方法. hashCode和equals同理
  
- 支持List/SparseArray属性编辑器. （相当于对list/SparseArray的增删改增加了便捷操作）
      
- 支持依赖或继承 @Field注解的接口（代表数据实体). 继承只能继承一个, 否则error.
    
- 支持链式调用. data-mediator-compiler 1.0.9 之后默认链式。
 ```java
  DataMediator<StudentModule> mediator = DataMediatorFactory.createDataMediator(StudentModule.class);
        //数据代理层
        mediator.getDataProxy()
                .setName(null)
                .setAge(0)
                .setId(0);

        //数据真正的模型实现
        mediator.getData().setName(null)
                .setAge(0)
                .setId(0);
 ```
 - 支持数据缓存 (使用请参考下面[进阶指南](#进阶指南))
 - 支持android平台的双向绑定, 新增万能的Binder. 支持绑定任意控件的属性。(常用的已经集成)
 <br> 绑定以后操作数据代理就是操作view. (使用请参考下面[进阶指南](#进阶指南))
 - 完美搭配gson（支持所有Gson注解）。
 - 支持自定义方法，字段, 可实现任意接口.(使用请参考下面[进阶指南](#进阶指南))

# 安装
 * 安装idea插件。（see release）
 * java平台.
    * 1, gradle配置
    ```java
   apply plugin: "net.ltgt.apt-idea"
   apply plugin: 'java'

   buildscript {
       repositories {
           jcenter()
           maven {
               url "https://plugins.gradle.org/m2/"
           }
       }
       dependencies {
           classpath "net.ltgt.gradle:gradle-apt-plugin:0.12"
       }
   }

   repositories {
       jcenter()
       maven {
           url "https://plugins.gradle.org/m2/"
       }
   }
   idea {
       project {
           // experimental: whether annotation processing will be configured in the IDE; only actually used with the 'idea' task.
           configureAnnotationProcessing = true
       }
       module {
           apt {
               // whether generated sources dirs are added as generated sources root
               addGeneratedSourcesDirs = true
               // whether the apt and testApt dependencies are added as module dependencies
               addAptDependencies = true

               // The following are mostly internal details; you shouldn't ever need to configure them.
               // whether the compileOnly and testCompileOnly dependencies are added as module dependencies
               addCompileOnlyDependencies = false // defaults to true in Gradle < 2.12
               // the dependency scope used for apt and/or compileOnly dependencies (when enabled above)
               //PROVIDED
               mainDependenciesScope = "COMPILE" // defaults to "COMPILE" in Gradle < 3.4, or when using the Gradle integration in                                IntelliJ IDEA
           }
       }
   }
   ```
   * 2, idea设置 <br>
   
     * (1), setting -> compiler -> annotation Processor -> ![勾选](https://www.jetbrains.com/help/img/idea/2017.2/annotation_profile_move.png)  更多请参见 [idea](https://www.jetbrains.com/help/idea/configuring-annotation-processing.html)<br>
     * (2), 将生成代码的目录设置为source目录/test source 目录.
   * 3, 添加依赖.
   ```java
   dependencies {
     compile 'com.heaven7.java.data.mediator.annotation:data-mediator-annotations:<see release>'
     compile 'com.heaven7.java.data.mediator.support.gson:data-mediator-support-gson:<see release>'

     compile 'com.heaven7.java.data.mediator:data-mediator:<see release>'
     apt 'com.heaven7.java.data.mediator.compiler:data-mediator-compiler:<see release>'
     apt 'com.squareup:javapoet:1.9.0'
   }
   ```
 * android平台.
   * 1, 在项目根目录添加apt依赖。
   ```java 
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
   ```
   * 2, 在使用的app module中加入。apt plugin
   ```java
      apply plugin: 'com.neenbedankt.android-apt'
   ```
   * 3, 添加dependencies
   ```java
   dependencies {
       // gson支持库( 1.2.0 版本新增)
       compile 'com.heaven7.java.data.mediator.support.gson:data-mediator-support-gson:<see release>'
       compile 'com.heaven7.java.data.mediator:data-mediator:<see release>'

       compile 'com.heaven7.java.data.mediator.annotation:data-mediator-annotations:<see release>'
       apt 'com.heaven7.java.data.mediator.compiler:data-mediator-compiler:<see release>'
       apt 'com.squareup:javapoet:1.9.0'

       // 如果需要生成对应的gson注解。请加入gson依赖。(1.2.0版本后 data-mediator-support-gson自带)
       compile "com.google.code.gson:gson:2.8.2"
       // 如果要支持android平台的数据绑定. 请添加依赖
       compile 'com.heaven7.android.data.mediator:data-mediator-android:<see release>'

   }
   ```

# 快速入门

1, [可选], 全局配置.
```java
@GlobalConfig(
        gsonConfig = @GsonConfig(
                version = 2.0,
                forceDisable = false,
                generateJsonAdapter = true
        )
)
```

2, 定义你的数据实体。比如我要定义关于学生的数据模型, 需要实现Serializable, Parcelable. 
假如学生有。年龄，名称, id属性。
那么简单的数据定义为:
```java

@Fields({
        @Field(propName = "age" , type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name" , type = String.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "id" , type = long.class, flags = FLAGS_ALL_SCOPES),
})
public interface Student extends Serializable, Parcelable{
}
```
3, 使用idea插件生成代码, 快捷键比如 alt + insert. (安装release里面的idea插件).

4,[可选], 编译项目生成代码.
 * java: module上鼠标右键. compile/build (module) XXX
 * android: 点击android studio 工具栏上的图标
   ![make project](https://github.com/LightSun/data-mediator/blob/master/res/as_make_project.png)
  即可自动生成代码（数据定义没变化，不会重新生成).

5, 调用示例 （来自data-mediator-demo下的[TestPropertyChangeActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestPropertyChangeActivity.java)）
```java
/**
 * 属性改变demo
 * Created by heaven7 on 2017/9/18 0018.
 */
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

        //为数据模型创建  中介者。
        mMediator = DataMediatorFactory.createDataMediator(Student.class);
        //添加属性callback
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
* [简易教程](https://github.com/LightSun/data-mediator/blob/master/docs/zh/courses.md)
* 更多sample 代码 见 [demos](https://github.com/LightSun/data-mediator/tree/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity)

# 进阶指南
 * [域和支持的接口说明](https://github.com/LightSun/data-mediator/blob/master/docs/zh/scope_desc.md)
 * [binder-详解](https://github.com/LightSun/data-mediator/blob/master/docs/zh/binder.md)
 * [数据缓存-详解](https://github.com/LightSun/data-mediator/blob/master/docs/zh/data_cache.md) 
 * [api 说明](https://github.com/LightSun/data-mediator/blob/master/docs/zh/api.md)
 * [自定义方法,接口实现](https://github.com/LightSun/data-mediator/blob/master/docs/zh/self_method_field.md)

# 混淆配置
```java
-keepclasseswithmembers public class * implements com.heaven7.java.data.mediator.DataPools$Poolable{
   *;
}
-keepclasseswithmembers public interface * extends com.heaven7.java.data.mediator.DataPools$Poolable{
   *;
}
-keep class * extends com.heaven7.java.data.mediator.BaseMediator{
   *;
}
-keep class com.heaven7.java.data.mediator.BaseMediator
# 1.1.3 新增
-keep public class com.heaven7.android.data.mediator.DataMediatorDelegateImpl
# 1.2.2新增
-keep class com.heaven7.java.data.mediator.internal.$StaticLoader
# since 1.4.0
-keepclasseswithmembers class com.heaven7.java.data.mediator.Binder{
  *;
}
# since 1.4.0
-keepclasseswithmembers class * extends com.heaven7.java.data.mediator.Binder{
   *;
}
```
