package cn.cqs.im;

import android.app.Application;
import android.content.Context;

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
    public void initModuleApp(Application application) {
//        LogUtils.i("ChatApp");
        chatApp = application;
        initBmob(application);
        //ServiceFactory普通接口方式，将内部需要交互的模块开放出去
//        BmobServiceImpl bmobService = new BmobServiceImpl();
//        bmobService.init(application);
        ServiceFactory.getInstance().setBmobService(new BmobServiceImpl());
    }

    /**
     * 初始化当前的
     * @param context
     */
    private void initBmob(Context context){
//        LogUtils.i("initBmob");
        /**
         * SDK初始化方式一
         */
        //Bmob.initialize(application, BuildConfig.BMOB_APP_ID);
        /**
         * SDK初始化方式二
         * 设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间
         */
        BmobConfig config = new BmobConfig.Builder(context)
                //设置APPID
                .setApplicationId("3ef3bbfa8d13e8269ae6ede1fede675b")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(5500)
                .build();
        Bmob.initialize(config);
        if (context.getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(context);
            BmobIM.registerDefaultMessageHandler(new ChatMessageHandler(context));
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
