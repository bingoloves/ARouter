package cn.cqs.login.services;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.cqs.android.utils.ACache;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.login.LoginApp;
import cn.cqs.login.fragment.UserFragment;
import cn.cqs.service.constants.Cache;
import cn.cqs.service.constants.IRoutePath;
import cn.cqs.service.login.LoginService;

@Route(path = IRoutePath.LOGIN_SERVICE ,name = "登录模块服务")
public class LoginServiceImpl implements LoginService {
    public static String userInfo;
    /**
     * 这里将IM服务器当前登录用户是否登录也判断了，具体根据实际业务来
     * @return
     */
    @Override
    public boolean isLogin() {
        return ACache.get(LoginApp.getApp()).getAsBoolean(Cache.IS_LOGIN);
    }

    @Override
    public String getUser() {
        return userInfo;
    }


    @Override
    public Fragment newUserFragment(Activity activity, int containerId, FragmentManager manager, Bundle bundle, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment userFragment = new UserFragment();
        transaction.add(containerId, userFragment, tag);
        transaction.commit();
        return userFragment;
    }

    @Override
    public void init(Context context) {

    }
}
