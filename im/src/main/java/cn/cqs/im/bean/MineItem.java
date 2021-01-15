package cn.cqs.im.bean;

import android.view.View;

/**
 * Created by bingo on 2020/12/2.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/2
 */

public class MineItem {
    public int icon;
    public String name;
    public String msg;
    public boolean hasLine;
    public View.OnClickListener clickListener;

    public MineItem(int icon, String name, String msg, boolean hasLine, View.OnClickListener clickListener) {
        this.icon = icon;
        this.name = name;
        this.msg = msg;
        this.hasLine = hasLine;
        this.clickListener = clickListener;
    }
    public MineItem(int icon, String name, String msg, View.OnClickListener clickListener) {
        this.icon = icon;
        this.name = name;
        this.msg = msg;
        this.clickListener = clickListener;
    }
    public MineItem(int icon, String name, View.OnClickListener clickListener) {
        this.icon = icon;
        this.name = name;
        this.clickListener = clickListener;
    }
}
