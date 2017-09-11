package com.heaven7.data.mediator.demo;

/**
 * Created by heaven7 on 2017/9/11 0011.
 */

public class FieldFlags {


    public static final int COMPLEXT_ARRAY = 1;
    public static final int COMPLEXT_LIST = 2;

    public static final int FLAG_TRANSIENT = 0x00000001;
    public static final int FLAG_VOLATILE = 0x00000002;

    public static final int FLAG_SNAP = 0x00000004;
    public static final int FLAG_SHARE = 0x00000008;
    public static final int FLAG_COPY = 0x00000010;//16
    public static final int FLAG_RESET = 0x00000020;//32

    public static final int FLAG_PARCEABLE = 0x00000080; //128

    /**
     * @ Expose : serialize, deserialize, default is true
     */
    public static final int FLAG_EXPOSE_DEFAULT = 0x00000100;//256
    /**
     * @ Expose : serialize = false
     */
    public static final int FLAG_EXPOSE_SERIALIZE_FALSE = 0x00000200;//512
    /**
     * @ Expose : deserialize = false
     */
    public static final int FLAG_EXPOSE_DESERIALIZE_FALSE = 0x00000400;//1024

}
