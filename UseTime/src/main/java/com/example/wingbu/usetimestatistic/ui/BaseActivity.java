package com.example.wingbu.usetimestatistic.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wingbu.usetimestatistic.Bayes;
import com.example.wingbu.usetimestatistic.BuildConfig;
import com.example.wingbu.usetimestatistic.R;
import com.example.wingbu.usetimestatistic.dao.AppInfoDao;
import com.example.wingbu.usetimestatistic.dao.DaoMaster;
import com.example.wingbu.usetimestatistic.dao.DaoSession;
import com.example.wingbu.usetimestatistic.dao.GreenDaoManager;
import com.example.wingbu.usetimestatistic.domain.AppInfo;
import com.example.wingbu.usetimestatistic.domain.EventTypeEnum;
import com.example.wingbu.usetimestatistic.domain.UseTimeDataManager;
import com.example.wingbu.usetimestatistic.file.EventCopyToFileUtils;
import com.example.wingbu.usetimestatistic.file.ReadRecordFileUtils;
import com.example.wingbu.usetimestatistic.file.WriteRecordFileUtils;
import com.example.wingbu.usetimestatistic.utils.DateTransUtils;
import com.example.wingbu.usetimestatistic.utils.EventUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Wingbu on 2017/9/13.
 */

public class BaseActivity extends Activity{

    protected ActionBar mActionBar;
    private UseTimeDataManager mUseTimeDataManager = UseTimeDataManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */


    private void initActionBar(){
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            mActionBar.setCustomView(R.layout.actionbar_custom);//设置自定义的布局：actionbar_custom
            TextView tv_action_title = mActionBar.getCustomView().findViewById(R.id.action_bar_title);
            tv_action_title.setText(R.string.action_bar_title_1);

            ImageView iv_setting = mActionBar.getCustomView().findViewById(R.id.iv_setting);
            iv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jumpToSystemPermissionActivity();
                }
            });

            ImageView iv_write = mActionBar.getCustomView().findViewById(R.id.iv_write);
            iv_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyEventsToFile(UseTimeDataManager.getInstance(getApplicationContext()).getmDayNum());
                }
            });

            ImageView iv_content = mActionBar.getCustomView().findViewById(R.id.iv_content);
            iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDetail("all");
                }
            });

            ImageView iv_predict = mActionBar.getCustomView().findViewById(R.id.iv_predict);
            iv_predict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(BaseActivity.this)
                            .setTitle("预测结果")//标题
                            .setMessage(Bayes.getPrediction(BaseActivity.this))//内容
                            .setIcon(R.drawable.statistic_icon)//图标
                            .create();
                    alertDialog1.show();
                }
            });
        }
    }


    private void jumpToSystemPermissionActivity(){
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void showDetail(String pkg){
        Intent i = new Intent();
        i.setClassName(this,"com.example.wingbu.usetimestatistic.ui.UseTimeDetailActivity");
        i.putExtra("type","times");
        i.putExtra("pkg",pkg);
        startActivity(i);
    }

    private String copyEventsToFile(int dayNumber){
        AppInfoDao appInfoDao = GreenDaoManager.getInstance().getNewSession().getAppInfoDao();

        long endTime = 0,startTime = 0;
        long time = System.currentTimeMillis() - dayNumber * DateTransUtils.DAY_IN_MILLIS;
        startTime = ReadRecordFileUtils.getRecordStartTime(WriteRecordFileUtils.BASE_FILE_PATH,time);
        endTime = ReadRecordFileUtils.getRecordEndTime(WriteRecordFileUtils.BASE_FILE_PATH,time) ;

        Toast.makeText(this,"已将系统数据写入本地文件",Toast.LENGTH_SHORT).show();
        Log.i("BaseActivity"," BaseActivity--copyEventsToFile()    startTime = " + startTime + "  endTime = " + endTime);

        EventCopyToFileUtils.write(this,startTime-1000,endTime);

        ArrayList<UsageEvents.Event> eventList = EventUtils.getEventList(this, startTime, endTime);
        for (int i = 0 ; i < eventList.size() ; i++) {
            UsageEvents.Event thisEvent = eventList.get(i);
            UsageStats usageStats = mUseTimeDataManager.getUsageStats(thisEvent.getPackageName());
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(usageStats.getPackageName());
            appInfo.setEndTime(String.valueOf(usageStats.getLastTimeStamp()));
            appInfo.setStartTime( String.valueOf(thisEvent.getTimeStamp()));
            appInfo.setRunningTime(String.valueOf(usageStats.getTotalTimeInForeground()));
            appInfo.setEvent(String.valueOf(thisEvent.getEventType()));
            appInfo.setLastTimeUsed(String.valueOf(usageStats.getLastTimeUsed()));
            appInfoDao.insert(appInfo);
        }

        return null;
    }

    protected void setActionBarTitle(String title){
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            mActionBar.setCustomView(R.layout.actionbar_custom);//设置自定义的布局：actionbar_custom
            TextView tv_action_title = mActionBar.getCustomView().findViewById(R.id.action_bar_title);
            tv_action_title.setText(title);
        }
    }

    protected void setActionBarTitle(int stringId){
        setActionBarTitle(getString(stringId));
    }
}
