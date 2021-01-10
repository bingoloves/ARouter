package cn.cqs.android.route;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import cn.cqs.android.utils.log.LogUtils;

/**
 * Created by Administrator on 2021/1/9 0009.
 * 默认的页面跳转回调实现
 */

public class DefaultNavCallback extends NavCallback{
    /**
     * 页面抵达
     * @param postcard
     */
    @Override
    public void onArrival(Postcard postcard) {

    }

    /**
     * 页面未找到的处理
     * @param postcard
     */
    @Override
    public void onLost(Postcard postcard) {
        LogUtils.e("请查看目标路由是否配置正确！path = " + postcard.getPath());
    }
}
