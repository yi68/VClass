package com.top.Splash;

/**
 * @main 启动界面
 * @author zym
 */

import com.top.guide.GuideMainActivity;
import com.top.vclass2.MainActivity;
import com.top.vclass2.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity {

    private SharedPreferences sh;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题栏+全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        version = (TextView) findViewById(R.id.splash_txt_version);
        //创建软件包管理器，用来获取软件版本
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            version.setText("版本：" + info.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sh = getSharedPreferences("one", MODE_PRIVATE);
                int Message = sh.getInt("one", 0);
                if (Message != 1) {
                    startActivity(new Intent(SplashActivity.this, GuideMainActivity.class));
                    SplashActivity.this.finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            }
        }, 2000);
    }
}
