package com.lchtime.safetyexpress.bean;

/**
 * Created by ${Hongcha36} on 2017/8/30.
 */

public class UpdateResponse {

    public BasicResult result;

    private String versionCode;
    private String versionName;
    private String versionUrl;


    public String getVersionCode() {
        if(versionCode == null){
            return "";
        }
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }
}
