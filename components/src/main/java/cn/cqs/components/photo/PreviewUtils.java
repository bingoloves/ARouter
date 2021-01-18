package cn.cqs.components.photo;

import android.app.Activity;

import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingo on 2021/1/18.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/18
 */

public class PreviewUtils {
    public static void start(Activity activity, ArrayList<String> paths, int current){
        if (paths == null || paths.size() == 0) return;
        ZoomMediaLoader.getInstance().init(GlideEngine.getInstance());
        GPreviewBuilder.from(activity)
                .setData(getPreviewInfos(paths))
                .setCurrentIndex(current)
                .setSingleFling(true)//是否在黑屏区域点击返回
                .setDrag(true)//是否禁用图片拖拽返回
                .setType(GPreviewBuilder.IndicatorType.Number)//指示器类型
                .start();//启动
    }

    private static List<PreviewInfo> getPreviewInfos(ArrayList<String> paths){
        List<PreviewInfo> result = new ArrayList<>();
        for (String resultPath : paths) {
            result.add(new PreviewInfo(resultPath));
        }
        return result;
    }
}
