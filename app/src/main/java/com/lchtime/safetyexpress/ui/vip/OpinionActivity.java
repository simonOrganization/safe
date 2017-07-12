package com.lchtime.safetyexpress.ui.vip;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.GridImageAdapter;
import com.lchtime.safetyexpress.bean.AdviceBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.circle.PublishCircleUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.OpinionProtocal;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.views.FullyGridLayoutManager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/4/21.意见反馈界面
 */
@ContentView(R.layout.vip_opinion)
public class OpinionActivity extends BaseUI implements PopupWindow.OnDismissListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    @ViewInject(R.id.recycler)
    private RecyclerView picView;
    @ViewInject(R.id.et_describe_text)
    private EditText describe_text;
    @ViewInject(R.id.et_phone_num)
    private EditText et_phone_num;
    private List<File> fileList;

    private OpinionProtocal protocal;
    private UpdataImageUtils updataImageUtils;
    private String filesid;

    private PopupWindow popupWindow;

    private View contentView;
    private FunctionOptions options;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("意见反馈");
        rightTextVisible("提交");

        initRecyclerView();
        initPopWindow();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setSelectMax(9);//最多选择的个数
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                PictureConfig.getPictureConfig().externalPicturePreview(OpinionActivity.this, position, selectMedia);

            }
        });
    }

    @Override
    protected void prepareData() {

    }
    /**
     * 初始化提示框
     */
    private void initPopWindow() {

        contentView = LayoutInflater.from(OpinionActivity.this).inflate(
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

        contentView.findViewById(R.id.tv_picture_list).setOnClickListener(this);
        contentView.findViewById(R.id.tv_takepic).setOnClickListener(this);
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    backgroundAlpha(0.5f);
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    // 先初始化参数配置，在启动相册
                    //PictureConfig.getPictureConfig().init(options).openPhoto(OpinionActivity.this, resultCallback);
                    // 只拍照
                    //PictureConfig.getPictureConfig().init(options).startOpenCamera(mContext, resultCallback);
                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            fileList = new ArrayList<>();
            for (int i = 0; i < resultList.size() ; i ++){
                LocalMedia media = resultList.get(i);
                String path = media.getCompressPath();
                File file = new File(path);
                fileList.add(file);
            }

            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }
           /* if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }*/
        }
    };

    @Override
    protected void clickEvent() {
        if (protocal == null){
            protocal = new OpinionProtocal();
        }

        if (updataImageUtils == null){
            updataImageUtils = new UpdataImageUtils();
        }

        final String advice = describe_text.getText().toString().trim();
        if (TextUtils.isEmpty(advice)){
            Toast.makeText(this,"意见不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        final String phone_num = et_phone_num.getText().toString().trim();
        if (fileList != null && fileList.size() > 0){
            updataImageUtils.upMuchDataPic(fileList, new UpdataImageUtils.UpdataPicListener() {
                @Override
                public void onResponse(String response) {
                    if (TextUtils.isEmpty(response)){
                        CommonUtils.toastMessage("上传图片失败！");
                        return;
                    }
                    UpdataBean updataBean = (UpdataBean) JsonUtils.stringToObject(response, UpdataBean.class);

                    filesid = "";
                    if (updataBean != null&& updataBean.file_ids != null) {
                        //拼接上传picture的id
                        for (int i = 0; i < updataBean.file_ids.size(); i ++) {
                            if (i == 0){
                                filesid = filesid + updataBean.file_ids.get(i);
                            }else {
                                filesid = filesid + ";" + updataBean.file_ids.get(i);
                            }
                        }

                        protocal.getDataInternet(advice, filesid, SpTools.getString(OpinionActivity.this, Constants.userId, ""), phone_num,new OpinionProtocal.OpinionResultListener() {
                            @Override
                            public void onResponseMessage(Object result) {
                                AdviceBean adviceBean = (AdviceBean) result;
                                //Toast.makeText(OpinionActivity.this,adviceBean.result.info,Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }
            });
        }else {
            filesid = "";
            protocal.getDataInternet(advice, filesid, SpTools.getString(OpinionActivity.this, Constants.userId, ""),phone_num, new OpinionProtocal.OpinionResultListener() {
                @Override
                public void onResponseMessage(Object result) {
                    AdviceBean adviceBean = (AdviceBean) result;
                    Toast.makeText(OpinionActivity.this,adviceBean.result.info,Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_picture_list://从手机相册选取
                getPhoto(1);
                //PictureConfig.getPictureConfig().init(options).openPhoto(this, resultCallback);
                break;
            case R.id.tv_takepic:       //拍照获取
                getPhoto(2);
                //PictureConfig.getPictureConfig().init(options).startOpenCamera(this, resultCallback);
                break;
        }
        popupWindow.dismiss();
    }

    /**
     * 根据type 获取照片
     * @param type
     */
    private void getPhoto(int type){
        // 设置主题样式
        int themeStyle = ContextCompat.getColor(OpinionActivity.this, R.color.grey);
        int previewColor = ContextCompat.getColor(OpinionActivity.this, R.color.tab_color_true);
        int completeColor = ContextCompat.getColor(OpinionActivity.this, R.color.tab_color_true);


        options = new FunctionOptions.Builder()
                .setType(1) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_DEFAULT) // 裁剪模式 默认、1:1、3:4、3:2、16:9
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(9) // 可选择图片的数量
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选
                .setShowCamera(false) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
//                            .setPreviewVideo(false) // 是否预览视频(播放) mode or 多选有效
                .setCheckedBoxDrawable( R.drawable.select_cb)
//                            .setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
//                            .setRecordVideoSecond(60) // 视频秒数
                .setGif(false)// 是否显示gif图片，默认不显示
//                            .setCropW(cropW) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
//                            .setCropH(cropH) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setMaxB(512000) // 压缩最大值 例如:200kb  就设置202400，202400 / ic_launcher = 200kb
                .setPreviewColor(previewColor) //预览字体颜色
                .setCompleteColor(completeColor) //已完成字体颜色
                .setPreviewBottomBgColor(ContextCompat.getColor(OpinionActivity.this, R.color.transparent)) //预览底部背景色
                .setBottomBgColor(ContextCompat.getColor(OpinionActivity.this, R.color.transparent)) //图片列表底部背景色
                .setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
                .setCheckNumMode(false)//设置是否显示数字模式
                .setCompressQuality(100) // 图片裁剪质量,默认无损
                .setImageSpanCount(3) // 每行个数
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
//                            .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
//                            .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(themeStyle) // 设置主题样式
                .create();
        if(type ==1){
            // 先初始化参数配置，在启动相册
            PictureConfig.getPictureConfig().init(options).openPhoto(mContext , resultCallback);
        }else{
            // 只拍照
            PictureConfig.getPictureConfig().init(options).startOpenCamera(mContext, resultCallback);
        }
    }
}
