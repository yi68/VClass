package com.top.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.top.vclass2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zym on 2017/5/19.
 */
public class plFragment extends Fragment {

    private ListView listView;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_pl_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.play_pl_listView);
        button = (Button) view.findViewById(R.id.play_pl_Button);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", "张三" + i);
            map.put("time", "2015-05-20");
            map.put("message", "我娃后的hi哦啊我会懂啊how等候啊哈第哦啊和我斗啊我hi哦我");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.play_pl_fragment_style, new String[]{"name", "time", "message"}, new int[]{R.id.play_pl_fragment_username, R.id.play_pl_fragment_time, R.id.play_pl_fragment_message});
        listView.setAdapter(adapter);
        return view;
    }
}
