package com.lchtime.safetyexpress.bean.res;

import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.SearchBean;

import java.util.ArrayList;

/**
 * Created by yxn on 2017/4/26.
 */

public class SearchRes {
    private BasicResult result;
    private ArrayList<SearchBean> search;
    private ArrayList<String> history;
    private ArrayList<String> hot;

    public BasicResult getResult() {
        return result;
    }

    public void setResult(BasicResult result) {
        this.result = result;
    }

    public ArrayList<SearchBean> getSearch() {
        return search;
    }

    public void setSearch(ArrayList<SearchBean> search) {
        this.search = search;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public ArrayList<String> getHot() {
        return hot;
    }

    public void setHot(ArrayList<String> hot) {
        this.hot = hot;
    }
}
