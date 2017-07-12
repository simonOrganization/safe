package com.lchtime.safetyexpress.ui.chat.hx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.ui.chat.hx.activity.protocal.GetInfoProtocal;
import com.lchtime.safetyexpress.ui.chat.hx.adapter.AllPeopleAdapter;
import com.lchtime.safetyexpress.ui.chat.hx.bean.InfoBean;
import com.lchtime.safetyexpress.ui.chat.hx.fragment.protocal.AddCommandProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.JsonUtils;
import com.lchtime.safetyexpress.utils.SpTools;

/**
 * Created by Dreamer on 2017/6/8.
 */

public class AllPeopleActivity extends Activity implements View.OnClickListener {

    private ListView mListView;
    private String mGroupId;
    private GetInfoProtocal mProtocal;
    private String mUb_id;

    private TextView mTitle;
    private TextView mTitleRight;
    private LinearLayout mTitleLeft;
    private LinearLayout mLlTitleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_people_qun);
        mGroupId = getIntent().getStringExtra("groupId");
        initData();
        mListView = (ListView) findViewById(R.id.list);
        initTitle();

    }

    private void initTitle() {
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = (TextView) findViewById(R.id.tv_delete);
        mTitleLeft = (LinearLayout) findViewById(R.id.ll_back);
        mLlTitleRight = (LinearLayout) findViewById(R.id.ll_right);
        mLlTitleRight.setVisibility(View.GONE);
        mTitle.setText("所有群成员");
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleLeft.setOnClickListener(this);
    }

    private void initData() {
        if (mProtocal == null){
            mProtocal = new GetInfoProtocal();
        }
        mUb_id = SpTools.getString(this, Constants.userId,"");
        if (TextUtils.isEmpty(mUb_id) || TextUtils.isEmpty(mGroupId)){
            CommonUtils.toastMessage("没有获取到群组信息");
            return;
        }
        mProtocal.getQuners(mUb_id, mGroupId, new AddCommandProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                InfoBean bean = (InfoBean) JsonUtils.stringToObject((String) response, InfoBean.class);
                AllPeopleAdapter adapter = new AllPeopleAdapter(AllPeopleActivity.this,1,bean.quners);
                mListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_back){
            finish();
        }
    }
}
