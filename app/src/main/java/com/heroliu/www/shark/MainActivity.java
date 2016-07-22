package com.heroliu.www.shark;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PageRange;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.heroliu.beans.News;
import com.heroliu.beans.Party;
import com.mxn.soul.flowingdrawer.FeedAdapter;
import com.mxn.soul.flowingdrawer.MyMenuFragment;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity implements MyMenuFragment.BackListener{
    private RecyclerView rvFeed;
    private LeftDrawerLayout mLeftDrawerLayout;
    private List<News> newsList;
    private List<Party> partyList;
    private FeedAdapter feedAdapter;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbar();
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        rvFeed = (RecyclerView) findViewById(R.id.rvFeed);
        FragmentManager fm = getSupportFragmentManager();
        MyMenuFragment mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
        newsList = new ArrayList<News>();
        partyList = new ArrayList<Party>();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                type = "news";
                getNews();
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                setupFeed();
            }
        }.execute(null, null, null);

    }
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftDrawerLayout.toggle();
            }
        });
    }
    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);
        feedAdapter = new FeedAdapter(this,newsList);
        feedAdapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent i = new Intent();
                i.putExtra("type",type);
                i.putExtra("title",newsList.get(position).getTitle());
                i.putExtra("article",newsList.get(position).getArticle());
                i.putExtra("author",newsList.get(position).getAuthor());
                i.putExtra("date",newsList.get(position).getDate());
                i.setClass(MainActivity.this,DetailActivity.class);
                startActivity(i);
                finish();
            }
        });
        rvFeed.setAdapter(feedAdapter);
        feedAdapter.updateItems();
    }
    @Override
    public void onBackPressed() {
        if (mLeftDrawerLayout.isShownMenu()) {
            mLeftDrawerLayout.closeDrawer();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("确定退出吗?");
            builder.setPositiveButton("再想想", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                }
            } )
            .setNegativeButton("嗯", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                    Intent i = new Intent();
                    i.setClass(MainActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }).show();
        }
    }
    @Override
    public void onBackWork(int id) {
        switch (id){
            case R.id.menu_feed:{
                mLeftDrawerLayout.closeDrawer();

                break;
            }
            case R.id.menu_weibo:{
                Intent i = new Intent();
                i.setClass(MainActivity.this,JokeActivity.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.menu_news:{
                mLeftDrawerLayout.closeDrawer();
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        newsList.clear();
                        //Log.e("newsList size() be--->",newsList.size()+"");
                        type = "news";
                        getNews();
                        //Log.e("newsList size() af--->",newsList.size()+"");
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        feedAdapter.updateItems();
                    }
                }.execute(null, null, null);
                break;
            }
            case R.id.menu_popular:{
                mLeftDrawerLayout.closeDrawer();
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        newsList.clear();
                        type = "party";
                        getParty();
                        for(int i = 0;i < partyList.size();i++){
                            News news = new News(partyList.get(i).getTitle(),partyList.get(i).getAuthor(),partyList.get(i).getArticle(),partyList.get(i).getDate(),"","http://www.heroliu.com/images/"+partyList.get(i).getImage());
                            Log.d("party iamge--->",news.getImage());
                            newsList.add(news);

                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        feedAdapter.updateItems();
                    }
                }.execute(null, null, null);
                break;
            }
            case R.id.menu_add:{
                Intent i = new Intent();
                i.setClass(MainActivity.this,AddNewsActivity.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.menu_exit:{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确定退出吗?");
                builder.setPositiveButton("再想想", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        arg0.dismiss();
                    }
                } )
                        .setNegativeButton("嗯", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                arg0.dismiss();
                                Intent i = new Intent();
                                i.setClass(MainActivity.this, WelcomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).show();
                break;
            }
            case R.id.menu_about:{
                Toast.makeText(getApplicationContext(),"更多详情请访问Shark官方主页",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void getNews(){
        List<HashMap<String, Object>> lists = null;
        try {
            lists = NewsAnalysis(doNewsPost());
            if(lists == null){
                return;
            }
            newsList.clear();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int count = 0;
        for(HashMap<String, Object> newsMap : lists){
            News news = new News(newsMap.get("title").toString(),newsMap.get("author").toString(),newsMap.get("article").toString(),newsMap.get("date").toString(),newsMap.get("positive").toString(),"http://www.heroliu.com/images/"+newsMap.get("image").toString());
            count++;
            Log.d("news iamge--->",news.getImage());
            newsList.add(news);
        }
        Log.e("count--->", count+"");
    }
    private static ArrayList<HashMap<String, Object>> NewsAnalysis(String jsonStr)
            throws JSONException {
        /*******************解析字符串***********************/
        if(jsonStr.equals("连接错误")){
            return null;
        }
        //Log.e("json string--->", jsonStr);
        JSONArray jsonArray = null;
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", jsonObject.getString("ntitle"));
            map.put("author", jsonObject.getString("nauthor"));
            map.put("article", jsonObject.getString("narticle"));
            map.put("date", jsonObject.getString("ndate"));
            map.put("positive",jsonObject.getString("npositive"));
            map.put("image",jsonObject.getString("nimage"));
            list.add(map);
        }
        return list;
    }
    public static String doNewsPost()
            throws Exception {
        String result = "连接错误";
        String url = "http://www.heroliu.com/com.nine.download.news.DownNewsServlet";
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);

        try {
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(response.getEntity(),"utf-8");
            }
            Log.e("HTTP_STATUS--->", response.getStatusLine().getStatusCode()+"");

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private static ArrayList<HashMap<String, Object>> PartyAnalysis(String jsonStr)
            throws JSONException {
        /******************* 解析字符串 ***********************/
        if(jsonStr.equals("连接错误")){
            return null;
        }
        JSONArray jsonArray = null;
        Log.e("Analysis jsonStr--->", jsonStr);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", jsonObject.getString("ptitle"));
            map.put("article", jsonObject.getString("particle"));
            map.put("date", jsonObject.getString("pdate"));
            map.put("location", jsonObject.getString("plocation"));
            map.put("author", jsonObject.getString("pauthor"));
            map.put("image",jsonObject.getString("pimage"));
            list.add(map);
        }
        Log.e("return list--->", "true");
        return list;
    }

    private void getParty(){
        List<HashMap<String, Object>> lists = null;
        try {
            lists = PartyAnalysis(doPartyPost());
            if(lists == null){
                return;
            }
            partyList.clear();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(HashMap<String, Object> partyMap : lists){
            Party party = new Party(partyMap.get("title").toString(),partyMap.get("article").toString(),partyMap.get("date").toString(),partyMap.get("location").toString(),partyMap.get("author").toString(),partyMap.get("image").toString());
            Log.e("title--->", party.getTitle());
            partyList.add(party);
        }
    }
    public static String doPartyPost()
            throws Exception {
        String result = "连接错误";
        String url = "http://www.heroliu.com/com.nine.download.party.DownPartyServlet";
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);

        try {
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(response.getEntity());
            }
            //Log.e("result--->", result);
            Log.e("HTTP_CONNECT-->", response.getStatusLine().getStatusCode()+"");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}