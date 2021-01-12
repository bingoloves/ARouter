package cn.cqs.im.widget.emotion;/**
 * Created by cpoopc on 2015/9/9.
 */

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.widget.emotion.bean.EmotionEntity;

/**
 * User: cpoopc
 * Date: 2015-09-09
 * Time: 14:23
 * Ver.: 0.1
 */
public class EmotionEditText extends EditText implements TextWatcher,EmotionInputEventBus.EmotionInputEventListener {


    private String mDealingText;

    public EmotionEditText(Context context) {
        super(context);
    }

    public EmotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener(this);
        EmotionInputEventBus.instance.setEmotionInputEventListener(this);
    }

    @Override
    public void onEmotionInput(EmotionEntity emotionEntity) {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        mDealingText = emotionEntity.getCode();
        EmotionManager.getImageSpan(emotionEntity);
        Editable text = getText();
        ImageSpan imageSpan = EmotionManager.getImageSpan(emotionEntity);
        if (selectionStart != selectionEnd) {
            text.replace(selectionStart, selectionEnd, emotionEntity.getCode());
            text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            text.insert(selectionStart, emotionEntity.getCode());
            text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mDealingText = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        DebugLog.e("before:" + s);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count >1 && !TextUtils.equals(s.subSequence(start, start + count), mDealingText)) {
//            DebugLog.e("复制的:" + s.subSequence(start, start + count));
            EmotionManager.parseCharSequence((SpannableStringBuilder) s, start, start + count);
        }
//        DebugLog.e("onTextChanged:" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
