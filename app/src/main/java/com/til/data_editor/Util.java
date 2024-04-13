package com.til.data_editor;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

public class Util {

    public static Class<?> tryGetClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return tryGetClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = tryGetClass(componentType);
            return Array.newInstance(componentClass, 0).getClass();
        }
        return null;
    }

    public static boolean isIntegerType(Class<?> numberClass) {
        return numberClass.equals(Byte.class) || numberClass.equals(byte.class) ||
               numberClass.equals(Short.class) || numberClass.equals(short.class) ||
               numberClass.equals(Integer.class) || numberClass.equals(int.class) ||
               numberClass.equals(Long.class) || numberClass.equals(long.class);
    }

    public static boolean isFloatType(Class<?> numberClass) {
        return numberClass.equals(Float.class) || numberClass.equals(float.class) ||
               numberClass.equals(Double.class) || numberClass.equals(double.class);
    }

    public static boolean isUndetermined(Type type) {
        if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = ((ParameterizedType) type);

            if (isUndetermined(parameterizedType.getRawType())) {
                return true;
            }

            for (Type _type : parameterizedType.getActualTypeArguments()) {
                if (isUndetermined(_type)) {
                    return true;
                }
            }
        }
        if (type instanceof GenericArrayType) {
            return isUndetermined(((GenericArrayType) type).getGenericComponentType());
        }
        if (type instanceof TypeVariable) {
            /*for (Type bound : ((TypeVariable<?>) type).getBounds()) {
                if (isUndetermined(bound)) {
                    return true;
                }
            }*/
            return true;
        }


        if (type instanceof Class) {
            Class<?> clazz = ((Class<?>) type);
            if (clazz == Object.class) {
                return false;
            }
            if (isFloatType(clazz) || isIntegerType(clazz)) {
                return false;
            }
            if (clazz == List.class || clazz == Map.class) {
                return false;
            }
            if (Modifier.isAbstract(clazz.getModifiers())) {
                return true;
            }
        }

        return false;

    }

    public static ParameterizedType mappingParameterizedType(ParameterizedType basics, Type clazzType) {


        Class<?> clazz = Util.tryGetClass(clazzType);

        Type[] _actualTypeArguments = basics.getActualTypeArguments();
        Type[] mappingActualTypeArguments = new Type[_actualTypeArguments.length];

        TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
        Type[] actualTypeArguments = new Type[0];
        if (clazzType instanceof ParameterizedType) {
            actualTypeArguments = ((ParameterizedType) clazzType).getActualTypeArguments();
        }

        for (int i = 0; i < _actualTypeArguments.length; i++) {
            Type argument = _actualTypeArguments[i];
            if (argument instanceof TypeVariable) {

                for (int ii = 0; ii < typeParameters.length; ii++) {
                    if (typeParameters[ii].equals(argument)) {
                        mappingActualTypeArguments[i] = actualTypeArguments[ii];
                        break;
                    }
                }
                if (mappingActualTypeArguments[i] == null) {
                    //TODO
                    throw new RuntimeException();
                }
            }
            mappingActualTypeArguments[i] = argument;
        }

        return new ParameterizedTypeImpl((Class<?>) basics.getRawType(), mappingActualTypeArguments, basics.getRawType());

    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int pxToDp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) px / density);
    }
}
