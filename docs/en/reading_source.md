# Reading The Source
* Sample project
  * [Data-mediator-demo](https://github.com/LightSun/data-mediator/tree/master/Data-mediator-demo) core demo of data-mediator.
     * [app/.../TestAnalyseActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestAnalyseActivity.java)  a demo of simple analyse .
     * [app/.../TestChainCallActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestChainCallActivity.java) a demo show use chain call.
     * [app/.../TestParcelableDataActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestParcelableDataActivity.java) test transport parcelable data.
     * [app/.../TestPropertyChangeActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestPropertyChangeActivity.java) a demo how to listen property change.
     * [app/.../TestRecyclerListBindActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestRecyclerListBindActivity.java) show how to bind list which is using Binder and ListPropertyEditor.
     * [app/.../TestRecyclerListBind2Activity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestRecyclerListBind2Activity.java)  bind list by another way
     * [app/.../TestSelfMethodWithImplInterface](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestSelfMethodWithImplInterface.java)  show how to implement self method with interface in the module of data-mediator
     * [app/.../app/.../TestSparseArrayActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestSparseArrayActivity.java) show how to listen SparseArray property change with SparseArrayEditor
     * [app/.../TestTextViewBindActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestTextViewBindActivity.java) show how to bind some methods of TextView by a data module.
     * [app/.../TestViewBindActivity](https://github.com/LightSun/data-mediator/blob/master/Data-mediator-demo/app/src/main/java/com/heaven7/data/mediator/demo/activity/TestViewBindActivity.java) show how to bind some methods of View by a data module.
  * [data-binding-test](https://github.com/LightSun/data-mediator/tree/master/data-binding-test)  core data-binding demo of data-mediator.
     * [app/.../TestViewBindActivity](https://github.com/LightSun/data-mediator/blob/master/data-binding-test/app/src/main/java/com/heaven7/data/mediator/data_binding_test/sample/TestViewBindActivity.java) show how to use 'Data-binding-annotation' to bind View.
     * [app/.../TestImageViewBindActivity](https://github.com/LightSun/data-mediator/blob/master/data-binding-test/app/src/main/java/com/heaven7/data/mediator/data_binding_test/sample/TestImageViewBindActivity.java)  show how to use 'Data-binding-annotation' to bind ImageView.
     * [app/.../TestBindArrayPropertyToOneView](https://github.com/LightSun/data-mediator/blob/master/data-binding-test/app/src/main/java/com/heaven7/data/mediator/data_binding_test/sample/TestBindArrayPropertyToOneView.java) show how to use 'Data-binding-annotation' bind an array property to one TextView.
     * [app/.../TestSelfBinderActivity](https://github.com/LightSun/data-mediator/blob/master/data-binding-test/app/src/main/java/com/heaven7/data/mediator/data_binding_test/sample/TestSelfBinderActivity.java)  show how to use 'Data-binding-annotation' bind View with self Binder class.
     * [app/.../TestSelfBinderFactory](https://github.com/LightSun/data-mediator/blob/master/data-binding-test/app/src/main/java/com/heaven7/data/mediator/data_binding_test/sample/TestSelfBinderFactory.java) show how to use 'Data-binding-annotation' bind View with self BinderFactory class.
     * [app/.../TestDatabindingAdapter]() show how to use 'Data-binding-annotation' for adapter. eg: RecyclerView adapter. it also show 'Data-binding-annotation' is the best partner of butterknife.
     * [app/.../DataBindingAdapterWithHeader]() show how to use 'Data-binding-annotation' bind adapter with header and GridLayoutManager. 
  * [plugin-data-mediator-test](https://github.com/LightSun/data-mediator/tree/master/plugin-data-mediator-test) the java project demo of Data-mediator.
* library
  * [data-mediator](https://github.com/LightSun/data-mediator/tree/master/data-mediator)  the core lib of whole project
  * [data-mediator-compiler](https://github.com/LightSun/data-mediator/tree/master/data-mediator)  the core compiler lib of whole project
  * [data-mediator-annotations](https://github.com/LightSun/data-mediator/tree/master/data-mediator)  the annotation lib of whole project
  * [Data-mediator-demo/data-mediator-android](https://github.com/LightSun/data-mediator/tree/master/Data-mediator-demo/data-mediator-android)  support lib on android platform of data-mediator
  * [data-mediator-support-gson](https://github.com/LightSun/data-mediator/tree/master/data-mediator-support-gson) the gson support lib 
* Plugin
  * [data-mediator-intellij-plugin](https://github.com/LightSun/data-mediator/tree/master/data-mediator-intellij-plugin)  the idea plugin of data-mediator
  * [data-mediator-convertor-intellij-plugin](https://github.com/LightSun/data-mediator/tree/master/data-mediator-convertor-intellij-plugin)  the idea plugin of data-mediator,  used to convert java bean to data module of this framework. 