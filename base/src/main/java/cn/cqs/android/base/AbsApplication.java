package cn.cqs.android.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;

import cn.cqs.android.R;
import cn.cqs.android.utils.ActivityStackManager;
import cn.cqs.android.utils.ApplicationUtils;
import cn.cqs.android.utils.crash.CaocConfig;
import cn.cqs.android.utils.log.LogUtils;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public abstract class AbsApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 ARouter
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
        initCatchException();
        iniLogUtils();
        initModuleApp(this);
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * Activity栈管理
     */
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ActivityStackManager.getStackManager().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ActivityStackManager.getStackManager().removeActivity(activity);
        }
    };
    /**
     * 初始化日志开关
     */
    private void iniLogUtils() {
        LogUtils.openLog(openLog());
    }

    /**
     * 配置全局异常崩溃操作
     */
    private void initCatchException() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(isDebug())          //是否启动全局异常捕获
                .showErrorDetails(isDebug()) //是否显示错误详细信息
                .showRestartButton(isDebug())//是否显示重启按钮
                .trackActivities(isDebug())  //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
//                .errorDrawable(R.mipmap.ic_launcher) //错误图标
//                .restartActivity(LoginActivity.class) //重新启动后的activity
                //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }

    /**
     * 通过子类复写设置Debug模式
     * @return
     */
    public boolean isDebug(){
        return true;
    }
    /**
     * 通过子类复写设置日志开关
     * @return
     */
    public boolean openLog(){
        return true;
    }
    /**
     * Module Application 初始化
     */
    public abstract void initModuleApp(Application application);


}
