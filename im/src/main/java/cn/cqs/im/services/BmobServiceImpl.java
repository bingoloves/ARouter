package cn.cqs.im.services;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.BuildConfig;
import cn.cqs.im.ChatMessageHandler;
import cn.cqs.im.bean.User;
import cn.cqs.im.model.UserModel;
import cn.cqs.service.constants.IRoutePath;
import cn.cqs.service.im.IBmobService;
import cn.cqs.service.im.LoginCallback;

/**
 * Created by bingo on 2021/1/14.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/14
 */

public class BmobServiceImpl implements IBmobService {

    @Override
    public void init(Context context) {
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

    @Override
    public boolean isLogin() {
        return UserModel.getInstance().getCurrentUser() != null;
    }

    @Override
    public void login(String name, String password, final LoginCallback callback) {
        if (TextUtils.isEmpty(name)) {
            callback.onError("请填写用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            callback.onError("请填写密码");
            return;
        }
        final User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    callback.onSuccess(new Gson().toJson(user));
                } else {
                    callback.onError(e.toString());
                }
            }
        });
    }

    @Override
    public void register(String name, String password, final LoginCallback callback) {
        if (TextUtils.isEmpty(name)) {
            callback.onError("请填写用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            callback.onError("请填写密码");
            return;
        }
        final User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    callback.onSuccess(new Gson().toJson(user));
                } else {
                    callback.onError(e.toString());
                }
            }
        });
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

    @Override
    public void logout() {
        UserModel.getInstance().logout();
    }
}
