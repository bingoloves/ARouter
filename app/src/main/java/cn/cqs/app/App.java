package cn.cqs.app;

import android.app.Application;

import cn.cqs.android.base.AbsApplication;
import cn.cqs.android.utils.ApplicationUtils;
import cn.cqs.service.ServiceFactory;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class App extends AbsApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtils.getInstance().initModuleApp(this);
    }

    @Override
    public void initModuleApp(Application application) {

    }
}
