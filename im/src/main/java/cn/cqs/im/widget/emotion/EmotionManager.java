package cn.cqs.im.widget.emotion;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cqs.android.utils.ApplicationUtils;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.widget.emotion.bean.EmotionEntity;

/**
 * User: cpoopc
 * Date: 2015-09-07
 * Time: 21:51
 */
public class EmotionManager {

    public static final Pattern PATTERN = Pattern.compile("\\[[^\\]]*\\]");

    private static Map<String, SoftReference<Bitmap>> bitmapMap = new HashMap<>();

    private static Map<String, EmotionEntity> emotionMap = new HashMap<>();

    public static void registerEmotion(EmotionEntity emotionEntity) {
        emotionMap.put(emotionEntity.getCode(), emotionEntity);
    }

    public static CharSequence parseCharSequence(SpannableStringBuilder text, int start, int end) {
        CharSequence charSequence = text.subSequence(start, end);
        Matcher matcher = PATTERN.matcher(charSequence);
        while (matcher.find()) {
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            String group = matcher.group();
            EmotionEntity emotionEntity = emotionMap.get(group);
            if (emotionEntity != null) {
                text.setSpan(getImageSpan(emotionEntity), start + matcherStart, start + matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return text;
    }

    public static ImageSpan getImageSpan(EmotionEntity emotionEntity) {
        Bitmap bitmap = getBitmap(emotionEntity.getSource());
        return new ImageSpan(ApplicationUtils.getInstance().getApplication().getApplicationContext(), bitmap);
    }

    public static Bitmap getBitmap(String source) {
        SoftReference<Bitmap> bitmapSoftReference = bitmapMap.get(source);
        if (bitmapSoftReference != null && bitmapSoftReference.get() != null) {
            return bitmapSoftReference.get();
        }
        AssetManager mngr = ApplicationUtils.getInstance().getApplication().getAssets();
        InputStream in = null;

        try {
            in = mngr.open(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
        bitmapSoftReference = new SoftReference<Bitmap>(temp);
        bitmapMap.put(source, bitmapSoftReference);
        return temp;
    }
}
