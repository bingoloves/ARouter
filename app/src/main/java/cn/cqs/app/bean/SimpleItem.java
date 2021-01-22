package cn.cqs.app.bean;

import android.view.View;

/**
 * Created by bingo on 2021/1/22.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 示例对象
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/22
 */

public class SimpleItem {
    public String name;
    public String desc;//说明或者描述
    public Class clazz;//跳转页面对象
    public String routePath;//用于ARouter页面跳转

    /**
     * 普通跳转
     * @param name
     * @param desc
     * @param clazz
     */
    public SimpleItem(String name, String desc,Class clazz) {
        this.name = name;
        this.desc = desc;
        this.clazz = clazz;
    }

    /**
     * ARouter 页面跳转
     * @param name
     * @param desc
     * @param routePath
     */
    public SimpleItem(String name, String desc, String routePath) {
        this.name = name;
        this.desc = desc;
        this.routePath = routePath;
    }
}
