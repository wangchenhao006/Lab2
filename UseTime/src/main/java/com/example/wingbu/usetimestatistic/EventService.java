package com.example.wingbu.usetimestatistic;

import android.app.Notification;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.wingbu.usetimestatistic.dao.AppInfoDao;
import com.example.wingbu.usetimestatistic.dao.GreenDaoManager;
import com.example.wingbu.usetimestatistic.domain.AppInfo;
import com.example.wingbu.usetimestatistic.domain.EventTypeEnum;
import com.example.wingbu.usetimestatistic.domain.UseTimeDataManager;
import com.example.wingbu.usetimestatistic.file.ReadRecordFileUtils;
import com.example.wingbu.usetimestatistic.file.WriteRecordFileUtils;
import com.example.wingbu.usetimestatistic.utils.DateTransUtils;
import com.example.wingbu.usetimestatistic.utils.EventUtils;

import java.util.ArrayList;

/**
 * com.example.wingbu.usetimestatistic
 *
 * @author: WangX
 * @create: 2021/10/31
 */
public class EventService extends Service {
    private UseTimeDataManager mUseTimeDataManager = UseTimeDataManager.getInstance(this);

    public static final String TAG = "EventService";
    AppInfoDao appInfoDao = GreenDaoManager.getInstance().getNewSession().getAppInfoDao();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        long endTime = 0,startTime = 0;
        long time = System.currentTimeMillis() - mUseTimeDataManager.getmDayNum() * DateTransUtils.DAY_IN_MILLIS;
        startTime = ReadRecordFileUtils.getRecordStartTime(WriteRecordFileUtils.BASE_FILE_PATH,time);
        endTime = ReadRecordFileUtils.getRecordEndTime(WriteRecordFileUtils.BASE_FILE_PATH,time) ;


        ArrayList<UsageEvents.Event> eventList = EventUtils.getEventList(this, startTime, endTime);
        for (int i = 0 ; i < eventList.size() ; i++) {
            UsageEvents.Event thisEvent = eventList.get(i);
            UsageStats usageStats = mUseTimeDataManager.getUsageStats(thisEvent.getPackageName());
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(usageStats.getPackageName());
            appInfo.setEndTime(DateTransUtils.stampToDate(usageStats.getLastTimeStamp()));
            appInfo.setStartTime( DateTransUtils.stampToDate(thisEvent.getTimeStamp()));
            appInfo.setRunningTime(DateTransUtils.stampToDate(usageStats.getTotalTimeInForeground()));
            appInfo.setEvent(EventTypeEnum.getName(thisEvent.getEventType()));
            appInfo.setLastTimeUsed(String.valueOf(usageStats.getLastTimeUsed()));
            appInfoDao.insert(appInfo);
        }
        String CHANNEL_ID = "my_channel_01";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

