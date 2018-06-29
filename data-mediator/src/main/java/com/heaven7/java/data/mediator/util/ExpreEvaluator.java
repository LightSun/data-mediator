package com.heaven7.java.data.mediator.util;

/**
 * the expression evaluator
 * Created by heaven7 on 2018/6/29.
 * @since 1.4.5
 */
public interface ExpreEvaluator {

    Object evaluate(String expression, ExpreContext context);

    interface ExpreContext{
        Class<?> resolveClassname(String simpleName);
        Object resolveVariable(String alas);//care about java.lang.xxx.
    }
}
