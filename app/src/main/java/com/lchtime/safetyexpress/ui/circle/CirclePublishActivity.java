package com.lchtime.safetyexpress.ui.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dalong.recordlib.RecordVideoActivity;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/16. 发布圈子界面
 */
@ContentView(R.layout.circle_publish)
public class CirclePublishActivity extends BaseUI implements PopupWindow.OnDismissListener, View.OnClickListener {
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.v_title)
    TextView vTitle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_describe_text)
    EditText etDescribeText;
    @BindView(R.id.iv_video_pic)
    ImageView ivVideoPic;
    private String videoPath;
    private View contentView;
    public static final int REQUEST_CODE = 2000;
    private UpdataImageUtils updataImageUtils;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        rightTextVisible("发送");
        setTitle("发圈子");
        videoPath = getIntent().getStringExtra("path");
        //Toast.makeText(this, "视频路径：" + videoPath, Toast.LENGTH_SHORT).show();
        //得到视频的第一帧图片并且规定大小
        setImageView(videoPath);
        ivVideoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(videoPath)) {
                    backgroundAlpha(0.5f);
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    contentView.findViewById(R.id.tv_picture_list).setOnClickListener(CirclePublishActivity.this);
                    contentView.findViewById(R.id.tv_takepic).setOnClickListener(CirclePublishActivity.this);
                }else {
                    startVideo(videoPath);
                }
            }
        });
        initPopWindow();
    }

    private void startVideo(String path) {
        Intent intent = new Intent(CirclePublishActivity.this, VideoPlayActivity.class);
        intent.putExtra("filePath", path);
        intent.putExtra("fileType", VideoPlayActivity.FILE_TYPE_VIDEO);
        startActivityForResult(intent,REQUEST_CODE);
    }
    private void startVideo(String path,String text) {
        Intent intent = new Intent(CirclePublishActivity.this, VideoPlayActivity.class);
        intent.putExtra("filePath", path);
        intent.putExtra("fileType", VideoPlayActivity.FILE_TYPE_VIDEO);
        intent.putExtra("title_text", text);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void setImageView(String path) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 210, 210);
        UpdataImageUtils.saveBitmapFile(bitmap,"video_pic");
        ivVideoPic.setImageBitmap(bitmap);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void prepareData() {

    }


    private PopupWindow popupWindow;
    private void initPopWindow() {

        contentView = LayoutInflater.from(this).inflate(
                R.layout.activity_pic_pop, null);
        //设置弹出框的宽度和高度
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(this);
        ColorDrawable dw = new ColorDrawable(55000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setFocusable(true);// 取得焦点
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的

        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);


    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    FunctionOptions options;
    @Override
    public void onClick(View v) {
        if (v == contentView.findViewById(R.id.tv_picture_list)){
            if(options == null){
                // 可选择图片的数量
                // 是否打开剪切选项
                options = new FunctionOptions.Builder()
                        .setType(FunctionConfig.TYPE_VIDEO) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                        .setSelectMode(FunctionConfig.MODE_SINGLE) // 可选择图片的数量
                        .setEnableCrop(true) // 是否打开剪切选项
                        .setShowCamera(false)
                        .setCropMode(FunctionConfig.CROP_MODEL_1_1)
                        .setCompress(true)
                        .setCompressFlag(1)
                        .create();
            }
            PictureConfig.getPictureConfig().init(options).openPhoto(this, resultCallback);
        }else if (v == contentView.findViewById(R.id.tv_takepic)){
            //拍摄
            initVideoPath();
            Intent intent=new Intent(CirclePublishActivity.this, RecordVideoActivity.class);
            intent.putExtra(RecordVideoActivity.RECORD_VIDEO_PATH,newVideoPath);
            startActivityForResult(intent,REQUEST_CODE);

        }
        popupWindow.dismiss();
    }
    public String newVideoPath;
    private void initVideoPath() {
        File path=new File(Environment.getExternalStorageDirectory(),
                "kuaichevideo");
        if (!path.exists()) {
            path.mkdirs();
        }
        newVideoPath=path.getAbsolutePath()+File.separator+System.currentTimeMillis()+".mp4";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RecordVideoActivity.TAKE_VIDEO_CODE){
                //如果是拍摄后的回调
                videoPath=data.getStringExtra(RecordVideoActivity.TAKE_VIDEO_PATH);
                setImageView(videoPath);
            }else {
                if (data != null){
                    //如果要是返回来0就删除
                    int delete = data.getIntExtra("delete",188);
                    if (delete == 0){
                        videoPath = null;
                        ivVideoPic.setImageDrawable(getResources().getDrawable(R.drawable.updata_img_btn));
                    }else if (delete == 1){
                        //若是选取本地的视频，本地视频的path
                        videoPath = data.getStringExtra("file_path");
                        //CommonUtils.toastMessage(videoPath);
                        setImageView(videoPath);
                    }
                }
            }

        }
    }

    /**
     * 图片回调方法
     */

    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
            LocalMedia media = list.get(0);
            //本地视频路径
            String localPath = media.getPath();
            if (!TextUtils.isEmpty(localPath)) {
                startVideo(localPath,"保存");
//                videoPath = localPath;
//                setImageView(videoPath);
            }
        }
    };

    private Gson gson;
    private CircleProtocal protocal;
    @Override
    protected void clickEvent() {
        CommonUtils.toastMessage("发送");
        final String text = etDescribeText.getText().toString().trim();
        if (updataImageUtils == null) {
            updataImageUtils = new UpdataImageUtils();
        }
        if (gson == null){
            gson = new Gson();
        }
        if (protocal == null){
            protocal = new CircleProtocal();
        }
        final String ub_id = SpTools.getString(CirclePublishActivity.this, Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            CommonUtils.toastMessage("登录后才能发布圈子！");
            return;
        }
        if (TextUtils.isEmpty(videoPath)){
            CommonUtils.toastMessage("视频不能为空");
            return;
        }
        if (TextUtils.isEmpty(text)){
            CommonUtils.toastMessage("文字不能为空");
            return;
        }

        File file = new File(getFilesDir(),"video_pic");
        updataImageUtils.upDataVideo(videoPath,file, new UpdataImageUtils.UpdataPicListener() {
            @Override
            public void onResponse(String response) {
                String videoId = "";
                String videoPicId = "";
                UpdataBean updataBean = gson.fromJson(response, UpdataBean.class);
                if (updataBean == null){
                    Toast.makeText(CirclePublishActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (updataBean != null&& updataBean.file_ids != null) {
                    videoId = updataBean.file_ids.get(0);
                    videoPicId = updataBean.file_ids.get(1);

                    //发送视频id和圈子内容到服务器
                    protocal.getUpdataVideoData(ub_id, text, videoId, videoPicId, new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            Result result = (Result) response;
                            CommonUtils.toastMessage(result.result.info);
                            finish();
                        }
                    });
                }
            }
        });
    }

}
