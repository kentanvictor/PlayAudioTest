package com.example.dell.playaudiotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //初始化創建MediaPlayer的實例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        //動態申請WRITE_EXTERNAL_STORAGE權限，讀取SD卡
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else
        {
            initMediaPlayer();//初始化MdiaPlayer
        }
    }
    private void initMediaPlayer()
    {
        try
        {
            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
            mediaPlayer.setDataSource(file.getPath());//指定音頻文件的路徑
            mediaPlayer.prepare();//讓音頻文件進入到準備狀態
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    initMediaPlayer();
                }
                else
                {
                    Toast.makeText(this,"拒接權限將無法使用程序",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.start();//開始播放
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();//暫停播放
                }
                break;
            case R.id.stop:
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.reset();//如果MEdiaPlayer正在播放音頻，則調用reset()方法將MediaPlayer重置為剛剛創建的狀態
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
