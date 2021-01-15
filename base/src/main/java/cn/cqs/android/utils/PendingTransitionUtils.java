package cn.cqs.android.utils;

import android.app.Activity;

import cn.cqs.android.R;
import cn.cqs.android.enums.TransitionEnum;

/**
 * Created by bingo on 2021/1/12.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 转场动画工具类
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/12
 */

public class PendingTransitionUtils {
    public static void animateEnter(Activity activity,TransitionEnum transitionEnum){
        switch (transitionEnum){
            case SLIDE_LEFT:
                activity.overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
                break;
            case SLIDE_RIGHT:
                activity.overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
                break;
            case SLIDE_TOP:
                activity.overridePendingTransition(R.anim.slide_in_from_top,R.anim.slide_out_from_bottom);
                break;
            case SLIDE_BOTTOM:
                activity.overridePendingTransition(R.anim.slide_in_from_bottom,R.anim.slide_out_from_top);
                break;
//            case TOP:
//                activity.overridePendingTransition(R.anim.slide_in_from_top,0);
//                break;
//            case RIGHT:
//                activity.overridePendingTransition(R.anim.slide_in_from_right,0);
//                break;
//            case BOTTOM:
//                activity.overridePendingTransition(R.anim.slide_in_from_bottom,0);
//                break;
//            case LEFT:
//                activity.overridePendingTransition(R.anim.slide_in_from_left,0);
//                break;
            case FADE:
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case ZOOM:
                activity.overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
                break;
        }
    }
    public static void animateExit(Activity activity,TransitionEnum transitionEnum){
        switch (transitionEnum){
            case SLIDE_LEFT:
                activity.overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
                break;
            case SLIDE_RIGHT:
                activity.overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
                break;
            case SLIDE_TOP:
                activity.overridePendingTransition(R.anim.slide_in_from_bottom,R.anim.slide_out_from_top);
                break;
            case SLIDE_BOTTOM:
                activity.overridePendingTransition(R.anim.slide_in_from_top,R.anim.slide_out_from_bottom);
                break;
//            case TOP:
//                activity.overridePendingTransition(0,R.anim.slide_out_from_top);
//                break;
//            case RIGHT:
//                activity.overridePendingTransition(0,R.anim.slide_out_from_right);
//                break;
//            case BOTTOM:
//                activity.overridePendingTransition(0,R.anim.slide_out_from_bottom);
//                break;
//            case LEFT:
//                activity.overridePendingTransition(0,R.anim.slide_out_from_left);
//                break;
            case FADE:
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case ZOOM:
                activity.overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
                break;
        }
    }
}
