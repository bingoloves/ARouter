package cn.cqs.android.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.cqs.android.base.AbsApplication;
import cn.cqs.android.route.Config;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class ApplicationUtils {

    private ApplicationUtils applicationUtils;
    private Application application;
    /**
     * 禁止外部创建 ServiceFactory 对象
     */
    private ApplicationUtils() {
    }

    /**
     * 通过静态内部类方式实现 ServiceFactory 的单例
     */
    public static ApplicationUtils getInstance() {
        return Inner.applicationUtils;
    }

    private static class Inner {
        private static ApplicationUtils applicationUtils = new ApplicationUtils();
    }

    /**
     * 可外部传入
     * @param application
     */
    public void init(Application application){
        this.application = application;
    }

    /**
     * 获取当前的Application
     * @return
     */
    public Application getApplication(){
        if (application == null){
            Application curApplication = getCurApplication();
            if (curApplication != null){
                application = curApplication;
            }
        }
        return application;
    }
    /**
     * 获取应用名称
     * @return
     */
    public String getAppName() {
        try {
            PackageManager packageManager = getApplication().getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(getApplication().getApplicationInfo()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取当前应用的Application
     * 先使用ActivityThread里获取Application的方法，如果没有获取到，
     * 再使用AppGlobals里面的获取Application的方法
     * @return
     */
    private static Application getCurApplication(){
        Application application = null;
        try{
            Class atClass = Class.forName("android.app.ActivityThread");
            Method currentApplicationMethod = atClass.getDeclaredMethod("currentApplication");
            currentApplicationMethod.setAccessible(true);
            application = (Application) currentApplicationMethod.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(application != null) return application;
        try{
            Class atClass = Class.forName("android.app.AppGlobals");
            Method currentApplicationMethod = atClass.getDeclaredMethod("getInitialApplication");
            currentApplicationMethod.setAccessible(true);
            application = (Application) currentApplicationMethod.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return application;
    }

    /**
     * 已经初始化后的数组
     */
    private List<String> moduleAfterList = new ArrayList<>();
    /**
     * 初始化module库操作，除了当前的Application，在每个moudule的onCreate()方法中调用
     * TODO 使用异步分段初始化
     */
    public void initModuleApp(Application application){
        String currentApplicationName = application.getClass().getCanonicalName();
        for (String moduleApp : Config.moduleApps) {
            //双重判断避免重复调用
            if (!TextUtils.isEmpty(currentApplicationName) && !moduleAfterList.contains(moduleApp)){
                moduleAfterList.add(moduleApp);
                try {
                    Class clazz = Class.forName(moduleApp);
                    AbsApplication app = (AbsApplication) clazz.newInstance();
                    app.initModuleApp(application);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
