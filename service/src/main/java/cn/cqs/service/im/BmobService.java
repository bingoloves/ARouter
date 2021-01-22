package cn.cqs.service.im;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by bingo on 2021/1/14.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/14
 */
public interface BmobService extends IProvider{
    /*IM服务器当前登录用户是否存在*/
    boolean isLogin();
    void login(String name,String password,LoginCallback callback);
    void register(String name,String password,LoginCallback callback);
    void logout();
}
