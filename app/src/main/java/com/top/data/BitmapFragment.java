package com.top.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.BaseAdapter;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.top.application.DataApplication;
import com.top.book.BookDataActivity;
import com.top.vclass2.R;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

/**
 * @Author: Yi
 * @DateAll 二〇一七年四月二十六日 19:50:44
 */
public class BitmapFragment extends Fragment {

    private ListView imageListView;
    private ImageListAdapter imageListAdapter;
    public static BitmapUtils bitmapUtils;

    private String[] imgSites = {"https://www.54lxb.cn/vclass/mobile/courseType/sonList?id="};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v_fenlei_data, container, false); // 加载fragment布局
//        ViewUtils.inject(this, view); // 注入view和事件
        DataApplication application = (DataApplication) getActivity().getApplication();
        String id = application.getId();
        Log.i("idlist2", id);
        bitmapUtils = BitmapHelp.getBitmapUtils(this.getActivity().getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        // 设置最大宽高, 不设置时更具控件属性自适应.
        //bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()).scaleDown(3));
        // 滑动时加载图片，快速滑动时不加载图片
        // imageListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
        imageListAdapter = new ImageListAdapter(inflater.getContext());
        imageListView = (ListView) view.findViewById(R.id.v_fenlei_data_listView);
        imageListView.setAdapter(imageListAdapter);
        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idText = (TextView) view.findViewById(R.id.v_fenlei_data_listView_id);
                String pid = idText.getText().toString().trim();
                DataApplication application = (DataApplication) getActivity().getApplication();
                application.setId(pid);
                Intent intent = new Intent(getActivity(), BookDataActivity.class);
                startActivity(intent);
            }
        });
        // 加载url请求返回的图片连接给listview
        // 这里只是简单的示例，并非最佳实践，图片较多时，最好上拉加载更多...
        for (String url : imgSites) {
            loadImgList(url + id);
        }
        return view;
    }


    private void loadImgList(String url) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                imageListAdapter.addSrc(getImgSrcList(responseInfo.result));
                imageListAdapter.addtxt(getImgSrcList2(responseInfo.result));
                imageListAdapter.addId(getImgSrcList3(responseInfo.result));
                imageListAdapter.notifyDataSetChanged();// 通知listview更新数据
            }

            @Override
            public void onFailure(HttpException error, String msg) {
            }
        });
    }

    private class ImageListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;
        private ArrayList<String> imgSrcList, txtList, textId;

        public ImageListAdapter(Context context) {
            super();
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
            this.imgSrcList = new ArrayList<String>();
            this.txtList = new ArrayList<String>();
            this.textId = new ArrayList<String>();
        }

        public void addSrc(List<String> imgSrcList) {
            this.imgSrcList.addAll(imgSrcList);
        }

        public void addtxt(List<String> txtList) {
            this.txtList.addAll(txtList);
        }

        public void addId(List<String> id) {
            this.textId.addAll(id);
        }

        @Override
        public int getCount() {
            return imgSrcList.size();
        }

        @Override
        public Object getItem(int position) {
            return imgSrcList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ImageItemHolder holder = null;
            if (view == null) {
                view = mInflater.inflate(R.layout.v_fenlei_data_style, null);
                holder = new ImageItemHolder();
                ViewUtils.inject(holder, view);
                view.setTag(holder);
            } else {
                holder = (ImageItemHolder) view.getTag();
            }
            bitmapUtils.display(holder.imgItem, imgSrcList.get(position));
            holder.text.setText(txtList.get(position));
            holder.id.setText(textId.get(position));
            return view;
        }
    }

    private class ImageItemHolder {
        @ViewInject(R.id.v_fenlei_data_listView_img)
        private ImageView imgItem;

        @ViewInject(R.id.v_fenlei_data_listView_text)
        private TextView text;

        @ViewInject(R.id.v_fenlei_data_listView_id)
        private TextView id;

    }

    /**
     * 得到网页中图片的地址
     */
    public static List<String> getImgSrcList(String htmlStr) {
        List<String> pics = new ArrayList<String>();

        JSONTokener jsonTokener = new JSONTokener(htmlStr);
        try {
            JSONObject jsonObject1 = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(i);
                pics.add("https://www.54lxb.cn/vclass" + jsonObject3.getString("sonimageurl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pics;
    }

    public static List<String> getImgSrcList2(String htmlStr) {
        List<String> pics = new ArrayList<String>();

        JSONTokener jsonTokener = new JSONTokener(htmlStr);
        try {
            JSONObject jsonObject1 = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(i);
                pics.add(jsonObject3.getString("sontypename"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pics;
    }

    public static List<String> getImgSrcList3(String htmlStr) {
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

}
