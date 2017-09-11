package com.heaven7.java.data.mediator;

/**
 * the all field flags for this framework.
 * Created by heaven7 on 2017/9/11 0011.
 */
public final class FieldFlags {

    /**
     * a complex type that indicate the field is a array.
     *  eg:
     *  <code><pre>
     *  {@literal @}Field(propName = "test_Integer_array", type = Integer.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
     *  </pre></code>
     *  this means the real type is 'Integer[] '.
     */
    public static final int COMPLEXT_ARRAY = 1;
    /**
     * a complex type that indicate the field is a list.
     *  eg:
     *  <code><pre>
     *  {@literal @}Field(propName = "test_Integer_array", type = Integer.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
     *  </pre></code>
     *  this means the real type is 'List{@literal <}Integer>'.
     */
    public static final int COMPLEXT_LIST = 2;

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
     * a scope flag. that means the field is parcelable which will be used by android.os.Parcelable.
     */
    public static final int FLAG_PARCEABLE = 0x00000080; //128

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
     * a complex flags. which have multi flags. {@linkplain #FLAG_SNAP},{@linkplain #FLAG_RESET},
     * {@linkplain #FLAG_SHARE}, {@linkplain #FLAG_COPY}, {@linkplain #FLAG_PARCEABLE}.
     * @see #FLAG_SNAP
     * @see #FLAG_RESET
     * @see #FLAG_SHARE
     * @see #FLAG_COPY
     * @see #FLAG_PARCEABLE
     */
    public static final int FLAGS_ALL_SCOPES = FLAG_SNAP | FLAG_RESET | FLAG_SHARE | FLAG_COPY | FLAG_PARCEABLE;
}
