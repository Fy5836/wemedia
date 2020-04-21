package com.wemedia;

import java.lang.reflect.Method;

public class MethodTest {

    @MethodAnnotation(key = "key", value = "value")
    public void test() {}

    public static void main(String[] args) throws Exception {
        Method method = MethodTest.class.getDeclaredMethod("test");
        MethodAnnotation annotation = method.getAnnotation(MethodAnnotation.class);
        //@com.wemedia.MethodAnnotation(key=key, value=value)
        System.out.println(annotation);
    }
}