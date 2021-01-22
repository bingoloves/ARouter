package cn.cqs.android.interfaces;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by Administrator on 2021/1/10 0010.
 * 公共接口
 */
public interface IBaseProvider extends IProvider{
    Object done(Object... o);
}