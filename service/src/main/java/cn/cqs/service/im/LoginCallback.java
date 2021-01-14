package cn.cqs.service.im;

/**
 * Created by bingo on 2021/1/14.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/14
 */

public interface LoginCallback {
    /**
     * 返回一个用户信息的json子串
     * @param userInfo
     */
    void onSuccess(String userInfo);

    /**
     * 错误信息
     * @param error
     */
    void onError(String error);
}
