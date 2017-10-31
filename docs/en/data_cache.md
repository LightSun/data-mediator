# data cache
 * data cache£º use pool to reuse data
 * steps:
   * 1), use annotation to indicate pool count ¡£eg:
   ```java
   @Fields(value = {
       @Field(propName = "name", seriaName = "heaven7", type = String.class),
       @Field(propName = "data", seriaName = "result", type = ResultData.class),
    }, maxPoolCount = 100) //max 100
    public interface TestBind extends Parcelable{
    }
   ```
   * 2),how to get data module ?  use DataMediatorFactory , eg:
   ```java
   Student result = DataMediatorFactory.obtain(Student.class);
   ```
   * 3), how to recycle ?   use data entity¡£eg: Student
   ```java
   DataMediator<Student> dm =  DataMediatorFactory.obtain(Student.class);;
   dm.getData().recycle();
   ```
   * 4), ps: after recycle you must not use the data again or else may cause some problem you don't want¡£

 * data cha is thread safe£¿ yes , it is.
