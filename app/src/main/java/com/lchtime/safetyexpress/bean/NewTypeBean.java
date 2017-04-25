package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/25.
 */

public class NewTypeBean {
    private String cd_mark;
    private String cd_id;
    private String cd_parent_id;
    private String cd_parent_id_dir;
    private String cd_name;
    private String cd_des;
    private String cd_context;
    private String cd_modal;
    private String cd_pos;
    private String cd_type;
    private String cd_web_keyword;
    private String cd_web_description;
    private String cd_fieldid;
    private ArrayList<String> cd_child;

    public NewTypeBean(String cd_name) {
        this.cd_name = cd_name;
    }

    public String getCd_mark() {
        return cd_mark;
    }

    public void setCd_mark(String cd_mark) {
        this.cd_mark = cd_mark;
    }

    public String getCd_id() {
        return cd_id;
    }

    public void setCd_id(String cd_id) {
        this.cd_id = cd_id;
    }

    public String getCd_parent_id() {
        return cd_parent_id;
    }

    public void setCd_parent_id(String cd_parent_id) {
        this.cd_parent_id = cd_parent_id;
    }

    public String getCd_parent_id_dir() {
        return cd_parent_id_dir;
    }

    public void setCd_parent_id_dir(String cd_parent_id_dir) {
        this.cd_parent_id_dir = cd_parent_id_dir;
    }

    public String getCd_name() {
        return cd_name;
    }

    public void setCd_name(String cd_name) {
        this.cd_name = cd_name;
    }

    public String getCd_des() {
        return cd_des;
    }

    public void setCd_des(String cd_des) {
        this.cd_des = cd_des;
    }

    public String getCd_context() {
        return cd_context;
    }

    public void setCd_context(String cd_context) {
        this.cd_context = cd_context;
    }

    public String getCd_modal() {
        return cd_modal;
    }

    public void setCd_modal(String cd_modal) {
        this.cd_modal = cd_modal;
    }

    public String getCd_pos() {
        return cd_pos;
    }

    public void setCd_pos(String cd_pos) {
        this.cd_pos = cd_pos;
    }

    public String getCd_type() {
        return cd_type;
    }

    public void setCd_type(String cd_type) {
        this.cd_type = cd_type;
    }

    public String getCd_web_keyword() {
        return cd_web_keyword;
    }

    public void setCd_web_keyword(String cd_web_keyword) {
        this.cd_web_keyword = cd_web_keyword;
    }

    public String getCd_web_description() {
        return cd_web_description;
    }

    public void setCd_web_description(String cd_web_description) {
        this.cd_web_description = cd_web_description;
    }

    public String getCd_fieldid() {
        return cd_fieldid;
    }

    public void setCd_fieldid(String cd_fieldid) {
        this.cd_fieldid = cd_fieldid;
    }

    public ArrayList<String> getCd_child() {
        return cd_child;
    }

    public void setCd_child(ArrayList<String> cd_child) {
        this.cd_child = cd_child;
    }
}
