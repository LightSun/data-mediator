# Gson ×¢½âÉú³É
* here is a demo.
```java
@Fields({
        @Field(propName = "id", seriaName = "_id", since = 1.1, until = 1.5),
        @Field(propName = "test", flags = FieldFlags.FLAGS_MAIN_SCOPES_2 &~ FieldFlags.FLAG_GSON_PERSISTENCE)
})
public interface GsonTest extends DataPools.Poolable {
    Property PROP_id = SharedProperties.get("java.lang.String", "id", 0);
    Property PROP_test = SharedProperties.get("java.lang.String", "test", 0);

    GsonTest setId(String id1);

    String getId();

    GsonTest setTest(String test1);

    String getTest();/*
================== start methods from super properties ===============
======================================================================= */
}

//generateJsonAdapter  = true is default
@GlobalConfig(gsonConfig = @GsonConfig(generateJsonAdapter = true))
class GlobalSetting{

}
```

it will generate impl class.
```java
@JsonAdapter($GsonTest$TypeAdapter.class)
public class GsonTest_$Impl implements GsonTest, DataPools.Poolable {
  static {
    TypeHandler.registerTypeAdapter(GsonTest_$Impl.class, new $GsonTest$TypeAdapter());
  }

  @SerializedName("_id")
  @Since(1.1)
  @Until(1.5)
  private String id;

  private String test;

//you should not create module by this. use DataMediatorFactory instead
  public GsonTest_$Impl() {
  }

  //more other code  ......
}

```

* serialize or deserialize json data?
 use class 'GsonSupport'. 