package com.lchtime.safetyexpress.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.LoginResult;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;

import static com.lchtime.safetyexpress.bean.Constants.password;

/**
 * @author Admin
 * @time 2017/4/7 9:56
 * @des ${TODO}
 */

public class LoginInternetRequest {

    private static int index = 0;
    private static Context context;
    private static Gson mGson;
    private static ForResultListener mListener;
    private static TimeCount timeCount;
    private static TextView mTextView;
    private static boolean isRun = false;
    static {
        mGson = new Gson();
        context = MyApplication.getContext();
        timeCount = new TimeCount(60000, 1000);
    }


/**
 *  登录
 *  */
    public static void login(String phonenumber, final String password, ForResultListener listener){
        mListener = listener;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }
        if(TextUtils.isEmpty(phonenumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            mListener.onResponseMessage("");
            return ;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.toastMessage("您输入的密码为空");
            mListener.onResponseMessage("");
            return;
        }

        if(!TextUtils.isEmpty(phonenumber)){
//            if(!CommonUtils.isMobilePhone(phonenumber)){
//                CommonUtils.toastMessage("您输入的手机号有误");
//                mListener.onResponseMessage("");
//                return ;
//            }
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.getLogin));
        Log.d("host",url);
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phonenumber)
                .addParams("index",(index++)+"")
                .addParams("ud_pwd",password)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.d("0000---------------0000",e.getMessage());
                SpTools.setString(MyApplication.getContext(), Constants.userId, null);//存储用户的ub_id
                SpTools.setString(MyApplication.getContext(), Constants.phoneNum, null);//存储用户的手机号码
                SpTools.setString(MyApplication.getContext(), password, null);//存储用户的密码
                mListener.onResponseMessage("");
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }

            @Override
            public void onResponse(String response, int id) {
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if (code.equals("10")) {
                    SpTools.setString(context, Constants.userId, result.ub_id);//存储用户的ub_id
                    if (!TextUtils.isEmpty(result.ub_id)){
                        PushManager.getInstance().bindAlias(MyApplication.getContext(),result.ub_id);
//                        Tag t = new Tag();
//                        //name 字段只支持：中文、英文字母（大小写）、数字、除英文逗号以外的其他特殊符号, 具体请看代码示例
//                        t.setName("Android");
//                        int i = PushManager.getInstance().setTag(MyApplication.getContext(),new Tag[]{t},
//                                System.currentTimeMillis() +"");
                    }
                    mListener.onResponseMessage("成功");
                } else if (code.equals("20")) {
                    SpTools.setString(MyApplication.getContext(), Constants.userId, null);//存储用户的ub_id
                    SpTools.setString(MyApplication.getContext(), Constants.phoneNum, null);//存储用户的手机号码
                    SpTools.setString(MyApplication.getContext(), password, null);//存储用户的密码
                    mListener.onResponseMessage("失败");
                    CommonUtils.toastMessage(info);



                }
            }
        });
    }


    /**
     *  环信登录
     *  */
    public static void getHXinfo(ForResultListener listener){
        mListener = listener;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }

        String ub_id = SpTools.getString(context,Constants.userId,"");
        if (TextUtils.isEmpty(ub_id)){
            CommonUtils.toastMessage("您没有登录");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.hxLogin));
        Log.d("host",url);
        OkHttpUtils.post().url(url)
                .addParams("ub_id",ub_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Log.d("0000---------------0000",e.getMessage());
                SpTools.setString(MyApplication.getContext(), Constants.userId, null);//存储用户的ub_id
                SpTools.setString(MyApplication.getContext(), Constants.phoneNum, null);//存储用户的手机号码
                SpTools.setString(MyApplication.getContext(), password, null);//存储用户的密码
                mListener.onResponseMessage("");
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }

            @Override
            public void onResponse(String response, int id) {
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if (code.equals("10")) {
                    SpTools.setString(context, Constants.userId, result.ub_id);//存储用户的ub_id
                    mListener.onResponseMessage(response);
                } else if (code.equals("20")) {
                    SpTools.setString(MyApplication.getContext(), Constants.userId, null);//存储用户的ub_id
                    SpTools.setString(MyApplication.getContext(), Constants.phoneNum, null);//存储用户的手机号码
                    SpTools.setString(MyApplication.getContext(), password, null);//存储用户的密码
                    mListener.onResponseMessage("");
                    CommonUtils.toastMessage(info);



                }
            }
        });
    }
    /**
     *  获取验证码
     * @param phoneNumber 电话号码
     * @param view 验证码的view
     * @param listener listener
     */
    public static void verificationCode(String phoneNumber, TextView view, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("net");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            //mListener.onResponseMessage("");
            return ;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                //mListener.onResponseMessage("");
                return ;
            }
        }
        if(isRun){
            return ;
        }
        timeCount.start();
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.sendCode));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phoneNumber)
                .addParams("index",(index++)+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
                mListener.onResponseMessage("net");
            }

            @Override
            public void onResponse(String response, int id) {
                LoginResult result = mGson.fromJson(response,LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("20")){
                    if(info.equals("ub_phone error!")){
                        CommonUtils.toastMessage("您输入的手机号错误");
                        //mListener.onResponseMessage("");
                    }
                }else{
                    String mVc_code = result.vc_code;
                    mListener.onResponseMessage(mVc_code);
                }
            }
        });
    }

    /**
     * 注册新用户
     * @param phoneNumber 电话号码
     * @param vc_code 获取的验证码
     * @param password 密码
     * @param code 用户输入的验证码
     * @param view 验证码的View
     * @param listener listener
     */
    public static void register(String phoneNumber, String vc_code, String password, String code, TextView view, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            mListener.onResponseMessage("");
            return;
        }else if(TextUtils.isEmpty(code)){
            CommonUtils.toastMessage("您输入的验证码为空");
            mListener.onResponseMessage("");
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.toastMessage("您输入的密码为空");
            mListener.onResponseMessage("");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!vc_code.equals(code)){
            CommonUtils.toastMessage("您输入的验证码错误");
            mListener.onResponseMessage("");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.reg));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phoneNumber)
                .addParams("index",(index++)+"")
                .addParams("ud_pwd",password)
                .addParams("vc_code",vc_code)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mListener.onResponseMessage("");
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }
            @Override
            public void onResponse(String response, int id) {
                Log.d("----------","RegisterUI="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    reset();
                    if (!TextUtils.isEmpty(result.ub_id)) {
                        mListener.onResponseMessage(result.ub_id);
                    }
                }else if(code.equals("20")){
                    if(info.equals("手机长度不正确")){
                        CommonUtils.toastMessage("您输入的手机号错误");
                    }else if(info.equals("手机号已存在！")){
                        CommonUtils.toastMessage("您输入的手机号已注册");
                    }else if(info.equals("密码长度不正确！")){
                        CommonUtils.toastMessage("您输入的密码位数不足6位");
                    }else{
                        CommonUtils.toastMessage("注册失败");
                    }
                    mListener.onResponseMessage("");
                }else {
                    CommonUtils.toastMessage("未知错误");
                    mListener.onResponseMessage("");
                }
            }
        });

    }

    /**
     * 忘记密码
     * @param phoneNumber 电话号码
     * @param vc_code 获得到验证码
     * @param code 输入的验证码
     * @param password 输入的密码
     * @param nextPassword 再次输入的密码
     * @param view 验证码的View
     * @param passport 输入密码的Editext
     * @param nextPassport 再次输入密码的Editext
     * @param listener listener
     */
    public static void forgetPassword(String phoneNumber, String vc_code, String code, String password, String nextPassword, TextView view, final EditText passport, final EditText nextPassport, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            mListener.onResponseMessage("");
            return;
        }else if(TextUtils.isEmpty(code)){
            CommonUtils.toastMessage("您输入的验证码为空");
            mListener.onResponseMessage("");
            return;
        }else if(TextUtils.isEmpty(password) | TextUtils.isEmpty(nextPassword)){
            CommonUtils.toastMessage("您输入的密码为空");
            passport.setText("");
            nextPassport.setText("");
            mListener.onResponseMessage("");
            return;
        }else if(password.length()<6 |nextPassword.length()<6){
            CommonUtils.toastMessage("输入的密码长度不要小于6位");
            passport.setText("");
            nextPassport.setText("");
            mListener.onResponseMessage("");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!password.equals(nextPassword)){
            CommonUtils.toastMessage("二次输入的密码不一致");
            passport.setText("");
            nextPassport.setText("");
            mListener.onResponseMessage("");
            return;
        }
        if(!vc_code.equals(code)){
            CommonUtils.toastMessage("您输入的验证码错误");
            mListener.onResponseMessage("");
            return;
        }

        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.reset));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phoneNumber)
                .addParams("index",(index++)+"")
                .addParams("ud_pwd",password)
                .addParams("vc_code",code)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
                mListener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                Log.d("---------","ChangePswUI="+response);
                if(code.equals("10")){
                    reset();
                    passport.setText("");
                    nextPassport.setText("");
                    mListener.onResponseMessage("成功");

                }else if(code.equals("20")){
                    CommonUtils.toastMessage("修改密码失败，请重新输入");
                    mListener.onResponseMessage("");
                }else {
                    CommonUtils.toastMessage(info);
                    mListener.onResponseMessage("");
                }
            }
        });

    }

    /**
     *
     * @param newpassword 新密码
     * @param confirmpassword 新密码确认
     * @param editnewpass 新密码的Editext
     * @param listener listener
     */
    public static void reviseCode(String newpassword, String confirmpassword, EditText editnewpass, ForResultListener listener) {
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        mListener = listener;
        if(TextUtils.isEmpty(newpassword) | TextUtils.isEmpty(confirmpassword)){
            CommonUtils.toastMessage("您输入的密码为空");
            editnewpass.setText("");
            return;
        }else if(confirmpassword.length()<6){
            CommonUtils.toastMessage("输入的新密码长度不要小于6位");
            editnewpass.setText("");
            return;
        }
        if(!newpassword.equals(confirmpassword)){
            CommonUtils.toastMessage("二次输入的新密码不一致");
            editnewpass.setText("");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.chpwd));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("ud_pwd",newpassword)
                .addParams("vc_code",newpassword)
                .addParams("ub_phone",newpassword)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }
            @Override
            public void onResponse(String response, int id) {
                Log.d("----------","RegisterUI="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
//                    if(info.equals("ub_phone error!")){
//                        CommonUtils.toastMessage("您输入的手机号错误");
//                    }else if(info.equals("ub_phone exist!")){
//                        CommonUtils.toastMessage("您输入的手机号已注册");
//                    }else if(info.equals("ud_pwd error!")){
//                        CommonUtils.toastMessage("您输入的密码位数不足6位");
//                    }
                }
            }
        });

    }


    /**
     *
     * 更换手机号
     */
    public static void ChangePhone(String code, String ub_id, String phoneNum, ForResultListener listener) {
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        mListener = listener;

        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.chphone));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("code",code)
                .addParams("ub_id",ub_id)
                .addParams("phone",phoneNum)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }
            @Override
            public void onResponse(String response, int id) {
                Log.d("----------","RegisterUI="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
//                    if(info.equals("ub_phone error!")){
//                        CommonUtils.toastMessage("您输入的手机号错误");
//                    }else if(info.equals("ub_phone exist!")){
//                        CommonUtils.toastMessage("您输入的手机号已注册");
//                    }else if(info.equals("ud_pwd error!")){
//                        CommonUtils.toastMessage("您输入的密码位数不足6位");
//                    }
                }
            }
        });

    }

    /**
     *  获取个人信息
     *  */
    public static void getVipInfo(String ub_id,ForResultListener listener){
        mListener = listener;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            InitInfo.isLogin = false;
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.mycount));
        Log.d("host",url);
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",ub_id)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
               // Log.d("0000---------------0000",e.getMessage());
                InitInfo.isLogin = false;
                mListener.onResponseMessage("");
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        VipInfoBean vipInfoBean = mGson.fromJson(response, VipInfoBean.class);
                        String code = vipInfoBean.result.code;
                        if (code.equals("10")) {
                            mListener.onResponseMessage(response);
                        } else {
                            mListener.onResponseMessage("");
                            CommonUtils.toastMessage("请求网络数据失败，请检查网络");
                        }
                    }catch (Exception exception){
                        mListener.onResponseMessage("");
                    }
                }else{
                    mListener.onResponseMessage("");
                }
            }
        });
    }


    /**
     *  得到行业
     *  */
    public static void getProfession(String ub_id, final ForResultListener listener){
        //mListener = listener;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            InitInfo.isLogin = false;
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            if (listener != null){
                listener.onResponseMessage("");
            }
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.mycountEdit));
        Log.d("host",url);
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",ub_id)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("type",1+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
             //   Log.d("0000---------------0000",e.getMessage());
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
                listener.onResponseMessage("");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    ProfessionBean professionBean = mGson.fromJson(response, ProfessionBean.class);
                    if (!TextUtils.isEmpty(response) && professionBean != null) {
                        String code = professionBean.result.code;
                        if (code.equals("10")) {
//                        CommonUtils.toastMessage(professionBean.result.info);

                            listener.onResponseMessage(response);

                        } else {
                            CommonUtils.toastMessage("请求网络数据失败，请检查网络");
                            listener.onResponseMessage("");
                        }
                    } else {
                        listener.onResponseMessage("");
                    }
                }catch (Exception exception){
                    listener.onResponseMessage("");
                }
            }
        });
    }

    /**
     *  得到岗位
     *  */
    public static void getPost(String ub_id, final ForResultListener listener){
        //mListener = listener;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            InitInfo.isLogin = false;
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            if (listener != null){
                listener.onResponseMessage("");
            }
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.mycountEdit));
        Log.d("host",url);
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",ub_id)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("type",2+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.d("0000---------------0000",e.getMessage());
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
                listener.onResponseMessage("");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    PostBean postBean = mGson.fromJson(response, PostBean.class);
                    if (!TextUtils.isEmpty(response) && postBean != null) {
                        String code = postBean.result.code;
                        if (code.equals("10")) {
                            //             CommonUtils.toastMessage(postBean.result.info);

                            listener.onResponseMessage(response);

                        } else {
                            CommonUtils.toastMessage("请求网络数据失败，请检查网络");
                            listener.onResponseMessage(response);
                        }
                    } else {
                        listener.onResponseMessage("");
                    }
                }catch (Exception exception){
                    listener.onResponseMessage("");
                }
            }
        });
    }



    /**
     *  修改个人信息
     *  */
    public static void editVipInfo(String phoneNum, Map<String,String> map, String ub_id , final DialogUtil dialog , ForResultListener listener){
        mListener = listener;
        if(!CommonUtils.isNetworkAvailable(MyApplication.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            if (mListener != null){
                mListener.onResponseMessage("");
            }
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.mycountEdit));
        Log.d("host",url);
        PostFormBuilder builder = OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",ub_id)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("user_base","\"user_base\": \"" + phoneNum + "\"");

        StringBuffer user_detail = new StringBuffer();
        user_detail.append("{");
        Set<String> keySet = map.keySet();
        int max = keySet.size();
        int current = 0;
         for (String key : keySet) {
             current++;
             String value = map.get(key);
             if (current == max){
                 user_detail.append("\""+key+"\": \""+value+"\"" + "}");
             }else {

                 user_detail.append("\""+key+"\": \""+value+"\"" + ",");

             }

         }
        Log.i("qaz", "editVipInfo: " +user_detail.toString());
        builder.addParams("user_detail",user_detail.toString());

        builder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dissmiss();
                Log.i("qaz", "onError: " +e.getMessage());
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }

            @Override
            public void onResponse(String response, int id) {
                dialog.dissmiss();
                if (!TextUtils.isEmpty(response)) {
                    Result result = mGson.fromJson(response, Result.class);
                    String code = result.result.code;
                    if ("10".equals(code)) {
                        CommonUtils.toastMessage(result.result.info);
                        if (mListener != null) {
                            mListener.onResponseMessage(code);
                        }
                    } else {
                        CommonUtils.toastMessage(result.result.info);
                    }
                }else {
                    CommonUtils.toastMessage("上传失败");
                }
            }
        });
    }

    public interface ForResultListener{
        void onResponseMessage(String code);
    }

    /**
     * 验证码倒计时timecount
     */
    static class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {// 计时完毕时触发
            mTextView.setText("获取验证码");
            mTextView.setClickable(true);
            isRun = false;
        }
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mTextView.setClickable(false);
            isRun = true;
            mTextView.setText("获取验证码("+millisUntilFinished / 1000 + "秒)");
        }
    }

    public static void reset() {
        if(timeCount != null){
            timeCount.cancel();
            timeCount.onFinish();
        }
    }
}
