package cn.cqs.login;

import android.app.Application;
import android.content.Context;

import cn.cqs.android.base.AbsApplication;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.login.services.LoginService;
import cn.cqs.service.ServiceFactory;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class LoginApp extends AbsApplication {
    private static Application loginApp;
    public static Application getApp(){
        return loginApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initModuleApp(Application application) {
        LogUtils.d("LoginApp");
        loginApp = application;
        //将内部需要交互的模块开放出去
        ServiceFactory.getInstance().setLoginService(new LoginService());
    }
}
