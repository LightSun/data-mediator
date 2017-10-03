测试需要考虑到各种情况：
     有无， super interfaece, 各种type, 各种flag, 各种接口。 gson.

     next will support map for parceable.


    javapoet 生成builder
    https://github.com/josketres/builderator/blob/master/src/test/java/com/josketres/builderator/BuilderatorTest.java


javax.lang.module Elements 等之类的封装。参考项目: juzu


数据初始化?
扩展接口？
数据绑定方便的注解？


9.30 target:  1.1.2
1,  list编辑器增加set  ok
2,  applyTo  dataConsumer  ok
3,  batch apply.      no need

1.1.3
   避免属性冲突 for BaseMediator. ok
   prepare SparseArray/map  doing (MapPropertyCallback, SparseArrayPropertyCallback, SparseArrayPropertyEditor)
