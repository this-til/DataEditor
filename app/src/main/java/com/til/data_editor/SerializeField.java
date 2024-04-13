package com.til.data_editor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/**
 * @author til
 */

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeField {



}

