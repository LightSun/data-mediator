package com.heaven7.java.data.mediator.compiler;

import javax.lang.model.element.Element;

public interface CodeGeneratorProvider {

    CodeGenerator getCodeGenerator(Element element);
}
