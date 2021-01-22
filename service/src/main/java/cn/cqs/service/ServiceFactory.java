package cn.cqs.service;

import cn.cqs.service.im.BmobService;
import cn.cqs.service.login.LoginService;

/**
 * 普通接口服务，提供组件间数据通信，缺点需要在需要提供服务的地方手动暴露接口出来
 */
public class ServiceFactory {
    /**
     * 登录服务
     */
    private LoginService loginService;
    /**
     * IM 服务
     */
    private BmobService bmobService;

    /**
     * 单例模式,创建公共服务工厂
     */
    private ServiceFactory() {}
    public static ServiceFactory getInstance() {
        return Inner.serviceFactory;
    }

    private static class Inner {
        private static ServiceFactory serviceFactory = new ServiceFactory();
    }

    public void setLoginService(LoginService accountService) {
        this.loginService = accountService;
    }
    public LoginService getLoginService() {
        return loginService;
    }


    public BmobService getBmobService(){
        return bmobService;
    }

    public void setBmobService(BmobService bmobService) {
        this.bmobService = bmobService;
    }
}
