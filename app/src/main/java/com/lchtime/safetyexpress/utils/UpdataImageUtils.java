package com.lchtime.safetyexpress.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;

import static com.igexin.push.core.a.i;
import static com.igexin.sdk.GTServiceManager.context;

/**
 * Created by android-cp on 2017/4/26.
 */

public class UpdataImageUtils {

    private int index;
    private UpdataPicListener mListener;
    public void upDataPic(final String filePath, final DialogUtil dialog , UpdataPicListener updataPicListener){
        mListener = updataPicListener;
        final Context context = MyApplication.getContext();
        File file = new File(filePath);
        String id = SpTools.getString(context , Constants.userId ,"");

        OkHttpUtils.post()
                .url(context.getResources().getString(R.string.service_host_address).concat(context.getResources().getString(R.string.upload)))
                .addFile("image[]",file.getName(),file)
                .addParams("sid", "")
                .addParams("index", (index++) + "")
                .addParams("ub_id", SpTools.getString(context , Constants.userId ,""))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("--------","response="+e);
                dialog.dissmiss();
                Toast.makeText(context,"上传头像失败，请重新上传", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dissmiss();
                Log.d("--------","response="+response);
                if (mListener != null){
                    mListener.onResponse(response);
                }
                Toast.makeText(context,"上传头像成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

/**
 * 上传多张图片
 * */
    public void upMuchDataPic(List<File> list, final DialogUtil mDialog , UpdataPicListener updataPicListener){
        mListener = updataPicListener;
        String ub_id = SpTools.getString(MyApplication.getContext(), Constants.userId ,"");
        final Context context = MyApplication.getContext();

        //if(list.size() <= 3){
            PostFormBuilder builder = OkHttpUtils.post()
                    .url(context.getResources().getString(R.string.service_host_address).concat(context.getResources().getString(R.string.upload)));
        //PostFormBuilder builder = OkHttpUtils.post().url("http://fcar.lchtime.cn:8001/index.php/system/uploads");
            for (int i = 0 ;i < list.size(); i ++){
                builder.addFile("image[]" , list.get(i).getName() , list.get(i));
            }
            builder
                    .addParams("sid", "")
                    .addParams("index", (index++) + "")
                    .addParams("ub_id", ub_id)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    mDialog.dissmiss();
                    Toast.makeText(context,"上传图片失败，请重新上传", Toast.LENGTH_SHORT).show();
                    //mListener.onResponse(null);
                }
                @Override
                public void onResponse(String response, int id) {
                    if (mListener != null){
                        /*UpdataBean updataBean = (UpdataBean) JsonUtils.stringToObject(response, UpdataBean.class);
                        mListener.onResponse(updataBean);*/
                        mListener.onResponse(response);
                    }else {
                        mListener.onResponse(null);
                    }
                    //Toast.makeText(context,"上传图片成功",Toast.LENGTH_SHORT).show();
                }
            });
        /*}else if(list.size() <= 6 ){


        }*/


        /*int count = list.size() / 3;
        for(int j = 0 ; j < 3; j++){
            builder.addFile("image[]" , "advice" + i , list.get(i));
        }*/



    }

    public interface UpdataPicListener{
        void onResponse(String response);
        //void onResponse(UpdataBean updataBean);
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
        Log.d("------------","file="+file.getName());
        try {
            if(file.exists()){
                file.delete();
            }

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void upDataVideo(final String filePath , File video_pic, final DialogUtil dialog , final UpdataPicListener updataPicListener){
        final Context context = MyApplication.getContext();
        String upLoadUrl = context.getResources().getString(R.string.service_host_address).concat(context.getResources().getString(R.string.upload));
        File file = new File(filePath);

        OkHttpUtils.post()
                .url(upLoadUrl)
                .addFile("image[]" , file.getName(),file)
                .addFile("image[]" , video_pic.getName() + ".jpg" , video_pic)
                .addParams("sid", "")
                .addParams("index", (index++) + "")
                .addParams("ub_id", SpTools.getString(context , Constants.userId ,""))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dissmiss();
                Log.d("--------","response="+e);
                Toast.makeText(context,"上传视频失败，请重新上传", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.d("--------","response="+response);
                if (updataPicListener != null){
                    updataPicListener.onResponse(response);
                }
                Toast.makeText(context,"上传视频成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
