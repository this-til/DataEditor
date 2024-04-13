package com.til.data_editor;

import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ReflectTest {


    @Test
    public void test() throws NoSuchFieldException {

        Type at = new TypeToken<A<String>>() {
        }.getType();

        Class<A> aClass = A.class;
        Field t = aClass.getDeclaredField("t");

        TypeVariable<Class<A>>[] typeParameters = aClass.getTypeParameters();

        Type genericType = t.getGenericType();

        boolean a = typeParameters[0].equals(genericType);

    }


    public static class A<T> {
        T t;
    }
}
