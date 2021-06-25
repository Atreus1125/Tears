package com.example.tears;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.*;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton leftButton;
    private ImageButton playButton;
    private ImageButton rightButton;
    private TextView infoTextView;
    private SeekBar seekBar;
    private LinkedHashMap musicList;
    private Timer timer;
    private TimerTask timerTask;
    private Music music;
    private int[] musicId;
    private String[] musicPath;
    private boolean playTag = false;//标记现在是true否false有歌曲播放
    private boolean newTag = false;//标记刚才是true否false由歌曲播放过
    private boolean seekBarTag = false;//标记进度表当前是true否false被触摸
    private int position;
    private int sourceTag;

    MediaPlayer mediaPlayer = new MediaPlayer();
    Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        leftButton = findViewById(R.id.leftButton);
        playButton = findViewById(R.id.playButton);
        rightButton = findViewById(R.id.rightButton);
        infoTextView = findViewById(R.id.infoTextView);
        seekBar = findViewById(R.id.seekBar);
        leftButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

        seekBar.setEnabled(false);

        infoTextView.setSelected(true);

        Intent intent = this.getIntent();
        musicId = intent.getIntArrayExtra("musicId");
        musicPath = intent.getStringArrayExtra("musicPath");
        position = intent.getIntExtra("position", 0);
        sourceTag = intent.getIntExtra("sourceTag", -1);
        Log.i("2007", "musicId: " + musicId[position]);//列表传给播放器的音乐id
        Log.i("2007", "musicPath: " + musicPath[position]);//列表传给播放器的音乐路径
        Log.i("2007", "position: " + position);//音乐的列表位置
        Log.i("2007", "sourceTag: " + sourceTag);//音乐的来源列表

        musicList = util.getAllMusic(this);

        initMusic(position);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    playButton.setImageResource(R.mipmap.pause);
                    mp.reset();
                    initMusic(++position);
                    mp.setDataSource(music.getPath());
                    mp.prepare();
                    mp.start();
                    playTag = true;
                    newTag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarTag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int touchProgress = seekBar.getProgress();
                if (newTag == true) {
                    mediaPlayer.seekTo(touchProgress);
                }
                seekBarTag = false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(mediaPlayer.getDuration() / seekBar.getMax() * percent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.playButton:
                    if (playTag == false) {
                        if (newTag == false) {
                            playButton.setImageResource(R.mipmap.pause);
                            mediaPlayer.setDataSource(music.getPath());
                            mediaPlayer.prepare();
                            seekBar.setEnabled(true);
                            //更新进度条
                            seekBar.setMax(mediaPlayer.getDuration());
                            timer = new Timer();
                            timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    if(seekBarTag == false) {
                                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    }
                                }
                            };
                            timer.schedule(timerTask, 0, 1000);
                            mediaPlayer.start();
                            playTag = true;
                            newTag = true;
                        } else {
                            playButton.setImageResource(R.mipmap.pause);
                            mediaPlayer.start();
                            playTag = true;
                            newTag = true;
                        }
                    } else {
                        playButton.setImageResource(R.mipmap.start);
                        mediaPlayer.pause();
                        playTag = false;
                        newTag = true;
                    }
                    break;

                case R.id.leftButton:
                    playButton.setImageResource(R.mipmap.pause);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    position = position - 1;
                    Log.i("2020", String.valueOf(position));
                    if (position < 0) {
                        position = musicId.length-1;
                        Log.i("2020", String.valueOf(position));
                    }
                    initMusic(position);
                    mediaPlayer.setDataSource(music.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    playTag = true;
                    newTag = true;
                    break;

                case R.id.rightButton:
                    playButton.setImageResource(R.mipmap.pause);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    initMusic(++position);
                    mediaPlayer.setDataSource(music.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    playTag = true;
                    newTag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据列表位置播放对应歌曲
     * @param position
     */
    public void initMusic(int position) {
        if (sourceTag == 0) {
            Iterator iterator = musicList.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Log.i("2010", entry.getKey().toString());
                Log.i("2011", String.valueOf(musicId[position % (musicId.length)]));
                if (Integer.parseInt(entry.getKey().toString()) == musicId[position % (musicId.length)]) {
                    music = (Music) entry.getValue();
                    Log.i("2012", music.toString());
                    infoTextView.setText(music.getName() + "-" + music.getArtist());
                }
            }
        } else if (sourceTag == 1) {
            music = new Music();
            music.setPath(musicPath[position % (musicId.length)]);
            infoTextView.setText(util.getMusicInfo(music.getPath()));
        }
    }

    /**
     * 返回时释放播放器资源
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(timer != null) {
                timer.cancel();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}