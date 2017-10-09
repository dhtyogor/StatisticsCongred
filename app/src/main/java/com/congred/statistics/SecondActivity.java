package com.congred.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Tao on 2017/9/25.
 */

public class SecondActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CongredAgent.onResume(this);
//        CongredAgent.update(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CongredAgent.onPause(this);
    }
}
