package com.heroliu.www.shark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.heroliu.applications.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddNewsActivity extends AppCompatActivity {

    private LocationClient mLocationClient;
    @ViewInject(R.id.inputTitle)
    EditText etTitle;
    @ViewInject(R.id.inputLocation)
    TextView tvLocation;
    @ViewInject(R.id.inputMsg)
    EditText etMsg;
    ProgressDialog dialog;
    String result;
    private SendNewsTask sendTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        x.view().inject(this);
        dialog = new ProgressDialog(this);
        result = "";
        mLocationClient = ((User)getApplication()).mLocationClient;
        ((User)getApplication()).locationInfo = tvLocation;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = etTitle.getText().toString();
                String News = etMsg.getText().toString();
                //String Addr = locationInfo.getText().toString();
                String name = ((User)getApplication()).getUsername();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format.format(date);
                String[] info = new String[]{Title,News,time,name};
                sendTask = (SendNewsTask) new SendNewsTask().execute(info);
            }
        });
        mLocationClient.start();
        mLocationClient.requestLocation();
    }
    private class SendNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog.setTitle("正在上传");
            dialog.setMessage("正在疯狂写入数据库");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            if(result.equals("SUCCESS")){
                Intent i = new Intent();
                i.setClass(AddNewsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            else if(result.equals("FAILURE")){
                Toast.makeText(getApplicationContext(), "完犊子", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            result = "Î´Öª´íÎó";
            String url = "http://www.heroliu.com/Focus/com.nine.news.NewsServlet";
            List<NameValuePair> news = new ArrayList<NameValuePair>();
            news.add(new BasicNameValuePair("title", params[0]));
            news.add(new BasicNameValuePair("article", params[1]));
            news.add(new BasicNameValuePair("date", params[2]));
            news.add(new BasicNameValuePair("author", params[3]));
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            try {
                request.setEntity(new UrlEncodedFormEntity(news,"utf-8"));
                HttpResponse response = client.execute(request);
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    result = EntityUtils.toString(response.getEntity());
                }else{
                    result = "CONNECT_ERROR";
                }
                Log.e("CODE--->", response.getStatusLine().getStatusCode()+"");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setClass(AddNewsActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
