package com.top.register;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.top.vclass2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Url: 注册提交地址:https://www.54lxb.cn/vclass/mobile/user/inserted
 * @Url: 院系：https://www.54lxb.cn/vclass/mobile/department/list
 * @Url: 专业：https://www.54lxb.cn/vclass/mobile/specialty/list?id=
 * @Url: 班级：https://www.54lxb.cn/vclass/mobile/classes/list?id=
 * @Methad: POST
 */

/**
 * @main： Spinner联动传值功能待完善(院系名称已提取，Post提交方法已写)
 */
public class RegisterActivity extends Activity {

    private EditText name_editText, pwd_editText, phone_editText, email_editText;
    private RadioGroup radioGroup;
    private Spinner spinner_a, spinner_b, spinner_c;
    private Button register_button;

    private OkHttpClient okHttpClient;
    private String name, pwd, phone, email, sex = "男", a, b, c;

    private String a_id, b_id;
    private List<String> listId, listId2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> list = (List<String>) msg.obj;
            int size = list.size();
            String[] data = list.toArray(new String[size]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, data);
            spinner_a.setAdapter(adapter);
        }
    };

    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> list = (List<String>) msg.obj;
            int size = list.size();
            String[] data = list.toArray(new String[size]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, data);
            spinner_b.setAdapter(adapter);
        }
    };

    private Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> list = (List<String>) msg.obj;
            int size = list.size();
            String[] data = list.toArray(new String[size]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, data);
            spinner_c.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
        initEvent();
        getWebSite();
    }

    private void initView() {
        okHttpClient = new OkHttpClient();
        name_editText = (EditText) findViewById(R.id.register_username);
        pwd_editText = (EditText) findViewById(R.id.register_password);
        phone_editText = (EditText) findViewById(R.id.register_phone);
        email_editText = (EditText) findViewById(R.id.register_email);
        radioGroup = (RadioGroup) findViewById(R.id.register_radioGroup);
        spinner_a = (Spinner) findViewById(R.id.register_spinner);
        spinner_b = (Spinner) findViewById(R.id.register_spinner2);
        spinner_c = (Spinner) findViewById(R.id.register_spinner3);
        register_button = (Button) findViewById(R.id.register_button);
    }

    //Button事件，点击执行Post提交事件
    private void initEvent() {
        //获得性别
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sex = checkedId == R.id.register_man ? "男" : "女";
            }
        });
        spinner_a.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a_id = listId.get(position);
                a = (String) spinner_a.getSelectedItem();
                getWebSite2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_b.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                b_id = listId2.get(position);
                b = (String) spinner_b.getSelectedItem();
                getWebSite3();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_c.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c = (String) spinner_c.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //提交
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_editText.getText().toString().trim();
                pwd = pwd_editText.getText().toString().trim();
                phone = phone_editText.getText().toString().trim();
                email = email_editText.getText().toString().trim();
                //执行异步操作
                Log.i("info", name + pwd + phone + email + sex + a + b + c);
                new Thread(runnable2).start();
            }
        });
    }


    private void getWebSite() {
        new Thread(runnable).start();
    }

    private void getWebSite2() {
        new Thread(runnable3).start();
    }

    private void getWebSite3() {
        new Thread(runnable4).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDepartMent();
        }
    };

    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            Request request = new Request.Builder().get().url("https://www.54lxb.cn/vclass/mobile/specialty/list?id=" + a_id).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String html = response.body().string();
                    List<String> specialty = getSpecialty(html);
                    getSpecialtyId(html);
                    Message message = new Message();
                    message.obj = specialty;
                    handler2.sendMessage(message);
                }

                private List<String> getSpecialty(String html) {
                    List<String> list = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(html);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            list.add(jsonObject1.getString("specialty"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return list;
                }

                private void getSpecialtyId(String html) {
                    listId2 = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(html);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            listId2.add(jsonObject1.getString("id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            Request request = new Request.Builder().get().url("https://www.54lxb.cn/vclass/mobile/classes/list?id=" + b_id).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String html = response.body().string();
                    List<String> specialty = getSpecialty(html);
                    Message message = new Message();
                    message.obj = specialty;
                    handler3.sendMessage(message);
                }

                private List<String> getSpecialty(String html) {
                    List<String> list = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(html);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            list.add(jsonObject1.getString("classes"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return list;
                }
            });
        }
    };

    //提取院系
    private void getDepartMent() {
        Request request = new Request.Builder().get().url("https://www.54lxb.cn/vclass/mobile/department/list").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String html = response.body().string();
                List<String> depart = getJsonDepart(html);
                getJsonId(html);
                Message message = new Message();
                message.obj = depart;
                handler.sendMessage(message);
            }
        });
    }

    //解析院系名称
    private List<String> getJsonDepart(String html) {
        List<String> list = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(html);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                list.add(jsonObject1.getString("department"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //保存院系id
    private void getJsonId(String html) {
        listId = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(html);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                listId.add(jsonObject1.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //注册按钮事件
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            postData();
        }
    };

    //Post提交注册
    private void postData() {
        Log.i("info", "用户名：" + name + "密码:" + pwd + "电话：" + phone + "邮箱：" + email + "性别：" + sex + "院系：" + a + "专业：" + b + "班级：" + c);
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        RequestBody requestBody = formEncodingBuilder.add("username ", name).add("password", pwd)
                .add("sex", sex).add("phone", phone).add("email", email).add("department", a).add("specialty", b).add("classes", c).build();
        Request request = new Request.Builder().post(requestBody).url("https://www.54lxb.cn/vclass/mobile/user/inserted")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String html = response.body().string();
                Log.i("info", html);
            }
        });
    }
}
