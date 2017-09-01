package com.top.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.top.application.DataApplication;
import com.top.vclass2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zym on 2017/5/19.
 *
 * @URl: http://139.199.68.51/vclass/mobile/courseDuty/1000/list
 */
public class lbFragment extends Fragment {

    private ListView listView;
    private lbFragmentAdapter adapter;
    private DataApplication application;
    private String detailUrl = "https://www.54lxb.cn/vclass/mobile/courseDuty/list?id=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_lis_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.play_lis_fragment_listView);
        application = (DataApplication) getActivity().getApplication();
        String id = application.getId();
        loadUrl(detailUrl + id);
        adapter = new lbFragmentAdapter(getActivity().getApplicationContext());
        listView.setAdapter(adapter);
        return view;
    }

    private void loadUrl(String url) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                adapter.addId(getBookId(responseInfo.result));
                adapter.addData(getBookData(responseInfo.result));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private List<String> getBookId(String html) {
        List<String> id = new ArrayList<String>();
        JSONTokener jsonTokener = new JSONTokener(html);
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                id.add(json.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    private List<String> getBookData(String html) {
        List<String> data = new ArrayList<String>();
        JSONTokener jsonTokener = new JSONTokener(html);
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                data.add(json.getString("dutyname"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
