package cn.cqs.im.services;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.cqs.im.bean.User;
import cn.cqs.im.model.UserModel;
import cn.cqs.service.constants.IRoutePath;
import cn.cqs.service.im.BmobService;
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
@Route(path = IRoutePath.IM_SERVICE ,name = "即时通讯模块服务")
public class BmobServiceImpl implements BmobService {

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

    @Override
    public void logout() {
        UserModel.getInstance().logout();
    }

    @Override
    public void init(Context context) {

    }
}
