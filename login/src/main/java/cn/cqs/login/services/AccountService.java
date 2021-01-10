package cn.cqs.login.services;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import cn.cqs.login.LoginApp;
import cn.cqs.login.fragment.UserFragment;
import cn.cqs.service.login.ILoginService;

public class AccountService implements ILoginService {
    @Override
    public boolean isLogin() {
        return LoginApp.userInfo != null;
    }

    @Override
    public String getAccountId() {
        return LoginApp.userInfo == null ? null : LoginApp.userInfo.getAccountId();
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
