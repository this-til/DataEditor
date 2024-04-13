package com.til.data_editor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.til.data_editor.R;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author til
 */
public abstract class FieldFragment extends Fragment {

    protected boolean update;
    protected Type type;

    public FieldFragment(Type type) {
        this.type = type;
    }

    public FieldFragment(int contentLayoutId, Type type) {
        super(contentLayoutId);
        this.type = type;
    }

    public boolean withUpdate() {
        boolean u = update;
        update = false;
        return u;
    }


    public abstract Object get();

    public abstract void set(Object obj);

    public boolean canExpanded() {
        return false;
    }

    public Iterable<View> mackOtherView(Context context){
        return Collections.emptyList();
    }


}
