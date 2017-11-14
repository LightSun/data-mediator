package com.heaven7.plugin.data.mediator.convertor;


public class Util {

    static void checkArgument(boolean condition, String format, Object... args) {
        if (!condition) throw new IllegalArgumentException(String.format(format, args));
    }

    public static void logError(Object... objs) {
        log(true, objs);
    }
    public static void log(Object... objs) {
        log(false, objs);
    }
    private static void log(boolean error, Object... objs) {
        StringBuilder sb = new StringBuilder();
        if (objs != null) {
            for (Object obj : objs) {
                sb.append(obj != null ? obj.toString() : null);
                sb.append("\r\n");
            }
        }
        if (error) {
            System.err.print(sb.toString());
        } else {
            System.out.print(sb.toString());
        }
    }
}
