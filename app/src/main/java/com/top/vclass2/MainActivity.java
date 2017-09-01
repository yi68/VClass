package com.top.vclass2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zym
 * @MainActivitty 显示程序主界面内容
 * @initView 初始化所有用到的控件
 * @initEvent 设置按钮监听事件（此处监听的为LinearLayout组件）
 * @setSelect 设置滑动效果（改编图片资源，实现更好的视觉效果）
 * @Bmob 6752e35f919a41b036024478056dc34f
 * @ChangeTime: 2017年5月9日 22:28:42
 */
public class MainActivity extends FragmentActivity implements OnClickListener {

    private ViewPager main_viewPager;
    private LinearLayout shouye_tab, fenlei_tab, xuexi_tab, geren_tab;
    private List<Fragment> mFragment;
    private ImageButton shouye_img, fenlei_img, xuexi_img, geren_img;

    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        setSelect(0);
    }

    private void initEvent() {
        shouye_tab.setOnClickListener(this);
        fenlei_tab.setOnClickListener(this);
        xuexi_tab.setOnClickListener(this);
        geren_tab.setOnClickListener(this);

    }

    private void initView() {
        // 初始化ViewPager组件
        main_viewPager = (ViewPager) findViewById(R.id.main_viewPager);
        // 初始化空LinearLayout组件
        shouye_tab = (LinearLayout) findViewById(R.id.bottom_tab_shouye);
        fenlei_tab = (LinearLayout) findViewById(R.id.bottom_tab_fenlei);
        xuexi_tab = (LinearLayout) findViewById(R.id.bottom_tab_xuexi);
        geren_tab = (LinearLayout) findViewById(R.id.bottom_tab_geren);
        // 初始化ImgButton控件
        shouye_img = (ImageButton) findViewById(R.id.bottom_img_shouye);
        fenlei_img = (ImageButton) findViewById(R.id.bottom_img_fenlei);
        xuexi_img = (ImageButton) findViewById(R.id.bottom_img_xuexi);
        geren_img = (ImageButton) findViewById(R.id.bottom_img_geren);
        // 添加Fragment到集合
        mFragment = new ArrayList<Fragment>();
        mFragment.add(new ShouyeFragment());
        mFragment.add(new FenleiFragment());
        mFragment.add(new XuexiFragment());
        mFragment.add(new GerenFragment());
        // 将集合添加到FragmentAdapter适配器中
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragment);
        main_viewPager.setAdapter(adapter);
        // 预加载页面（页面总数4页，参数含义：除去当前显示页面，需额外预加载三页，此属性用于解决滑动到第三个Fragment后第一个Fragment数据消失）
        main_viewPager.setOffscreenPageLimit(3);
        main_viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int item = main_viewPager.getCurrentItem();
                setTab(item);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_tab_shouye:
                setSelect(0);
                break;
            case R.id.bottom_tab_fenlei:
                setSelect(1);
                break;
            case R.id.bottom_tab_xuexi:
                setSelect(2);
                break;
            case R.id.bottom_tab_geren:
                setSelect(3);
                break;
            default:
                break;
        }
    }

    private void setSelect(int i) {
        setTab(i);
        main_viewPager.setCurrentItem(i);
    }

    private void setTab(int i) {
        resetImg();
        switch (i) {
            case 0:
                shouye_img.setImageResource(R.drawable.home_blue);
                break;
            case 1:
                fenlei_img.setImageResource(R.drawable.fenlei_blue);
                break;
            case 2:
                xuexi_img.setImageResource(R.drawable.xuexi_blue);
                break;
            case 3:
                geren_img.setImageResource(R.drawable.geren_blue);
                break;
        }
    }

    private void resetImg() {
        shouye_img.setImageResource(R.drawable.home_black);
        fenlei_img.setImageResource(R.drawable.fenlei_black);
        xuexi_img.setImageResource(R.drawable.xuexi_black);
        geren_img.setImageResource(R.drawable.geren_black);

    }

    /**
     *
     * @param menu
     * @return
     */

    /**
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * getMenuInflater().inflate(R.menu.main, menu);
     * return super.onCreateOptionsMenu(menu);
     * }
     * @Override public boolean onOptionsItemSelected(MenuItem item) {
     * switch (item.getItemId()) {
     * case R.id.action_settings:
     * Toast.makeText(MainActivity.this, "你点击了菜单", Toast.LENGTH_SHORT).show();
     * break;
     * }
     * return super.onOptionsItemSelected(item);
     * }
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
