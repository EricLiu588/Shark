package com.heroliu.www.shark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.iv_cancel)
    ImageView iv_cancel;
    @ViewInject(R.id.iv_confirm)
    ImageView iv_confirm;
    @ViewInject(R.id.etUsername)
    EditText et_username;
    @ViewInject(R.id.etPassword)
    EditText et_password;
    @ViewInject(R.id.tv_regist_now)
    TextView tvRegistNow;
    private String result;
    private ProgressDialog dialog;
    private LoginTask loginTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        dialog = new ProgressDialog(this);
        result = "";
    }
    @Event(R.id.iv_cancel)
    private void onCancelClick(View view){
        Intent i = new Intent();
        i.setClass(LoginActivity.this,WelcomeActivity.class);
        startActivity(i);
        finish();
    }
    @Event(R.id.iv_confirm)
    private void onConfirmClick(View view){
        if(!(et_username.getText().toString().equals("") || et_password.getText().toString().equals(""))) {
            loginTask = (LoginTask) new LoginTask().execute(new String[]{et_username.getText().toString(), et_password.getText().toString()});
        }else {
            Toast.makeText(getApplicationContext(),"请检查输入内容",Toast.LENGTH_SHORT).show();
        }
    }
    @Event(R.id.tv_regist_now)
    private void onRegistNow(View view){
        Intent i = new Intent();
        i.setClass(LoginActivity.this,RegistActivity.class);
        startActivity(i);
        finish();
    }
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            dialog.setTitle("正在登录");
            dialog.setMessage("正在疯狂请求服务器");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            String[] s = result.split(":");
            if(s[0].equals("SUCCESS")){
                Intent i = new Intent();
                i.setClass(LoginActivity.this,MainActivity.class);
                User user = (User) getApplication();
                user.setUsername(et_username.getText().toString());
                startActivity(i);
                finish();
            }else if(result.equals("FAILURE")){
                Toast.makeText(getApplicationContext(), "登录失败,请检查用户名或密码", Toast.LENGTH_SHORT).show();
            }else if(result.equals("CONNECT_ERROR")){
                Toast.makeText(getApplicationContext(), "服务器未响应", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "未知错误,请上报", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = "http://www.heroliu.com/Focus/com.nine.login.LoginServlet";
            List<NameValuePair> user = new ArrayList<NameValuePair>();
            user.add(new BasicNameValuePair("username",params[0]));
            user.add(new BasicNameValuePair("password",params[1]));
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            try {
                request.setEntity(new UrlEncodedFormEntity(user, HTTP.UTF_8));
                HttpResponse response = client.execute(request);
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    result = EntityUtils.toString(response.getEntity());
                }else{
                    result = "CONNECT ERROR";
                }
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
