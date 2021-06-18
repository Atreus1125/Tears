package com.example.tears;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private ProgressBar progressBar;
    Util util = new Util();
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_main);

        /*setTitle("异步非阻塞多线程绿钻加密文件转MP3");*/
        setTitle("绿钻加密文件转MP3");
        progressBar = findViewById(R.id.waitProgressBar);
        progressBar.setVisibility(View.INVISIBLE);//进度条初始不可见
        button = findViewById(R.id.ActionButton);
        button.setOnClickListener(this);

        Permission.isGrantExternalRW(this, 1);//判断是否获得存储读写权限
        Log.i("应用程序文件根目录", getApplicationContext().getExternalFilesDir(null).toString());

        /*creatTestFile();*///新建模拟文件
    }

    @Override
    public void onClick(View v) {
        String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qqmusic/song";
        File musicDir = new File(musicPath);
        if (musicDir.exists() && musicDir.isDirectory()) {
            File[] musicFiles = musicDir.listFiles();//下载的全部文件
            if (musicFiles != null) {
                if (musicFiles.length != 0) {
                    button.setText("正在转换...");
                    progressBar.setVisibility(View.VISIBLE);
                    for (File file : musicFiles) {
                        Log.i("第" + count++ + "个文件", file.getName());
                        if (file.getName().endsWith(".qmcflac") || file.getName().endsWith(".qmc3") || file.getName().endsWith(".qmc0")) {
                            util.doTransform(file, getApplicationContext().getExternalFilesDir(null).toString());//对单个文件进行解码
                        }
                    }
                    Toast.makeText(getApplicationContext(), "转换完成", Toast.LENGTH_SHORT).show();
                    button.setText("开始转换");
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "没有找到需要转换的文件", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "qqmusic/song文件夹不存在", Toast.LENGTH_SHORT).show();
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
        Log.i("新生成的测试文件位置",qmcflac.getAbsolutePath());
        if (!qmcflac.exists()) {
            try {
                qmcflac.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
