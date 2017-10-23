package com.heaven7.plugin.idea.data_mediator;


public final class FieldFlags {
    public static final int COMPLEX_ARRAY         = 1;
    public static final int COMPLEX_LIST          = 2;

    public static final int COMPLEX_SPARSE_ARRAY  =  3;

    public static final int FLAG_TRANSIENT = 0x00000001;

    /**
     * a field flag that indicate the field is volatile.
     * eg: <pre> private volatile int test_int;</pre>
     */
    public static final int FLAG_VOLATILE = 0x00000002;

    public static final int FLAG_SNAP     = 0x00000004;
    public static final int FLAG_SHARE    = 0x00000008;
    public static final int FLAG_COPY     = 0x00000010;//16
    public static final int FLAG_RESET    = 0x00000020;//32

    public static final int FLAG_TO_STRING = 0x00000040; //64

    public static final int FLAG_PARCELABLE = 0x00000080; //128

    public static final int FLAG_EXPOSE_DEFAULT = 0x00000100;//256
    public static final int FLAG_EXPOSE_SERIALIZE_FALSE = 0x00000200;//512
    public static final int FLAG_EXPOSE_DESERIALIZE_FALSE = 0x00000400;//1024

    public static final int FLAG_HASH_EQUALS              = 0x00000800; //2048
    public static final int FLAG_GSON_PERSISTENCE         = 0x00001000;

}
