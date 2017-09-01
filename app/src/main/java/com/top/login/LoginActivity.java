package com.top.login;

/**
 * @main 登陆Activity
 * @author zym
 */

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.top.register.RegisterActivity;
import com.top.vclass2.MainActivity;
import com.top.vclass2.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

    private EditText uname, pwd, yzm;
    private ImageButton y_img;
    private Button denglu, zhuce;
    private String l_uname, l_pwd, l_yzm;
    private ProgressDialog progressDialog;
    private Boolean first = true;
    private static String url = "https://www.54lxb.cn";
    private OkHttpClient okhttp = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        okhttp.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        initView();
        initEvent();
        new Thread(getyzm).start();
    }

    private void initView() {
        uname = (EditText) findViewById(R.id.login_userName);
        pwd = (EditText) findViewById(R.id.login_passWord);
        yzm = (EditText) findViewById(R.id.login_yzm);
        y_img = (ImageButton) findViewById(R.id.login_yzm_img);
        denglu = (Button) findViewById(R.id.login);
        zhuce = (Button) findViewById(R.id.zhuce);
    }

    private void initEvent() {
        denglu.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        y_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (first) {
                    l_uname = uname.getText().toString().trim();
                    l_pwd = pwd.getText().toString().trim();
                    l_yzm = yzm.getText().toString().trim();
                    progressDialog = ProgressDialog.show(LoginActivity.this, "提示", "正在登录...");
                    new Thread(runnable).start();
                }
                break;
            case R.id.zhuce:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_yzm_img:
                new Thread(getyzm).start();
                break;
        }
    }

    /**
     * @main 登陆异步任务
     * @author zym
     */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            RequestBody body = formEncodingBuilder.add("username", l_uname).add("password", l_pwd).add("passcode", l_yzm)
                    .build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.post(body).url(url + "/vclass/auth/logon").build();
            okhttp.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Response arg0) throws IOException {
                    String entity = arg0.body().string();
                    setName(jsonArray(entity));
                    int state = jsonState(entity);
                    Log.i("body", entity);
                    Message ms = new Message();
                    ms.what = state;
                    try {
                        Thread.sleep(2000);
                        handler.sendMessage(ms);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Request arg0, IOException arg1) {
                    Toast.makeText(LoginActivity.this, "数据发送失败!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    /**
     * @throws Exception
     * @main 解析httpEntity
     */
    private int jsonState(String httpEntity) {
        int flag = 0;
        JSONTokener jsonTokener = new JSONTokener(httpEntity);
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            String state = jsonObject.getString("state");
            System.out.println("state----->" + state);
            if (state.equals("true")) {
                flag = 1;
            } else {
                return flag;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @main 解析json数组
     * @author zym
     */
    private String jsonArray(String httpEntity) {
        String name = "";
        JSONTokener jsonTokener = new JSONTokener(httpEntity);
        try {
            JSONObject jsonObject = new JSONObject(jsonTokener);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            name = jsonObject1.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * @throws Exception
     * @main 获取验证码方法
     */
    private void getPasscode() throws Exception {
        // HttpGet get = new HttpGet("http://139.199.68.51/vclass/passcode");
        // HttpResponse response = new DefaultHttpClient().execute(get);
        // HttpEntity entity = response.getEntity();
        // conn = new ConnectWeb();
        // HttpEntity entity = conn.getPasscode();
        // byte[] img = EntityUtils.toByteArray(entity);
        // Message msg = new Message();
        // msg.what = 2;
        // Bundle img2 = new Bundle();
        // img2.putByteArray("img", img);
        // msg.setData(img2);
        // handler.sendMessage(msg);
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url + "/vclass/passcode/").build();
        Call newCall = okhttp.newCall(request);
        newCall.enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                byte[] bytes = arg0.body().bytes();
                Message msg = new Message();
                msg.what = 2;
                msg.obj = bytes;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Log.i("Err", arg1.toString());
            }
        });
    }

    /**
     * @main 获取验证码异步任务
     */
    Runnable getyzm = new Runnable() {

        @Override
        public void run() {
            try {
                getPasscode();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * @main 用于处理异步任务所发送的结果在主线程更新
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                first = false;
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            } else if (i == 2) {
                byte[] img = (byte[]) msg.obj;
                Bitmap imgmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                y_img.setImageBitmap(imgmap);
            } else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "登陆失败,请检查数据是否正确！", Toast.LENGTH_LONG).show();
            }
        }

        ;
    };

    /**
     * @param name
     * @message 写入数据到SharedPreferences
     */
    private void setName(String name) {
        SharedPreferences preferences = getSharedPreferences("message", MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString("name", name);
        edit.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
