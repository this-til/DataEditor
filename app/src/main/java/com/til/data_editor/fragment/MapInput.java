package com.til.data_editor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.text.input.PlatformTextInput;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.ParameterizedTypeImpl;
import com.til.data_editor.R;
import com.til.data_editor.Util;
import com.til.data_editor.factory.FragmentFactoryManage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MapInput extends FieldFragment {

    protected Type kType;
    protected Type vType;

    protected List<MapEntryPack> mapEntryPackList = new ArrayList<>();

    protected CheckBox checkbox;
    protected LinearLayout vLayout;
    protected Button add;

    public MapInput(Type type) {
        super(type);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_array_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                vLayout.setVisibility(View.VISIBLE);
            } else {
                vLayout.setVisibility(View.GONE);
            }
        });

        add.setOnClickListener(v -> {
            addCell();
            update = true;
        });


        if (Util.isUndetermined(type)) {
            //TODO
            throw new RuntimeException();
        }

        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        kType = actualTypeArguments[0];
        vType = actualTypeArguments[1];


    }

    protected void addCell() {
        FieldFragment fieldFragment = FragmentFactoryManage.DEF.mack(new ParameterizedTypeImpl(KV.class, new Type[]{kType, vType}, null), requireActivity());

        LinearLayout linearLayout = new LinearLayout(requireActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setId(View.generateViewId());
        vLayout.addView(linearLayout);


        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(linearLayout.getId(), fieldFragment);
        fragmentTransaction.commit();

        Button button = new Button(requireActivity());
        button.setLayoutParams(new LinearLayout.LayoutParams(Util.dpToPx(requireActivity(), 30), Util.dpToPx(requireActivity(), 30)));
        button.setBackground(null);
        button.setForeground(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_reduce, null));

        int id = mapEntryPackList.size();
        button.setOnClickListener(v -> {
            deleteCell(id);
        });
        linearLayout.addView(button);

        mapEntryPackList.add(new MapEntryPack(fieldFragment, linearLayout));
    }

    protected void deleteCell(int at) {
        if (at < 0 || at >= mapEntryPackList.size()) {
            return;
        }
        MapEntryPack mapEntryPack = mapEntryPackList.get(at);

        mapEntryPack.fieldFragment.onDestroy();
        vLayout.removeView(mapEntryPack.pack);
        mapEntryPackList.remove(at);
    }

    protected void fill(int a) {
        for (int i = a; i < mapEntryPackList.size(); i++) {
            deleteCell(i);
            i--;
        }
        for (int i = mapEntryPackList.size(); i < a; i++) {
            addCell();
        }
    }

    @Override
    public boolean withUpdate() {
        boolean u = false;

        for (MapEntryPack mapEntryPack : mapEntryPackList) {
            u |= mapEntryPack.fieldFragment.withUpdate();
        }

        return u || super.withUpdate();
    }

    @Override
    public void set(Object obj) {
        if (obj == null) {
            fill(0);
            return;
        }


        //noinspection unchecked
        Map<Object, Object> map = ((Map<Object, Object>) obj);

        fill(map.size());

        List<Map.Entry<Object, Object>> collect = new ArrayList<>(map.entrySet());

        for (int i = 0; i < collect.size(); i++) {
            mapEntryPackList.get(i).fieldFragment.set(new KV(collect.get(i).getKey(), collect.get(i).getValue()));
        }

    }

    @Override
    public Object get() {
        HashMap<Object, Object> hasMap = new HashMap<>();

        for (MapEntryPack mapEntryPack : mapEntryPackList) {
            //noinspection unchecked
            KV<Object, Object> o = ((KV<Object, Object>) mapEntryPack.fieldFragment.get());
            hasMap.put(o.k, o.v);
        }

        return hasMap;
    }

    public static class MapEntryPack {

        public final FieldFragment fieldFragment;
        public final View pack;

        public MapEntryPack(FieldFragment fieldFragment, View pack) {
            this.fieldFragment = fieldFragment;
            this.pack = pack;
        }
    }

    public static class KV<K, V> {
        public K k;
        public V v;

        public KV(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }
}
