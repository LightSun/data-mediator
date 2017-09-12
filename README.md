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
- 自定义数据缓存。


下面是一个简单的数据定义。
```java
@Fields({
        @Field(propName = "name", seriaName = "heaven7", type = String.class),
        @Field(propName = "test_object", seriaName = "test_object",
                flags = FLAG_EXPOSE_DEFAULT | FLAG_EXPOSE_SERIALIZE_FALSE, type = Object.class),
        @Field(propName = "test_Format", seriaName = "test_Format", flags = 1, type = Double.class),
        @Field(propName = "test_int", seriaName = "test_int", type = int.class,
                flags = FLAG_EXPOSE_DEFAULT | FLAG_COPY | FLAG_RESET),
        @Field(propName = "test_list", seriaName = "test_list", type = long.class, complexType = COMPLEXT_LIST,
                flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP),
        @Field(propName = "test_array", seriaName = "test_array", type = String.class,
                complexType = COMPLEXT_ARRAY,
                flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP
        ),
})
public interface StudentBind extends ICopyable, IResetable, IShareable, ISnapable {
}
 
```
更多文档请看 [the wiki](https://github.com/LightSun/data-mediator/wiki).



