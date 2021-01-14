package cn.cqs.service;

import cn.cqs.service.im.IBmobService;
import cn.cqs.service.login.ILoginService;

public class ServiceFactory {

    private ILoginService accountService;
    private IBmobService bmobService;

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

    public void setLoginService(ILoginService accountService) {
        this.accountService = accountService;
    }
    public ILoginService getLoginService() {
        return accountService;
    }


    public IBmobService getBmobService(){
        return bmobService;
    }

    public void setBmobService(IBmobService bmobService) {
        this.bmobService = bmobService;
    }
}
