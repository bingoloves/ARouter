package cn.cqs.android.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

import cn.cqs.android.route.Priority;
import cn.cqs.android.utils.log.LogUtils;

/**
 * Created by Administrator on 2021/1/9 0009.
 * 路由拦截器
 */
@Interceptor(priority = Priority.LOGIN, name = "登录拦截器")
public class LoginInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        //String path = postcard.getPath();
        //LogUtils.e(path);
        callback.onContinue(postcard);
        //callback.onInterrupt();
    }

    @Override
    public void init(Context context) {

    }
}
