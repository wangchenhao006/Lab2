package com.example.wingbu.usetimestatistic;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.content.Context;
import com.example.wingbu.usetimestatistic.domain.*;
import com.example.wingbu.usetimestatistic.utils.DateTransUtils;
import com.example.wingbu.usetimestatistic.utils.EventUtils;

import java.util.*;


/**
 * com.example.wingbu.usetimestatistic
 * 查询APP序列，将当前APP的历史下一个应用作为预测结果
 * 可选择参数为
 * 1。打开时间
 * 2。是否曾经为当前app的下一序列
 * 3。使用地点
 * 4。使用频率
 * 5。是否在当前应用打开前（或30s内）被切入后台
 *
 * @author: WangX
 * @create: 2021/11/3
 */
public class Bayes {
    static ArrayList<UsageEvents.Event> eventData = new ArrayList<UsageEvents.Event>();
    static ArrayList<UsageStats> usageData = new ArrayList<UsageStats>();
    static ArrayList<PackageInfo> packageData = new ArrayList<PackageInfo>();


    static HashMap<String, UsageStats> categories = new HashMap<>();
    static HashMap<String, UsageStats> categories1 = new HashMap<>();

    static Context context ;
    static UseTimeDataManager mUseTimeDataManager ;

    private static long startTime = DateTransUtils.getTodayStartStamp(0,0,0);
    private static long endTime = DateTransUtils.getTodayStartStamp(23,59,59);

    //Read the data from the file and store it in the collection data
    public static ArrayList<UsageEvents.Event> init(Context context){
        mUseTimeDataManager = UseTimeDataManager.getInstance(context);

        ArrayList<UsageStats> usageList = EventUtils.getUsageList(context, startTime, endTime);

        Collections.sort(usageList, new Comparator<UsageStats>(){
            @Override
            public int compare(UsageStats h1, UsageStats h2) {
                return Long.compare(h1.getLastTimeUsed(), h2.getLastTimeUsed());
            }
        });

        for (int i = 1; i < usageList.size(); i++) {
            usageData.add(usageList.get(i));
            if (!"android".equals(usageList.get(i).getPackageName()) &&
                    !usageList.get(i).getPackageName().contains("com.android")){
                categories.put(usageList.get(i).getPackageName(),null);
            }
        }

        ArrayList<UsageEvents.Event> eventList = EventUtils.getEventList(context, startTime, endTime);
        for (int i = 0 ; i < eventList.size()-1 ; i++) {
            eventData.add(eventList.get(i));

            if (eventList.get(i).getPackageName().equals(context.getPackageName())
                && !eventList.get(i+1).getPackageName().equals(context.getPackageName())){
                categories1.put(eventList.get(i+1).getPackageName(),null);
            }
        }
        packageData = mUseTimeDataManager.getmPackageInfoListOrderByTime();

        return eventData;
    }

    //Calculates the value of the denominator and returns
    public static double denominator(PackageInfo packageInfo) {
        double result = 0;
        //打开时间
        int count1 = 0;
        //事件类型
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        for(int i = 0; i < packageData.size() ;i++) {
            if(packageInfo.getmUsedCount() > packageData.get(i).getmUsedCount()
            ) {
                count1++;
            }
            if(packageInfo.getmUsedCount()>packageData.get(i).getmUsedCount()){
                count2++;
            }
        }
        result = (count1 / (packageData.size()*1.0))*(count2 / (packageData.size()*1.0));
        System.out.println(result);
        return result;
    }

    //Calculate the value of the molecule
    public static double moleculeIsNext(PackageInfo packageInfo) {
        double result;
        int countIsNext = 0;
        //一天内打开过
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        for(int i = 0; i < packageData.size() ;i++) {
            //是否是当前应用下一个

            boolean isNext = categories.containsKey(packageData.get(i).getmPackageName())
                    || categories1.containsKey(packageData.get(i).getmPackageName());

            if(!isNext) {
                continue;
            }
            countIsNext++;
            if(packageInfo.getmUsedTime() > packageData.get(i).getmUsedTime()
            ) {
                count1++;
            }
            if(packageInfo.getmUsedCount()>packageData.get(i).getmUsedCount()){
                count2++;
            }
        }
        result = (countIsNext / (packageData.size()*1.0))*(count1 / (countIsNext*1.0))*(count2 / (countIsNext*1.0));
        return result;
    }

    //Compare the probability of catching a cold and not catching a cold, and return the comparison results
    private static String compared(Context context){
        StringBuilder str = new StringBuilder();
        double d1 = 0;
        double max = 0;
        PackageInfo maxPackage = null;

        for (PackageInfo packageInfo : packageData) {
            d1 = moleculeIsNext(packageInfo)*1.0 / denominator(packageInfo);
            str.append(packageInfo.getmPackageName()).append("的概率为：").append(d1).append("\n");
            if (d1>max) {
                max = d1;
                maxPackage = packageInfo;
            }
        }

        assert maxPackage != null;
        UsageEvents.Event lastEvent = mUseTimeDataManager.getLastEvent(maxPackage.getmPackageName());
        UsageStats usageStats = mUseTimeDataManager.getUsageStats(maxPackage.getmPackageName());

        System.out.println("Probability of next:"+max);
        System.out.println(str);
        StringBuilder builder = new StringBuilder()
                .append("最有可能的App：").append(maxPackage.getmPackageName()).append("\n")
                .append(" 打开次数为：").append(maxPackage.getmUsedCount()).append("\n")
                .append(" 使用时间为：").append(maxPackage.getmUsedTime() / 1000).append("s").append("\n")
                .append("打开时间为：").append(DateTransUtils.stampToDate(usageStats.getFirstTimeStamp())).append("\n")
                .append("关闭时间为：").append(DateTransUtils.stampToDate(usageStats.getLastTimeStamp())).append("\n")
                .append("最后一次事件为：").append(EventTypeEnum.getName(lastEvent.getEventType()))
                .append("\n\n")
                .append(str);

        return builder.toString();
    }

    public static String getPrediction(Context context) {
        Bayes.context = context;
        init(context);
        String result = compared(context);

        return result;
    }


}
