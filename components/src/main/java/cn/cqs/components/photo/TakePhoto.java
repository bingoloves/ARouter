package cn.cqs.components.photo;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.constant.Type;

/**
 * Created by bingo on 2021/1/18.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: EasyPhotos 的简单使用
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/18
 */

public class TakePhoto {
    /**
     * 打开相册 默认都显示相机按钮
     * @param activity
     * @param mMaxCount
     * @param selectCallback
     * 返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
     * ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
     * 返回图片地址集合：如果你只需要获取图片的地址，可以用这个
     * ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
     * 返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
     * boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
     */
    public static void openAlbum(Activity activity, int mMaxCount, SelectCallback selectCallback){
        openAlbum(activity,mMaxCount,true,selectCallback);
    }
    public static void openAlbum(Activity activity, int mMaxCount, boolean isShowCamera, SelectCallback selectCallback){
        EasyPhotos.createAlbum(activity, isShowCamera, GlideEngine.getInstance())
                .setCount(mMaxCount <= 1 ? 1 : mMaxCount)
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .start(selectCallback);
    }
    public static void openAlbum(Fragment fragment, int mMaxCount, SelectCallback selectCallback){
        openAlbum(fragment, mMaxCount,true, selectCallback);
    }
    public static void openAlbum(Fragment fragment, int mMaxCount, boolean isShowCamera, SelectCallback selectCallback){
        EasyPhotos.createAlbum(fragment, isShowCamera, GlideEngine.getInstance())
                .setCount(mMaxCount <= 1 ? 1 : mMaxCount)
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .start(selectCallback);
    }
    public static void openVideo(Activity activity, int mMaxCount, SelectCallback selectCallback){
        openVideo(activity,mMaxCount,true,selectCallback);
    }
    public static void openVideo(Activity activity, int mMaxCount, boolean isShowCamera, SelectCallback selectCallback){
        EasyPhotos.createAlbum(activity, isShowCamera, GlideEngine.getInstance())
                .setCount(mMaxCount <= 1 ? 1 : mMaxCount)
                .filter(Type.VIDEO)
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .start(selectCallback);
    }
    public static void openVideo(Fragment fragment, int mMaxCount, SelectCallback selectCallback){
        openVideo(fragment,mMaxCount,true,selectCallback);
    }
    public static void openVideo(Fragment fragment, int mMaxCount, boolean isShowCamera, SelectCallback selectCallback){
        EasyPhotos.createAlbum(fragment, isShowCamera, GlideEngine.getInstance())
                .setCount(mMaxCount <= 1 ? 1 : mMaxCount)
                .filter(Type.VIDEO)
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .start(selectCallback);
    }
    /**
     * 打开相机
     * @param activity
     * @param selectCallback
     */
    public static void openCamera(Activity activity, SelectCallback selectCallback){
        EasyPhotos.createCamera(activity).start(selectCallback);
    }
    public static void openCamera(Fragment fragment, SelectCallback selectCallback){
        EasyPhotos.createCamera(fragment).start(selectCallback);
    }
}
