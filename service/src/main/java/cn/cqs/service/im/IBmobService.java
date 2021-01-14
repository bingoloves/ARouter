package cn.cqs.service.im;

import android.content.Context;

/**
 * Created by bingo on 2021/1/14.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/14
 */

public interface IBmobService {
    void init(Context context);
    /*IM服务器当前登录用户是否存在*/
    boolean isLogin();
    void login(String name,String password,LoginCallback callback);
    void register(String name,String password,LoginCallback callback);
    void logout();
}
