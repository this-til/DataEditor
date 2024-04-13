package com.til.data_editor.activity;

import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.til.data_editor.R;
import com.til.data_editor.SerializeField;
import com.til.data_editor.factory.FragmentFactoryManage;
import com.til.data_editor.fragment.FieldFragment;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LinearLayout view = findViewById(R.id.v);
        FieldFragment mack = FragmentFactoryManage.DEF.mack(Test.class, this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(view.getId(), mack);
        fragmentTransaction.commit();
    }

    public static class Test {
        @SerializeField
        public int anInt;
        @SerializeField
        public float aFloat;
        @SerializeField
        public String string;
        @SerializeField
        public Test2 test2;

        @SerializeField
        public List<String> stringList;

        @SerializeField
        public List<Test2> test2List;
        @SerializeField
        public List<List<Test2>> test2ListList;
    }

    public static class Test2 {

        @SerializeField
        public int anInt;
        @SerializeField
        public float aFloat;
    }
}