package com.heaven7.java.data.mediator.util;

import com.heaven7.java.base.util.SparseArray;

/**
 * the expression evaluator
 * Created by heaven7 on 2018/6/29.
 * @since 1.4.5
 */
public interface ExpreEvaluator {

    Object evaluate(ExpreContext context, String expression);

    interface ExpreContext{
        void addStaticClass(String alias, Class<?> clazz);
        void addVariable(String alias, Object obj);

        Class<?> resolveClass(String alias);
        Object resolveVariable(String alias);//care about java.lang.xxx.
    }

    /**
     * a simple expression context that impl {@linkplain com.heaven7.java.data.mediator.util.ExpreEvaluator.ExpreContext}.
     * @author heaven7
     */
    class SimpleExpreContext implements ExpreEvaluator.ExpreContext {
        final SparseArray<Class<?>> staticMap = new SparseArray<>(3);
        final SparseArray<Object> varMap = new SparseArray<>(3);

        @Override
        public void addStaticClass(String alias, Class<?> clazz) {
            staticMap.put(alias.hashCode(), clazz);
        }
        @Override
        public void addVariable(String alias, Object obj){
            varMap.put(alias.hashCode(), obj);
        }
        @Override
        public Class<?> resolveClass(String alias) {
            return staticMap.get(alias.hashCode());
        }
        @Override
        public Object resolveVariable(String alas) {
            return varMap.get(alas.hashCode());
        }
    }

    class ExpreEvaluatorException extends RuntimeException{
        public ExpreEvaluatorException(String message) {
            super(message);
        }
        public ExpreEvaluatorException(String message, Throwable cause) {
            super(message, cause);
        }
        public ExpreEvaluatorException(Throwable cause) {
            super(cause);
        }
    }
}
