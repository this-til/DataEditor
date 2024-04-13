package com.til.data_editor.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.til.data_editor.R;
import com.til.data_editor.Util;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * @author til
 */
public class StringInput extends FieldFragment {


    protected EditText input;

    protected boolean update;

    public StringInput(Type type) {
        super(type);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        input = new EditText(requireContext());
        input.setId(View.generateViewId());
        input.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dpToPx(requireContext(), 30)));
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        return input;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                update = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Class<?> clazz = Util.tryGetClass(type);

        if (clazz == String.class) {
            return;
        }

        if (Util.isIntegerType(clazz)) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            return;
        }
        if (Util.isFloatType(clazz)) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

    }

    @Override
    public void set(Object obj) {
        input.setText(obj.toString());
    }

    @Override
    public Object get() {
        String v = input.getText().toString();

        Class<?> type = Util.tryGetClass(this.type);
        if (type == String.class) {
            return v;
        }

        if (type == Byte.class) {
            return Byte.valueOf(v);
        }
        if (type == byte.class) {
            return Byte.parseByte(v);
        }

        if (type == Short.class) {
            return Short.valueOf(v);
        }
        if (type == short.class) {
            return Short.parseShort(v);
        }

        if (type == Integer.class) {
            return Integer.valueOf(v);
        }
        if (type == int.class) {
            return Integer.parseInt(v);
        }

        if (type == Long.class) {
            return Long.valueOf(v);
        }
        if (type == long.class) {
            return Long.parseLong(v);
        }


        if (type == Float.class) {
            return Float.valueOf(v);
        }
        if (type == float.class) {
            return Float.parseFloat(v);
        }

        if (type == Double.class) {
            return Float.valueOf(v);
        }
        if (type == double.class) {
            return Float.parseFloat(v);
        }

        return null;
    }

}