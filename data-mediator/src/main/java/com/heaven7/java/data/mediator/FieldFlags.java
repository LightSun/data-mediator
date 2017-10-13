/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator;

/**
 * the all field flags for this framework.
 * Created by heaven7 on 2017/9/11 0011.
 */
public final class FieldFlags {

    /**
     * use {@linkplain #COMPLEX_ARRAY} instead.
     */
    @Deprecated
    public static final int COMPLEXT_ARRAY = 1;
    /**
     * a complex type that indicate the field is a array.
     *  eg:
     *  <code><pre>
     *  {@literal @}Field(propName = "test_Integer_array", type = Integer.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
     *  </pre></code>
     *  this means the real type is 'Integer[] '.
     *  @since 1.1.3
     */
    public static final int COMPLEX_ARRAY = 1;
    /**
     * use {@linkplain #COMPLEX_LIST} instead.
     */
    @Deprecated
    public static final int COMPLEXT_LIST = 2;
    /**
     * a complex type that indicate the field is a list.
     *  eg:
     *  <code><pre>
     *  {@literal @}Field(propName = "test_Integer_array", type = Integer.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
     *  </pre></code>
     *  this means the real type is 'List{@literal <}Integer>'.
     *  @since 1.1.3
     */
    public static final int COMPLEX_LIST = 2;

    /**
     * a complex type that indicate the field is a sparse array..
     *  eg:
     *  <code><pre>
     *  {@literal @}Field(propName = "test_Integer_array", type = String.class, flags = FLAG_PARCEABLE, complexType = COMPLEX_SPARSE_ARRAY),
     *  </pre></code>
     *  this means the real type is 'SparseArray{@literal <}String>'.
     */
    public static final int COMPLEX_SPARSE_ARRAY  =  3;

    /**
     * a field flag that indicate 'transient' . this flag often used by {@linkplain java.io.Serializable}.
     * like: <pre>private transient Double test_double;</pre>
     */
    public static final int FLAG_TRANSIENT = 0x00000001;

    /**
     * a field flag that indicate the field is volatile.
     * eg: <pre> private volatile int test_int;</pre>
     */
    public static final int FLAG_VOLATILE = 0x00000002;

    /**
     * a scope flag. that means the field is snap which will be used by {@linkplain ISnapable}.
     */
    public static final int FLAG_SNAP     = 0x00000004;
    /**
     * a scope flag. that means the field is shareable which will be used by {@linkplain IShareable}.
     */
    public static final int FLAG_SHARE    = 0x00000008;
    /**
     * a scope flag. that means the field is copyable which will be used by {@linkplain ICopyable}.
     */
    public static final int FLAG_COPY     = 0x00000010;//16
    /**
     * a scope flag. that means the field is resetable which will be used by {@linkplain IResetable}.
     */
    public static final int FLAG_RESET    = 0x00000020;//32

    /**
     * a scope flag for toString method.
     * @since 1.0.5
     */
    public static final int FLAG_TO_STRING = 0x00000040; //64

    /**
     * a scope flag. that means the field is parcelable which will be used by android.os.Parcelable.
     */
    public static final int FLAG_PARCELABLE = 0x00000080; //128

    /**
     * a flag of third lib google-gson : that means the field will add annotation com.google.gson.annotations.Expose.
     * and default serialize = true,  deserialize = true. eg:
     * <pre>@Expose(
     serialize = true,
     deserialize = true
     )</pre>
     */
    public static final int FLAG_EXPOSE_DEFAULT = 0x00000100;//256
    /**
     * a flag of third lib google-gson : that means the field will add annotation com.google.gson.annotations.Expose.
     * and serialize = false, eg:
     * <pre>@Expose(
     serialize = false,
     deserialize = true //indicate by FLAG_EXPOSE_DESERIALIZE_FALSE
     )</pre>
     but, if the field have flag {@linkplain #FLAG_EXPOSE_SERIALIZE_FALSE} and {@linkplain #FLAG_EXPOSE_DESERIALIZE_FALSE} at the same time.
     the field will be annotated like this:
     <pre>@Expose(
     serialize = false,
     deserialize = false
     )</pre>
     */
    public static final int FLAG_EXPOSE_SERIALIZE_FALSE = 0x00000200;//512
    /**
     * a flag of third lib google-gson : that means the field will add annotation com.google.gson.annotations.Expose.
     * and deserialize = false, eg:
     * <pre>@Expose(
     serialize = true,
     deserialize = false
     )</pre>
     but, if the field have flag {@linkplain #FLAG_EXPOSE_SERIALIZE_FALSE} and {@linkplain #FLAG_EXPOSE_DESERIALIZE_FALSE} at the same time.
     the field will be annotated like this:
     <pre>@Expose(
     serialize = false,
     deserialize = false
     )</pre>
     */
    public static final int FLAG_EXPOSE_DESERIALIZE_FALSE = 0x00000400;//1024


    /**
     *  a scope flag of hashCode and equals method
     * @since 1.1.1
     */
    public static final int FLAG_HASH_EQUALS              = 0x00000800; //2048
    /**
     * a scope flag of gson serialize/deserialize json for field.
     * <p><h3>Note this only effect the auto generate TypeAdapter.</h3></p>
     * <p><h3>Note if this has effect on json, in priority, this is better than the Gson.setVersion(xxx). </h3></p>
     * @since 1.2.0
     */
    public static final int FLAG_GSON_PERSISTENCE         = 0x00001000;

    /**
     * a complex flags. which have multi flags. .
     * @see #FLAG_SNAP
     * @see #FLAG_RESET
     * @see #FLAG_SHARE
     * @see #FLAG_COPY
     * @see #FLAG_PARCELABLE
     * @see #FLAG_TO_STRING
     * @see #FLAG_HASH_EQUALS
     * @see #FLAG_GSON_PERSISTENCE
     */
    public static final int FLAGS_ALL_SCOPES = FLAG_SNAP | FLAG_RESET | FLAG_SHARE
            | FLAG_COPY | FLAG_PARCELABLE | FLAG_TO_STRING | FLAG_HASH_EQUALS | FLAG_GSON_PERSISTENCE;

    /**
     * a main complex flags . which have multi flags
     * @see #FLAG_COPY
     * @see #FLAG_PARCELABLE
     * @see #FLAG_TO_STRING
     * @see #FLAG_GSON_PERSISTENCE
     * @since 1.0.7
     */
    public static final int FLAGS_MAIN_SCOPES = FLAG_COPY | FLAG_PARCELABLE | FLAG_TO_STRING | FLAG_GSON_PERSISTENCE;
    /**
     * a complex flags . which means gson no expose
     * <pre>{@literal @}Expose(serialize = false,
     deserialize = false) </pre>
     * @see #FLAG_EXPOSE_DEFAULT
     * @see #FLAG_EXPOSE_SERIALIZE_FALSE
     * @see #FLAG_EXPOSE_DESERIALIZE_FALSE
     * @since 1.0.7
     */
    public static final int FLAGS_NO_EXPOSE = FLAG_EXPOSE_DEFAULT | FLAG_EXPOSE_SERIALIZE_FALSE | FLAG_EXPOSE_DESERIALIZE_FALSE;

    /**
     * a complex flags. which have multi flags. relative to {@linkplain #FLAGS_MAIN_SCOPES} add {@linkplain #FLAG_HASH_EQUALS}.
     * @see #FLAG_COPY
     * @see #FLAG_PARCELABLE
     * @see #FLAG_TO_STRING
     * @see #FLAG_GSON_PERSISTENCE
     * @see #FLAG_HASH_EQUALS
     * @since 1.1.1
     */
    public static final int FLAGS_MAIN_SCOPES_2 = FLAGS_MAIN_SCOPES | FLAG_HASH_EQUALS;
}
