package cn.cqs.login;

import android.app.Application;

import cn.cqs.android.base.AbsApplication;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.login.bean.UserInfo;
import cn.cqs.login.services.AccountService;
import cn.cqs.service.ServiceFactory;

/**
 * Created by Administrator on 2021/1/9 0009.
 */

public class LoginApp extends AbsApplication {
    /**
     * 用户登录成功的内存缓存
     */
    public static UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initModuleApp(Application application) {
        LogUtils.d("LoginApp");
        //将内部需要交互的模块开放出去
        ServiceFactory.getInstance().setLoginService(new AccountService());
    }
}
