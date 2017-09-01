package com.top.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.top.vclass2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zym on 2017/5/10.
 */
public class ShouyeAdapter extends BaseAdapter {

    private Context context;
    private BitmapUtils bitmapUtils;
    private LayoutInflater layoutInflater;
    private ArrayList<String> imgList, txtlist, textid;

    public ShouyeAdapter(Context context) {
        this.context = context;
        this.bitmapUtils = new BitmapUtils(context);
        this.layoutInflater = LayoutInflater.from(context);
        this.imgList = new ArrayList<String>();
        this.txtlist = new ArrayList<String>();
        this.textid = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addText(List<String> txt) {
        this.txtlist.addAll(txt);
    }

    public void addImg(List<String> img) {
        this.imgList.addAll(img);
    }

    public void addId(List<String> id) {
        this.textid.addAll(id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.v_shouye_main_style, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.v_shouye_main_style_img);
            viewHolder.textView_txt = (TextView) convertView.findViewById(R.id.v_shouye_main_style_txt);
            viewHolder.textView_id = (TextView) convertView.findViewById(R.id.v_shouye_main_style_txtid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(viewHolder.imageView, imgList.get(position));
        viewHolder.textView_txt.setText(txtlist.get(position));
        viewHolder.textView_id.setText(textid.get(position));
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView textView_txt;
        private TextView textView_id;
    }
}
