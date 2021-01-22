package cn.cqs.login.bean;

/**
 * Bmob登录成功的返回的用户数据
 */
public class UserInfo {

    /**
     * sessionToken : ab32eed340047b60808ededfd13814e0
     * username : xurui
     * _c_ : User
     * createdAt : 2020-11-26 16:36:08
     * objectId : c4ae5dffdb
     * updatedAt : 2020-11-26 16:36:08
     */

    private String sessionToken;
    private String username;
    private String _c_;
    private String createdAt;
    private String objectId;
    private String updatedAt;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String get_c_() {
        return _c_;
    }

    public void set_c_(String _c_) {
        this._c_ = _c_;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
