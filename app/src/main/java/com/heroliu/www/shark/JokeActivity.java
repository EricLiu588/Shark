package com.heroliu.www.shark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.heroliu.adapters.JokeAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JokeActivity extends AppCompatActivity {

    private RecyclerView rvFeed;
    private List<String> lists;
    private JokeAdapter jokeAdapter;
    private JokeTask jokeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lists = new ArrayList<String>();
        rvFeed = (RecyclerView) findViewById(R.id.rvFeed);
        jokeTask = new JokeTask();
        jokeTask.execute();
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);
        jokeAdapter = new JokeAdapter(this,lists);
        rvFeed.setAdapter(jokeAdapter);
        jokeAdapter.updateItems();
    }
    private class JokeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("success")){
                setupFeed();
            }
            else{
                Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String  doInBackground(Void... params) {
            String result = "";
            try {
                Document doc = Jsoup.connect("http://www.qiushibaike.com/textnew/page/1")
                        .userAgent("Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)") // 设置 User-Agent
                        .timeout(3000)           // 设置连接超时时间
                        .get();
                String s = doc.toString();
                String ss = s.replace("\"", "s");
                s = ss.replace("<br>", "\n        ");
                ss = s.replace("        ", "");
                s = ss.replace("\n", "");
                String[] tem = s.split("<div class=scontents>");
                String[] tem2 = new String[tem.length];
                for(int i = 1;i < tem.length;i++){
                    String[] tem3 = tem[i].split("</div>");
                    tem2[i-1] = tem3[0];
                }
                for(int i = 0;i < tem2.length-1;i++){
                    lists.add(tem2[i]);
                }
                result = "success";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setClass(JokeActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
