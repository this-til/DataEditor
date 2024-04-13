package com.til.data_editor.factory;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.Util;
import com.til.data_editor.fragment.ArrayInput;
import com.til.data_editor.fragment.FieldFragment;
import com.til.data_editor.fragment.MapInput;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author til
 */
public class MapInputFactory extends FragmentFactory {

    @Nullable
    @Override
    public FieldFragment mack(Type type, FragmentActivity activity) {
        Class<?> aClass = Util.tryGetClass(type);
        if (aClass != Map.class) {
            return null;
        }
        return new MapInput(type);
    }


}
