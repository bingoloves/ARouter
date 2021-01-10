package cn.cqs.im;

import android.app.Application;
import android.util.Log;

import cn.cqs.android.base.AbsApplication;
import cn.cqs.android.utils.log.LogUtils;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class ImApp extends AbsApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initModuleApp(Application application) {
        LogUtils.d("ImApp");
    }
}
