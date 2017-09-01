package com.top.player;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.top.application.DataApplication;
import com.top.vclass2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zym on 2017/5/15.
 *
 * @URL: http://139.199.68.51/vclass/mobile/courseDuty/1000/list
 */
public class PlayerActivity extends FragmentActivity implements View.OnClickListener {

    private DataApplication application;
    private String id;

    private VideoView videoView;
    private LinearLayout controllerBar;
    private ImageView play_img, screen_img;
    private TextView start_time, max_time;
    private SeekBar time_seekbar;
    private RelativeLayout videoLayout;

    public static int UPDATA_ID = 0;

    private boolean isScreen = false;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private String[] detailUrl = {" https://www.54lxb.cn/vclass/mobile/courseDuty/list?id="};

    //Fragment
    private TextView lb_text, js_text, pl_text;
    private ViewPager viewPager;
    private List<Fragment> fragments;

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play_message_main);
        initView();
        initEvent();
        setSelect(0);
        new MyAsynctask().execute(detailUrl);
//        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.a));
//        videoView.setVideoURI(Uri.parse(video));
        setPlayerEvent();
    }

    private void initView() {
        application = (DataApplication) this.getApplication();
        id = application.getId();
        //初始化控件
        videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
        videoView = (VideoView) findViewById(R.id.play_videoView);
        controllerBar = (LinearLayout) findViewById(R.id.play_controllerBar_layout);
        play_img = (ImageView) findViewById(R.id.play_start_video);
        screen_img = (ImageView) findViewById(R.id.play_screen_video);
        start_time = (TextView) findViewById(R.id.play_time_start);
        max_time = (TextView) findViewById(R.id.play_time_max);
        time_seekbar = (SeekBar) findViewById(R.id.play_time_seekBar);
        //Fragment相关组件初始化
        lb_text = (TextView) findViewById(R.id.play_kc_lis);
        js_text = (TextView) findViewById(R.id.play_kc_js);
        pl_text = (TextView) findViewById(R.id.play_kc_pl);
        viewPager = (ViewPager) findViewById(R.id.play_viewPager);
        //初始化Fragment集合
        fragments = new ArrayList<Fragment>();
        fragments.add(new lbFragment());
        fragments.add(new jsFragment());
        fragments.add(new plFragment());
        //将集合添加到自定义adapter中
        PlayerFragmentAdapter adapter = new PlayerFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        //缓存页面
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvent() {
        lb_text.setOnClickListener(this);
        js_text.setOnClickListener(this);
        pl_text.setOnClickListener(this);
    }

    //视频播放相关点击事件
    private void setPlayerEvent() {
        play_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    play_img.setImageResource(R.drawable.play_bt_style);
                    videoView.pause();
                    handler.removeMessages(UPDATA_ID);
                } else {
                    play_img.setImageResource(R.drawable.pause_bt_style);
                    videoView.start();
                    handler.sendEmptyMessage(UPDATA_ID);
                }
            }
        });

        time_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTmieFormat(start_time, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(UPDATA_ID);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = time_seekbar.getProgress();
                videoView.seekTo(progress);
                handler.sendEmptyMessage(UPDATA_ID);
            }
        });

        screen_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
    }

    /**
     * @message: 获取网络视频
     */

    class MyAsynctask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            final String[] videorl = {""};
            String url = params[0];
            Log.i("zym", url);
            Request.Builder builder = new Request.Builder();
            Request request = builder.get().url(url + id).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String html = response.body().string();
                    Log.i("zym", html);
                    JSONTokener jsonTokener = new JSONTokener(html);
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        String state = jsonObject.getString("state");
                        Log.i("state", state);
                        if (state.equals("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            videorl[0] = jsonObject1.getString("videourl");
                        } else {
                            videorl[0] = "";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return videorl;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            try {
                Thread.sleep(200);
                Log.i("zym", s[0]);
                if (s[0].equals("")) {
                    Toast.makeText(PlayerActivity.this, "管理员正在努力添加视频中！", Toast.LENGTH_SHORT).show();
                } else {

                    videoView.setVideoURI(Uri.parse(s[0]));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param textView
     * @param time
     * @main: 转换时间格式
     */
    private void setTmieFormat(TextView textView, int time) {
        int second = time / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str = null;
        if (hh != 0) {
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
        }
        textView.setText(str);
    }

    // 主线程更新UI组件
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取播放进度
            if (msg.what == UPDATA_ID) {
                int startTime = videoView.getCurrentPosition();
                //获取总进度
                int maxTime = videoView.getDuration();
                //格式化视频时间
                setTmieFormat(start_time, startTime);
                setTmieFormat(max_time, maxTime);
                //更新进度条
                time_seekbar.setMax(maxTime);
                time_seekbar.setProgress(startTime);
                //自动更新handler
                handler.sendEmptyMessageDelayed(UPDATA_ID, 500);
            }
        }
    };


    /**
     * @main: 设置布局大小
     */

    private void setLayoutScreen(int width, int height) {
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        videoView.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams1 = videoLayout.getLayoutParams();
        layoutParams1.width = width;
        layoutParams1.height = height;
        videoLayout.setLayoutParams(layoutParams1);
    }

    /**
     * @param newConfig
     * @mian: 监听屏幕方向改变
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLayoutScreen(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            isScreen = true;
            getWindow().clearFlags((WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN));
            getWindow().addFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
        } else {
            // 转换像素格式
            int dp2px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, PlayerActivity.this.getResources().getDisplayMetrics());
            setLayoutScreen(ViewGroup.LayoutParams.MATCH_PARENT, dp2px);
            isScreen = false;
            getWindow().clearFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
            getWindow().addFlags((WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(UPDATA_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.sendEmptyMessage(UPDATA_ID);
    }

    //Fragment-->TextView点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_kc_lis:
                setSelect(0);
                break;
            case R.id.play_kc_js:
                setSelect(1);
                break;
            case R.id.play_kc_pl:
                setSelect(2);
                break;
        }
    }

    //Fragment切换事件
    private void setSelect(int item) {
        setTab(item);
        viewPager.setCurrentItem(item);
    }

    //初始化TextView颜色
    private void resetTextColor() {
        lb_text.setTextColor(Color.BLACK);
        js_text.setTextColor(Color.BLACK);
        pl_text.setTextColor(Color.BLACK);
    }

    //改变TextView颜色实现视觉效果
    public void setTab(int tab) {
        resetTextColor();
        switch (tab) {
            case 0:
                lb_text.setTextColor(Color.BLUE);
                break;
            case 1:
                js_text.setTextColor(Color.BLUE);
                break;
            case 2:
                pl_text.setTextColor(Color.BLUE);
                break;
        }
    }
}
