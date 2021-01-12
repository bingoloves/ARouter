package cn.cqs.im.widget.emotion;

import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.widget.emotion.bean.EmotionEntity;

public enum EmotionInputEventBus {
    instance;

    private long lastDealTime = 0, lastEndTime = 0;

    public interface EmotionInputEventListener {
        void onEmotionInput(EmotionEntity emotionEntity);
    }

    private EmotionInputEventListener emotionInputEventListener;

    public void setEmotionInputEventListener(EmotionInputEventListener emotionInputEventListener) {
        this.emotionInputEventListener = emotionInputEventListener;
    }

    public void postEmotion(EmotionEntity emotionEntity) {
        LogUtils.i("lastDealTime:" + lastDealTime);
        if (emotionInputEventListener != null) {
            long startTime = System.currentTimeMillis();
            if (startTime < lastEndTime + 100) {
                LogUtils.e(startTime + ", " + (lastEndTime + 100));
                return;
            }
            emotionInputEventListener.onEmotionInput(emotionEntity);
            lastEndTime = System.currentTimeMillis();
            lastDealTime = lastEndTime - startTime;
        }
    }
}
