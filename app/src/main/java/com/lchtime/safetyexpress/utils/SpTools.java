package com.lchtime.safetyexpress.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.VipInfoBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.lchtime.safetyexpress.bean.InitInfo.vipInfoBean;

public class SpTools {
	public static String USER_DATA = "user_data";

	
	public static void setString(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getString(key, value);
	}
	
	public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE,Context.MODE_PRIVATE);
        return sp.getBoolean(key,value);
    }
    
    public static void setInt(Context context,String key,int value){
		SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	
	public static int getInt(Context context,String key,int value){
		SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getInt(key, value);
	}
	public static void setLong(Context context,String key,Long value){
		SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}
	
	public static Long getLong(Context context,String key,Long value){
		SharedPreferences sp = context.getSharedPreferences(Constants.CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getLong(key, value);
	}
	
	public static String InputToString(Context context,String key){
		String result = null;
    	try {
			InputStream is = context.getAssets().open(key);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
			}
			is.close();
			result = baos.toString();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 保存用户信息
	 */
	public static void saveUser(Context context , VipInfoBean vipInfoBean){
		SharedPreferences sp = context.getSharedPreferences(USER_DATA , Context.MODE_APPEND);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("ub_id" , vipInfoBean.user_detail.ub_id);
		editor.putString("ub_phone" , vipInfoBean.user_detail.ub_phone);
		editor.putString("ud_addr" , vipInfoBean.user_detail.ud_addr);
		editor.putString("ud_bm" , vipInfoBean.user_detail.ud_bm);
		editor.putString("share" , vipInfoBean.user_detail.share);
		editor.putString("ud_borth" , vipInfoBean.user_detail.ud_borth);
		editor.putString("ud_company_name" , vipInfoBean.user_detail.ud_company_name);
		editor.putString("ud_memo" , vipInfoBean.user_detail.ud_memo);
		editor.putString("ud_nickname" , vipInfoBean.user_detail.ud_nickname);
		editor.putString("ud_photo_fileid" , vipInfoBean.user_detail.ud_photo_fileid);
		editor.putString("ud_post" , vipInfoBean.user_detail.ud_post);
		editor.putString("ud_profession" , vipInfoBean.user_detail.ud_profession);
		editor.putString("ud_sex" , vipInfoBean.user_detail.ud_sex);
		editor.commit();
	}

	/**
	 * 获取保存的用户数据
	 * @param context
	 * @return
	 */
	public static VipInfoBean getUser(Context context){
		VipInfoBean.UserDetail detail = new VipInfoBean.UserDetail();
		SharedPreferences sp = context.getSharedPreferences(USER_DATA , Context.MODE_APPEND);
		detail.ub_id = sp.getString("ub_id" , "");
		detail.ub_phone = sp.getString("ub_phone" , "");
		detail.ud_addr = sp.getString("ud_addr" , "");
		detail.ud_sex = sp.getString("ud_sex" , "");
		detail.ud_nickname = sp.getString("ud_nickname" , "");
		detail.ud_post = sp.getString("ud_post" , "");
		detail.ud_borth = sp.getString("ud_borth" , "");
		detail.ud_company_name = sp.getString("ud_company_name" , "");
		detail.ud_memo = sp.getString("ud_memo" , "");
		detail.ud_photo_fileid = sp.getString("ud_photo_fileid" , "");
		detail.ud_profession = sp.getString("ud_profession" , "");
		detail.share = sp.getString("share" , "");
		detail.ud_bm = sp.getString("ud_bm" , "");
		VipInfoBean bean = new VipInfoBean(detail);
		return bean;
	}

	/**
	 * 获取用户id
	 * @param context
	 * @return
	 */
	public static String getUserId(Context context){
		SharedPreferences sp = context.getSharedPreferences(USER_DATA , Context.MODE_APPEND);
		return sp.getString("ub_id" , "");
	}

	/**
	 * 保护用户id
	 * @param context
	 * @param ub_id
	 */
	public static void setUserId(Context context , String ub_id){
		SharedPreferences sp = context.getSharedPreferences(USER_DATA , Context.MODE_APPEND);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("ub_id" , ub_id);
		editor.commit();
	}



}
