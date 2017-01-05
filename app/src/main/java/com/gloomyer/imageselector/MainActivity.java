package com.gloomyer.imageselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gloomyer.ui.OnImageClickListener;
import com.gloomyer.ui.OnSelectedListener;
import com.gloomyer.ui.UIManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void c1(View v) {
        UIManager.getInstance().start(this, new OnSelectedListener() {
            @Override
            public void onSelect(List<String> selecteds) {
                Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void c2(View v) {
        UIManager.getInstance().start(this, 9, new OnSelectedListener() {
            @Override
            public void onSelect(List<String> selecteds) {
                Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void c3(View v) {
        UIManager.getInstance().setOnImageClickListener(new OnImageClickListener() {
            @Override
            public void onClick(String path) {
                Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
            }
        });
        UIManager.getInstance().start(this, 9, new OnSelectedListener() {
            @Override
            public void onSelect(List<String> selecteds) {
                Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
                UIManager.getInstance().removeOnImageClickListener();
            }
        });
    }

    List<String> history;

    public void c4(View v) {
        UIManager.getInstance().setOnImageClickListener(new OnImageClickListener() {
            @Override
            public void onClick(String path) {
                Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
            }
        });
        UIManager.getInstance().start(this, 9, false, history, new OnSelectedListener() {
            @Override
            public void onSelect(List<String> selecteds) {
                history = selecteds;
                Toast.makeText(MainActivity.this, selecteds.toString(), Toast.LENGTH_LONG).show();
                UIManager.getInstance().removeOnImageClickListener();
            }
        });
    }
}
