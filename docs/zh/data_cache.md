
### data-mediator数据缓存详解 
 * 数据缓存： 使用缓存池，反复利用对象。
 * 缓存好处： 在某些大对象数据多的情况下，避免内存抖动和反复gc.
 * 使用步骤:
   * 1), 用注解标识缓存个数。eg:
   ```java
   @Fields(value = {
       @Field(propName = "name", seriaName = "heaven7", type = String.class),
       @Field(propName = "data", seriaName = "result", type = ResultData.class),
    }, maxPoolCount = 100) //最多缓存100个
    public interface TestBind extends Parcelable{
    }
   ```
   * 2), 如何获取对象？     使用 DataMediatorFactory 获取, eg:
   ```java
   StudentModule result = DataMediatorFactory.obtain(StudentModule.class);
   ```
   * 3), 如何回收对象？     使用 生成的实体数据对象。 比如StudentModule..
   ```java
   StudentModule result = ....; //必须是真正的数据。代理层如果调用会报异常.
   result.recycle();
   ```
   ps: 数据回收之后。请不要再保持引用这个对象，除非重新obtain(因为所有属性将会回归初始状态）。
