package com.til.data_editor.factory;

import android.app.Activity;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.Util;
import com.til.data_editor.fragment.ArrayInput;
import com.til.data_editor.fragment.FieldFragment;

import java.lang.reflect.Type;
import java.util.List;

public class ArrayInputFactory extends FragmentFactory {

    @Nullable
    @Override
    public FieldFragment mack(Type type, FragmentActivity activity) {
        Class<?> aClass = Util.tryGetClass(type);
        if (aClass != List.class) {
            return null;
        }
        return new ArrayInput(type);

    }


}
