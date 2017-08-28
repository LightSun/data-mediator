package com.heaven7.java.data.mediator.processor;

import javax.annotation.processing.Filer;

/**
 * Created by heaven7 on 2017/8/28.
 */
public interface IJavaWriter {

    void writeTo(Filer filer);
}
