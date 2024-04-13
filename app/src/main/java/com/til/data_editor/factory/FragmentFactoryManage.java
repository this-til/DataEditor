package com.til.data_editor.factory;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;
import com.til.data_editor.Util;
import com.til.data_editor.fragment.FieldFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author til
 */
public class FragmentFactoryManage {

    protected List<FragmentFactory> fragmentFactoryList;

    public static final FragmentFactoryManage DEF;

    static {
        List<FragmentFactory> fragmentFactories = new ArrayList<>();
        fragmentFactories.add(new ArrayInputFactory());
        fragmentFactories.add(new BoolInputFactory());
        fragmentFactories.add(new MapInputFactory());
        fragmentFactories.add(new ObjectInputFactory());
        fragmentFactories.add(new StringInputFactory());
        DEF = new FragmentFactoryManage(fragmentFactories);
    }


    public FragmentFactoryManage(List<FragmentFactory> fragmentFactoryList) {
        this.fragmentFactoryList = fragmentFactoryList.stream().sorted((r1, r2) -> r2.getPriority() - r1.getPriority()).collect(Collectors.toList());
    }

    public FieldFragment mack(Type type, FragmentActivity activity) {
        if (Util.isUndetermined(type)) {
            return null;
        }
        for (FragmentFactory fragmentFactory : fragmentFactoryList) {
            FieldFragment fieldFragment = fragmentFactory.mack(type, activity);
            if (fieldFragment != null) {
                return fieldFragment;
            }
        }
        return null;
    }

}
