package com.heroliu.www.shark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RegistActivity extends AppCompatActivity {

    @ViewInject(R.id.etUsername)
    EditText etUername;
    @ViewInject(R.id.etPassword)
    EditText etPassword;
    @ViewInject(R.id.iv_confirm)
    ImageView ivConfirm;
    @ViewInject(R.id.iv_cancel)
    ImageView ivCancel;
    @ViewInject(R.id.tv_login_now)
    TextView tvLoginNow;

    private String result = "";
    private RegistTask registTask;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        x.view().inject(this);
        dialog = new ProgressDialog(this);
    }

    @Event(R.id.iv_cancel)
    private void onCancelClick(View view) {
        Intent i = new Intent();
        i.setClass(RegistActivity.this, WelcomeActivity.class);
        startActivity(i);
        finish();
    }

    @Event(R.id.iv_confirm)
    private void onConfirmClick(View view) {
        String username = etUername.getText().toString();
        String password = etPassword.getText().toString();
        registTask = new RegistTask();
        registTask.execute(new String[]{username,password,"undefined","undefined"});
    }

    @Event(R.id.tv_login_now)
    private void onLoginNowClick(View view){
        Intent i = new Intent();
        i.setClass(RegistActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class RegistTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            dialog.setTitle("提示信息");
            dialog.setMessage("正在注册");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            if (result.equals("EXIST")) {
                Toast.makeText(getApplicationContext(), "该用户也存在，请重新注册", Toast.LENGTH_SHORT).show();
            } else if (result.equals("CREATE_SUCCESS")) {
                Intent i = new Intent();
                i.setClass(RegistActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            } else if (result.equals("CREATE_FAILURE")) {
                Toast.makeText(getApplicationContext(), "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if (result.equals("CONNECT_ERROR")) {
                Toast.makeText(getApplicationContext(), "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = "http://www.heroliu.com/Focus/com.nine.regist.RegistServlet";
            List<NameValuePair> user = new ArrayList<NameValuePair>();
            user.add(new BasicNameValuePair("username", params[0]));
            user.add(new BasicNameValuePair("password", params[1]));
            user.add(new BasicNameValuePair("sex", params[2]));
            user.add(new BasicNameValuePair("school", params[3]));
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            try {
                request.setEntity(new UrlEncodedFormEntity(user, HTTP.UTF_8));
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(response.getEntity());
                } else {
                    result = "CONNECT_ERROR";
                }
                Log.e("REGIST CODE--->", response.getStatusLine().getStatusCode() + "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    }
}
