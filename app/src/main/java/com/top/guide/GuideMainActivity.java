package com.top.guide;

/**
 * @a 引导Activity
 * @author zym
 */

import java.util.ArrayList;
import java.util.List;

import com.top.vclass2.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GuideMainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ImageButton image1, image2, image3;
    private LinearLayout tab1, tab2;
    private RelativeLayout tab3;
    private List<Fragment> listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.g_main);
        Toast.makeText(this, "左滑动查看简介", Toast.LENGTH_LONG).show();
        initView();
        initEvent();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.g_viewPager);
        //**
        image1 = (ImageButton) findViewById(R.id.image_1);
        image2 = (ImageButton) findViewById(R.id.image_2);
        image3 = (ImageButton) findViewById(R.id.image_3);
        //**
        tab1 = (LinearLayout) findViewById(R.id.g_tab1);
        tab2 = (LinearLayout) findViewById(R.id.g_tab2);
        tab3 = (RelativeLayout) findViewById(R.id.g_tab3);
        //**
        listFragment = new ArrayList<Fragment>();
        listFragment.add(new ImageFragment1());
        listFragment.add(new ImageFragment2());
        listFragment.add(new ImageFragment3());
        ImageFragmentAdapter adapter = new ImageFragmentAdapter(getSupportFragmentManager(), listFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int item = viewPager.getCurrentItem();
                setTab(item);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvent() {
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_1:
                select(0);
                break;
            case R.id.image_2:
                select(1);
                break;
            case R.id.image_3:
                select(2);
                break;
        }
    }

    private void select(int i) {
        setTab(i);
        viewPager.setCurrentItem(i);
    }

    private void setTab(int item) {
        resetImg();
        switch (item) {
            case 0:
                image1.setImageResource(R.drawable.pagenow);
                break;
            case 1:
                image2.setImageResource(R.drawable.pagenow);
                break;
            case 2:
                image3.setImageResource(R.drawable.pagenow);
                break;
        }
    }

    private void resetImg() {
        image1.setImageResource(R.drawable.page);
        image2.setImageResource(R.drawable.page);
        image3.setImageResource(R.drawable.page);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "向左滑动结束引导...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
