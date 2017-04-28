package com.lchtime.safetyexpress.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;

/**
 * Created by android-cp on 2017/4/26.
 */

public class UpdataImageUtils {

    private int index;
    private UpdataPicListener mListener;
    public void upDataPic(final Bitmap zoomBitMap, final String MYICON, UpdataPicListener updataPicListener){
        mListener = updataPicListener;
        final Context context = MyApplication.getContext();

        OkHttpUtils.post()
                .addFile("image[]",MYICON,new File(context.getFilesDir(),MYICON))
                .url(context.getResources().getString(R.string.service_host_address).concat(context.getResources().getString(R.string.upload)))
                .addParams("sid", "")
                .addParams("index", (index++) + "")
                .addParams("ub_id", SpTools.getString(context , Constants.userId ,""))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context,"上传头像失败，请重新上传", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {

                if (mListener != null){
                    mListener.onResponse(response);
                }
                Toast.makeText(context,"上传头像成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface UpdataPicListener{
        void onResponse(String response);
    }

    public static void getUrlBitmap(final String url, final BitmapListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL pictureUrl = new URL(url);
                    InputStream in = pictureUrl.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    listener.giveBitmap(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public interface BitmapListener{
        void giveBitmap(Bitmap bitmap);
    }

    /**
     * 保存图片到本地缓存
     * @param bitmap
     * @param str
     */
    public static void saveBitmapFile(Bitmap bitmap , String str){

        File file = new File(MyApplication.getContext().getFilesDir(),str);//将要保存图片的路径
        try {
            if(file.exists()){
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
