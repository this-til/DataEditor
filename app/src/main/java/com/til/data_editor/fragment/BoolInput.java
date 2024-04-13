package com.til.data_editor.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.widget.CheckBox;
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
public class BoolInput extends FieldFragment {


    protected CheckBox checkBox;

    public BoolInput(Type type) {
        super(type);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkBox = new CheckBox(requireContext());
        checkBox.setId(View.generateViewId());
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dpToPx(requireContext(), 30)));
        return checkBox;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            update = true;
        });
    }

    @Override
    public Object get() {
        return checkBox.isChecked();
    }

    @Override
    public void set(Object obj) {
        checkBox.setChecked((boolean) obj);
    }

}