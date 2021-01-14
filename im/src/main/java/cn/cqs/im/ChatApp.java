package cn.cqs.im;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.cqs.android.base.AbsApplication;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.services.BmobServiceImpl;
import cn.cqs.service.ServiceFactory;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class ChatApp extends AbsApplication {

    private static Application chatApp;
    public static Application getApp(){
        return chatApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initModuleApp(Application application) {
        LogUtils.d("ChatApp");
        chatApp = application;
        //将内部需要交互的模块开放出去
        BmobServiceImpl bmobService = new BmobServiceImpl();
        bmobService.init(application);
        ServiceFactory.getInstance().setBmobService(bmobService);
    }
}
