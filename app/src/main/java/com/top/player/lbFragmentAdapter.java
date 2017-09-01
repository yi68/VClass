package com.top.player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.top.vclass2.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by zym on 2017/5/25.
 */
public class lbFragmentAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> textId;
    private List<String> textData;

    //构造方法
    public lbFragmentAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.textId = new ArrayList<String>();
        this.textData = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return textId.size();
    }

    @Override
    public Object getItem(int position) {
        return textId.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addId(List<String> id) {
        this.textId.addAll(id);
    }

    public void addData(List<String> data) {
        this.textData.addAll(data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.play_lis_fragment_style, null);
            viewHolder.textView_id = (TextView) convertView.findViewById(R.id.play_lis_fragment_style_textView_id);
            viewHolder.textView_data = (TextView) convertView.findViewById(R.id.play_lis_fragment_style_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView_id.setText(textId.get(position));
        viewHolder.textView_data.setText(textData.get(position));
        return convertView;
    }

    //优化
    class ViewHolder {
        private TextView textView_id;
        private TextView textView_data;
    }
}
