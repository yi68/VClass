package com.top.vclass2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.top.application.DataApplication;
import com.top.data.BitmapFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author zym
 * @FenleiFragment 用于装载程序分类中的数据
 * @hideFragment 用于隐藏Fragment
 * @beginTransaction.show 用于显示Fragment
 */
public class FenleiFragment extends Fragment {

    private ListView fenleiList;
    private Fragment fragment1;
    private FragmentManager fragmentManager;
    private FragmentTransaction beginTransaction;
    private String url = "https://www.54lxb.cn";
    private List<Map<String, Object>> typelist = new ArrayList<Map<String, Object>>();

    private DataApplication application;
    private boolean isOn = true;

    /**
     * @message 接收子线程发送的message，用于更新主线程ListView控件
     * @parm Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<Map<String, Object>> listItem = (List<Map<String, Object>>) msg.obj;
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItem, R.layout.v_fenlei_list_style,
                    new String[]{"typename", "id"}, new int[]{R.id.v_fenlei_list_style_txt, R.id.v_fenlei_list_style_id});
            fenleiList.setAdapter(adapter);
        }
    };

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return null
     * @message 创建当前Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v_fenlei, container, false);
        fenleiList = (ListView) view.findViewById(R.id.v_fenlei_listView);
        application = (DataApplication) getActivity().getApplication();
        new Thread(runnable).start();
        if (isOn) {
            fragmentManager = getFragmentManager();
            //创建Fragment操作事件对象
            beginTransaction = fragmentManager.beginTransaction();
            String pid = "1000";
            application.setId(pid);
            fragment1 = new BitmapFragment();
            beginTransaction.add(R.id.v_fenlei_linear_zairu, fragment1);
            beginTransaction.show(fragment1);
            //提交Fragment（必须实现的方法）
            beginTransaction.commit();
            isOn = false;
        }
        //ListView点击事件
        fenleiList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获得Fragment管理者
                fragmentManager = getFragmentManager();
                //创建Fragment操作事件对象
                beginTransaction = fragmentManager.beginTransaction();
                //隐藏Fragment（用于解决显示下一个Fragment时未清除前一个Fragment导致程序FC）
                hideFragment(beginTransaction);
                TextView textView = (TextView) view.findViewById(R.id.v_fenlei_list_style_id);
                String pid = textView.getText().toString();
                application.setId(pid);
                fragment1 = new BitmapFragment();
                beginTransaction.add(R.id.v_fenlei_linear_zairu, fragment1);
                beginTransaction.show(fragment1);
                //提交Fragment（必须实现的方法）
                beginTransaction.commit();
            }

            // 隐藏Fragment
            private void hideFragment(FragmentTransaction beginTransaction) {
                if (fragment1 != null) {
                    beginTransaction.hide(fragment1);
                }
            }
        });
        return view;
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getType(url);
        }
    };

    private void getType(String url) {
        url = url + "/vclass/mobile/courseType/list";
        Log.i("excute", url);
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        Call newCall = client.newCall(request);
        newCall.enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                String body = arg0.body().string();
                Log.i("excute1", body);
                typelist = jsonArray(body);
                Message message = new Message();
                message.obj = typelist;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Log.i("excute2", "执行失败！");
                arg1.printStackTrace();
            }
        });
    }

    /**
     * @param body
     * @return List<Map<String, Object>>
     * @message 解析listView数组中的课程类型
     */
    private List<Map<String, Object>> jsonArray(String body) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONTokener jsonTokener = new JSONTokener(body);
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                HashMap<String, Object> lis = new HashMap<String, Object>();
                lis.put("id", jsonObject2.getString("id"));
                lis.put("typename", jsonObject2.getString("typename"));
                list.add(lis);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
