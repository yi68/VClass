package com.top.book;

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
 * Created by zym on 2017/5/9.
 * ChangeTime: 2017年5月9日 22:27:01
 */
public class BookAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imgList, txtList, txtId;
    private LayoutInflater layoutInflater;
    private BitmapUtils bitmapUtils;

    public BookAdapter(Context context) {
        this.context = context;
        this.bitmapUtils = new BitmapUtils(context);
        this.layoutInflater = LayoutInflater.from(context);
        this.imgList = new ArrayList<String>();
        this.txtList = new ArrayList<String>();
        this.txtId = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return txtList.size();
    }

    @Override
    public Object getItem(int position) {
        return txtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addSrc(List<String> imgSrcList) {
        this.imgList.addAll(imgSrcList);
    }

    public void addTxt(List<String> txtList) {
        this.txtList.addAll(txtList);
    }

    public void addId(List<String> txtid) {
        this.txtId.addAll(txtid);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.v_book_data_style, null);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.v_book_data_listView_img);
            viewHolder.itemTxt = (TextView) convertView.findViewById(R.id.v_book_data_listView_text);
            viewHolder.id = (TextView) convertView.findViewById(R.id.v_book_data_listView_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(viewHolder.itemImage, imgList.get(position));
        viewHolder.itemTxt.setText(txtList.get(position));
        viewHolder.id.setText(txtId.get(position));
        return convertView;
    }

    class ViewHolder {
        private ImageView itemImage;
        private TextView itemTxt;
        private TextView id;
    }
}
