data-mediator
=======================================

利用编译时注解技术， 在java和android平台自动生成 数据实体的代码。

# 特点
- 自动生成数据的接口和实现类.可自动实现Serializable和 Parcelable(android)接口。
- 为数据实体 自动生成get 和 set方法 
- 字段: 支持生成字段的注解 for 'Google-Gson'.
- 字段: 支持多种类型 , 8大基本类型及其包装类型， String类型, 和其他类型 . 数组和list结构同样支持。（map暂不支持parcelable）
- 字段: 支持多域， 比如： 重置(IResetable接口), 拷贝（ICopyable接口), 共享（Shareable), 快照（ISnapable)接口。
      作用: 比如重置： 很多时候我们调用了数据的一些方法，改变了一些属性。然后想重置以便重新使用。
      
- 支持依赖或继承 @Field注解的接口（代表数据实体). 继承只能继承一个。
   * 平常我们写 BaseEntity(内有代表http/https响应的code, message, data字段）, 通常业务接口的数据会继承这个BaseEntity。
    所以这里规定 继承@Field注解的接口（代表数据实体) 只能一个。否则error.

# 即将支持的特性
- 自动生成代理层 以便监听数据变化。
- 实现android平台的双向绑定
- 丰富的调用层支持和数据缓存
- 更多接口的支持

# 快速入门

1, 在项目根目录添加apt依赖。
```java
 classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
```

2, 在使用的app module中加入。apt plugin
```java
   apply plugin: 'com.neenbedankt.android-apt'
```

3, 添加dependencies
```java
dependencies {
    //......
    compile 'com.heaven7.java.data.mediator:data-mediator:1.0'
    compile 'com.heaven7.java.data.mediator.annotation:data-mediator-annotations:1.0'
    apt 'com.heaven7.java.data.mediator.compiler:data-mediator-compiler:1.0.2'
    apt 'com.squareup:javapoet:1.9.0'
    
    // 如果需要生成对应的gson注解。请加入gson依赖。比如
    compile "com.google.code.gson:gson:2.7"
}
```

4, 开始定义你的数据实体。比如我要定义关于学生的数据模型, 需要实现Serializable, Parcelable. 
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

生成的实现类为
```java
public class StudentModule_Impl implements StudentModule, Serializable, Parcelable {
  private static final long serialVersionUID =  1L;

  public static final Parcelable.Creator<StudentModule_Impl> CREATOR = new Parcelable.Creator<StudentModule_Impl>() {
    @Override
    public StudentModule_Impl createFromParcel(Parcel in) {
      return new StudentModule_Impl(in);
    }

    @Override
    public StudentModule_Impl[] newArray(int size) {
      return new StudentModule_Impl[size];
    }
  };

  private int age;

  private String name;

  private long id;

  protected StudentModule_Impl(Parcel in) {
    this.age = in.readInt();
    this.name = in.readString();
    this.id = in.readLong();
  }

  public StudentModule_Impl() {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.age);
    dest.writeString(this.name);
    dest.writeLong(this.id);
  }

  public int getAge() {
    return age;
  }

  public void setAge(int int1) {
    this.age = int1;
  }

  public String getName() {
    return name;
  }

  public void setName(String string1) {
    this.name = string1;
  }

  public long getId() {
    return id;
  }

  public void setId(long long1) {
    this.id = long1;
  }
}

```

更多文档请看 [the wiki](https://github.com/LightSun/data-mediator/wiki).





