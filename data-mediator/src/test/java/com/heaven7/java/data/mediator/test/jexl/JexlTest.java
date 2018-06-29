package com.heaven7.java.data.mediator.test.jexl;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

public class JexlTest {

    public static void main(String[] args) {
        // Create or retrieve an engine
        JexlEngine jexl = new JexlEngine();

        // Create an expression
        String jexlExp = "Convertor.convert(foo.bar())"; //wrong
        Expression e = jexl.createExpression(jexlExp);

        // Create a context and add data
        JexlContext jc = new MapContext();
        jc.set("foo", new Foo());
        jc.set("Convertor","com.heaven7.java.data.mediator.test.jexl.Convertor");

        // Now evaluate the expression, getting the result
        Object o = e.evaluate(jc);
        System.out.println(0);
    }

    public static class Foo {

        public String bar(){
            return "Hello Jxel";
        }
    }

    public static class Convertor {

        public static String convert(String input) {
            return input + "__Convertor";
        }
    }
}
