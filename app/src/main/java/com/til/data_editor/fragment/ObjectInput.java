package com.til.data_editor.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.R;
import com.til.data_editor.SerializeField;
import com.til.data_editor.Util;
import com.til.data_editor.factory.FragmentFactoryManage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author til
 */
public class ObjectInput extends FieldFragment {

    protected LinearLayout vLayout;

    protected List<FieldPack> fieldPackList = new ArrayList<>();

    public ObjectInput(Type type) {
        super(type);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vLayout = new LinearLayout(requireContext());
        vLayout.setOrientation(LinearLayout.VERTICAL);
        vLayout.setId(View.generateViewId());
        vLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return vLayout;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Class<?> clazz = Util.tryGetClass(type);

        if (Util.isUndetermined(type)) {
            //TODO
            throw new RuntimeException();
        }

        for (Field declaredField : clazz.getDeclaredFields()) {

            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }

            SerializeField annotation = declaredField.getAnnotation(SerializeField.class);

            if (annotation == null) {
                continue;
            }

            Type _type = declaredField.getGenericType();

            if (_type instanceof ParameterizedType) {
                _type = Util.mappingParameterizedType((ParameterizedType) _type, type);
            }

            FieldFragment fieldFragment = FragmentFactoryManage.DEF.mack(_type, requireActivity());

            if (fieldFragment == null) {
                continue;
            }


            if (fieldFragment.canExpanded()) {
                LinearLayout linearLayout = new LinearLayout(requireContext());
                linearLayout.setId(View.generateViewId());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                vLayout.addView(linearLayout);

                CheckBox checkBox = new CheckBox(requireContext());
                checkBox.setId(View.generateViewId());
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
                checkBox.setChecked(true);
                linearLayout.addView(checkBox);

                LinearLayout _linearLayout = new LinearLayout(requireContext());
                _linearLayout.setId(View.generateViewId());
                _linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                _linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(_linearLayout);

                LinearLayout __linearLayout = new LinearLayout(requireContext());
                __linearLayout.setId(View.generateViewId());
                __linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                __linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                _linearLayout.addView(__linearLayout);

                TextView textView = new TextView(requireContext());
                textView.setId(View.generateViewId());
                textView.setText(declaredField.getName());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dpToPx(requireContext(), 30));
                params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                textView.setLayoutParams(params);
                __linearLayout.addView(textView);

                View _view = new View(requireContext());
                _view.setId(View.generateViewId());
                _view.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
                linearLayout.addView(_view);

                LinearLayout otherViewlinearLayout = null;
                for (View otherView : fieldFragment.mackOtherView(requireContext())) {
                    if (otherViewlinearLayout == null) {
                        otherViewlinearLayout = new LinearLayout(requireContext());
                        otherViewlinearLayout.setId(View.generateViewId());
                        otherViewlinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        otherViewlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        __linearLayout.addView(otherViewlinearLayout);

                        View blankFill = new View(requireContext());
                        blankFill.setId(View.generateViewId());
                        blankFill.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
                        otherViewlinearLayout.addView(blankFill);
                    }
                    otherViewlinearLayout.addView(otherView);
                }

                LinearLayout finalOtherViewlinearLayout = otherViewlinearLayout;
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    fieldFragment.requireView().setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    if (finalOtherViewlinearLayout != null) {
                        finalOtherViewlinearLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                });

                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(_linearLayout.getId(), fieldFragment);
                fragmentTransaction.commit();
                continue;
            }

            LinearLayout linearLayout = new LinearLayout(requireContext());
            linearLayout.setId(View.generateViewId());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            vLayout.addView(linearLayout);

            /*View _view = new View(requireContext());
            _view.setId(View.generateViewId());
            _view.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
            linearLayout.addView(_view);*/

            TextView textView = new TextView(requireContext());
            textView.setId(View.generateViewId());
            textView.setText(declaredField.getName());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dpToPx(requireContext(), 30));
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            textView.setLayoutParams(params);
            linearLayout.addView(textView);

            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(linearLayout.getId(), fieldFragment);
            fragmentTransaction.commit();

            fieldPackList.add(new FieldPack(declaredField, fieldFragment, _type));
        }

    }


    @Override
    public boolean withUpdate() {
        boolean u = false;
        for (FieldPack fieldPack : fieldPackList) {
            u |= fieldPack.fieldFragment.withUpdate();
        }
        return u;
    }

    @Override
    public void set(Object obj) {
        if (obj == null) {
            Class<?> clazz = Util.tryGetClass(type);
            Object o;
            try {
                o = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            set(o);
        }
        for (FieldPack fieldPack : fieldPackList) {
            Object o;
            try {
                fieldPack.field.setAccessible(true);
                o = fieldPack.field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            fieldPack.fieldFragment.set(o);
        }
    }

    @Override
    public Object get() {

        Class<?> clazz = Util.tryGetClass(type);
        Object o;
        try {
            o = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (FieldPack fieldPack : fieldPackList) {
            Object extract = fieldPack.fieldFragment.get();

            try {
                fieldPack.field.setAccessible(true);
                fieldPack.field.set(o, extract);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        return o;
    }

    @Override
    public boolean canExpanded() {
        return true;
    }

    public static class FieldPack {

        public final Field field;
        public final FieldFragment fieldFragment;
        public final Type type;

        public FieldPack(Field field, FieldFragment fieldFragment, Type type) {
            this.field = field;
            this.fieldFragment = fieldFragment;
            this.type = type;
        }
    }
}
