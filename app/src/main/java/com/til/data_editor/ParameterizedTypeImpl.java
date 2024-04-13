package com.til.data_editor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("restriction")
public class ParameterizedTypeImpl implements ParameterizedType {
    private Type[] actualTypeArguments;
    private Class<?> rawType;
    private Type ownerType;

    /**
     * 构造方法
     * 基于已有{@link ParameterizedType}实例构造一个新对象
     *
     * @param source 不可为{@link null}
     */
    public ParameterizedTypeImpl(ParameterizedType source) {
        this(TypeToken.of(checkNotNull(source)).getRawType(), source.getActualTypeArguments(), source.getOwnerType());
    }

    public ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType != null ? ownerType : rawType.getDeclaringClass();
        this.validateConstructorArguments();
    }

    private void validateConstructorArguments() {
        TypeVariable<?>[] formals = this.rawType.getTypeParameters();
        if (formals.length != this.actualTypeArguments.length) {
            throw new MalformedParameterizedTypeException();
        }
        for (int i = 0; i < this.actualTypeArguments.length; ++i) {
        }
    }

    @Override
    public Type[] getActualTypeArguments() {
        return (Type[]) this.actualTypeArguments.clone();
    }

    @Override
    public Class<?> getRawType() {
        return this.rawType;
    }

    @Override
    public Type getOwnerType() {
        return this.ownerType;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ParameterizedType) {
            ParameterizedType that = (ParameterizedType) o;
            if (this == that) {
                return true;
            }
            Type thatOwner = that.getOwnerType();
            Type thatRawType = that.getRawType();
            return (Objects.equals(this.ownerType, thatOwner))
                   && (Objects.equals(this.rawType, thatRawType))
                   && Arrays.equals(this.actualTypeArguments, that.getActualTypeArguments());
        }
        return false;
    }

    @Override

    public int hashCode() {
        return Arrays.hashCode(this.actualTypeArguments) ^ (this.ownerType == null ? 0 : this.ownerType.hashCode())
               ^ (this.rawType == null ? 0 : this.rawType.hashCode());
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.ownerType != null) {
            if (this.ownerType instanceof Class) {
                sb.append(((Class) this.ownerType).getName());
            } else {
                sb.append(this.ownerType.toString());
            }
            sb.append(".");
            if (this.ownerType instanceof ParameterizedTypeImpl) {
                sb.append(this.rawType.getName()
                        .replace(((ParameterizedTypeImpl) this.ownerType).rawType.getName() + "$", ""));
            } else {
                sb.append(this.rawType.getName());
            }
        } else {
            sb.append(this.rawType.getName());
        }
        if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for (Type t : this.actualTypeArguments) {
                if (!first) {
                    sb.append(", ");
                }
                if (t instanceof Class) {
                    sb.append(((Class<?>) t).getName());
                } else {
                    sb.append(t.toString());
                }
                first = false;
            }
            sb.append(">");
        }
        return sb.toString();
    }

    /**
     * 将当前对象的类型参数中为{@code oldType}的元素替换为{@code newType}
     *
     * @param oldType 不可为{@code null}
     * @param newType 不可为{@code null}
     * @return
     */
    public ParameterizedType transform(Type oldType, Type newType) {
        checkNotNull(oldType);
        checkNotNull(newType);
        Type[] typeArgs = getActualTypeArguments();
        for (int i = 0; i < typeArgs.length; ++i) {
            if (typeArgs[i] == oldType) {
                typeArgs[i] = newType;
            }
        }
        return new ParameterizedTypeImpl(TypeToken.of(this).getRawType(), typeArgs, getOwnerType());
    }

    /**
     * 用指定的类型参数替换当前对象的类型参数<br>
     * 新参数的个数与当前对象的类型参数个数必须一致,
     * 如果新参数数组中元素为{@code null}则对应的参数不会被替换
     *
     * @param newTypeArguments
     * @return
     */
    public ParameterizedType transform(Type[] newTypeArguments) {
        checkNotNull(newTypeArguments);
        Type[] typeArgs = getActualTypeArguments();
        checkArgument(newTypeArguments.length == typeArgs.length);
        for (int i = 0; i < typeArgs.length; ++i) {
            if (null != newTypeArguments[i]) {
                typeArgs[i] = newTypeArguments[i];
            }
        }
        return new ParameterizedTypeImpl(TypeToken.of(this).getRawType(), typeArgs, getOwnerType());
    }
}
