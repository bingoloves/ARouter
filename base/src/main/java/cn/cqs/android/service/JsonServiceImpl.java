package cn.cqs.android.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.service.constants.IRoutePath;

/**
 * Created by Administrator on 2021/1/9.
 * 数据解析类.主要作用于
 * ARouter.getInstance().build("/xx/xx").withObject("xx",object)...
 */
@Route(path = IRoutePath.SERVICE_JSON)
public class JsonServiceImpl implements SerializationService {
    private Gson gson;
    @Override
    public void init(Context context) {
        gson = new Gson();
    }

    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        return gson.fromJson(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return gson.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return gson.fromJson(input, clazz);
    }
}
