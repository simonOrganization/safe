package com.lchtime.safetyexpress.ui.circle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.PublicCircleAdapter;
import com.lchtime.safetyexpress.adapter.SpinerAdapter;
import com.lchtime.safetyexpress.bean.CircleTwoBean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ImageUtils;
import com.lchtime.safetyexpress.views.GridSpacingItemDecoration;
import com.lchtime.safetyexpress.views.SpinerPopWindow;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yxn on 2017/4/23.
 */

public class PublicCircleUI extends Activity implements PublicCircleAdapter.OnItemClick {
    @BindView(R.id.public_circle_rc)
    RecyclerView public_circle_rc;
    @BindView(R.id.public_circle_activity)
    LinearLayout public_circle_activity;
    private ArrayList<CircleTwoBean> images = new ArrayList<CircleTwoBean>();
    private PublicCircleAdapter adapter;
    private SpinerPopWindow spinerPopWindow;

    private String fileName = "";
    private File tempFile;
    private static final int OPEN_CAMERA_CODE = 10;
    private static final int OPEN_GALLERY_CODE = 11;
    private static final int CROP_PHOTO_CODE = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_cicle_activity);
        ButterKnife.bind(this);
        public_circle_rc.setLayoutManager(new GridLayoutManager(this,3));
        public_circle_rc.addItemDecoration(new GridSpacingItemDecoration(3,5,true));
        adapter = new PublicCircleAdapter(images,this);
        public_circle_rc.setAdapter(adapter);

    }
    @OnClick({R.id.public_circle_cancel,R.id.public_circle_submit})
    void setOnclick(View view){
        switch (view.getId()){
            case R.id.public_circle_cancel:
                PublicCircleUI.this.finish();
                break;
            case R.id.public_circle_submit:

                break;
        }
    }

    @Override
    public void setOnItemClick(int position) {
        final int saaa = position;
        final ArrayList<String> moreData = new ArrayList<String>();
        moreData.add("照相机");
        moreData.add("相册");
        moreData.add("取消");
        spinerPopWindow = new SpinerPopWindow(PublicCircleUI.this,moreData);
        spinerPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        spinerPopWindow.showAtLocation(public_circle_activity, Gravity.BOTTOM,0,0);
        spinerPopWindow.setSpinerInterface(new SpinerPopWindow.SpinerInterface() {
            @Override
            public void setSpinerInterface(int position) {
                switch (position){
                    case 0:
                        initFile(saaa);
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机
                        camera_intent.putExtra("output", Uri.fromFile(tempFile));
                        startActivityForResult(camera_intent, OPEN_CAMERA_CODE);
                        break;
                    case 1:
                        initFile(saaa);
                        Intent intent_pic = new Intent(Intent.ACTION_PICK);// 打开相册
                        intent_pic.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                        intent_pic.putExtra("output", Uri.fromFile(tempFile));
                        startActivityForResult(intent_pic, OPEN_GALLERY_CODE);
                        break;
                }
                spinerPopWindow.dismiss();
                Toast.makeText(PublicCircleUI.this,"id == "+moreData.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initFile(int position) {
        if (fileName.equals("")) {
            if (CommonUtils.ExistSDCard()) {
                String path = Environment.getExternalStorageDirectory() + File.separator + "JanuBookingOnline" + File.separator;
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                fileName = path + "a_image"+position+".jpg";

                tempFile = new File(fileName);
                if (tempFile.length() > 0) {
                    //清楚以前保存的照片（刷新下）
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(tempFile);
                    intent.setData(uri);
                    this.sendBroadcast(intent);
                }
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            } else {
                Toast.makeText(this,"请插入sd卡",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_CAMERA_CODE:
                if (tempFile.exists() && tempFile.length() > 0 && resultCode == -1) {
                    ImageUtils.cropPhoto(Uri.fromFile(tempFile), PublicCircleUI.this, tempFile);
                }
                break;
            case OPEN_GALLERY_CODE:
                if (data != null) {
                    ImageUtils.cropPhoto(data.getData(), PublicCircleUI.this, tempFile);
                }
                break;
            case CROP_PHOTO_CODE:
                if (data != null) {
                    Bitmap bitmap = ImageUtils.getSmallBitmap(fileName);
//                    ac_perfect_image.setImageDrawable(null);
//                    ac_perfect_image.setImageBitmap(bitmap);
                    if (bitmap != null) {
                        //上传图片，并将返回的图片地址显示在图片上
//                        upImage(fileName);
                        images.add(new CircleTwoBean(bitmap));
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
