package com.heaven7.java.data.mediator.processor;

import com.heaven7.java.data.mediator.FieldData;
import com.heaven7.java.data.mediator.TypeInterfaceFiller;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */

public class TypeResetableFiller extends TypeInterfaceFiller {

    @Override
    protected String getInterfaceName() {
        return Util.NAME_RESET;
    }
    @Override
    protected int getInterfaceFlag() {
        return FieldData.FLAG_RESET;
    }
}
