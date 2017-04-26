package com.lchtime.safetyexpress.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/26.
 */

public class MediaActivity extends Activity {
    @BindView(R.id.mSurfaceView)
    SurfaceView mSurfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;

    private String url;   //视频播放地址
    private boolean flag = true;   //用于判断视频是否在播放中
    private PlayMovie playmove;
    private String uriStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent!=null){
            url = intent.getStringExtra("url");
        }
//        uriStr = "android.resource://" + this.getPackageName() + "/"+R.raw.video_20170209_104311;
        mediaPlayer = new MediaPlayer();
        playmove=new PlayMovie(0);
        surfaceHolder=mSurfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //不缓冲
        surfaceHolder.setKeepScreenOn(true);   //保持屏幕高亮
        surfaceHolder.addCallback(new surFaceView());   //设置监听事件

    }
    private class surFaceView implements SurfaceHolder.Callback {     //上面绑定的监听的事件

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {   //创建完成后调用
            Log.i("yang","surfaceCreated======");
            playmove.start();   //表明是第一次开始播放
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) { //activity调用过pase方法，保存当前播放位置
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                flag = false;
                Log.i("yang","surfaceDestroyed======");
            }
        }
    }
    class PlayMovie extends Thread {   //播放视频的线程

        int post = 0;

        public PlayMovie(int post) {
            this.post = post;
            Log.i("yang","PlayMovie======");
        }
        @Override
        public void run() {
            Message message = Message.obtain();
            Log.i("yang","run======");
            try {
                url="http://baobab.wdjcdn.com/145076769089714.mp4";
//                Uri uri= Uri.parse(uriStr);
                if(!TextUtils.isEmpty(url)){
                    Log.i("yang","uri!=null======");
                    mediaPlayer.reset();    //回复播放器默认
//                    mediaPlayer.setDataSource(MediaActivity.this,uri);   //设置播放路径
                    mediaPlayer.setDataSource(url);   //设置播放路径
                    mediaPlayer.setDisplay(surfaceHolder);  //把视频显示在SurfaceView上
                    mediaPlayer.setOnPreparedListener(new Ok(post));  //设置监听事件
                    mediaPlayer.prepare();  //准备播放
                }else{
                    Toast.makeText(MediaActivity.this,"视频未下载！",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                message.what = 2;
            }
            super.run();
        }
    }
    class Ok implements MediaPlayer.OnPreparedListener {
        int postSize;

        public Ok(int postSize) {
            this.postSize = postSize;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            flag = true;

            if (mediaPlayer != null) {
                mediaPlayer.start();  //开始播放视频
            } else {
                return;
            }
            Log.i("yang","onPrepared======");
        }
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
