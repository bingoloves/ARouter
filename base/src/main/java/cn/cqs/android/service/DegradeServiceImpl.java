package cn.cqs.android.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

import cn.cqs.service.constants.IRoutePath;

/**
 * Created by Administrator on 2021/1/9 0009.
 * 降级策略
 */
@Route(path = IRoutePath.SERVICE_DEGRADE)
public class DegradeServiceImpl implements DegradeService {
    @Override
    public void onLost(Context context, Postcard postcard) {
        // do something.
    }

    @Override
    public void init(Context context) {

    }
}
