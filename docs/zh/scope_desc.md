
### 关于域的说明
 * 在本框架内, 有很多域。比如toString/hashCode/Equals. 比如域接口 :重置(IResetable接口), 拷贝（ICopyable接口),
  <br>共享（Shareable), 快照（ISnapable)接口。 这些域指的是什么 ？
  <br>答：
   * 在这里的意思是 字段(field)的作用域。 比如字段可以选择参加toString或者不参加。 hashCode/equals同理。
   * 域的接口也是类似的。域接口的方法。也是类似toString等方法的使用。 
   * 比如IResetable.的方法 reset() 就是重置那些已有 域标志 FLAG_RESET 的 所有字段。 其他域接口同理.
   
   
### 扩展接口(继承这些接口后编译层会自动实现)
 * 数据模型支持接口的扩展。但是只支持已知的。
 <br>下面是已知的扩展接口
 
```java
//java和android的序列化
java.io.Serializable
android.os.Parcelable

//quick adapter库。数据模型作为List结构view的item 需要实现的接口
com.heaven7.adapter.ISelectable
// 如果你希望有些属性支持拷贝 (extends 的时候不要使用泛型,否则还要强转)
com.heaven7.java.data.mediator.ICopyable
// 如果你希望有些属性支持 重置
com.heaven7.java.data.mediator.IResetable
// 如果你希望有些属性支持 共享(比如多个组件共享数据，后面可以通过clearShare去清除这些共享的数据)
com.heaven7.java.data.mediator.IShareable
// 如果你希望有些属性支持 快照。
com.heaven7.java.data.mediator.ISnapable
```   