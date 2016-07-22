package com.heroliu.www.shark;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class DetailActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_author)
    TextView tvAuthor;
    @ViewInject(R.id.tv_date)
    TextView tvDate;
    @ViewInject(R.id.tv_article)
    TextView tvArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        x.view().inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();i.setClass(DetailActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String author = i.getStringExtra("author");
        String article = i.getStringExtra("article");
        String date = i.getStringExtra("date");
        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvDate.setText(date);
        tvArticle.setText(article);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setClass(DetailActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
