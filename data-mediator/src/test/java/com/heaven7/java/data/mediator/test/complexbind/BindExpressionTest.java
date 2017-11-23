package com.heaven7.java.data.mediator.test.complexbind;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BindExpressionTest<T> extends TestCase{
    int $dfsf;
    int $df$sf$4;

    private static final Pattern REG_NODE_BIND = Pattern.compile("[\\[]\\d+[]]$");
    static final Pattern SIMPLE =  Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??$");
    private static final Pattern PATTERN_NAMES = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??" +
            "([\\.][A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??$)");


    @Test
    public void testNodeBind(){
        assertTrue(REG_NODE_BIND.matcher("[1]").find());
        assertTrue(REG_NODE_BIND.matcher("sdfsfd[1]").find());
        assertFalse(REG_NODE_BIND.matcher("sdf[1]f").find());
        assertFalse(REG_NODE_BIND.matcher("[1]f").find());

        Matcher matcher = REG_NODE_BIND.matcher("sdfsfd[1]");
        if(matcher.find()){
            System.out.println(matcher.group());
        }

        Integer integer = Integer.valueOf("05");
        System.out.println(integer);
    }

    @Test
    public void test(){
        assertTrue(SIMPLE.matcher("stus$[0]").find());
        assertTrue(SIMPLE.matcher("$st_us$[0]").find());
        assertTrue(SIMPLE.matcher("stu_$s$[0]").find());
        assertTrue(SIMPLE.matcher("stu$s$").find());
        assertTrue(SIMPLE.matcher("stu$s$[0]").find());

        assertFalse(SIMPLE.matcher("stu$s$[0").find());
        assertFalse(SIMPLE.matcher("stu$s$0]").find());

        assertFalse(SIMPLE.matcher("stu_$s$[0][1]").find());
        assertFalse(SIMPLE.matcher("[6]s_tu$s$[0]").find());
        assertFalse(SIMPLE.matcher("stu$s$[0]e234").find());
        assertFalse(SIMPLE.matcher("3stu$s$[0]e234").find());

        String[] okList = {
                "stus$[0].name",
                "stus[0].name[2]",
        };
        for(String str : okList){
            assertTrue(PATTERN_NAMES.matcher(str).find());
        }
        String[] wrongList = {
                "stus[0].na[1]me",
                "stus[0].[1]name",
                "stus[0].name[1][2]",
        };
        for(String str : wrongList){
            System.out.println("str = " + str) ;
            assertFalse(PATTERN_NAMES.matcher(str).find());
        }
    }


}
