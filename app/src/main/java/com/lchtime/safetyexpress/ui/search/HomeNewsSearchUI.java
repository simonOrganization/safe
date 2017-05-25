package com.lchtime.safetyexpress.ui.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.search.fragment.CircleSearchResultFragment;
import com.lchtime.safetyexpress.ui.search.fragment.HomeSearchContentFragment;
import com.lchtime.safetyexpress.ui.search.fragment.HomeSearchResultFragment;
import com.lchtime.safetyexpress.ui.search.fragment.NewsSearchResultFragment;
import com.lchtime.safetyexpress.ui.search.fragment.VideosSearchResultFragment;
import com.lchtime.safetyexpress.ui.search.protocal.SerchProtocal;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * 新闻 搜索
 * Created by user on 2017/4/17.
 */
@ContentView(R.layout.home_news_search_ui)
public class HomeNewsSearchUI extends BaseUI {

    //取消
    @ViewInject(R.id.tv_news_search_cancel)
    private TextView tv_news_search_cancel;
    //编辑框
    @ViewInject(R.id.et_news_search)
    private EditText et_news_search;
    //删除全部文字按钮
    @ViewInject(R.id.ivDeleteText)
    private ImageView ivDeleteText;
    //fragment放置处
    @ViewInject(R.id.fl_conent)
    private FrameLayout fl_conent;


    //搜索内容
    public String content;
    public String mType;

    private HomeSearchContentFragment keyWordFragment;
    private Fragment currentFragment;

    private String intentData;
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(intentData)){
            et_news_search.setText(intentData);

            searchDown();


        }
    }
    @Override
    protected void setControlBasis() {
        mType = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(mType)){
            mType = "0";
        }
        intentData = getIntent().getStringExtra("content");
        initListener();
        changeUI(7);

    }
    //软键盘调用两次，记录次数让其调用一次
    private int searchTimes = 0;
    private void initListener() {
        //监听edittext变化
        et_news_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = et_news_search.getText().toString().trim();
                if (!TextUtils.isEmpty(content)){
                    //tv_news_search_cancel.setText("搜索");
                    ivDeleteText.setVisibility(View.VISIBLE);
                    //设置键盘为搜索
                    et_news_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                }else {
                    //tv_news_search_cancel.setText("取消");
                    ivDeleteText.setVisibility(View.GONE);
                    //设置键盘为完成
                    et_news_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    if (!(currentFragment instanceof HomeSearchContentFragment)){
                        //当搜索词为空并且当前界面不是词汇界面，则跳转到词汇界面
                        changeUI(7);
                        currentFragment = mCacheFragments.get(7);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //对edittext设置键盘监听
        et_news_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                content = et_news_search.getText().toString().trim();
                /*判断是否是“search”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH||actionId==0){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }

                    //搜索数据,并且防止搜索两次
                    if (!TextUtils.isEmpty(content)){
                        if (searchTimes == 0) {
                            searchDown();
                            searchTimes = 1;
                        }else if (searchTimes == 1){
                            searchTimes = 0;
                        }
                    }
                    return true;
                }else if (actionId == EditorInfo.IME_ACTION_DONE){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        //删除全部文字
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_news_search.setText("");
            }
        });

    }


    @Override
    protected void prepareData() {

    }


    /**
     * 取消
     * @param view
     */
    @OnClick(R.id.tv_news_search_cancel)
    private void getCancel(View view){
        String text = tv_news_search_cancel.getText().toString().trim();
        if ("取消".equals(text)){
            if (currentFragment instanceof HomeSearchContentFragment){
                //如果是词汇界面直接关闭就可以
                finish();
            }else {

                finish();
            }
        }else {
            //按下搜索键
            //searchDown();
            finish();

        }
    }




    public static Map<Integer,Fragment> mCacheFragments = new HashMap<>();
    public static final int FRAGMENT_HOME = 0;//首页
    public static final int FRAGMENT_NEWS = 1;//新闻
    public static final int FRAGMENT_VEDIO = 2;//视频
    public static final int FRAGMENT_CIRCLE = 3;//圈子
    public static final int FRAGMENT_FIRST = 7;//第一个界面
    private void changeUI(int position) {

        Fragment fragment = null;

        if(mCacheFragments.containsKey(position)){

            fragment = mCacheFragments.get(position);

        }else {
            switch (position) {

                case FRAGMENT_FIRST:
                    fragment = new HomeSearchContentFragment();
                    break;
                case FRAGMENT_HOME://返回 首页 对应的fragment
                    fragment = new HomeSearchResultFragment();
                    break;

                case FRAGMENT_NEWS://返回 新闻 对应的fragment
                    fragment = new NewsSearchResultFragment();
                    break;
                case FRAGMENT_VEDIO://返回 视频 对应的fragment
                    fragment = new VideosSearchResultFragment();
                    break;
                case FRAGMENT_CIRCLE://返回 圈子 对应的fragment
                    fragment = new CircleSearchResultFragment();
                    break;
            }

            mCacheFragments.put(position,fragment);

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        boolean isShow = false;
        if (fragment.isAdded()) {
            //显示 Fragment
            transaction.show(fragment);
            isShow = true;

        }else {
            //显示、加载Fragment
            transaction.add(R.id.fl_conent, fragment);
        }

        if (currentFragment != null){
            transaction.hide(currentFragment);
        }

        transaction.commit();

        currentFragment = fragment;
        if (isShow){
            if (!(fragment instanceof HomeSearchContentFragment)) {
                getSearch(key);
            }
            isShow = false;
        }

        if (!TextUtils.isEmpty(content)){
            addSearchView(content);
        }

    }

    private SerchProtocal mProtocal;
    //搜索请求网络数据
    public void getSearch(String key) {
        if (mProtocal == null){
            mProtocal = new SerchProtocal();
        }
        //搜索
        //先进行加入view中
        addSearchView(key);

        if (currentFragment != null){
            switch (Integer.parseInt(mType)) {

                case FRAGMENT_HOME://返回 首页 对应的fragment

                    ((HomeSearchResultFragment)currentFragment).getSearch(key);

                    break;

                case FRAGMENT_NEWS://返回 新闻 对应的fragment
                    ((NewsSearchResultFragment)currentFragment).getSearch(key);
                    break;
                case FRAGMENT_VEDIO://返回 视频 对应的fragment
                    ((VideosSearchResultFragment)currentFragment).getSearch(key);
                    break;
                case FRAGMENT_CIRCLE://返回 圈子 对应的fragment
                    ((CircleSearchResultFragment)currentFragment).getSearch(key);
                    break;
            }

        }
//        mProtocal.getSearchResult(key, Integer.parseInt(mType), mType, new SerchProtocal.NormalListener() {
//            @Override
//            public void normalResponse(Object response) {
//                if (response == null){
//                    CommonUtils.toastMessage("服务器数据类型异常");
//                    return;
//                }
//                switch (Integer.parseInt(mType)){
//                    case 0:
//                        //全局搜索
//                        QJSearchBean bean0 = (QJSearchBean) response;
//                        break;
//                    case 1:
//                    case 2:
//                        //新闻视频
//                        QJSearchBean bean1 = (QJSearchBean) response;
//                        break;
//                    case 3:
//                        //圈子搜索
//                        QJSearchBean bean2 = (QJSearchBean) response;
//                        break;
//                }
//            }
//        });
    }

    private void addSearchView(String key) {
        HomeSearchContentFragment fragment = (HomeSearchContentFragment) mCacheFragments.get(7);
        fragment.addLoacalSearchView(key);
    }

    private void searchDown() {
        content = et_news_search.getText().toString().trim();
        this.key = content;
        if(mCacheFragments.containsKey(7)){
            Fragment temp = mCacheFragments.get(7);
            if (temp.isAdded()&&currentFragment == temp){
                //从词汇界面跳到搜索到的内容界面
                changeUI(Integer.parseInt(mType));
                return;
            }
        }
        //如果以上条件不符合，那么就触动搜索
        getSearch(content);
    }

    public void setSearchContent(String key){
        //现将文字设置到输入框
        et_news_search.setText(key);
        //进行搜索操作
        if(mCacheFragments.containsKey(7)){
            Fragment temp = mCacheFragments.get(7);
            if (temp.isAdded()&&currentFragment == temp){
                //从词汇界面跳到搜索到的内容界面
                this.key = key;
                changeUI(Integer.parseInt(mType));
                return;
            }
        }
        //如果以上条件不符合，那么就触动搜索
        getSearch(key);
    }
    private String key = "";
    public String getKey(){
        return key;
    }

}
