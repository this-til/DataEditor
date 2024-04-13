package com.til.data_editor.factory;

import android.app.Activity;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.til.data_editor.fragment.FieldFragment;

import java.lang.reflect.Type;

/**
 * @author til
 */
public abstract class FragmentFactory {

    @Nullable
    public abstract FieldFragment mack(Type type, FragmentActivity activity);

    public int getPriority() {
        return 0;
    }

}
