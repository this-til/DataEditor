package com.til.data_editor.factory;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.Util;
import com.til.data_editor.fragment.ArrayInput;
import com.til.data_editor.fragment.FieldFragment;
import com.til.data_editor.fragment.ObjectInput;

import java.lang.reflect.Type;
import java.util.List;

public class ObjectInputFactory extends FragmentFactory {

    @Nullable
    @Override
    public FieldFragment mack(Type type, FragmentActivity activity) {
        return new ObjectInput(type);
    }

    @Override
    public int getPriority() {
        return -10000;
    }
}
