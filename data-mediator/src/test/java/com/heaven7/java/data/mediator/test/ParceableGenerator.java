package com.heaven7.java.data.mediator.test;

import com.squareup.javapoet.*;
import junit.framework.TestCase;

import javax.lang.model.element.Modifier;

/**
 *   public void writeToParcel(Parcel dest, int flags) {
 dest.writeInt(this.id);
 dest.writeString(this.title);
 dest.writeTypedList(fileList);
 dest.writeList(this.fileIdList);
 dest.writeString(this.extra);
 }

 protected CaseRecordEntity(Parcel in) {
 this.id = in.readInt();
 this.title = in.readString();
 this.fileList = in.createTypedArrayList(FileEntity.CREATOR);
 this.fileIdList = new ArrayList<Integer>();
 in.readList(this.fileIdList, Integer.class.getClassLoader());
 this.extra = in.readString();
 }
 * Created by heaven7 on 2017/9/7.
 */
public class ParceableGenerator{

    private String  mClassname = "TestBean";
    private String  mTargetPackage = "com.heaven7";
    private ClassName creator = ClassName.get("com.heaven7.java.data.mediator.test",
            "Parcelable", "Creator");
    private ClassName parcel = ClassName.get("com.heaven7.java.data.mediator.test",
            "Parcel");
    private ClassName parcelable = ClassName.get("com.heaven7.java.data.mediator.test",
            "Parcelable");

    public static void main(String[] args){
        ParceableGenerator generator = new ParceableGenerator();
        generator.mockGenerateCreatorField();
        //generator.mockGenerateMethods();
    }

    void mockGenerateMethods(){

        /**
         * protected HistoryData(Parcel in) {
         * }
         * note: may have super.
         */
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(parcel, "in")
                .addStatement("super(in)")
                .addStatement("this.age = in.readInt()")
                .addStatement("this.id = in.readLong()")
                .addStatement("this.testByte = in.readByte()")
                .addStatement("this.testBoolean = in.readByte() != 0")
                .addStatement("this.testFloat = in.readFloat()")
                .addStatement("this.testDouble = in.readDouble()")
                .addStatement("this.testChar = (char) in.readInt()")
                .addStatement("this.name = in.readString()")
                .addStatement("this.data = in.readParcelable(ResultData.class.getClassLoader())")
                .addStatement("this.datas = in.createTypedArrayList(ResultData.CREATOR)")
                .build();

        /**
         * @Override
        public int describeContents() {
        return 0;
        }
         note: may have super.
         */
        MethodSpec describeContents = MethodSpec.methodBuilder("describeContents")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.INT)
                .addStatement("return 0")
                .build();

        /**
         *
         @Override
         public void writeToParcel(Parcel dest, int flags) {
         super.writeToParcel(dest, flags);
         dest.writeInt(this.age);
         dest.writeLong(this.id);
         dest.writeInt(this.testShort);
         dest.writeByte(this.testByte);
         dest.writeByte(this.testBoolean ? (byte) 1 : (byte) 0);
         dest.writeFloat(this.testFloat);
         dest.writeDouble(this.testDouble);
         dest.writeInt(this.testChar);
         dest.writeString(this.name);
         dest.writeParcelable(this.data, flags);
         dest.writeTypedList(this.datas);
         }
         */
        MethodSpec writeToParcel = MethodSpec.methodBuilder("writeToParcel")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addParameter(parcel, "dest")
                .addParameter(TypeName.INT, "flags")
                .addStatement("super.writeToParcel(dest,flags)") //optional
                .addStatement("dest.writeInt(this.age)")
                .addStatement("dest.writeLong(this.id)")
                .addStatement("dest.writeInt(this.testShort)")
                .addStatement("dest.writeByte(this.testByte)")
                .addStatement("dest.writeByte(this.testBoolean ? (byte) 1 : (byte) 0)")
                .addStatement("dest.writeFloat(this.testFloat)")
                .addStatement("dest.writeDouble(this.testDouble)")
                .addStatement("dest.writeInt(this.testChar)")
                .addStatement("dest.writeString(this.name)")
                .addStatement("dest.writeParcelable(this.data, flags)")
                .addStatement("dest.writeTypedList(this.datas)")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(mClassname)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(parcelable)
                .addMethod(constructor)
                .addMethod(describeContents)
                .addMethod(writeToParcel)
                .build();

        String test = JavaFile.builder(mTargetPackage, typeSpec).build().toString();
        System.out.println(test);
    }

    /**
     * generate the CREATOR field for parcel
     */
    private void mockGenerateCreatorField() {
        String className = mClassname;
        String targetPackage = mTargetPackage;

        ClassName obj = ClassName.get(targetPackage, className);

        // ClassName creator = ClassName.get("android.os", "Parcelable", "Creator");
        //  ClassName parcel = ClassName.get("android.os", "Parcel");

        TypeName creatorOfobj = ParameterizedTypeName.get(creator, obj);//泛型

        //方法参数。
        ParameterSpec sizeParam = ParameterSpec.builder(TypeName.INT, "size")
                .build();
        //return 匿名对象
        TypeSpec aSimpleThung = TypeSpec.anonymousClassBuilder(
                       "" //匿名无参构造
                   )
                .superclass(creatorOfobj)
                /**
                 *   @Override
                public CaseRecordEntity createFromParcel(Parcel source) {
                return new CaseRecordEntity(source);
                }
                 */
                .addMethod(MethodSpec.methodBuilder("createFromParcel")
                        .addAnnotation(Override.class)
                        .returns(obj)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(parcel, "in")
                        .addCode("/* code snippets */\n")
                        .addStatement("return new $T(in)", obj)
                        .build())
                /**
                 *  @Override
                    public CaseRecordEntity[] newArray(int size) {
                        return new CaseRecordEntity[size];
                    }
                 */
                .addMethod(MethodSpec.methodBuilder("newArray")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(ArrayTypeName.of(obj))
                        .addParameter(sizeParam)
                        .addStatement("return new $T[size]", obj)
                        .build())
                .build();

        TypeSpec taco = TypeSpec.classBuilder(className)
                .addField(FieldSpec.builder(creatorOfobj, "CREATOR")
                        .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                        .initializer("$L", aSimpleThung)
                        .build())
                .build();
        System.out.println(JavaFile.builder(targetPackage, taco)
                .build()
                .toString());
    }
}
