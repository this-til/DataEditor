package com.til.data_editor.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.R;
import com.til.data_editor.Util;
import com.til.data_editor.factory.FragmentFactoryManage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArrayInput extends FieldFragment {

    protected Type vType;
    protected List<ArrayCellPack> arrayCellPacks = new ArrayList<>();

    protected LinearLayout vLayout;


    public ArrayInput(Type type) {
        super(type);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vLayout = new LinearLayout(requireContext());
        vLayout.setId(View.generateViewId());
        vLayout.setOrientation(LinearLayout.VERTICAL);
        vLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        return vLayout;
    }

    @Override
    public Iterable<View> mackOtherView(Context context) {
        List<View> viewList = new ArrayList<>();

        Button add = new Button(context);
        add.setId(View.generateViewId());
        add.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(context, 30), Util.dpToPx(context, 30)));
        viewList.add(add);

        add.setOnClickListener(v -> {
            addCell();
            update = true;
        });


        return viewList;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* editText.setOnClickListener(v -> {
            fill(Integer.parseInt(v.toString()));
            update = true;
        });*/

        if (Util.isUndetermined(type)) {
            //TODO
            throw new RuntimeException();
        }
        if (type instanceof GenericArrayType) {
            vType = ((GenericArrayType) type).getGenericComponentType();
        }
        if (type instanceof ParameterizedType) {
            vType = ((ParameterizedType) type).getActualTypeArguments()[0];
        }

        if (vType == null) {
            //TODO
            throw new RuntimeException();
        }

    }

    protected void addCell() {
        ArrayCellPack arrayCellPack = new ArrayCellPack();

        LinearLayout linearLayout = new LinearLayout(requireActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setId(View.generateViewId());
        vLayout.addView(linearLayout);
        arrayCellPack.pack = linearLayout;

        FieldFragment fieldFragment = FragmentFactoryManage.DEF.mack(vType, requireActivity());
        arrayCellPack.fieldFragment = fieldFragment;

        Button button = new Button(requireActivity());
        button.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
        button.setForeground(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_reduce, null));
        button.setBackground(null);
        linearLayout.addView(button);
        button.setOnClickListener(v -> deleteCell(arrayCellPack.id));

        if (fieldFragment.canExpanded()) {

            LinearLayout _linearLayout = new LinearLayout(requireContext());
            _linearLayout.setId(View.generateViewId());
            _linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            _linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(_linearLayout);

            LinearLayout __linearLayout = new LinearLayout(requireContext());
            __linearLayout.setId(View.generateViewId());
            __linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            __linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            _linearLayout.addView(__linearLayout);

            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setId(View.generateViewId());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
            __linearLayout.addView(checkBox);
            checkBox.setChecked(true);

            LinearLayout otherViewlinearLayout = null;


            for (View otherView : fieldFragment.mackOtherView(requireContext())) {

                if (otherViewlinearLayout == null) {
                    otherViewlinearLayout = new LinearLayout(requireContext());
                    otherViewlinearLayout.setId(View.generateViewId());
                    otherViewlinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    otherViewlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    __linearLayout.addView(otherViewlinearLayout);
                }

                otherViewlinearLayout.addView(otherView);
            }

            final LinearLayout finalOtherViewlinearLayout = otherViewlinearLayout;
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                fieldFragment.requireView().setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (finalOtherViewlinearLayout != null) {
                    finalOtherViewlinearLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                }
            });

            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(_linearLayout.getId(), fieldFragment);
            fragmentTransaction.commit();
        } else {


            for (View view : fieldFragment.mackOtherView(requireContext())) {
                linearLayout.addView(view);
            }

            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(linearLayout.getId(), fieldFragment);
            fragmentTransaction.commit();

        }


        arrayCellPacks.add(arrayCellPack);
        upId();

    }

    public void upId() {
        for (int i = 0; i < arrayCellPacks.size(); i++) {
            arrayCellPacks.get(i).id = i;
        }
    }

    protected void deleteCell(int at) {
        if (at < 0 || at >= arrayCellPacks.size()) {
            return;
        }
        ArrayCellPack arrayCellPack = arrayCellPacks.get(at);

        arrayCellPack.fieldFragment.onDestroy();
        vLayout.removeView(arrayCellPack.pack);
        arrayCellPacks.remove(at);

        upId();
    }

    protected void fill(int a) {
        for (int i = a; i < arrayCellPacks.size(); i++) {
            deleteCell(i);
            i--;
        }
        for (int i = arrayCellPacks.size(); i < a; i++) {
            addCell();
        }
    }

    @Override
    public boolean withUpdate() {
        boolean u = false;

        for (ArrayCellPack arrayCellPack : arrayCellPacks) {
            u |= arrayCellPack.fieldFragment.withUpdate();
        }

        return u || super.withUpdate();
    }


    @Override
    public boolean canExpanded() {
        return true;
    }

    @Override
    public void set(Object obj) {
        if (obj == null) {
            fill(0);
            return;
        }
        List<?> list = ((List<?>) obj);
        fill(list.size());
        for (int i = 0; i < list.size(); i++) {
            arrayCellPacks.get(i).fieldFragment.set(list.get(i));
        }
    }

    @Override
    public Object get() {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (ArrayCellPack arrayCellPack : arrayCellPacks) {
            arrayList.add(arrayCellPack.fieldFragment.get());
        }
        return arrayList;
    }

    protected static class ArrayCellPack {

        public FieldFragment fieldFragment;
        public View pack;
        public int id;

    }


}
