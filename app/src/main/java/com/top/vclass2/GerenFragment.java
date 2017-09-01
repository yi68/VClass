package com.top.vclass2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.top.login.LoginActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GerenFragment extends Fragment {

    private ListView list;
    private Button login_button;
    private TextView user;
    private String[] title = new String[]{"Item1", "Item2", "Item3", "Item4", "Item5", "关于", "设置"};
    private int[] limg = new int[]{R.drawable.forward_grey};
    private int[] rimg = new int[]{0, 1, 2, 3, 4, R.drawable.info_black, R.drawable.settings_black};
    private boolean frist = true;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.v_geren, container, false);
        user = (TextView) view.findViewById(R.id.v_geren_txt_user);
        login_button = (Button) view.findViewById(R.id.v_geren_button_login);
        preferences = getActivity().getSharedPreferences("message", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        if ("".equals(name)) {
            user.setText("还没有登陆哦");
            login_button.setText("登陆");
        } else {
            user.setText(name);
            login_button.setText("注销");
            frist = false;
        }
        login_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (frist) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "注销成功", Toast.LENGTH_SHORT).show();
                    user.setText("还没有登陆哦！");
                    login_button.setText("登陆");
                    frist = true;
                    Editor edit = preferences.edit();
                    edit.remove("name");
                    edit.commit();
                }

            }
        });
        list = (ListView) view.findViewById(R.id.v_geren_listView);
        List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < title.length; i++) {
            HashMap<String, Object> hList = new HashMap<String, Object>();
            hList.put("rimg", rimg[i]);
            hList.put("title", title[i]);
            hList.put("limg", limg[0]);
            mList.add(hList);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mList, R.layout.v_geren_list_style,
                new String[]{"rimg", "title", "limg"},
                new int[]{R.id.v_geren_list_img, R.id.V_geren_list_style_txt, R.id.v_geren_img_button});
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
            }

            private void select(int i) {
                switch (i) {
                    case 5:
                        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                        ab.setCancelable(false);
                        ab.setIcon(R.drawable.info_black);
                        ab.setTitle("关于");
                        ab.setMessage("程序员正在抓紧完善中...");
                        ab.setNegativeButton("确定", null);
                        ab.show();
                        break;
                }
            }
        });
        return view;
    }
}
