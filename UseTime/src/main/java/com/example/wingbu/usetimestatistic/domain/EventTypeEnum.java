package com.example.wingbu.usetimestatistic.domain;

/**
 * com.example.wingbu.usetimestatistic.domain
 *
 * @author: WangX
 * @create: 2021/10/31
 */
public enum  EventTypeEnum{
//    public static final int CONFIGURATION_CHANGE = 5;
//    public static final int MOVE_TO_BACKGROUND = 2;
//    public static final int MOVE_TO_FOREGROUND = 1;
//    public static final int NONE = 0;
//    public static final int SHORTCUT_INVOCATION = 8;
//    public static final int USER_INTERACTION = 7;
    CONFIGURATION_CHANGE("环境改变",5),
    MOVE_TO_BACKGROUND("切入后台",2),
    MOVE_TO_FOREGROUND("切入前台",1),
    NONE("无",0),
    SHORTCUT_INVOCATION("快捷方式",8),
    USER_INTERACTION("用户交互",7),
    ;
// 成员变量
private String name;
private int index;
// 构造方法
private EventTypeEnum(String name,int index){
        this.name=name;
        this.index=index;
        }
// 普通方法
public static String getName(int index){
        for(EventTypeEnum c:EventTypeEnum.values()){
        if(c.getIndex()==index){
        return c.name;
        }
        }
        return null;
        }
// get set 方法
public String getName(){
        return name;
        }
public void setName(String name){
        this.name=name;
        }
public int getIndex(){
        return index;
        }
public void setIndex(int index){
        this.index=index;
        }
}
