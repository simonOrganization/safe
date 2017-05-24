package com.lchtime.safetyexpress.ui.circle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.dalong.recordlib.RecordVideoActivity;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.vip.VipInfoUI;

/**
 * 视频播放
 */
public class VideoPlayActivity extends Activity implements View.OnClickListener{
    public static final String TAG = VideoPlayActivity.class.getSimpleName();
    public static final int FILE_TYPE_VIDEO=0;
    public static final int FILE_TYPE_PHOTO = 1;
    private VideoView videoView;

    private String filePath;
    private int fileType;
    private int direction;
    private LinearLayout videoUse,videoCancel;
    private ImageView photoPlay;
    private RelativeLayout title;
    private RelativeLayout framelayout;
    private TextView tvDelete;

//    @SuppressLint("ValidFragment")
//    public VideoPlayActivity(String filePath, int type) {
//        this.filePath = filePath;
//        this.fileType = type;
//    }
//    @SuppressLint("ValidFragment")
//    public VideoPlayActivity(String filePath, int type, int direction) {
//        this.filePath = filePath;
//        this.fileType = type;
//        this.direction = direction;
//    }

    private String titleText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        this.filePath = getIntent().getStringExtra("filePath");
        this.fileType = getIntent().getIntExtra("fileType",0);
        this.direction = getIntent().getIntExtra("direction",0);
        initView();
        titleText = getIntent().getStringExtra("title_text");
        if (!TextUtils.isEmpty(titleText)){
            titleText = "选取";
            tvDelete.setText(titleText);
        }else {
            titleText = "删除";
            tvDelete.setText(titleText);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view=inflater.inflate(com.dalong.recordlib.R.layout.fragment_video_play, container, false);
//        initView(view);
//        return view;
//    }

    private void initView() {
        title = (RelativeLayout)findViewById(R.id.rl_title);
        videoView = (VideoView)findViewById(R.id.video_play);
        framelayout = (RelativeLayout)findViewById(R.id.fl_videoview);
        photoPlay = (ImageView)findViewById(R.id.photo_play);
        videoCancel = (LinearLayout)findViewById(R.id.video_cancel);
        videoUse = (LinearLayout)findViewById(R.id.video_use);
        tvDelete = (TextView)findViewById(R.id.tv_delete);
        videoCancel.setOnClickListener(this);
        videoUse.setOnClickListener(this);
        framelayout.setOnClickListener(this);
        if(fileType==FILE_TYPE_VIDEO){
            videoView.setVisibility(View.VISIBLE);
            photoPlay.setVisibility(View.GONE);
            videoView.setVideoURI(Uri.parse(filePath));
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.setVideoPath(filePath);
                    videoView.start();
                }
            });
        }else if(fileType==FILE_TYPE_PHOTO){
            videoView.setVisibility(View.GONE);
            photoPlay.setVisibility(View.VISIBLE);
            Bitmap bitmap= BitmapFactory.decodeFile(filePath);
            Matrix m = new Matrix();
            m.setRotate(direction == Camera.CameraInfo.CAMERA_FACING_FRONT?270:90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                photoPlay.setImageBitmap(bm1);
            } catch (OutOfMemoryError ex) {
            }
        }


    }

    boolean isShow = true;
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.video_use) {
            useVideo();
        } else if (i == R.id.video_cancel) {
            onCancel();
        }else if (i == R.id.fl_videoview) {
            isShow = !isShow;
            titleState(isShow);
        }
    }

    public void titleState(boolean state){
        if (state){
            title.setVisibility(View.VISIBLE);
        }else {
            title.setVisibility(View.GONE);
        }
    }



    /**
     * 删除或者保存
     */
    public void onCancel(){
        if ("删除".equals(titleText)) {
            Intent intent = new Intent();
            intent.putExtra("delete", 0);
            setResult(100, intent);
        }else {
            Intent intent = new Intent();
            //放1代表是保存的回调
            intent.putExtra("delete", 1);
            intent.putExtra("file_path",filePath);
            setResult(100, intent);
        }
        finish();
    }

    /**
     * 回退
     */
    public void useVideo(){
        finish();
    }

}


