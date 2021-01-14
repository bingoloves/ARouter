package cn.cqs.service.constants;

/**
 * Created by Administrator on 2021/1/10 0010.
 * 全局路由配置 这里前期可以只考虑对外服务的路由配置
 */

public interface IRoutePath {
    /**公共外部服务页面*/
    /*启动页*/
    String SPLASH = "/main/splash";
    /*登录页*/
    String LOGIN = "/login/login";
    /*IM 聊天页*/
    String WECHAT = "/im/wechat";

    /**主服务类*/
    String SERVICE = "/service/service";
    /*降级策略服务*/
    String SERVICE_DEGRADE = "/service/degrade";
    /*json服务*/
    String SERVICE_JSON = "/service/json";

}
