package com.top.vclass2;

/**
 * @main 软件主页Fragment包含内容：AD展示，推荐课程
 * @url http://139.199.68.51
 * @type /vclass/mobile/course/list
 * @client okhttp
 * @author zym
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.top.adapter.ShouyeAdapter;
import com.top.application.DataApplication;
import com.top.book.BookDataActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ShouyeFragment extends Fragment {

    private View view;
    private ViewFlipper AD_Img;
    private static String[] url = {"http://pic1.win4000.com/wallpaper/8/58806060b2023.jpg",
            "http://pic1.win4000.com/wallpaper/8/58806066be470.jpg",
            "http://pic1.win4000.com/wallpaper/b/57a82cadc80e1.jpg",
            "http://pic1.win4000.com/wallpaper/d/57400b3e21fbc.jpg"};
    private ProgressBar load;

    private String dataUrl = "https://www.54lxb.cn/vclass/";
    private GridView gridView;
    private ShouyeAdapter adapter;
    private BitmapUtils bitmapUtils;

    private DataApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.v_shouye, container, false);
        application = (DataApplication) getActivity().getApplication();
        AD_Img = (ViewFlipper) view.findViewById(R.id.v_shouye_ad_img);
        load = (ProgressBar) view.findViewById(R.id.v_shouye_ad_progress);
        File image = new File("sdcard/zym/a.png");
        if (image.exists()) {
            Bitmap[] bitmap2 = {BitmapFactory.decodeFile("sdcard/zym/a.png"),
                    BitmapFactory.decodeFile("sdcard/zym/b.png"), BitmapFactory.decodeFile("sdcard/zym/c.png"),
                    BitmapFactory.decodeFile("sdcard/zym/d.png")};
            for (int i = 0; i < bitmap2.length; i++) {
                AD_Img.addView(setImage(bitmap2[i]));
            }
            AD_Img.setFlipInterval(4000);
            AD_Img.setInAnimation(getActivity(), R.anim.translate_left);
            AD_Img.setOutAnimation(getActivity(), R.anim.translate_right);
            AD_Img.startFlipping();
        } else {
            new myAsyncTask().execute(url);
        }
        gridView = (GridView) view.findViewById(R.id.v_shouye_main_gridView);
        bitmapUtils = new BitmapUtils(this.getActivity().getApplicationContext());
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        adapter = new ShouyeAdapter(inflater.getContext());
        gridView.setAdapter(adapter);
        //GridView点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.v_shouye_main_style_txtid);
                application.setId(textView.getText().toString().trim());
                Intent intent = new Intent(getActivity(), BookDataActivity.class);
                startActivity(intent);
            }
        });
        loadImgList(dataUrl + "/mobile/course/list?id=1005");
        return view;
    }

    private void loadImgList(String url) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                adapter.addImg(getImgUrlList(responseInfo.result));
                adapter.addText(getBookName(responseInfo.result));
                adapter.addId(getBookId(responseInfo.result));
                adapter.notifyDataSetChanged();// 通知listview更新数据
            }

            @Override
            public void onFailure(HttpException error, String msg) {
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
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(i);
                pics.add(jsonObject3.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pics;
    }

    /**
     * @param img
     * @return
     * @param: 为ViewFlipper设置图片此处需new一个ImageView组件来存放图片
     */
    private ImageView setImage(Bitmap img) {
        ImageView image = new ImageView(getActivity());
        image.setImageBitmap(img);
        return image;
    }

    /**
     * @author zym
     * @main AsyncTask线程
     */
    class myAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            String[] test = {"a.png", "b.png", "c.png", "d.png"};
            for (int i = 0; i < params.length; i++) {
                String url = params[i];
                try {
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream inputStream = conn.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    setUrlImage(test[i], bitmap);
                    inputStream.close();
                    bufferedInputStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            load.setVisibility(View.GONE);
            Bitmap[] bitmap2 = {BitmapFactory.decodeFile("sdcard/zym/a.png"),
                    BitmapFactory.decodeFile("sdcard/zym/b.png"), BitmapFactory.decodeFile("sdcard/zym/c.png"),
                    BitmapFactory.decodeFile("sdcard/zym/d.png")};
            for (int i = 0; i < bitmap2.length; i++) {
                AD_Img.addView(setImage(bitmap2[i]));
            }
            AD_Img.setFlipInterval(4000);
            AD_Img.startFlipping();

        }

        /**
         * @param test
         * @param bitmap
         * @param: 保存图片到手机
         */
        private void setUrlImage(String test, Bitmap bitmap) {
            File f1 = new File("sdcard/zym/");
            if (f1.exists()) {
                File f2 = new File("sdcard/zym/" + test);
                if (f2.exists()) {
                    return;
                } else {
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(f2);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                f1.mkdirs();
                File f2 = new File("sdcard/zym/" + test);
                if (f2.exists()) {
                    return;
                } else {
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(f2);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
