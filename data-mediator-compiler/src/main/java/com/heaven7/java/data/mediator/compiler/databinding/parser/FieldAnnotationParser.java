package com.heaven7.java.data.mediator.compiler.databinding.parser;

import com.heaven7.java.data.mediator.compiler.DataBindingInfo;
import com.heaven7.java.data.mediator.compiler.DataBindingParser;

import javax.lang.model.element.Element;

/**
 * the field annotation parser.
 * @author heaven7
 * @since 1.4.0
 */
public interface FieldAnnotationParser {
    boolean parse(Element element, DataBindingParser parser, DataBindingInfo info);
}