package com.heroliu.www.shark;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class WelcomeActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_regist)
    Button btn_regist;
    @ViewInject(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        x.view().inject(this);


    }
    @Event(R.id.btn_regist)
    private void onRegistClick(View view){
        Intent i = new Intent();
        i.setClass(WelcomeActivity.this,RegistActivity.class);
        startActivity(i);
    }
    @Event(R.id.btn_login)
    private void onLoginClick(View view){
        Intent i = new Intent();
        i.setClass(WelcomeActivity.this,LoginActivity.class);
        startActivity(i);
    }
}
