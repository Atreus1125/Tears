package com.example.tears;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button actionButton;
    private Button listButton;
    private Button vipListButton;
    private ProgressBar progressBar;
    private ImageButton imageButton;
    private Intent intent;
    private int Tag = 0;

    private int count = 1;

    Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.waitProgressBar);
        actionButton = findViewById(R.id.actionButton);
        /*actionButton.setTypeface(Typeface.createFromFile("font/font_zh.ttf"));*/
        listButton = findViewById(R.id.listButton);
        vipListButton = findViewById(R.id.vipListButton);
        imageButton = findViewById(R.id.helpImageButton);

        setTitle("绿钻加密文件解码器");

        progressBar.setVisibility(View.INVISIBLE);//进度条初始不可见
        actionButton.setOnClickListener(this);
        listButton.setOnClickListener(this);
        vipListButton.setOnClickListener(this);

        imageButton.setOnClickListener(this);

        Permission.isGrantExternalRW(this, 1);//判断是否获得存储读写权限
        Log.i("2000", getApplicationContext().getExternalFilesDir(null).toString());//手机存储根目录

        /*creatTestFile();*///新建模拟文件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionButton:
                String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qqmusic/song";
                File musicDir = new File(musicPath);
                if (musicDir.exists() && musicDir.isDirectory()) {
                    File[] musicFiles = musicDir.listFiles();//下载的全部文件
                    if (musicFiles != null) {
                        if (musicFiles.length != 0) {

                            MainActivity.this.runOnUiThread(() -> {
                                actionButton.setText("正在转换...");
                                progressBar.setVisibility(View.VISIBLE);
                            });

                            new Thread(() -> {
                                for (File file : musicFiles) {
                                    Log.i("2004", "第" + count++ + "个文件" + file.getName());//当前进行转换的文件信息
                                    if (file.getName().endsWith(".qmcflac") || file.getName().endsWith(".qmc3") || file.getName().endsWith(".qmc0")) {
                                        util.doTransform(file, getApplicationContext().getExternalFilesDir(null).toString());//对单个文件进行解码
                                    }
                                }
                            }).start();

                            new Thread(() -> {
                                try {
                                    Looper.prepare();//给子线程创建消息循环
                                    Thread.sleep(3000);
                                    Toast.makeText(getApplicationContext(), "转换完成", Toast.LENGTH_SHORT).show();
                                    actionButton.setText("开始转换");
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Looper.loop();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();

                        } else {
                            Toast.makeText(getApplicationContext(), "没有找到需要转换的文件", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "qqmusic/song文件夹不存在", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.listButton:
                intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                break;

            case R.id.vipListButton:
                intent = new Intent(this, VipListActivity.class);
                startActivity(intent);
                break;

            case R.id.helpImageButton:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 根据系统时间新建.qmcflac文件模拟加密文件
     */
    public void creatTestFile() {
        String path = null;
        path = "/qqmusic/song/";
        /*path = "/Android/data/com.example.tears/files/";*/
        File qmcflac = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + path + System.currentTimeMillis() + ".qmcflac");
        Log.i("2002", qmcflac.getAbsolutePath());//新生成的测试文件位置
        if (!qmcflac.exists()) {
            try {
                qmcflac.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使字体大小不随系统设置变化
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration newConfig = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (resources != null && newConfig.fontScale != 1) {
            newConfig.fontScale = 1;
            if (Build.VERSION.SDK_INT >= 17) {
                Context configurationContext = createConfigurationContext(newConfig);
                resources = configurationContext.getResources();
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
            } else {
                resources.updateConfiguration(newConfig, displayMetrics);
            }
        }
        return resources;
    }
}
