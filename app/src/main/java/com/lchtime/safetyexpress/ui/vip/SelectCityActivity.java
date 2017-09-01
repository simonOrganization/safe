package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.CitySortAdapter;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.views.CityView.CharacterParser;
import com.lchtime.safetyexpress.views.CityView.CitySortModel;
import com.lchtime.safetyexpress.views.CityView.PinyinComparator;
import com.lchtime.safetyexpress.views.CityView.SideBar;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by android-cp on 2017/5/1.选择城市界面
 */
@ContentView(R.layout.city_activity)
public class SelectCityActivity extends BaseUI {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private CitySortAdapter adapter;
    private CharacterParser characterParser;
    private List<CitySortModel> SourceDateList;
    private String cityName;
    private Object oldview;
    private String city;
    private boolean boo;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
         Intent intent = this.getIntent();
         city = intent.getStringExtra("city");
       // Log.i("----------", "clickEvent:1 " +city );
        setTitle("选择城市");
        rightTextVisible("保存");
        initViews();
    }

    @Override
    protected void prepareData() {

    }
    /**
     * 修改
     * @param
     */
    @Override
    protected void clickEvent() {
        
        if (!cityName.equals("地理位置") ) {
            Intent intent = new Intent();
            intent.putExtra("city", cityName);
            setResult(0,intent);
            finish();

        }else {
            Intent intent = new Intent();
            intent.putExtra("city", "地理位置");
            setResult(0,intent);
            finish();

        }
    }
    private void initViews() {
        characterParser = CharacterParser.getInstance();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new CitySortAdapter(this, SourceDateList,city);
        //sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


               // adapter.clearSelection(position);
               // View v= sortListView.getChildAt(position);
                TextView tx = (TextView) view.findViewById(R.id.tv_city_name);
               // Log.i("qaz", "onItemClick: "+ view + "------" +tx +"-----"+ position);
                if(((CitySortModel) adapter.getItem(position)).isSelect){
                    ((CitySortModel) adapter.getItem(position)).isSelect = false;
                    tx.setTextColor(mContext.getResources().getColor(R.color.black));
                    cityName = "地理位置";
                    city="地理位置";
                    adapter.setCity(city);
                 //   Log.i("----------", "onItemClick: 1" + "点击了 " + city);
                }else{
                    ((CitySortModel) adapter.getItem(position)).isSelect = true;
                    tx.setTextColor(mContext.getResources().getColor(R.color.red));
                    cityName = ((CitySortModel) adapter.getItem(position)).getName();
                    adapter.setCity(cityName);
                    //Log.i("----------", "onItemClick: 2" + "点击了 " + cityName);
                }

                adapter.notifyDataSetChanged();
            }
        });


    }

    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setName(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;

    }
}
