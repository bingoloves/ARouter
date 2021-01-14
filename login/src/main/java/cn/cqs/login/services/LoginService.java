package cn.cqs.login.services;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.cqs.android.utils.ACache;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.login.LoginApp;
import cn.cqs.login.fragment.UserFragment;
import cn.cqs.service.ServiceFactory;
import cn.cqs.service.constants.Cache;
import cn.cqs.service.login.ILoginService;

public class LoginService implements ILoginService {
    /**
     * 这里将IM服务器当前登录用户是否登录也判断了，具体根据实际业务来
     * @return
     */
    @Override
    public boolean isLogin() {
        LogUtils.e("isLogin = "+ACache.get(LoginApp.getApp()).getAsBoolean(Cache.IS_LOGIN));
        return ACache.get(LoginApp.getApp()).getAsBoolean(Cache.IS_LOGIN) && ServiceFactory.getInstance().getBmobService().isLogin();
    }

    @Override
    public String getUser() {
        return null;
    }


    @Override
    public Fragment newUserFragment(Activity activity, int containerId, FragmentManager manager, Bundle bundle, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment userFragment = new UserFragment();
        transaction.add(containerId, userFragment, tag);
        transaction.commit();
        return userFragment;
    }
}
