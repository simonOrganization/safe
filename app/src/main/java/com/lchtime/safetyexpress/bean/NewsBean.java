package com.lchtime.safetyexpress.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/25.
 */

public class NewsBean implements Serializable {
    public String cc_id;
    public String cc_cd_id;
    public String cc_cd_parent_id_dir;
    public String cc_title;
    public String cc_description;
    public String cc_from;
    public String cc_datetime;
    public String cc_auth;
    public String cc_web_keyword;
    public String cc_web_description;
    public String cc_context;
    public String cc_pos;
    public String cc_agr;
    public String cc_aga;
    public String cc_count;
    public String cc_mark;
    public String cc_fielid;
    public String cc_fee;
    public String is_delete;
    public String count;
    public String plNum;
    public String video_time;
    public ArrayList<String> media;

    public boolean isCheck;

    public ArrayList<String> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<String> media) {
        this.media = media;
    }

    public String getPlNum() {
        return plNum;
    }

    public void setPlNum(String plNum) {
        this.plNum = plNum;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getCc_cd_id() {
        return cc_cd_id;
    }

    public void setCc_cd_id(String cc_cd_id) {
        this.cc_cd_id = cc_cd_id;
    }

    public String getCc_cd_parent_id_dir() {
        return cc_cd_parent_id_dir;
    }

    public void setCc_cd_parent_id_dir(String cc_cd_parent_id_dir) {
        this.cc_cd_parent_id_dir = cc_cd_parent_id_dir;
    }

    public String getCc_title() {
        return cc_title;
    }

    public void setCc_title(String cc_title) {
        this.cc_title = cc_title;
    }

    public String getCc_description() {
        return cc_description;
    }

    public void setCc_description(String cc_description) {
        this.cc_description = cc_description;
    }

    public String getCc_from() {
        return cc_from;
    }

    public void setCc_from(String cc_from) {
        this.cc_from = cc_from;
    }

    public String getCc_datetime() {
        return cc_datetime;
    }

    public void setCc_datetime(String cc_datetime) {
        this.cc_datetime = cc_datetime;
    }

    public String getCc_auth() {
        return cc_auth;
    }

    public void setCc_auth(String cc_auth) {
        this.cc_auth = cc_auth;
    }

    public String getCc_web_keyword() {
        return cc_web_keyword;
    }

    public void setCc_web_keyword(String cc_web_keyword) {
        this.cc_web_keyword = cc_web_keyword;
    }

    public String getCc_web_description() {
        return cc_web_description;
    }

    public void setCc_web_description(String cc_web_description) {
        this.cc_web_description = cc_web_description;
    }

    public String getCc_context() {
        return cc_context;
    }

    public void setCc_context(String cc_context) {
        this.cc_context = cc_context;
    }

    public String getCc_pos() {
        return cc_pos;
    }

    public void setCc_pos(String cc_pos) {
        this.cc_pos = cc_pos;
    }

    public String getCc_agr() {
        return cc_agr;
    }

    public void setCc_agr(String cc_agr) {
        this.cc_agr = cc_agr;
    }

    public String getCc_aga() {
        return cc_aga;
    }

    public void setCc_aga(String cc_aga) {
        this.cc_aga = cc_aga;
    }

    public String getCc_count() {
        return cc_count;
    }

    public void setCc_count(String cc_count) {
        this.cc_count = cc_count;
    }

    public String getCc_mark() {
        return cc_mark;
    }

    public void setCc_mark(String cc_mark) {
        this.cc_mark = cc_mark;
    }

    public String getCc_fielid() {
        return cc_fielid;
    }

    public void setCc_fielid(String cc_fielid) {
        this.cc_fielid = cc_fielid;
    }

    public String getCc_fee() {
        return cc_fee;
    }

    public void setCc_fee(String cc_fee) {
        this.cc_fee = cc_fee;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(cc_id);
//        parcel.writeString(cc_cd_id);
//        parcel.writeString(cc_cd_parent_id_dir);
//        parcel.writeString(cc_title);
//        parcel.writeString(cc_description);
//        parcel.writeString(cc_from);
//        parcel.writeString(cc_datetime);
//        parcel.writeString(cc_auth);
//        parcel.writeString(cc_web_keyword);
//        parcel.writeString(cc_web_description);
//        parcel.writeString(cc_context);
//        parcel.writeString(cc_pos);
//        parcel.writeString(cc_agr);
//        parcel.writeString(cc_aga);
//        parcel.writeString(cc_count);
//        parcel.writeString(cc_mark);
//        parcel.writeString(cc_fielid);
//        parcel.writeString(cc_fee);
//        parcel.writeString(is_delete);
//        parcel.writeString(count);
//        parcel.writeString(plNum);
//        parcel.writeStringList(media);
//
//
//    }
//    public static final Parcelable.Creator<NewsBean> CREATOR = new Creator(){
//
//        @Override
//        public NewsBean createFromParcel(Parcel source) {
//            // TODO Auto-generated method stub
//            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
//            NewsBean p = new NewsBean();
//            p.setCc_id(source.readString());
//            p.setCc_cd_id(source.readString());
//            p.setCc_cd_parent_id_dir(source.readString());
//            p.setCc_title(source.readString());
//            p.setCc_description(source.readString());
//            p.setCc_from(source.readString());
//            p.setCc_datetime(source.readString());
//            p.setCc_auth(source.readString());
//            p.setCc_web_keyword(source.readString());
//            p.setCc_web_description(source.readString());
//            p.setCc_context(source.readString());
//            p.setCc_pos(source.readString());
//            p.setCc_agr(source.readString());
//            p.setCc_aga(source.readString());
//            p.setCc_count(source.readString());
//            p.setCc_mark(source.readString());
//            p.setCc_fielid(source.readString());
//            p.setCc_fee(source.readString());
//            p.setIs_delete(source.readString());
//            p.setCount(source.readString());
//            p.setPlNum(source.readString());
////            p.setMedia(source.readArrayList());
//            return p;
//        }
//
//        @Override
//        public NewsBean[] newArray(int size) {
//            // TODO Auto-generated method stub
//            return new NewsBean[size];
//        }
//    };
}
