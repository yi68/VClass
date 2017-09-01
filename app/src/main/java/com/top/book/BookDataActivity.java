package com.top.book;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.top.application.DataApplication;
import com.top.player.PlayerActivity;
import com.top.vclass2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * @details: 获取所有课程名称
 * @ChangeTime： 2017年5月9日 22:30:19
 */

public class BookDataActivity extends Activity {

    private ListView listView;
    private BookAdapter bookAdapter;

    private BitmapUtils bitmapUtils;

    private DataApplication application;


    private String dataUrl = "https://www.54lxb.cn/vclass/mobile/course/list?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_book_data);
        bitmapUtils = new BitmapUtils(BookDataActivity.this);
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        bookAdapter = new BookAdapter(BookDataActivity.this);
        application = (DataApplication) this.getApplication();
        String id = application.getId();
        loadImgList(dataUrl + id);
        listView = (ListView) findViewById(R.id.v_book_data_listView);
        listView.setAdapter(bookAdapter);
        // 向player传值
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.v_book_data_listView_id);
                String bookId = textView.getText().toString().trim();
                application.setId(bookId);
                Intent intent = new Intent(BookDataActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadImgList(String url) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                bookAdapter.addSrc(getImgUrlList(responseInfo.result));
                bookAdapter.addTxt(getBookName(responseInfo.result));
                bookAdapter.addId(getBookId(responseInfo.result));
                bookAdapter.notifyDataSetChanged();// 通知listview更新数据
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 网络操作失败所做操作
            }
        });
    }

    public static List<String> getImgUrlList(String htmlStr) {
        List<String> pics = new ArrayList<String>();

        JSONTokener jsonTokener = new JSONTokener(htmlStr);
        try {
            JSONObject jsonObject1 = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(i);
                pics.add("https://www.54lxb.cn/vclass" + jsonObject3.getString("imageurl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pics;
    }

    public static List<String> getBookName(String htmlStr) {
        List<String> pics = new ArrayList<String>();

        JSONTokener jsonTokener = new JSONTokener(htmlStr);
        try {
            JSONObject jsonObject1 = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(i);
                pics.add(jsonObject3.getString("coursename"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pics;
    }

    public static List<String> getBookId(String htmlStr) {
        List<String> pics = new ArrayList<String>();

        JSONTokener jsonTokener = new JSONTokener(htmlStr);
        try {
            JSONObject jsonObject1 = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                pics.add(jsonObject2.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pics;
    }
}
