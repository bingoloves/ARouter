package cn.cqs.android.enums;

/**
 * Created by bingo on 2021/1/12.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 转场动画枚举
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/12
 */

public enum TransitionEnum {
    /**
     * slide 是两个Activity的连贯操作
     * 联动 左进左出
     */
    SLIDE_LEFT,
    /**
     * 联动 右进右出
     */
    SLIDE_RIGHT,
    /**
     * 联动 上进上出
     */
    SLIDE_TOP,
    /**
     * 联动 下进下出
     */
    SLIDE_BOTTOM,
    /**
     * 针对跳转的目标页面，单一的上进上出
     */
    TOP,
    /**
     * 针对跳转的目标页面，单一的下进下出
     */
    BOTTOM,
    /**
     * 针对跳转的目标页面，单一的左进左出
     */
    LEFT,
    /**
     * 针对跳转的目标页面，单一的右进右出
     */
    RIGHT,
    /**
     * 渐进渐出
     */
    FADE,
    /**
     * 放大缩小
     */
    ZOOM
}
