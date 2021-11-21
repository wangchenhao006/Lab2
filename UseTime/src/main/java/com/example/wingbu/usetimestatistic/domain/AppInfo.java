package com.example.wingbu.usetimestatistic.domain;

import android.app.usage.UsageEvents;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * com.example.wingbu.usetimestatistic.domain
 *
 * @author: WangX
 * @create: 2021/10/31
 */
@Entity
public class AppInfo {
    @Id(autoincrement = true)
    private Long id;
    private String appName;
    private String startTime;
    private String endTime;
    private String runningTime;

    //扩充

    //事件动作
    private String event;
    //最后一次使用的时长
    private String lastTimeUsed;

    @Generated(hash = 744253126)
    public AppInfo(Long id, String appName, String startTime, String endTime,
            String runningTime, String event, String lastTimeUsed) {
        this.id = id;
        this.appName = appName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runningTime = runningTime;
        this.event = event;
        this.lastTimeUsed = lastTimeUsed;
    }
    @Generated(hash = 1656151854)
    public AppInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getRunningTime() {
        return this.runningTime;
    }
    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }
    public String getEvent() {
        return this.event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public String getLastTimeUsed() {
        return this.lastTimeUsed;
    }
    public void setLastTimeUsed(String lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
    }

}
