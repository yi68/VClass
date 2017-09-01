package com.top.player;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.top.application.DataApplication;
import com.top.vclass2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

/**
 * Created by zym on 2017/5/19.
 *
 * @time: 二〇一七年五月二十日 14:00:09
 * @URL: http://139.199.68.51/vclass/mobile/course/1000/detail
 */
public class jsFragment extends Fragment {
    private static String[] detailUrl = {"https://www.54lxb.cn/vclass/mobile/course//detail?id="};
    private TextView textView;
    private DataApplication application;
    private String id;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_js_fragment, container, false);
        textView = (TextView) view.findViewById(R.id.play_js_fragment_text);
        application = (DataApplication) getActivity().getApplication();
        id = application.getId();
        Log.i("main", id);
        new MyAsyncTaske().execute(detailUrl);
        return view;
    }

    // 异步处理任务
    class MyAsyncTaske extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            final String[] s = {""};
            String url = params[0];
            Request.Builder builder = new Request.Builder();
            Request request = builder.get().url(url + id).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String html = response.body().string();
                    Log.i("main", html);
                    try {
                        JSONObject jsonObject = new JSONObject(html);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        s[0] = jsonObject1.getString("description");
                        Log.i("main", s[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return s;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            try {
                Thread.sleep(200);
                Log.i("main", strings[0]);
                textView.setText(strings[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
