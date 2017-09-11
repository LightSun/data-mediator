data-mediator
=======================================

auto generate code for data/entity on java or android platform  which uses annotation processing to 
generate code for you.

* auto generate interface and impl class which can implements Serializable and Parcelable(android).
* auto generate set and get methods for data
* Field: support generate field annoattion for 'Google-Gson'.
* Field: support multi type , primitive and with it's box. String, and any object type. The sample as Array and List.
* Field: support multi scope. like: Reset, Copy, Snap, Share, Parcelable.
* support depend or extends another data module. (extends only support one.)
* auto generate proxy(medaitor) class for listen the data change. (doing)
* double bind for android. (doing).

Here is a simple definition of data entity.
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




