package cn.cqs.android.route;

/**
 * 各模块化配置
 */
public class Config {
    /**
     * 登录模块
     */
    private static final String LOGIN = "cn.cqs.login.LoginApp";
    /**
     * 及时通讯模块
     */
    private static final String IM = "cn.cqs.im.ImApp";

    public static String[] moduleApps = {
            LOGIN,
            IM
    };
}
